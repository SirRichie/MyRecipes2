package de.lialuna.myrecipes2.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.StringJoiner;

import de.lialuna.myrecipes2.R;
import de.lialuna.myrecipes2.RecipeListFragmentDirections;
import de.lialuna.myrecipes2.databinding.ListRecipeBinding;
import de.lialuna.myrecipes2.entity.Category;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.util.Constants;

/**
 * Created by Tobias on 08.01.2018.
 */

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    public interface DeleteListener {
        void onDeleteClicked(String dbID);
    }

    private static final String TAG = "RecipeViewHolder";

    private TextView categoriesTextView, titleTextView;
    private ImageView recipeIconImageView, editImageView;
    private final String noCategories;

    private RequestManager glide;
    private ArrayList<String> ingredientNames;

    public RecipeViewHolder(ListRecipeBinding listRecipeBinding, RequestManager glide, ArrayList<String> ingredientNames) {
        super(listRecipeBinding.getRoot());
        this.glide = glide;
        this.ingredientNames = ingredientNames;
        titleTextView = listRecipeBinding.listRecipeTitle;
        categoriesTextView = listRecipeBinding.listRecipeCategories;
        recipeIconImageView = listRecipeBinding.recipeIcon;
        editImageView = listRecipeBinding.editButton;
        noCategories = listRecipeBinding.getRoot().getResources().getString(R.string.no_categories);
    }

    public void bindTo(Recipe recipe, int position) {
        titleTextView.setText(recipe.getTitle());
        categoriesTextView.setText(buildCategoriesString(recipe));

        itemView.setOnClickListener(v -> {
            Log.d(TAG, "clicked on " + recipe);
            RecipeListFragmentDirections.ActionRecipeListFragmentToViewRecipeFragment action
                    = RecipeListFragmentDirections.actionRecipeListFragmentToViewRecipeFragment(position);
            action.setDynamicTitle(recipe.getTitle());
            Navigation.findNavController(v).navigate(action);
        });

        itemView.setOnLongClickListener(v -> {
            Log.d(TAG, "long clicked on " + recipe.hashCode());
            startEditActivity(recipe, v.getContext(), ingredientNames);
            return true;
        });

        if (recipe.getThumbnail() != null) {
            glide.load(recipe.getThumbnail()).into(recipeIconImageView);
        } else {
            Drawable placeholder = itemView.getResources().getDrawable(R.drawable.ic_insert_photo_grey_500_48dp, null);
            glide.load(placeholder).into(recipeIconImageView);
        }

        editImageView.setOnClickListener(v -> startEditActivity(recipe, v.getContext(), ingredientNames));
    }

    private void startEditActivity(Recipe recipe, Context context, ArrayList<String> ingredientNames) {
//        Intent intent = new Intent(context, EditRecipeActivity.class);
//        intent.setAction(Intent.ACTION_EDIT);
//        intent.putExtra(Constants.INTENT_EXTRA_RECIPE, (Parcelable) recipe);
//        intent.putStringArrayListExtra(Constants.INTENT_EXTRA_INGREDIENT_NAMES, ingredientNames);
//        context.startActivity(intent); TODO
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
