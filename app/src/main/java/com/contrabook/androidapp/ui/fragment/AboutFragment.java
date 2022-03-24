package com.contrabook.androidapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.contrabook.androidapp.R;
import com.contrabook.androidapp.common.fragments.BaseFragment;

public class AboutFragment extends BaseFragment{

    public AboutFragment() {}

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextPlaceholder.setText(String.format("%s fragment", getString(R.string.nav_menu_title_about)));
    }


}
