/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.app;

import android.support.v17.leanback.R;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnChildSelectedListener;
import android.support.v17.leanback.widget.OnItemClickedListener;
import android.support.v17.leanback.widget.OnItemSelectedListener;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.Log;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Wrapper fragment for leanback details screens.
 */
public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";
    private static boolean DEBUG = false;

    private final RowsFragment mRowsFragment = new RowsFragment();

    private int mContainerListMarginLeft;
    private int mContainerListWidth;
    private int mContainerListAlignTop;
    private OnItemSelectedListener mExternalOnItemSelectedListener;
    private int mSelectedPosition = -1;

    /**
     * Sets the list of rows for the fragment.
     */
    public void setAdapter(ObjectAdapter adapter) {
        mRowsFragment.setAdapter(adapter);
    }

    /**
     * Returns the list of rows.
     */
    public ObjectAdapter getAdapter() {
        return mRowsFragment.getAdapter();
    }

    /**
     * Sets an item selection listener.
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mExternalOnItemSelectedListener = listener;
    }

    /**
     * Sets an item Clicked listener.
     */
    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mRowsFragment.setOnItemClickedListener(listener);
    }

    /**
     * Returns the item Clicked listener.
     */
    public OnItemClickedListener getOnItemClickedListener() {
        return mRowsFragment.getOnItemClickedListener();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContainerListMarginLeft = (int) getResources().getDimension(
                R.dimen.lb_details_rows_margin_left);
        mContainerListWidth =  getResources().getDimensionPixelSize(R.dimen.lb_details_rows_width);
        mContainerListAlignTop =
            getResources().getDimensionPixelSize(R.dimen.lb_details_rows_align_top);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lb_details_fragment, container, false);
    }

    private OnItemSelectedListener mRowSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(Object item, Row row) {
            if (mExternalOnItemSelectedListener != null) {
                mExternalOnItemSelectedListener.onItemSelected(item, row);
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getChildFragmentManager().findFragmentById(R.id.details_container_dock) == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.details_container_dock, mRowsFragment).commit();
            mRowsFragment.setOnItemSelectedListener(mRowSelectedListener);
        }
    }

    private void setVerticalGridViewLayout(VerticalGridView listview) {
        // align the top edge of item to a fixed position
        listview.setItemAlignmentOffset(0);
        listview.setItemAlignmentOffsetPercent(VerticalGridView.ITEM_ALIGN_OFFSET_PERCENT_DISABLED);
        listview.setWindowAlignmentOffset(mContainerListAlignTop);
        listview.setWindowAlignmentOffsetPercent(VerticalGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED);
        listview.setWindowAlignment(VerticalGridView.WINDOW_ALIGN_NO_EDGE);
    }

    /**
     * Setup dimensions that are only meaningful when the child Fragments are inside
     * DetailsFragment.
     */
    private void setupChildFragmentLayout() {
        VerticalGridView containerList = mRowsFragment.getVerticalGridView();
        setVerticalGridViewLayout(containerList);

        mRowsFragment.getVerticalGridView().getLayoutParams().width = mContainerListWidth;
        mRowsFragment.getVerticalGridView().requestLayout();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupChildFragmentLayout();
        mRowsFragment.getView().requestFocus();
    }
}