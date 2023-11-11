package de.lialuna.myrecipes2.ui.edit;

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
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import de.lialuna.myrecipes2.R;
import de.lialuna.myrecipes2.databinding.FragmentEditRecipeBinding;
import de.lialuna.myrecipes2.dialog.EditCategoriesDialogFragment;
import de.lialuna.myrecipes2.viewmodel.RecipeListViewModel;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditRecipeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_RECIPE_INDEX = "recipeIndex";

    private static final String TAG = "EditRecipeFragment";

    private int recipeIndex;

    private FragmentEditRecipeBinding binding;
    private RecipeViewModel viewModel;

    public EditRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeIndex the position of the recipe in the recipe list
     * @return A new instance of fragment EditRecipeFragment.
     */
    public static EditRecipeFragment newInstance(int recipeIndex) {
        EditRecipeFragment fragment = new EditRecipeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_INDEX, recipeIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeIndex = EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipeIndex();
        Log.d(TAG, "recipe index is " + recipeIndex);

        if (savedInstanceState == null) {
            // Bundle bundle = new Bundle();
            // bundle.putInt(ARG_RECIPE_INDEX, recipeIndex);

            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.editIngredientsFragmentContainer, EditIngredientsFragment.class, null)
                    .commit();

            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.editStepsFragmentContainer, EditStepsFragment.class, null)
                    .commit();


        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_edit_recipe, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_save) {
                    saveAndReturn();
                    return true;
                } else if (itemId == R.id.action_delete) {
                    viewModel.removeRecipeFromDB();
                    return true;
                } else if (itemId == R.id.action_categories) {
                    EditCategoriesDialogFragment dialog = new EditCategoriesDialogFragment();
                    dialog.show(getChildFragmentManager(), "editCategories");
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditRecipeBinding.inflate(inflater, container, false);

        // viewmodel
        RecipeListViewModel listViewModel = new ViewModelProvider(requireActivity()).get(RecipeListViewModel.class);
        RecipeViewModel.Factory factory = new RecipeViewModel.Factory(listViewModel.getRecipe(recipeIndex).getValue());
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        // populate UI with recipe information
        binding.recipeTitleEditText.setText(viewModel.getRecipe().getValue().getTitle());

        return binding.getRoot();
    }

    private void saveAndReturn() {
        // make sure to handle any title changes
        viewModel.getRecipe().getValue().setTitle(binding.recipeTitleEditText.getText().toString());

        viewModel.saveOrStoreToDB();
        Navigation.findNavController(requireView()).navigateUp();
    }
}