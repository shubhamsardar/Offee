package com.shubham.tripin1.officehandleruser.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shubham.tripin1.officehandleruser.Managers.SharedPrefManager;
import com.shubham.tripin1.officehandleruser.R;

public class SplashActivity extends Activity {

    TextView mTxtOffee, mTxtYo;
    LinearLayout mLinPass;
    EditText mEditTextPass;
    FloatingActionButton fab;
    SharedPrefManager mSharedPrefManager;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSharedPrefManager = new SharedPrefManager(getApplicationContext());
        mContext = getApplicationContext();
        initView();
        setListners();

        if(mSharedPrefManager.getUserHpass().isEmpty()){
            mLinPass.setVisibility(View.VISIBLE);
        }else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(mContext,RegActivity.class));
                    finish();
                }
            }, 3000);
        }
    }

    private void setListners() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mEditTextPass.getText().toString().isEmpty()){
                    Toast.makeText(mContext,"Enter Valid Password",Toast.LENGTH_LONG).show();
                }else {
                    mSharedPrefManager.setUserHpass(mEditTextPass.getText().toString().trim());
                    startActivity(new Intent(mContext,RegActivity.class));
                    finish();
                }

            }
        });
    }

    private void initView() {
        mEditTextPass = (EditText)findViewById(R.id.editText_hpass);
        mLinPass = (LinearLayout)findViewById(R.id.ll_pass);
        mTxtOffee =(TextView)findViewById(R.id.textViewOffee);
        mTxtYo = (TextView)findViewById(R.id.textView_yo);
        fab = (FloatingActionButton)findViewById(R.id.fabpass);
    }
}
