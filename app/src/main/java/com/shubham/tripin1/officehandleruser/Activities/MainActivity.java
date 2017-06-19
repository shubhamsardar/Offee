package com.shubham.tripin1.officehandleruser.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.shubham.tripin1.officehandleruser.Managers.SharedPrefManager;
import com.shubham.tripin1.officehandleruser.R;
import com.shubham.tripin1.officehandleruser.Utils.CircularImgView;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtWelcome;
    private SharedPrefManager mSharedPref;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mSharedPref = new SharedPrefManager(mContext);
        intView();
        getSupportActionBar().setTitle("Offee - Your Office Coffee!");
    }

    private void intView() {
        mTxtWelcome = (TextView)findViewById(R.id.textView_welcome);
        mTxtWelcome.setText("Hi "+mSharedPref.getUserFirstName()+"!"+" Have a Good Time :) ");
    }
}
