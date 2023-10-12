package de.lialuna.myrecipes2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.lialuna.myrecipes2.databinding.FragmentEditRecipeBinding;
import de.lialuna.myrecipes2.viewmodel.RecipeListViewModel;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_RECIPE_INDEX = "recipeIndex";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "EditRecipeFragment";

    // TODO: Rename and change types of parameters
    private int recipeIndex;
    private String mParam2;

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
     * @param param2      Parameter 2.
     * @return A new instance of fragment EditRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditRecipeFragment newInstance(int recipeIndex, String param2) {
        EditRecipeFragment fragment = new EditRecipeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_INDEX, recipeIndex);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeIndex = EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipeIndex();
        Log.d(TAG, "recipe index is " + recipeIndex);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_RECIPE_INDEX, recipeIndex);

            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.editIngredientsFragmentContainer, EditIngredientsFragment.class, bundle)
                    .commit();
            /*
            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.editStepsFragmentContainer, EditStepsFragment.class, bundle)
                    .commit();

             */
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditRecipeBinding.inflate(inflater, container, false);

        // viewmodel
        RecipeListViewModel listViewModel = new ViewModelProvider(getActivity()).get(RecipeListViewModel.class);
        RecipeViewModel.Factory factory = new RecipeViewModel.Factory(listViewModel.getRecipes().getValue().get(recipeIndex));
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        // populate UI with recipe information
        binding.recipeTitleEditText.setText(viewModel.getRecipe().getValue().getTitle());

        return binding.getRoot();
    }

    private void saveAndReturn() {
        // make sure to handle any title changes
        viewModel.getRecipe().getValue().setTitle(binding.recipeTitleEditText.getText().toString());

        viewModel.saveOrStoreToDB();
        // NavUtils.navigateUpFromSameTask(this);
    }
}