package com.shubham.tripin1.officehandleruser.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Base64;
import android.util.Log;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;
import com.shubham.tripin1.officehandleruser.Managers.SharedPrefManager;
import com.shubham.tripin1.officehandleruser.R;
import com.shubham.tripin1.officehandleruser.Utils.CircularImgView;
import com.shubham.tripin1.officehandleruser.Utils.ImageUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class RegActivity extends AppCompatActivity implements ImageUtils.ImagePickerListener, VerificationListener {

    private EditText mEditTextFname;
    private EditText mEditTextLname;
    private EditText mEditTextPhone;
    private EditText mEditTextOTP;

    private CircularImgView mImgBtn;
    private FloatingActionButton fab;
    private Boolean mBoolImgSet;
    private Context mContext;
    private SharedPrefManager mSharedPrefManager;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri mFilePath;
    ImageUtils mImageUtils;
    private StorageReference mStorageRef;
    private Verification mVerification;
    private Button mSendOTP;
    private RelativeLayout rlOTP;
    private TextView mTxtInfo, mTxtUploadPic;
    private Boolean mBoolOTPVarified = false;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_NETWORK_STATE};

    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
    static final int REQUEST_CODE_ASK_PERMISSIONS = 1002;
    final int PIC_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        checkPermissions();
        initView();
        setListeners();
        mContext = getApplicationContext();
        mSharedPrefManager = new SharedPrefManager(mContext);
        mImageUtils = new ImageUtils(this);
        mBoolImgSet = false;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (!mSharedPrefManager.getUserReg().isEmpty()) {
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }

        getSupportActionBar().setTitle("One Time Registration - Offee");
        generateKey();

    }



    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (mEditTextLname.getText().toString().isEmpty()
                        || mEditTextFname.getText().toString().isEmpty()
                        || mEditTextPhone.getText().toString().isEmpty()
                        || mEditTextOTP.getText().toString().isEmpty()
                        || !mBoolImgSet) {

                    if (mBoolOTPVarified) {
                        uploadFile();
                    } else {
                        Toast.makeText(mContext, "Fill up all fields bro!", Toast.LENGTH_LONG).show();
                        if (!mBoolImgSet) {
                            mTxtInfo.setText("Set Your Profile pic!");
                        }
                    }
                } else {
                    if(mBoolOTPVarified){
                        Toast.makeText(mContext, "Fill up all fields bro!", Toast.LENGTH_LONG).show();
                        if (!mBoolImgSet) {
                            mTxtInfo.setText("Set Your Profile pic!");
                        }
                    }else {
                        verifyOTP(mEditTextOTP.getText().toString().trim());

                    }
                }
            }
        });

        mImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageUtils.imagepicker(1);
            }
        });

        mSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP(mEditTextPhone.getText().toString().trim());
                hideKeyboard();
            }
        });
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void sendOTP(String mobile) {

        if (!mEditTextPhone.getText().toString().isEmpty()) {
            mVerification = SendOtpVerification.createSmsVerification(mContext, mobile.toString(), this, "91");
            mVerification.initiate();
            mSendOTP.setText("WAIT...");
            mTxtInfo.setText("Verifying...");
        } else {
            Toast.makeText(mContext, "Number Empty!", Toast.LENGTH_LONG).show();
        }
    }

    private void verifyOTP(String otp) {
        mVerification.verify(otp); //verifying otp for given number
    }

    private void initView() {
        mEditTextFname = (EditText) findViewById(R.id.editTextfname);
        mEditTextLname = (EditText) findViewById(R.id.editTextlaname);
        mEditTextPhone = (EditText) findViewById(R.id.editTextmobile);
        mEditTextOTP = (EditText) findViewById(R.id.et_otp);
        mImgBtn = (CircularImgView) findViewById(R.id.imageButton);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mSendOTP = (Button) findViewById(R.id.button2);
        rlOTP = (RelativeLayout) findViewById(R.id.relativeLayoutOTP);
        mTxtInfo = (TextView) findViewById(R.id.text_info);
        mTxtUploadPic = (TextView) findViewById(R.id.textViewUploadpic);

    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            mImageUtils.onActivityResult(requestCode, resultCode, data);
        }

        if(requestCode == 1){
            mImageUtils.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == PIC_CROP) {
            Bundle extras = data.getExtras();
            mFilePath = data.getData();
            Bitmap thePic;
            thePic = extras.getParcelable("data");
            mImgBtn.setImageBitmap(thePic);
            mBoolImgSet = true;
            mTxtUploadPic.setText("Looking good there! :D ");
        }
    }

    @Override
    public void onPicked(int from, String filename, Bitmap file, Uri uri) {
        performCrop(uri);
    }

    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (mFilePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child("userImages/" + mEditTextPhone.getText().toString().trim() + ".jpg");
            riversRef.putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "Registration Success", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(mContext, MainActivity.class));
                            mSharedPrefManager.setMobileNo(mEditTextPhone.getText().toString());
                            mSharedPrefManager.setUserName(mEditTextFname.getText().toString());
                            mSharedPrefManager.setUserLastName(mEditTextLname.getText().toString());
                            mSharedPrefManager.setUserReg("1");
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            UploadTask.TaskSnapshot t = taskSnapshot;
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + (int) progress + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    @Override
    public void onInitiated(String response) {

        Log.v("On Initiated", response);
        rlOTP.setVisibility(View.VISIBLE);
        mTxtInfo.setText("Wait for the OTP, Will Try To AutoFill here..");
        mSendOTP.setText("RESEND");

    }

    @Override
    public void onInitiationFailed(Exception paramException) {
        Log.v("On Initiated failed", paramException.toString());
        mTxtInfo.setText("Failed to Intialize OTP, try again..");
        mSendOTP.setText("VERIFY");


    }

    @Override
    public void onVerified(String response) {

        if (mBoolImgSet) {
            mTxtInfo.setText("Verification Successful.. Good to go!");
            uploadFile();
        } else {
            mTxtInfo.setText("Verification Successful..Set Image");
        }
        mSendOTP.setText("DONE");
        mBoolOTPVarified = true;
        mEditTextOTP.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onVerificationFailed(Exception paramException) {
        Log.v("On Verification Failed", paramException.toString());

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

    private void performCrop(Uri picUri) {

        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }

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
        Log.i("SecretKey = ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }
}
