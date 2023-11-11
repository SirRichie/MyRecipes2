package de.lialuna.myrecipes2.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Collections;
import java.util.List;

import de.lialuna.myrecipes2.data.RecipeRepository;
import de.lialuna.myrecipes2.entity.Category;
import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.entity.Step;

public class RecipeViewModel extends ViewModel {
    public static final String TAG = RecipeListViewModel.class.getSimpleName();

    private MutableLiveData<Recipe> recipe;

    public RecipeViewModel() {
    }

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

    public void addIngredient(String amount, String ingredientName) {
        addIngredient(amount, ingredientName, false);
    }

    public void addIngredient(String amount, String ingredientName, boolean isGroup) {
        Ingredient ingredient = new Ingredient(amount, ingredientName);
        ingredient.setGroupIdentifier(isGroup);
        recipe.getValue().getIngredients().add(ingredient);
        recipeChanged();
    }

    public void swapIngredients(int fromPosition, int toPosition) {
        Collections.swap(recipe.getValue().getIngredients(), fromPosition, toPosition);
        recipeChanged();
    }

    public void removeStep(int position) {
        recipe.getValue().removeStep(position);
    }

    public void addStep(String stepText) {
        recipe.getValue().getSteps().add(new Step(stepText));
        recipeChanged();
    }

    public void setCategories(List<Category> categories) {
        recipe.getValue().setCategories(categories);
        recipeChanged();
    }

    public void saveOrStoreToDB() {
        Recipe entity = recipe.getValue();

        if (RecipeRepository.isNewRecipe(entity)) {
            RecipeRepository.addRecipe(entity);
        } else {
            RecipeRepository.updateRecipe(entity);
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

    }

    public void removeRecipeFromDB() {
        RecipeRepository.removeRecipe(recipe.getValue());
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
