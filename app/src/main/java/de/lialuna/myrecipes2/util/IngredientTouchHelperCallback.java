package de.lialuna.myrecipes2.util;

import static androidx.recyclerview.widget.ItemTouchHelper.DOWN;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;
import static androidx.recyclerview.widget.ItemTouchHelper.UP;

import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Tobias on 04.02.2018.
 */

public class IngredientTouchHelperCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "IngredientTouchHelperCallback";

    public interface MoveListener {
        void onItemMove(int fromPosition, int toPosition);
    }

    public interface SwipeListener {
        void onItemSwipe(int position);
    }

    private MoveListener moveListener;
    private SwipeListener swipeListener;

    public IngredientTouchHelperCallback(MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(UP | DOWN, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (moveListener != null)
            moveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

//    @Override
//    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//        if (viewHolder != null && viewHolder instanceof EditIngredientViewHolder) {
//            final View foregroundView = ((EditIngredientViewHolder) viewHolder).getForegroundView();
//
//            getDefaultUIUtil().onSelected(foregroundView);
//        }
//        super.onSelectedChanged(viewHolder, actionState);
//    }
//
//    @Override
//    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if (viewHolder instanceof EditIngredientViewHolder) {
//            final View foregroundView = ((EditIngredientViewHolder) viewHolder).getForegroundView();
//            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
//        } else {
//            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
//    }
//
//    @Override
//    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        if (viewHolder instanceof EditIngredientViewHolder) {
//            final View foregroundView = ((EditIngredientViewHolder) viewHolder).getForegroundView();
//            getDefaultUIUtil().clearView(foregroundView);
//        } else {
//            super.clearView(recyclerView, viewHolder);
//        }
//    }
//
//    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if (viewHolder instanceof EditIngredientViewHolder) {
//            final View foregroundView = ((EditIngredientViewHolder) viewHolder).getForegroundView();
//
//            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
//                    actionState, isCurrentlyActive);
//        } else {
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
//    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG, "onSwiped direction = " + direction);
        if (swipeListener != null) {
            swipeListener.onItemSwipe(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    public void setSwipeListener(SwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }
}
