package de.lialuna.myrecipes2.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.util.Constants;

public class RecipeViewModel extends ViewModel {
    public static final String TAG = RecipeListViewModel.class.getSimpleName();

    private MutableLiveData<Recipe> recipe;

    public RecipeViewModel(Recipe recipe) {
        this.recipe = new MutableLiveData<>(recipe);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public void recipeChanged() {
        recipe.postValue(recipe.getValue());
    }

    public void removeIngredient(int position) {
        recipe.getValue().removeIngredient(position);
        recipeChanged();
    }

    public void addIngredient(Ingredient ingredient) {
        recipe.getValue().getIngredients().add((ingredient));
        recipeChanged();
    }

    public void swapIngredients(int fromPosition, int toPosition) {
        Collections.swap(recipe.getValue().getIngredients(), fromPosition, toPosition);
        recipeChanged();
    }

    public boolean isNewRecipe() {
        return recipe.getValue().getDbID() == null;
    }

    public void saveOrStoreToDB() {
        CollectionReference recipes = FirebaseFirestore.getInstance().collection(Constants.DB_RECIPE_COLLECTION);
        DocumentReference recipeRef = null;

        if (isNewRecipe()) {
            // create it in the database
            recipeRef = recipes.document();
            recipe.getValue().setDbID(recipeRef.getId());
        } else {
            recipeRef = recipes.document(recipe.getValue().getDbID());
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

    public static class Factory implements ViewModelProvider.Factory {

        private final Recipe recipe;

        public Factory(Recipe recipe) {
            this.recipe = recipe;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new RecipeViewModel(recipe);
        }
    }


}
