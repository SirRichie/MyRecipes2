package de.lialuna.myrecipes2.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lialuna.myrecipes2.databinding.ListViewIngredientBinding;
import de.lialuna.myrecipes2.entity.Ingredient;

public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredients;

    public IngredientsRecyclerAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO support multiple view types
        ListViewIngredientBinding binding = ListViewIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.bindTo(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

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

}
