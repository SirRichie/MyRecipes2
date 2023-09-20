package de.lialuna.myrecipes2.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lialuna.myrecipes2.databinding.ListViewIngredientBinding;
import de.lialuna.myrecipes2.databinding.ListViewStepBinding;
import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Step;

public class StepsRecyclerAdapter extends RecyclerView.Adapter<StepsRecyclerAdapter.StepViewHolder> {

    private List<Step> steps;

    public StepsRecyclerAdapter(List<Step> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListViewStepBinding binding = ListViewStepBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StepViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bindTo(steps.get(position));
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        private ListViewStepBinding binding;

        public StepViewHolder(@NonNull ListViewStepBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Step step) {
            binding.stepNumber.setText(Integer.toString(getBindingAdapterPosition() + 1));
            binding.stepText.setText(step.getText());
        }

        @Override
        public String toString() {
            return "StepViewHolder{" +
                    "binding=" + binding +
                    '}';
        }
    }

}
