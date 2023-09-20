package de.lialuna.myrecipes2.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FABHideOnScrollBehavior extends FloatingActionButton.Behavior {

    private static final String TAG = "FABHideOnScrollBehavior";

    /**
     * if limit percent of the nested scroll have been used, hide the button
     */
    private static double limit = 0.90;

    public FABHideOnScrollBehavior() {
        super();
    }

    public FABHideOnScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes == ViewCompat.SCROLL_AXIS_VERTICAL) ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        // Log.d(TAG, "onNestedScroll: " + dyConsumed + " | " + dyUnconsumed);


        if (target instanceof ScrollingView) {
            ScrollingView scrollingView = (ScrollingView) target;

            // since the view takes up space, only a part of its extend can be scrolled before reaching the end
            double maximumScroll = scrollingView.computeVerticalScrollRange() - scrollingView.computeVerticalScrollExtent();
            double scrollPercentage = scrollingView.computeVerticalScrollOffset() / maximumScroll;
            if (scrollPercentage > limit && child.getVisibility() == View.VISIBLE) { // check if we have scrolled enough to hide the button
                // need to use a listener because hide() sets visibility to GONE, causing CoordinatorLayout to skip this behavior
                child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
//                        fab.setVisibility(View.INVISIBLE);
                        fab.hide();
                    }
                });
            } else if (scrollPercentage < limit && child.getVisibility() != View.VISIBLE) {
                child.show();
            }
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

}
