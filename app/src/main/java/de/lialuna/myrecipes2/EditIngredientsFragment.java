package de.lialuna.myrecipes2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import de.lialuna.myrecipes2.adapter.EditIngredientsRecyclerAdapter;
import de.lialuna.myrecipes2.adapter.IngredientTouchHelperCallback;
import de.lialuna.myrecipes2.databinding.FragmentEditIngredientsBinding;
import de.lialuna.myrecipes2.dialog.EditIngredientDialogFragment;
import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditIngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditIngredientsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RECIPE_INDEX = "recipeIndex";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "EditIngredientsFragment";

    // TODO: Rename and change types of parameters
    private int recipeIndex;
    private String mParam2;

    private FragmentEditIngredientsBinding binding;
    private EditIngredientsRecyclerAdapter adapter;

    public EditIngredientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeIndex Parameter 1.
     * @param param2      Parameter 2.
     * @return A new instance of fragment EditIngredients.
     */
    // TODO: Rename and change types and number of parameters
    public static EditIngredientsFragment newInstance(int recipeIndex, String param2) {
        EditIngredientsFragment fragment = new EditIngredientsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_INDEX, recipeIndex);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recipeIndex = requireArguments().getInt(EditRecipeFragment.ARG_RECIPE_INDEX);

        // RecipeListViewModel viewModel = new ViewModelProvider(requireActivity()).get(RecipeListViewModel.class);
        RecipeViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(RecipeViewModel.class);

        // Log.d(TAG, "recipe (" + recipeIndex + ") = " + viewModel.getRecipes().getValue().get(recipeIndex));
        // Log.d(TAG, "going to create adapter with ingredients: " + viewModel.getRecipes().getValue().get(recipeIndex).getIngredients());
        // Log.d(TAG, "childFragmentManager = " + getChildFragmentManager());
        Log.d(TAG, "recipe = " + viewModel.getRecipe().getValue());
        // recycler view

        // create touch helpers for drag and drop support
        IngredientTouchHelperCallback touchHelperCallback = new IngredientTouchHelperCallback(viewModel::swapIngredients);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(binding.ingredientListRecyclerView);

        adapter = new EditIngredientsRecyclerAdapter(
                // viewModel.getRecipes().getValue().get(recipeIndex).getIngredients(),
                // viewModel.getRecipe().getValue().getIngredients(),
                position -> {   // lambda for click events
                    EditIngredientDialogFragment dialog = EditIngredientDialogFragment.newInstance(position);
                    dialog.show(getChildFragmentManager(), "editIngredient");
                },
                position -> {   // lambda for delete events
                    // viewModel.getRecipes().getValue().get(recipeIndex).removeIngredient(position);
                    viewModel.getRecipe().getValue().removeIngredient(position);
                },
                itemTouchHelper::startDrag);

        viewModel.getRecipe().observe(getViewLifecycleOwner(), recipe -> {
            Log.d(TAG, "submitting new list of ingredients + " + recipe.getIngredients());
            adapter.setIngredients(recipe.getIngredients());
        });

        binding.buttonAddIngredient.setOnClickListener(v -> {
            Ingredient ingredient = new Ingredient(
                    binding.inputNewIngredientAmount.getText().toString(),
                    binding.inputNewIngredientName.getText().toString());
            viewModel.addIngredient(ingredient);

            clearInput();

            // scroll the recycler view ot make the ingredient visible
            binding.ingredientListRecyclerView.smoothScrollToPosition(adapter.getItemCount());
        });

        binding.buttonAddGroup.setOnClickListener(v -> {
            Ingredient ingredient = new Ingredient(
                    "",
                    binding.inputNewIngredientName.getText().toString());
            ingredient.setGroupIdentifier(true);
            viewModel.addIngredient(ingredient);

            clearInput();

            // scroll the recycler view ot make the ingredient visible
            binding.ingredientListRecyclerView.smoothScrollToPosition(adapter.getItemCount());
        });

        binding.ingredientListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ingredientListRecyclerView.setAdapter(adapter);
    }

    private void clearInput() {
        binding.inputNewIngredientAmount.setText("");
        binding.inputNewIngredientName.setText("");
        binding.inputNewIngredientAmount.requestFocus();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditIngredientsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


}