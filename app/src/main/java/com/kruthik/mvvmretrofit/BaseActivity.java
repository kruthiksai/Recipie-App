package com.kruthik.mvvmretrofit;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public abstract class BaseActivity extends AppCompatActivity {

    public ProgressBar mProgressBar;

    @Override
    public void setContentView(int layoutResID) {

        RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_basr, null);
        FrameLayout frameLayout = relativeLayout.findViewById(R.id.activity_content);
        mProgressBar = relativeLayout.findViewById(R.id.progress_bar);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(relativeLayout);
    }

    public void showProgressBar(boolean visible) {

        mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

}
