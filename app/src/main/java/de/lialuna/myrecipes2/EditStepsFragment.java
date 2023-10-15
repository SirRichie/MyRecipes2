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

import de.lialuna.myrecipes2.adapter.EditStepsRecyclerAdapter;
import de.lialuna.myrecipes2.adapter.ItemTouchHelperCallback;
import de.lialuna.myrecipes2.databinding.FragmentEditStepsBinding;
import de.lialuna.myrecipes2.dialog.EditIngredientDialogFragment;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditStepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditStepsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RECIPE_INDEX = "recipeIndex";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "EditRecipeStepsFragment";

    // TODO: Rename and change types of parameters
    private int recipeIndex;
    private String mParam2;
    private FragmentEditStepsBinding binding;

    private EditStepsRecyclerAdapter adapter;

    public EditStepsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeIndex Parameter 1.
     * @param param2      Parameter 2.
     * @return A new instance of fragment EditStepsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditStepsFragment newInstance(int recipeIndex, String param2) {
        EditStepsFragment fragment = new EditStepsFragment();
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

        RecipeViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(RecipeViewModel.class);

        initStepsRecyclerView(viewModel);
        initButton(viewModel);
    }


    private void initStepsRecyclerView(RecipeViewModel viewModel) {
        adapter = new EditStepsRecyclerAdapter(
                position -> {   // lambda for click events
                    EditIngredientDialogFragment dialog = EditIngredientDialogFragment.newInstance(position);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditStepsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}