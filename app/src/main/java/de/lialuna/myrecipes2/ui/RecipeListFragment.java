package de.lialuna.myrecipes2.ui;

import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.R;
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

        initializeRecyclerViewAdapter();

        generateBottomMenu();

        ColorFilter colorFilter = new BlendModeColorFilter(getResources().getColor(R.color.complementary_700, null), BlendMode.SRC_ATOP);
        binding.bottomAppBar.setOnMenuItemClickListener(item -> {
            recipeRecyclerAdapter.setCategoryFilter(item.getTitle().toString());
            clearBottomAppBarColors();
            item.getIcon().setColorFilter(colorFilter);
            return true;
        });

        binding.fab.setOnClickListener(fab -> {
            RecipeListFragmentDirections.ActionRecipeListFragmentToEditRecipeFragment action
                    = RecipeListFragmentDirections.actionRecipeListFragmentToEditRecipeFragment(-1);
            action.setDynamicTitle(requireContext().getString(R.string.recipe_create));
            Navigation.findNavController(requireView()).navigate(action);
        });

    }

    private void initializeRecyclerViewAdapter() {
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

        addDividerToRecyclerView();
    }

    private void addDividerToRecyclerView() {
        Drawable divider = ResourcesCompat.getDrawable(getResources(), R.drawable.recipe_list_divider, requireActivity().getTheme());
        DividerItemDecoration decoration = new DividerItemDecoration(binding.recipesRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(divider);
        binding.recipesRecyclerView.addItemDecoration(decoration);
    }

    private void generateBottomMenu() {
        Menu menu = binding.bottomAppBar.getMenu();
        String[] categoryNames = getResources().getStringArray(R.array.categories_names);
        try (TypedArray categoryIcons = getResources().obtainTypedArray(R.array.categories_icons)) {
            for (int i = 0; i < categoryNames.length; i++) {
                MenuItem newItem = menu.add(Menu.NONE, 28304803 + i, Menu.NONE, categoryNames[i]);
                newItem.setIcon(categoryIcons.getDrawable(i));
                newItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            }
        }
    }

    private void clearBottomAppBarColors() {
        Menu menu = binding.bottomAppBar.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().clearColorFilter();
        }

    }

}