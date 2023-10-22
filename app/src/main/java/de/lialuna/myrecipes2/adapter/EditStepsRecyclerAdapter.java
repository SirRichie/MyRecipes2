package de.lialuna.myrecipes2.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.lialuna.myrecipes2.databinding.ListEditStepBinding;
import de.lialuna.myrecipes2.entity.Step;

public class EditStepsRecyclerAdapter extends RecyclerView.Adapter<EditStepsRecyclerAdapter.EditStepViewHolder> {

    private static final String TAG = "EditStepListAdapter";

    private List<Step> steps;
    private final RecyclerViewClickListener clickListener;
    private final DeleteStepListener deleteListener;
    private StartDragListener startDragListener;

    public EditStepsRecyclerAdapter(RecyclerViewClickListener clickListener, DeleteStepListener deleteListener) {
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EditStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListEditStepBinding binding = ListEditStepBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EditStepViewHolder(binding, clickListener, deleteListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(EditStepViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.bindTo(step);

        if (startDragListener != null) {
            holder.binding.handle.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startDragListener.onStartDrag(holder);
                }
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        if (steps == null)
            return 0;
        return steps.size();
    }

    public void setStartDragListener(StartDragListener startDragListener) {
        this.startDragListener = startDragListener;
    }

    public void addStep(Step step) {
        steps.add(step);
        notifyItemInserted(steps.size() - 1);
    }

    public void swapSteps(int fromPosition, int toPosition) {
        Collections.swap(steps, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface DeleteStepListener {
        void onStepDeleted(int position);
    }

    public interface StartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public static class EditStepViewHolder extends RecyclerView.ViewHolder {


        private static final String TAG = "EditStepViewHolder";

        protected final ListEditStepBinding binding;

        private WeakReference<RecyclerViewClickListener> clickListenerWeakReference;
        private WeakReference<DeleteStepListener> deleteStepListenerWeakReference;

        public EditStepViewHolder(@NonNull ListEditStepBinding binding, RecyclerViewClickListener clickListener, DeleteStepListener deleteStepListener) {
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
            binding.stepNumber.setText(String.format(Locale.GERMANY, "%d", getBindingAdapterPosition() + 1));

            if (clickListenerWeakReference != null) {
                itemView.setOnClickListener(v -> {
                    Log.d(TAG, "clicked on " + step);
                    clickListenerWeakReference.get().onPositionClicked(getBindingAdapterPosition());
                });
            }

            if (deleteStepListenerWeakReference != null) {
                binding.deleteButton.setOnClickListener(v -> deleteStepListenerWeakReference.get().onStepDeleted(getBindingAdapterPosition()));
            }
        }
    }
}
