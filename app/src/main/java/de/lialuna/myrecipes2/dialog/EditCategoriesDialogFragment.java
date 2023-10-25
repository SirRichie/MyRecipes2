package de.lialuna.myrecipes2.dialog;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import de.lialuna.myrecipes2.R;
import de.lialuna.myrecipes2.databinding.DialogEditCategoriesBinding;
import de.lialuna.myrecipes2.databinding.ListCategorySelectBinding;

public class EditCategoriesDialogFragment extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogEditCategoriesBinding binding = DialogEditCategoriesBinding.inflate(inflater);

        CategoryAdapter adapter = new CategoryAdapter(
                getResources().getStringArray(R.array.categories_names),
                getResources().obtainTypedArray(R.array.categories_icons)
        );

        binding.categorySelectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.categorySelectionRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    public static class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        private final String[] items;
        private final TypedArray icons;


        public CategoryAdapter(String[] items, TypedArray icons) {
            this.items = items;
            this.icons = icons;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListCategorySelectBinding binding = ListCategorySelectBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new CategoryViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            holder.bindTo(items[position], icons.getDrawable(position));
        }

        @Override
        public int getItemCount() {
            return items.length;
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ListCategorySelectBinding binding;

        public CategoryViewHolder(ListCategorySelectBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bindTo(String category, Drawable icon) {
            binding.checkBox.setText(category);
            icon.setTint(itemView.getResources().getColor(R.color.colorPrimary, null));
            binding.imageView.setImageDrawable(icon);
        }
    }
}
