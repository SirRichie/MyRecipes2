package de.lialuna.myrecipes2.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import de.lialuna.myrecipes2.R;
import de.lialuna.myrecipes2.databinding.DialogEditStepBinding;
import de.lialuna.myrecipes2.entity.Step;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

public class EditStepDialogFragment extends DialogFragment {

    public static final String KEY_STEP_INDEX = "stepIndex";

    public static EditStepDialogFragment newInstance(int stepIndex) {
        EditStepDialogFragment fragment = new EditStepDialogFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_STEP_INDEX, stepIndex);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        DialogEditStepBinding binding = DialogEditStepBinding.inflate(inflater);
        RecipeViewModel viewModel = new ViewModelProvider(requireParentFragment().requireParentFragment()).get(RecipeViewModel.class);

        int ingredientIndex = requireArguments().getInt(KEY_STEP_INDEX);

        Step step = viewModel.getRecipe().getValue().getSteps().get(ingredientIndex);

        binding.editStepText.setText(step.getText());

        builder.setView(binding.getRoot())
                .setTitle(binding.getRoot().getContext().getString(R.string.edit_step))
                .setView(binding.getRoot())
                // Add action buttons
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // write back to the ingredient - since these are objects this should directly reflect down to the model
                        step.setText(binding.editStepText.getText().toString());
                        viewModel.recipeChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditStepDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }
}
