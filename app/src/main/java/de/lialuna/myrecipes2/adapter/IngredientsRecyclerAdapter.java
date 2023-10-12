package de.lialuna.myrecipes2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lialuna.myrecipes2.databinding.ListViewIngredientBinding;
import de.lialuna.myrecipes2.databinding.ListViewIngredientGroupBinding;
import de.lialuna.myrecipes2.entity.Ingredient;

public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.AbstractIngredientViewHolder> {

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_GROUP = 1;

    private List<Ingredient> ingredients;

    public IngredientsRecyclerAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public AbstractIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                ListViewIngredientBinding binding = ListViewIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new IngredientViewHolder(binding);
            case VIEW_TYPE_GROUP:
                ListViewIngredientGroupBinding groupBinding = ListViewIngredientGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new IngredientGroupViewHolder(groupBinding);

        }
        throw new IllegalArgumentException("Unknown viewType");
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractIngredientViewHolder holder, int position) {
        holder.bindTo(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ingredients.get(position).isGroupIdentifier() ? VIEW_TYPE_GROUP : VIEW_TYPE_NORMAL;
    }

    public abstract static class AbstractIngredientViewHolder extends RecyclerView.ViewHolder {

        public AbstractIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bindTo(Ingredient ingredient);
    }

    public static class IngredientViewHolder extends AbstractIngredientViewHolder {

        private ListViewIngredientBinding binding;

        public IngredientViewHolder(@NonNull ListViewIngredientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Ingredient ingredient) {
            binding.amount.setText(ingredient.getAmount());
            binding.ingredient.setText(ingredient.getIngredient());
        }

        @Override
        public String toString() {
            return "IngredientViewHolder{" +
                    "binding=" + binding +
                    '}';
        }
    }

    public static class IngredientGroupViewHolder extends AbstractIngredientViewHolder {
        private ListViewIngredientGroupBinding binding;

        public IngredientGroupViewHolder(ListViewIngredientGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Ingredient ingredient) {
            binding.ingredientGroup.setText(ingredient.getIngredient());
        }
    }

}
