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

import de.lialuna.myrecipes2.adapter.StepsRecyclerAdapter;
import de.lialuna.myrecipes2.databinding.FragmentViewStepsListBinding;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewStepListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewStepListFragment extends Fragment {

    private FragmentViewStepsListBinding binding;

    public ViewStepListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewIngredientListFragment.
     */
    public static ViewStepListFragment newInstance() {
        return new ViewStepListFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewStepsListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecipeListViewModel recipeListViewModel = new ViewModelProvider(getActivity()).get(RecipeListViewModel.class);
        RecipeViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(RecipeViewModel.class);

        StepsRecyclerAdapter adapter = new StepsRecyclerAdapter(
                viewModel.getRecipe().getValue().getSteps()
        );

        binding.stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.stepsRecyclerView.setAdapter(adapter);
    }
}