package badeeb.com.daringo.adapters;

import android.view.View;

/**
 * Created by meldeeb on 12/6/17.
 */

public interface OnRecyclerItemClick<T> {
    void OnRecyclerItemClick(View view, T item, int position);
}
