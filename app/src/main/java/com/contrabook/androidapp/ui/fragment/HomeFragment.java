package com.contrabook.androidapp.ui.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contrabook.androidapp.R;
import com.contrabook.androidapp.common.Constants;
import com.contrabook.androidapp.common.CustomItemDecoration;
import com.contrabook.androidapp.data.DatabaseContract;
import com.contrabook.androidapp.data.ModelItemAdapter;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // callback method implemented by hosting activity
//    public interface Contract {
//        void listItemClick(int position);
//    }

    // private static final int MODEL_ITEM_LOADER = 0;
    private ModelItemAdapter mAdapter;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_recycler, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new CustomItemDecoration(
                getResources().getDimensionPixelOffset(R.dimen.list_item_vertical_margin),
                getResources().getDimensionPixelOffset(R.dimen.list_item_horizontal_margin)
        ));

        ModelItemAdapter.ModelItemClickListener itemClickListener = new ModelItemAdapter.ModelItemClickListener() {
            @Override
            public void onClick(Uri uri) {
                // getContract().listItemClick(position);

                // forward the call up to the hosting fragment
                ((MainFragment)getParentFragment()).listItemClick(uri);
            }

            @Override
            public void onLongClick(Uri uri) {
                // forward the call upto the hosting fragment
                ((MainFragment)getParentFragment()).listItemLongClick(uri);
            }
        };
        mAdapter = new ModelItemAdapter(recyclerView, itemClickListener);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    // called from MainFragment when item has been deleted or edited
    public void updateItemList() {
        mAdapter.notifyDataSetChanged();
    }


    // LoaderCallback impl
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initialize the cursor loader when the hosting activity is created
        getLoaderManager().initLoader(Constants.MODEL_ITEM_LOADER, null, this);
    }

    // impl LoadCallback methods
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.MODEL_ITEM_LOADER:
                return new CursorLoader(getActivity(),
                        DatabaseContract.Model.CONTENT_URI,
                        null,
                        null,
                        null,
                        DatabaseContract.Model.COLUMN_NAME + " COLLATE NOCASE ASC");
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}
