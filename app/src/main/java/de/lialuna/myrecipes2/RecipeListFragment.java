package de.lialuna.myrecipes2;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.adapter.RecipeRecyclerAdapter;
import de.lialuna.myrecipes2.databinding.FragmentRecipeListBinding;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.viewmodel.RecipeListViewModel;

public class RecipeListFragment extends Fragment {

    private RecipeListViewModel recipeListViewModel;

    private FragmentRecipeListBinding binding;

    private RecipeRecyclerAdapter recipeRecyclerAdapter;

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipeListViewModel = new ViewModelProvider(getActivity()).get(RecipeListViewModel.class);

        recipeRecyclerAdapter =
                new RecipeRecyclerAdapter(
                        recipeListViewModel.getRecipes().isInitialized() ? recipeListViewModel.getRecipes().getValue() : new ArrayList<>(),
                        Glide.with(this));
        // observe the LiveData and update the adapter upon changes
        recipeListViewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                recipeRecyclerAdapter.setAllRecipes(recipes);
            }
        });

        binding.recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recipesRecyclerView.setAdapter(recipeRecyclerAdapter);

        Drawable divider = ResourcesCompat.getDrawable(getResources(), R.drawable.recipe_list_divider, requireActivity().getTheme());
        DividerItemDecoration decoration = new DividerItemDecoration(binding.recipesRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(divider);
        binding.recipesRecyclerView.addItemDecoration(decoration);

    }

}