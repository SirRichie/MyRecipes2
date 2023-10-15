package de.lialuna.myrecipes2.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import de.lialuna.myrecipes2.databinding.ListEditIngredientBinding;
import de.lialuna.myrecipes2.databinding.ListEditIngredientGroupBinding;
import de.lialuna.myrecipes2.entity.Ingredient;

public class EditIngredientsRecyclerAdapter extends RecyclerView.Adapter<EditIngredientsRecyclerAdapter.AbstractEditIngredientViewHolder> {

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_GROUP = 1;
    public static final String TAG = "EditIngredientsRecyclerAdapter";

    private RecyclerViewClickListener clickListener;
    private DeleteIngredientListener deleteIngredientListener;
    private StartDragListener startDragListener;

    private List<Ingredient> ingredients;

    public EditIngredientsRecyclerAdapter(RecyclerViewClickListener clickListener, DeleteIngredientListener deleteIngredientListener) {

        this.clickListener = clickListener;
        this.deleteIngredientListener = deleteIngredientListener;
        // Log.d(TAG, "ingredients (" + ingredients.size() + ") = " + ingredients);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (ingredients == null)
            return 0;
        return ingredients.size();
    }

    @NonNull
    @Override
    public AbstractEditIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                ListEditIngredientBinding binding = ListEditIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new EditIngredientViewHolder(binding, clickListener, deleteIngredientListener);
            case VIEW_TYPE_GROUP:
                ListEditIngredientGroupBinding groupBinding = ListEditIngredientGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new EditIngredientGroupViewHolder(groupBinding, clickListener, deleteIngredientListener);

        }
        throw new IllegalArgumentException("unknown viewType");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull AbstractEditIngredientViewHolder holder, int position) {
        holder.bindTo(ingredients.get(position));


        if (startDragListener != null) {
            holder.getHandle().setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startDragListener.onStartDrag(holder);
                }
                return false;
            });
        }

    }


    @Override
    public int getItemViewType(int position) {
        return ingredients.get(position).isGroupIdentifier() ? VIEW_TYPE_GROUP : VIEW_TYPE_NORMAL;
    }

    public void setStartDragListener(StartDragListener startDragListener) {
        this.startDragListener = startDragListener;
    }

    public void swapIngredients(int fromPosition, int toPosition) {
        Collections.swap(ingredients, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface DeleteIngredientListener {
        void onRemoveIngredient(int position);
    }

    public interface StartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public static abstract class AbstractEditIngredientViewHolder extends RecyclerView.ViewHolder {

        protected WeakReference<RecyclerViewClickListener> clickListenerWeakReference;
        protected WeakReference<DeleteIngredientListener> deleteIngredientListenerWeakReference;

        protected ImageView handle;

        public AbstractEditIngredientViewHolder(@NonNull View itemView, RecyclerViewClickListener clickListener, DeleteIngredientListener deleteListener) {
            super(itemView);

            if (clickListener != null) {
                clickListenerWeakReference = new WeakReference<>(clickListener);
            }

            if (deleteListener != null) {   // if someone's interested in delete events, store the listener reference
                deleteIngredientListenerWeakReference = new WeakReference<>(deleteListener);
            }
        }

        public abstract void bindTo(Ingredient ingredient);

        public ImageView getHandle() {
            return handle;
        }
    }


    public static class EditIngredientViewHolder extends AbstractEditIngredientViewHolder {

        private ListEditIngredientBinding binding;

        private static final String TAG = "EditIngredientViewHolder";

        public EditIngredientViewHolder(@NonNull ListEditIngredientBinding binding, RecyclerViewClickListener clickListener, DeleteIngredientListener deleteListener) {
            super(binding.getRoot(), clickListener, deleteListener);
            this.binding = binding;
            handle = binding.handle;
        }

        public void bindTo(Ingredient ingredient) {
            Log.d(TAG, "binding to " + ingredient);
            binding.listIngredientsAmount.setText(ingredient.getAmount());
            binding.listIngredientsIngredient.setText(ingredient.getIngredient());

            if (clickListenerWeakReference != null && clickListenerWeakReference.get() != null) {
                itemView.setOnClickListener(v -> {
                    Log.d(TAG, "clicked on " + ingredient);
                    clickListenerWeakReference.get().onPositionClicked(getAbsoluteAdapterPosition());
                });
            }

            if (deleteIngredientListenerWeakReference != null) {
                binding.deleteButton.setOnClickListener(v -> {
                    Log.d(TAG, "deleting ingredient with index " + getAbsoluteAdapterPosition());
                    deleteIngredientListenerWeakReference.get().onRemoveIngredient(getAbsoluteAdapterPosition());

                });
            }
        }

        @Override
        public String toString() {
            return "IngredientViewHolder{" +
                    "binding=" + binding +
                    '}';
        }
    }

    public static class EditIngredientGroupViewHolder extends AbstractEditIngredientViewHolder {

        private ListEditIngredientGroupBinding binding;

        public EditIngredientGroupViewHolder(@NonNull ListEditIngredientGroupBinding binding, RecyclerViewClickListener clickListener, DeleteIngredientListener deleteListener) {
            super(binding.getRoot(), clickListener, deleteListener);
            this.binding = binding;
            handle = binding.handle;
        }

        @Override
        public void bindTo(Ingredient ingredient) {
            Log.d(TAG, "binding ingredient group to " + ingredient);
            binding.listIngredientsIngredientgroup.setText(ingredient.getIngredient());

            if (clickListenerWeakReference != null && clickListenerWeakReference.get() != null) {
                itemView.setOnClickListener(v -> {
                    Log.d(TAG, "clicked on " + ingredient);
                    clickListenerWeakReference.get().onPositionClicked(getAbsoluteAdapterPosition());
                });
            }

            if (deleteIngredientListenerWeakReference != null) {
                binding.deleteButton.setOnClickListener(v -> {
                    deleteIngredientListenerWeakReference.get().onRemoveIngredient(getAbsoluteAdapterPosition());
                });
            }
        }
    }

}
