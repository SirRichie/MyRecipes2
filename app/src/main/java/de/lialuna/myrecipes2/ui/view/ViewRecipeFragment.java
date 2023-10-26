package de.lialuna.myrecipes2.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.lialuna.myrecipes2.databinding.FragmentViewRecipeBinding;
import de.lialuna.myrecipes2.viewmodel.RecipeListViewModel;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRecipeFragment extends Fragment {

    private static final String ARG_RECIPE_INDEX = "recipeIndex";

    private static final String TAG = "ViewRecipeFragment";

    private int recipeIndex;
    private de.lialuna.myrecipes2.databinding.FragmentViewRecipeBinding binding;

    public ViewRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeIndex the position of the recipe in the recipe list
     * @return A new instance of fragment ViewRecipeFragment.
     */
    public static ViewRecipeFragment newInstance(int recipeIndex) {
        ViewRecipeFragment fragment = new ViewRecipeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_INDEX, recipeIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeIndex = ViewRecipeFragmentArgs.fromBundle(getArguments()).getRecipeIndex();
        /*if (getArguments() != null) {
            recipeIndex = getArguments().getInt(ARG_RECIPE_INDEX);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        Log.d(TAG, "recipe index is " + recipeIndex);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false);

        // viewmodel
        RecipeListViewModel listViewModel = new ViewModelProvider(requireActivity()).get(RecipeListViewModel.class);

        RecipeViewModel.Factory factory = new RecipeViewModel.Factory(listViewModel.getRecipes().getValue().get(recipeIndex));
        // we don't use the viewmodel here, but we need to create it so children have access to it
        new ViewModelProvider(this, factory).get(RecipeViewModel.class);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewRecipeStateAdapter adapter = new ViewRecipeStateAdapter(this);
        binding.pager.setAdapter(adapter);
    }

    public static class ViewRecipeStateAdapter extends FragmentStateAdapter {

        public ViewRecipeStateAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return ViewIngredientListFragment.newInstance();
                case 1:
                    return ViewStepListFragment.newInstance();
                default:
                    throw new IllegalStateException("Should never be here");
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}