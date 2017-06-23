package com.shubham.tripin1.officehandleruser.Model;

/**
 * Created by Tripin1 on 6/20/2017.
 */

public class CoffeeOrder {
    private String mCoffeeName;
    private String mCoffeeNumber;
    private String mOrderStatus;

    public CoffeeOrder(){
        //for firebase
    }

    public String getmCoffeeName() {
        return mCoffeeName;
    }

    public void setmCoffeeName(String mCoffeeName) {
        this.mCoffeeName = mCoffeeName;
    }

    public String getmCoffeeNumber() {
        return mCoffeeNumber;
    }

    public void setmCoffeeNumber(String mCoffeeNumber) {
        this.mCoffeeNumber = mCoffeeNumber;
    }

    public String getmOrderStatus() {
        return mOrderStatus;
    }

    public void setmOrderStatus(String mOrderStatus) {
        this.mOrderStatus = mOrderStatus;
    }


}
