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
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.lialuna.myrecipes2.databinding.FragmentViewRecipeBinding;
import de.lialuna.myrecipes2.viewmodel.RecipeListViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RECIPE_INDEX = "recipeIndex";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ViewRecipeFragment";

    // TODO: Rename and change types of parameters
    private int recipeIndex;
    private String mParam2;
    private de.lialuna.myrecipes2.databinding.FragmentViewRecipeBinding binding;

    public ViewRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeIndex the position of the recipe in the recipe list
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewRecipeFragment newInstance(int recipeIndex, String param2) {
        ViewRecipeFragment fragment = new ViewRecipeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_INDEX, recipeIndex);
        args.putString(ARG_PARAM2, param2);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false);
        RecipeListViewModel viewModel = new ViewModelProvider(requireActivity()).get(RecipeListViewModel.class);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewRecipeStateAdapter adapter = new ViewRecipeStateAdapter(this);
        binding.pager.setAdapter(adapter);
    }

    public class ViewRecipeStateAdapter extends FragmentStateAdapter {

        public ViewRecipeStateAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return ViewIngredientListFragment.newInstance(recipeIndex, "");
                case 1:
                    return ViewStepListFragment.newInstance(recipeIndex, "");
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