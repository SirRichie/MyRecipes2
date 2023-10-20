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

    private FragmentViewIngredientsListBinding binding;

    public ViewIngredientListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewIngredientListFragment.
     */
    public static ViewIngredientListFragment newInstance() {
        return new ViewIngredientListFragment();
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