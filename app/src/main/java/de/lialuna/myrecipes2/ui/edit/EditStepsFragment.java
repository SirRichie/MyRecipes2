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

import de.lialuna.myrecipes2.adapter.EditStepsRecyclerAdapter;
import de.lialuna.myrecipes2.adapter.ItemTouchHelperCallback;
import de.lialuna.myrecipes2.databinding.FragmentEditStepsBinding;
import de.lialuna.myrecipes2.dialog.EditStepDialogFragment;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditStepsFragment extends Fragment {

    private static final String TAG = "EditRecipeStepsFragment";

    private FragmentEditStepsBinding binding;

    private EditStepsRecyclerAdapter adapter;

    public EditStepsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecipeViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(RecipeViewModel.class);

        initStepsRecyclerView(viewModel);
        initButton(viewModel);
    }


    private void initStepsRecyclerView(RecipeViewModel viewModel) {
        adapter = new EditStepsRecyclerAdapter(
                position -> {   // lambda for click events
                    EditStepDialogFragment dialog = EditStepDialogFragment.newInstance(position);
                    dialog.show(getChildFragmentManager(), "editIngredient");
                },
                viewModel::removeStep);

        // create touch helpers for drag and drop support
        ItemTouchHelperCallback touchHelperCallback = new ItemTouchHelperCallback(adapter::swapSteps);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(binding.stepListRecyclerView);
        adapter.setStartDragListener(itemTouchHelper::startDrag);

        viewModel.getRecipe().observe(getViewLifecycleOwner(), recipe -> {
            Log.d(TAG, "submitting new list of steps + " + recipe.getIngredients());
            adapter.setSteps(recipe.getSteps());
        });

        binding.stepListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.stepListRecyclerView.setAdapter(adapter);
    }

    private void initButton(RecipeViewModel viewModel) {

        binding.addStepButton.setOnClickListener(v -> {
            viewModel.addStep(binding.newStepText.getText().toString());

            clearInput();

            // scroll the recycler view ot make the ingredient visible
            binding.stepListRecyclerView.smoothScrollToPosition(adapter.getItemCount());
        });
    }

    private void clearInput() {
        binding.newStepText.setText("");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditStepsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}