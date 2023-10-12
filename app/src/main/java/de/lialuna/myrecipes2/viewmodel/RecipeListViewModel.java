package de.lialuna.myrecipes2.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lialuna.myrecipes2.entity.Category;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.util.Constants;

public class RecipeListViewModel extends ViewModel {

    public static final String TAG = RecipeListViewModel.class.getSimpleName();

    private ListenerRegistration categoriesListenerRegistration;
    private ListenerRegistration recipesListenerRegistration;

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<List<Recipe>> recipes;

    public RecipeListViewModel() {
        subscribeToRecipes();
        subscribeToCategories();
    }



    public LiveData<List<Category>> getCategories() {
        if (categories == null) {
            categories = new MutableLiveData<>();
            subscribeToCategories();
        }
        return categories;
    }
    public LiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<>();
            subscribeToRecipes();
        }
        return recipes;
    }

    private void subscribeToRecipes() {
        // ensure recipes is initialized
        if (recipes == null) {
            recipes = new MutableLiveData<>();
        }

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
                });
    }

    private void subscribeToCategories() {
        // ensure categories is initialized
        if (categories == null) {
            categories = new MutableLiveData<>();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        categoriesListenerRegistration = db
                .collection(Constants.DB_CATEGORY_COLLECTION)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed: ", e);
                        return;
                    }
                    List<Category> newCategories = documentSnapshots.toObjects(Category.class);
                    // if (newCategories != null)  // safeguard against null
                    categories.postValue(newCategories);
                });

    }

    public void saveOrStoreToDB(Recipe recipe) {
        CollectionReference recipes = FirebaseFirestore.getInstance().collection(Constants.DB_RECIPE_COLLECTION);
        DocumentReference recipeRef = null;

        if (isNewRecipe(recipe)) {
            // create it in the database
            recipeRef = recipes.document();
            recipe.setDbID(recipeRef.getId());
        } else {
            recipeRef = recipes.document(recipe.getDbID());
        }

        /*if (imageChanged) { // only do image handling if the image changed
            // find image
            String url = "images/" + recipe.getValue().getDbID() + ".jpg";
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(url);
            if (imageUri != null) {
                // upload image
                imageRef.putFile(imageUri);
                // set URL
                recipe.getValue().setImageURL(url);
            } else {
                imageRef.delete(); // may fail, but silently
                recipe.getValue().setImageURL(null);
            }
        }*/ // TODO maybe add image handling later again

        // store recipe
        recipeRef.set(recipe);

    }

    public void removeRecipeFromDB(Recipe recipe) {
        if (isNewRecipe(recipe))
            throw new IllegalStateException("Cannot delete non-existing recipe");
        FirebaseFirestore.getInstance().collection(Constants.DB_RECIPE_COLLECTION)
                .document(recipe.getDbID()).delete();
    }

    public boolean isNewRecipe(Recipe recipe) {
        return recipe.getDbID() == null;
    }

    @Override
    protected void onCleared() {
        categoriesListenerRegistration.remove();    // make sure to stop listening to firestore
        recipesListenerRegistration.remove();
        // mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        // mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        super.onCleared();
    }
}