package de.lialuna.myrecipes2.ui;

import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuProvider;
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

    private static final String TAG = "RecipeListFragment";
    private RecipeListViewModel recipeListViewModel;

    private FragmentRecipeListBinding binding;
    private RecipeRecyclerAdapter recipeRecyclerAdapter;

    private String lastChosenCategory;

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView | lastChosenCategory = " + lastChosenCategory);
        binding = FragmentRecipeListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        recipeListViewModel = new ViewModelProvider(getActivity()).get(RecipeListViewModel.class);

        initializeRecyclerViewAdapter();
        addSearch();
        generateBottomMenu();


        binding.bottomAppBar.setOnMenuItemClickListener(item -> {
            clearBottomAppBarColors();
            // if the same category is pressed again "unselect" it
            if (item.getTitle().toString().equals(lastChosenCategory)) {
                recipeRecyclerAdapter.clearCategoryFilter();
                lastChosenCategory = "";
            } else {
                selectCategoryMenuItem(item);
            }
            return true;
        });

        binding.fab.setOnClickListener(fab -> {
            RecipeListFragmentDirections.ActionRecipeListFragmentToEditRecipeFragment action
                    = RecipeListFragmentDirections.actionRecipeListFragmentToEditRecipeFragment(-1);
            action.setDynamicTitle(requireContext().getString(R.string.recipe_create));
            Navigation.findNavController(requireView()).navigate(action);
        });

    }

    private void addSearch() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_search_recipes, menu);
                MenuItem searchMenuItem = menu.findItem(R.id.search);
                SearchView searchView = (SearchView) searchMenuItem.getActionView();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        recipeRecyclerAdapter.setTitleFilter(newText);
                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                // not implemented here
                return false;
            }
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

                // category might already be set if returning to this view
                if (categoryNames[i].equals(lastChosenCategory)) {
                    selectCategoryMenuItem(newItem);
                }
            }
        }
    }

    private void clearBottomAppBarColors() {
        Menu menu = binding.bottomAppBar.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().clearColorFilter();
        }

    }

    private void selectCategoryMenuItem(MenuItem item) {
        ColorFilter colorFilter = new BlendModeColorFilter(getResources().getColor(R.color.complementary_700, null), BlendMode.SRC_ATOP);
        recipeRecyclerAdapter.setCategoryFilter(item.getTitle().toString());
        item.getIcon().setColorFilter(colorFilter);
        lastChosenCategory = item.getTitle().toString();
    }

}