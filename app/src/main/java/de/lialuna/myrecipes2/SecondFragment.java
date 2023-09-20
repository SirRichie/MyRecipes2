package de.lialuna.myrecipes2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.databinding.FragmentSecondBinding;
import de.lialuna.myrecipes2.databinding.ListTextBinding;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.viewmodel.RecipeListViewModel;

public class SecondFragment extends Fragment {

    public static final String TAG = SecondFragment.class.getSimpleName();

    protected List<Recipe> recipes = new ArrayList<>();
    private RecipeListViewModel recipeListViewModel;

    public class TextViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        public TextViewHolder(ListTextBinding binding) {
            super(binding.getRoot());

            textView = binding.textView;
        }

        public void bindTo(String text) {
            textView.setText(text);
        }
    }

    public class TextAdapter extends RecyclerView.Adapter<TextViewHolder> {

        public String[] items = new String[] {"first", "second", "third"};

        @NonNull
        @Override
        public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListTextBinding binding = ListTextBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

            return new TextViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
            holder.bindTo(recipes.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            if (recipes == null) return 0;
            return recipes.size();
        }
    }

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
        recipeListViewModel = new ViewModelProvider(getActivity()).get(RecipeListViewModel.class);
        recipes = recipeListViewModel.getRecipes().isInitialized() ? recipeListViewModel.getRecipes().getValue() : new ArrayList<>();
        Log.d(TAG, "recipes has " + recipes.size() + " entries");

        recipeListViewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                Log.d(TAG, "received new recipes list with " + recipes.size() + " entries");
                SecondFragment.this.recipes = recipes;
                binding.recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recyclerView.setAdapter(new TextAdapter());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}