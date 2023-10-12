package de.lialuna.myrecipes2.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import de.lialuna.myrecipes2.databinding.ListEditStepBinding;
import de.lialuna.myrecipes2.entity.Step;

public class EditStepsRecyclerAdapter extends RecyclerView.Adapter<EditStepsRecyclerAdapter.EditStepViewHolder> {

    private static final String TAG = "EditStepListAdapter";

    private List<Step> steps;
    private final RecyclerViewClickListener clickListener;
    private final DeleteListener deleteListener;

    public EditStepsRecyclerAdapter(List<Step> steps, RecyclerViewClickListener clickListener, DeleteListener deleteListener) {
        this.steps = steps;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @Override
    public EditStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListEditStepBinding binding = ListEditStepBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EditStepViewHolder(binding, clickListener, deleteListener);
    }

    @Override
    public void onBindViewHolder(EditStepViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.bindTo(step);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void addStep(Step step) {
        steps.add(step);
        notifyItemInserted(steps.size() - 1);
    }

    public interface DeleteListener {
        void onStepDeleted(int position);
    }

    public class EditStepViewHolder extends RecyclerView.ViewHolder {


        private static final String TAG = "EditStepViewHolder";

        private final ListEditStepBinding binding;

        private WeakReference<RecyclerViewClickListener> clickListenerWeakReference;
        private WeakReference<DeleteListener> deleteStepListenerWeakReference;

        public EditStepViewHolder(@NonNull ListEditStepBinding binding, RecyclerViewClickListener clickListener, DeleteListener deleteStepListener) {
            super(binding.getRoot());
            this.binding = binding;

            if (clickListener != null) {
                clickListenerWeakReference = new WeakReference<>(clickListener);
            }

            if (deleteStepListener != null) {
                this.deleteStepListenerWeakReference = new WeakReference<>(deleteStepListener);
            }

        }

        public void bindTo(Step step) {
            binding.stepText.setText(step.getText());

            if (clickListenerWeakReference != null) {
                itemView.setOnClickListener(v -> {
                    Log.d(TAG, "clicked on " + step);
                    clickListenerWeakReference.get().onPositionClicked(getAdapterPosition());
                });
            }

            if (deleteStepListenerWeakReference != null) {
                binding.deleteButton.setOnClickListener(v -> deleteStepListenerWeakReference.get().onStepDeleted(getAdapterPosition()));
            }
        }
    }
}
