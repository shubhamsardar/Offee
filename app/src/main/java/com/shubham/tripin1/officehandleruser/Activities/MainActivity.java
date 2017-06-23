package com.shubham.tripin1.officehandleruser.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shubham.tripin1.officehandleruser.Adapters.CoffeeOrdersAdapter;
import com.shubham.tripin1.officehandleruser.Managers.SharedPrefManager;
import com.shubham.tripin1.officehandleruser.Model.CoffeeOrder;
import com.shubham.tripin1.officehandleruser.Model.MyOrder;
import com.shubham.tripin1.officehandleruser.R;
import com.shubham.tripin1.officehandleruser.Utils.CircularImgView;
import com.shubham.tripin1.officehandleruser.holders.OrderHolder;
import com.shubham.tripin1.officehandleruser.holders.OrderListHolder;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtWelcome;
    private SharedPrefManager mSharedPref;
    private Context mContext;
    private RecyclerView mRvOrders;
    private CoffeeOrdersAdapter coffeeOrdersAdapter;
    private TextView mOrderNow,mTxtOrdersInfo;
    private DatabaseReference ref2;
    private RelativeLayout mRvOrderInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mSharedPref = new SharedPrefManager(mContext);
        intView();
        setListners();
        getSupportActionBar().setTitle("Offee - Your Office Coffee!");

        mRvOrders = (RecyclerView) findViewById(R.id.rv_myorders);
        mRvOrders.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(mSharedPref.getUserHpass()).child(mSharedPref.getMobileNo());



        ref2 = FirebaseDatabase.getInstance().getReference()
                .child("pass").child("orders");

    }

    @Override
    protected void onResume() {
        super.onResume();


        coffeeOrdersAdapter = new CoffeeOrdersAdapter(MyOrder.class,R.layout.item_myorder,
                OrderListHolder.class,
                ref2.orderByChild("mUserMobile").equalTo(mSharedPref.getMobileNo()),mContext);
        mRvOrders.setAdapter(coffeeOrdersAdapter);
        if(coffeeOrdersAdapter.getItemCount()==0){
            mTxtOrdersInfo.setText("Your Cart is Empty!");
        }else {
            mRvOrderInfo.setVisibility(View.GONE);
        }
    }

    private void setListners() {

        mOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,CoffeePrefActivity.class));
            }
        });

    }

    private void intView() {
        mTxtWelcome = (TextView)findViewById(R.id.textView_welcome);
        mTxtWelcome.setText("Hi "+mSharedPref.getUserFirstName()+"!"+" Have a Good Time :) ");
        mOrderNow = (TextView)findViewById(R.id.textView_order);
        mTxtOrdersInfo = (TextView)findViewById(R.id.textViewOrderInfo);
        mRvOrderInfo = (RelativeLayout) findViewById(R.id.rv_ordersinfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coffeeOrdersAdapter.cleanup();
    }
}
