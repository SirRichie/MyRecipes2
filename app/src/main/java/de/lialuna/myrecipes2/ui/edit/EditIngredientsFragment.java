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
    // TODO: do we really need recipeIndex still?
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