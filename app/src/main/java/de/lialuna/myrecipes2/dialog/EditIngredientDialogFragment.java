package de.lialuna.myrecipes2.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import de.lialuna.myrecipes2.R;
import de.lialuna.myrecipes2.databinding.DialogEditIngredientBinding;
import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.viewmodel.RecipeListViewModel;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

public class EditIngredientDialogFragment extends DialogFragment {

    public static final String TAG = "EditIngredientDialogFragment";

    public static final String KEY_INGREDIENT_INDEX = "ingredientIndex";

    public static EditIngredientDialogFragment newInstance(int ingredientIndex) {
        EditIngredientDialogFragment fragment = new EditIngredientDialogFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_INGREDIENT_INDEX, ingredientIndex);
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        DialogEditIngredientBinding binding = DialogEditIngredientBinding.inflate(inflater);

        RecipeListViewModel listViewModel = new ViewModelProvider(requireActivity()).get(RecipeListViewModel.class);
        RecipeViewModel viewModel = new ViewModelProvider(requireParentFragment().requireParentFragment()).get(RecipeViewModel.class);

        int ingredientIndex = requireArguments().getInt(KEY_INGREDIENT_INDEX);

        Ingredient ingredient = viewModel.getRecipe().getValue().getIngredients().get(ingredientIndex);

        binding.editIngredientAmount.setText(ingredient.getAmount());
        binding.editIngredientName.setText(ingredient.getIngredient());
        // autocomplete
        ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, listViewModel.getIngredientNames().getValue());
        binding.editIngredientName.setAdapter(autocompleteAdapter);

        builder.setView(binding.getRoot())
                .setTitle(binding.getRoot().getContext().getString(R.string.edit_ingredient))
                .setView(binding.getRoot())
                // Add action buttons
                .setPositiveButton(R.string.dialog_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // write back to the ingredient - since these are objects this should directly reflect down to the model
                        ingredient.setAmount(binding.editIngredientAmount.getText().toString().trim());
                        ingredient.setIngredient(binding.editIngredientName.getText().toString().trim());
                        viewModel.recipeChanged();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditIngredientDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }
}
