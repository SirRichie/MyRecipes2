package de.lialuna.myrecipes2.adapter;

/**
 * Created by Tobias on 10.03.2018.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.lialuna.myrecipes2.databinding.ListRecipeBinding;
import de.lialuna.myrecipes2.entity.Category;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.util.Util;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private static final String TAG = "RecipeRecyclerAdapter";

    protected List<Recipe> allRecipes;
    protected List<Recipe> displayedRecipes;
    private ArrayList<String> ingredientNames;

    private Predicate<Recipe> titlePredicate;
    private Predicate<Recipe> categoryPredicate;

    private RequestManager glide;

    public RecipeRecyclerAdapter(List<Recipe> allRecipes, RequestManager glide) {
        this.glide = glide;
        displayedRecipes = new ArrayList<>();
        // initialize predicates to avoid NPE
        titlePredicate = recipe -> true;
        categoryPredicate = recipe -> true;
        setAllRecipes(allRecipes);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListRecipeBinding binding = ListRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe, parent, false);
//
        return new RecipeViewHolder(binding, glide, ingredientNames);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bindTo(displayedRecipes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return displayedRecipes.size();
    }

    public void setAllRecipes(List<Recipe> allRecipes) {
        this.allRecipes = allRecipes;
        this.ingredientNames = new ArrayList<>(Util.getIngredientNamesFromRecipes(allRecipes));
        obtainDisplayedRecipes();
    }

    public void setTitleFilter(String titleFilter) {
        if (titleFilter == null || titleFilter.isEmpty()) {
            titlePredicate = recipe -> true;
        } else {
            titlePredicate = recipe ->
                    recipe.getTitle().toLowerCase().contains(titleFilter.toLowerCase());
        }

        obtainDisplayedRecipes();
    }

    public void setCategoryFilter(Category categoryFilter) {
        if (categoryFilter == null) {
            categoryPredicate = recipe -> true;
        } else {
            categoryPredicate = recipe -> recipe.getCategories().contains(categoryFilter);
        }

        obtainDisplayedRecipes();
    }

    private void obtainDisplayedRecipes() {
        if (allRecipes == null)
            return;
        displayedRecipes = allRecipes.stream()
                .filter(categoryPredicate)
                .filter(titlePredicate)
                .collect(Collectors.toList());

        notifyDataSetChanged();
    }
}
