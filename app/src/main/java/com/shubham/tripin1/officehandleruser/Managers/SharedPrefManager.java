package com.shubham.tripin1.officehandleruser.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Tripin1 on 6/15/2017.
 */

public class SharedPrefManager {

    public static final String PREF_MOBILE_NUMBER = "user_mobile_no";
    public static final String PREF_USER_FIRSTNAME = "user_first_name";
    public static final String PREF_USER_COMPNAME = "user_comp_name";

    public static final String PREF_USER_LASTNAME = "user_last_name";
    public static final String PREF_USER_REG = "user_reg";
    public static final String PREF_USER_PASS = "user_pass";


    private SharedPreferences mSharedPref;
    private Context mContext;
    private static SharedPreferences.Editor editor;


    public SharedPrefManager(Context context){
        mContext = context;
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = mSharedPref.edit();
    }

    /**
     * @return
     */
    public String getUserId() {
        String userId = mSharedPref.getString(PREF_USER_REG, null);
        return userId;
    }

    /**
     * @param reginfo
     */
    public void setUserReg(String reginfo) {
        editor.putString(PREF_USER_REG, reginfo);
        editor.commit();
    }
    public String getUserReg() {
        String reginfo = mSharedPref.getString(PREF_USER_REG, "");
        return reginfo;
    }

    public void setUserHpass(String reginfo) {
        editor.putString(PREF_USER_PASS, reginfo);
        editor.commit();
    }
    public String getUserHpass() {
        String reginfo = mSharedPref.getString(PREF_USER_PASS, "");
        return reginfo;
    }

    public String getMobileNo() {
        String mobileNo = mSharedPref.getString(PREF_MOBILE_NUMBER, null);
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        editor.putString(PREF_MOBILE_NUMBER, mobileNo);
        editor.commit();
    }

    public String getUserFirstName() {
        String userName = mSharedPref.getString(PREF_USER_FIRSTNAME, null);
        return userName;
    }

    public void setUserName(String userName) {
        editor.putString(PREF_USER_FIRSTNAME, userName);
        editor.commit();
    }

    public String getUserlastName() {
        String userName = mSharedPref.getString(PREF_USER_LASTNAME, null);
        return userName;
    }

    public void setUserLastName(String userName) {
        editor.putString(PREF_USER_LASTNAME, userName);
        editor.commit();
    }

    public String getUserName() {
        return getUserFirstName()+" "+getUserlastName();
    }

    public void setUserCompany(String userCompany) {
        editor.putString(PREF_USER_COMPNAME, userCompany);
        editor.commit();
    }
}
