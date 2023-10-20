package de.lialuna.myrecipes2.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.StringJoiner;

import de.lialuna.myrecipes2.R;
import de.lialuna.myrecipes2.databinding.ListRecipeBinding;
import de.lialuna.myrecipes2.entity.Category;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.ui.RecipeListFragmentDirections;

/**
 * Created by Tobias on 08.01.2018.
 */

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    public interface DeleteListener {
        void onDeleteClicked(String dbID);
    }

    private static final String TAG = "RecipeViewHolder";

    private final String noCategories;

    private de.lialuna.myrecipes2.databinding.ListRecipeBinding binding;
    private RequestManager glide;
    private ArrayList<String> ingredientNames;

    public RecipeViewHolder(ListRecipeBinding binding, RequestManager glide, ArrayList<String> ingredientNames) {
        super(binding.getRoot());
        this.binding = binding;
        this.glide = glide;
        this.ingredientNames = ingredientNames;
        noCategories = binding.getRoot().getResources().getString(R.string.no_categories);
    }

    public void bindTo(Recipe recipe, int position) {
        binding.listRecipeTitle.setText(recipe.getTitle());
        binding.listRecipeCategories.setText(buildCategoriesString(recipe));

        itemView.setOnClickListener(v -> {
            Log.d(TAG, "clicked on " + recipe);
            RecipeListFragmentDirections.ActionRecipeListFragmentToViewRecipeFragment action
                    = RecipeListFragmentDirections.actionRecipeListFragmentToViewRecipeFragment(position);
            action.setDynamicTitle(recipe.getTitle());
            Navigation.findNavController(v).navigate(action);
        });

        itemView.setOnLongClickListener(v -> {
            Log.d(TAG, "long clicked on " + recipe.hashCode());

            startEditActivity(position, v);
            return true;
        });

        if (recipe.getThumbnail() != null) {
            glide.load(recipe.getThumbnail()).into(binding.recipeIcon);
        } else {
            Drawable placeholder = itemView.getResources().getDrawable(R.drawable.ic_insert_photo_grey_500_48dp, null);
            glide.load(placeholder).into(binding.recipeIcon);
        }

        binding.editButton.setOnClickListener(v -> startEditActivity(position, v));
    }

    private void startEditActivity(int position, View view) {
        RecipeListFragmentDirections.ActionRecipeListFragmentToEditRecipeFragment action
                = RecipeListFragmentDirections.actionRecipeListFragmentToEditRecipeFragment(position);
        action.setDynamicTitle(itemView.getContext().getString(R.string.title_edit_recipe));
        Navigation.findNavController(view).navigate(action);
    }

    private String buildCategoriesString(Recipe recipe) {
        if (recipe.getCategories() == null || recipe.getCategories().isEmpty())
            return noCategories;

        StringJoiner joiner = new StringJoiner(" | ");

        for (Category category : recipe.getCategories()) {
            joiner.add(category.getName());
        }

        return joiner.toString();
    }
}
