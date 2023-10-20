package de.lialuna.myrecipes2.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import de.lialuna.myrecipes2.adapter.IngredientsRecyclerAdapter;
import de.lialuna.myrecipes2.databinding.FragmentViewIngredientsListBinding;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewIngredientListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewIngredientListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RECIPE_ID = "recipeId";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int recipeIndex;
    private String mParam2;
    private FragmentViewIngredientsListBinding binding;

    public ViewIngredientListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeIndex the position of the recipe in the recipe list
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewIngredientListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewIngredientListFragment newInstance(int recipeIndex, String param2) {
        ViewIngredientListFragment fragment = new ViewIngredientListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeIndex);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeIndex = getArguments().getInt(ARG_RECIPE_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewIngredientsListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecipeListViewModel recipeListViewModel = new ViewModelProvider(getActivity()).get(RecipeListViewModel.class);
        RecipeViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(RecipeViewModel.class);

        IngredientsRecyclerAdapter adapter = new IngredientsRecyclerAdapter(
                viewModel.getRecipe().getValue().getIngredients());

        binding.ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ingredientsRecyclerView.setAdapter(adapter);
    }
}