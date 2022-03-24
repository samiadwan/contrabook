package com.contrabook.androidapp.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import com.contrabook.androidapp.R;
import com.contrabook.androidapp.common.Constants;
import com.contrabook.androidapp.data.DatabaseContract;
import com.contrabook.androidapp.event.MessageEvent;
import com.contrabook.androidapp.ui.fragment.ModelItemFragment;

import de.greenrobot.event.EventBus;

public class ModelItemActivity extends AppCompatActivity implements
        ModelItemFragment.Contract{

    // impl contract methods
    @Override
    public void saveModelItem(ContentValues values) {
        // DEBUG
        // String name = values.getAsString(DatabaseContract.Model.COLUMN_NAME);
        // String address = values.getAsString(DatabaseContract.Model.COLUMN_ADDRESS);
        // String url = values.getAsString(DatabaseContract.Model.COLUMN_URL);
        // String email = values.getAsString(DatabaseContract.Model.COLUMN_EMAIL);
        // String phone = values.getAsString(DatabaseContract.Model.COLUMN_PHONE);
        // Timber.i("%s: name: %s, address: %s, url: %s, email: %s, phone: %s",
        //        Constants.LOG_TAG, name, address, url, email, phone);

        Uri newRecordUri = getContentResolver().insert(DatabaseContract.Model.CONTENT_URI, values);
        if (newRecordUri != null) {
            EventBus.getDefault().postSticky(new MessageEvent(Constants.MESSAGE_SUCCESS_SAVING_ITEM));
        } else {
            EventBus.getDefault().postSticky(new MessageEvent(Constants.MESSAGE_ERROR_SAVING_ITEM));
        }
        finish();
    }

    @Override
    public void updateModelItem(Uri itemUri, ContentValues values) {
        // update item record in database
        int numRows = getContentResolver().update(itemUri, values, null, null);
        if (numRows > 0) {
            EventBus.getDefault().postSticky(new MessageEvent(Constants.MESSAGE_SUCCESS_UPDATING_ITEM));
        } else {
           EventBus.getDefault().postSticky(new MessageEvent(Constants.MESSAGE_ERROR_UPDATING_ITEM));
        }
        finish();
    }

    @Override
    public void quit() {
        finish();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, ModelItemActivity.class);
        activity.startActivity(intent);
    }

    public static void launch(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, ModelItemActivity.class);
        intent.putExtra(Constants.MODEL_ITEM_URI, uri);
        activity.startActivity(intent);
    }

    private CoordinatorLayout mLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_item);
        mLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        Uri itemUri = getIntent().getParcelableExtra(Constants.MODEL_ITEM_URI);
        ModelItemFragment fragment =
                (ModelItemFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            if (itemUri == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, ModelItemFragment.newInstance())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, ModelItemFragment.newInstance(itemUri))
                        .commit();
            }
        }
    }
}
