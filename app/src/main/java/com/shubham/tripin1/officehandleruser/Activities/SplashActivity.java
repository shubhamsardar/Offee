package com.shubham.tripin1.officehandleruser.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.shubham.tripin1.officehandleruser.Managers.SharedPrefManager;
import com.shubham.tripin1.officehandleruser.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.shubham.tripin1.officehandleruser.Activities.RegActivity.REQUEST_CODE_ASK_PERMISSIONS;

public class SplashActivity extends Activity {

    TextView mTxtOffee, mTxtYo, mTxtScanQr;
    LinearLayout mLinPass;
    EditText mEditTextPass;
    FloatingActionButton fab;
    SharedPrefManager mSharedPrefManager;
    Context mContext;
    private IntentIntegrator qrScan;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.ACCESS_NETWORK_STATE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSharedPrefManager = new SharedPrefManager(getApplicationContext());
        mContext = getApplicationContext();
        initView();
        setListners();
        mTxtYo.setVisibility(View.INVISIBLE);
        generateKey();
        checkPermissions();


        //intializing scan object
        qrScan = new IntentIntegrator(this);

        ViewAnimator.animate(mTxtOffee).translationY(100,0).alpha(0,1).decelerate().onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                mTxtYo.setVisibility(View.VISIBLE);
                ViewAnimator.animate(mTxtYo).alpha(0,1).duration(500).onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        if(mSharedPrefManager.getUserHpass().isEmpty()){
                            mLinPass.setVisibility(View.VISIBLE);
                            mTxtScanQr.setVisibility(View.VISIBLE);
                            ViewAnimator.animate(mTxtScanQr).pulse().duration(1000).start();

                        }else {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(mContext,RegActivity.class));
                                    finish();
                                }
                            }, 1000);
                        }

                    }
                }).start();
            }
        }).duration(2000).start();
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

        mTxtScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });
    }

    private void initView() {
        mEditTextPass = (EditText)findViewById(R.id.editText_hpass);
        mLinPass = (LinearLayout)findViewById(R.id.ll_pass);
        mTxtOffee =(TextView)findViewById(R.id.textViewOffee);
        mTxtYo = (TextView)findViewById(R.id.textView_yo);
        fab = (FloatingActionButton)findViewById(R.id.fabpass);
        mTxtScanQr = (TextView)findViewById(R.id.textViewScanqr);
    }

    private void generateKey() {

        MessageDigest md = null;
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        Log.i("SecretKey g========= ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    if(obj.has("pass")&&obj.has("comp")){
                        mSharedPrefManager.setUserHpass(obj.getString("pass"));
                        mSharedPrefManager.setUserCompany(obj.getString("comp"));
                        Toast.makeText(getApplicationContext(),"Scan Success",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(mContext,RegActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void checkPermissions() {
        Log.d("checkPermissions", "Inside");
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            Log.d("checkPermissions", "missingPermissions is not empty");
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions,
                    REQUEST_CODE_ASK_PERMISSIONS);
        } else {
//            Logger.v(" premissions already granted ");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d("onRequestPermissions", "Inside");
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
//                Logger.v("all premissions granted from dialog");
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }
}
