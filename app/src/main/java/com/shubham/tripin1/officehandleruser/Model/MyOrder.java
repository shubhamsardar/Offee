package com.shubham.tripin1.officehandleruser.Model;

import java.util.List;

/**
 * Created by Tripin1 on 6/21/2017.
 */

public class MyOrder {
    private List<CoffeeOrder> mOrderList;
    private String mUserName;
    private String mUserMobile;

    public MyOrder(){
        //for firebase
    }

    public MyOrder(String mUserName , String mUserMobile , List<CoffeeOrder> mOrderList){
        this.mOrderList = mOrderList;
        this.mUserName = mUserName;
        this.mUserMobile = mUserMobile;
    }

    public List<CoffeeOrder> getmOrderList() {
        return mOrderList;
    }

    public void setmOrderList(List<CoffeeOrder> mOrderList) {
        this.mOrderList = mOrderList;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserMobile() {
        return mUserMobile;
    }

    public void setmUserMobile(String mUserMobile) {
        this.mUserMobile = mUserMobile;
    }
}
