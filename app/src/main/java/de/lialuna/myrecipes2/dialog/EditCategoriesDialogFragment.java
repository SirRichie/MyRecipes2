package de.lialuna.myrecipes2.dialog;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.lialuna.myrecipes2.R;
import de.lialuna.myrecipes2.databinding.DialogEditCategoriesBinding;
import de.lialuna.myrecipes2.databinding.ListCategorySelectBinding;
import de.lialuna.myrecipes2.entity.Category;
import de.lialuna.myrecipes2.viewmodel.RecipeViewModel;

public class EditCategoriesDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = "EditCategoriesDialogFragment";
    private DialogEditCategoriesBinding binding;
    private List<String> selectedCategories;
    private String[] categoryNames;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        selectedCategories = new ArrayList<>();
        categoryNames = getResources().getStringArray(R.array.categories_names);
        binding = DialogEditCategoriesBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecipeViewModel viewModel = new ViewModelProvider(requireParentFragment()).get(RecipeViewModel.class);
        readSelectedCategories(viewModel);

        CategoryAdapter adapter = new CategoryAdapter(
                categoryNames,
                getResources().obtainTypedArray(R.array.categories_icons),
                viewModel
        );

        binding.categorySelectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.categorySelectionRecyclerView.setAdapter(adapter);

        binding.buttonOK.setOnClickListener(v -> {
            writeSelectedCategories(viewModel);
            dismiss();
        });
        binding.buttonCancel.setOnClickListener(v -> dismiss());
    }

    public void onCategorySelected(int position) {
        selectedCategories.add(categoryNames[position]);
        Log.d(TAG, "added " + categoryNames[position] + "; selectedCategories = " + selectedCategories);
    }

    public void onCategoryUnselected(int position) {
        selectedCategories.remove(categoryNames[position]);
        Log.d(TAG, "removed " + categoryNames[position] + "; selectedCategories = " + selectedCategories);
    }

    private void readSelectedCategories(RecipeViewModel viewModel) {
        Log.d(TAG, "reading categories " + viewModel.getRecipe().getValue().getCategories());
        selectedCategories = viewModel.getRecipe().getValue().getCategories()
                .parallelStream()
                .map(c -> c.getName())
                .collect(Collectors.toList());
        Log.d(TAG, "selectedCategories = " + selectedCategories);
    }

    private void writeSelectedCategories(RecipeViewModel viewModel) {
        List<Category> categories = selectedCategories.stream().map(Category::new).collect(Collectors.toList());
        viewModel.setCategories(categories);
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        private final String[] items;
        private final TypedArray icons;
        private RecipeViewModel viewModel;


        public CategoryAdapter(String[] items, TypedArray icons, RecipeViewModel viewModel) {
            this.items = items;
            this.icons = icons;
            this.viewModel = viewModel;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListCategorySelectBinding binding = ListCategorySelectBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new CategoryViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            holder.bindTo(items[position], icons.getDrawable(position), selectedCategories);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ListCategorySelectBinding binding;

        public CategoryViewHolder(ListCategorySelectBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bindTo(String category, Drawable icon, List<String> selectedCategories) {
            binding.checkBox.setText(category);
            binding.checkBox.setChecked(selectedCategories.contains(category));

            icon.setTint(itemView.getResources().getColor(R.color.colorPrimary, null));
            binding.imageView.setImageDrawable(icon);

            binding.checkBox.setOnCheckedChangeListener((buttonView, checked) -> {
                if (checked) {
                    onCategorySelected(getBindingAdapterPosition());
                } else {
                    onCategoryUnselected(getBindingAdapterPosition());
                }
            });
        }
    }
}
