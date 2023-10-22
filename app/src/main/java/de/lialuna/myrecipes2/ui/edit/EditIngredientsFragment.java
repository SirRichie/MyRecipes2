package de.lialuna.myrecipes2.ui.edit;

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
import de.lialuna.myrecipes2.adapter.ItemTouchHelperCallback;
import de.lialuna.myrecipes2.databinding.FragmentEditIngredientsBinding;
import de.lialuna.myrecipes2.dialog.EditIngredientDialogFragment;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditIngredientsFragment extends Fragment {

    private static final String TAG = "EditIngredientsFragment";

    private FragmentEditIngredientsBinding binding;
    private EditIngredientsRecyclerAdapter adapter;

    public EditIngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // RecipeListViewModel viewModel = new ViewModelProvider(requireActivity()).get(RecipeListViewModel.class);
        RecipeViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(RecipeViewModel.class);

        // recycler view

        initIngredientsRecyclerView(viewModel);
        initButtons(viewModel);
    }


    private void initIngredientsRecyclerView(RecipeViewModel viewModel) {
        adapter = new EditIngredientsRecyclerAdapter(
                position -> {   // lambda for click events
                    EditIngredientDialogFragment dialog = EditIngredientDialogFragment.newInstance(position);
                    dialog.show(getChildFragmentManager(), "editIngredient");
                },
                viewModel::removeIngredient);

        // create touch helpers for drag and drop support
        ItemTouchHelperCallback touchHelperCallback = new ItemTouchHelperCallback(adapter::swapIngredients);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(binding.ingredientListRecyclerView);
        adapter.setStartDragListener(itemTouchHelper::startDrag);

        viewModel.getRecipe().observe(getViewLifecycleOwner(), recipe -> {
            Log.d(TAG, "submitting new list of ingredients + " + recipe.getIngredients());
            adapter.setIngredients(recipe.getIngredients());
        });

        binding.ingredientListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ingredientListRecyclerView.setAdapter(adapter);
    }

    private void initButtons(RecipeViewModel viewModel) {
        binding.buttonAddIngredient.setOnClickListener(v -> {
            viewModel.addIngredient(
                    binding.inputNewIngredientAmount.getText().toString(),
                    binding.inputNewIngredientName.getText().toString());

            clearInput();

            // scroll the recycler view ot make the ingredient visible
            binding.ingredientListRecyclerView.smoothScrollToPosition(adapter.getItemCount());
        });

        binding.buttonAddGroup.setOnClickListener(v -> {
            viewModel.addIngredient(
                    "",
                    binding.inputNewIngredientName.getText().toString(),
                    true);

            clearInput();

            // scroll the recycler view ot make the ingredient visible
            binding.ingredientListRecyclerView.smoothScrollToPosition(adapter.getItemCount());
        });
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