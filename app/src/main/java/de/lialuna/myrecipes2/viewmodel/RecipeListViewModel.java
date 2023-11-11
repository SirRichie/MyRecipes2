package de.lialuna.myrecipes2.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lialuna.myrecipes2.data.RecipeRepository;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.util.Constants;
import de.lialuna.myrecipes2.util.Util;

public class RecipeListViewModel extends ViewModel implements RecipeRepository.RecipeRepositoryDataObserver {

    public static final String TAG = RecipeListViewModel.class.getSimpleName();

    private ListenerRegistration recipesListenerRegistration;

    private final MutableLiveData<List<Recipe>> recipes;
    private final MutableLiveData<List<String>> ingredientNames;

    public RecipeListViewModel() {
        // make sure we always have live data
        recipes = new MutableLiveData<>();
        ingredientNames = new MutableLiveData<>();
        RecipeRepository.loadRecipes(this);
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<List<String>> getIngredientNames() {
        return ingredientNames;
    }

    public LiveData<Recipe> getRecipe(int index) {
        if (index == -1) {
            Recipe recipe = new Recipe();
            recipe.setSteps(new ArrayList<>());
            recipe.setIngredients(new ArrayList<>());
            recipe.setCategories(new ArrayList<>());
            return new MutableLiveData<>(recipe);
        } else {
            return new MutableLiveData<>(recipes.getValue().get(index));
        }
    }

    public int addRecipe(Recipe recipe) {
        // first add the recipe to the DB, if anything goes wrong we'll get an exception
        RecipeRepository.addRecipe(recipe);

        recipes.getValue().add(recipe);
        Collections.sort(recipes.getValue());
        return recipes.getValue().indexOf(recipe);
    }

    private void subscribeToRecipes() {
        // ensure recipes is initialized
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recipesListenerRegistration = db
                .collection(Constants.DB_RECIPE_COLLECTION)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Listening to recipes failed: ", e);
                        return;
                    }
                    // manually construct the list so we can get the database ID
                    List<Recipe> newRecipes = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        Recipe r = snapshot.toObject(Recipe.class);
                        r.setDbID(snapshot.getId());
                        newRecipes.add(r);
                    }

                    Collections.sort(newRecipes);
                    // publish the new result
                    Log.d(TAG, "received list of recipies from db with size " + newRecipes.size());
                    recipes.postValue(newRecipes);
                    setIngredientNames(newRecipes);
                });
    }

    private void setIngredientNames(List<Recipe> recipes) {
        ingredientNames.postValue(Util.getIngredientNamesFromRecipes(recipes));
    }

    @Override
    protected void onCleared() {
        // recipesListenerRegistration.remove(); // make sure to stop listening to firestore
        super.onCleared();
    }

    @Override
    public void recipesLoaded(List<Recipe> recipes) {
        this.recipes.postValue(recipes);
        setIngredientNames(recipes);
    }
}