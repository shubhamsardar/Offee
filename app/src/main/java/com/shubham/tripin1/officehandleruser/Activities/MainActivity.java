package com.shubham.tripin1.officehandleruser.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubham.tripin1.officehandleruser.Adapters.CoffeeOrdersAdapter;
import com.shubham.tripin1.officehandleruser.Managers.SharedPrefManager;
import com.shubham.tripin1.officehandleruser.Model.CoffeeOrder;
import com.shubham.tripin1.officehandleruser.Model.MyOrder;
import com.shubham.tripin1.officehandleruser.R;
import com.shubham.tripin1.officehandleruser.Utils.CircularImgView;
import com.shubham.tripin1.officehandleruser.holders.OrderHolder;
import com.shubham.tripin1.officehandleruser.holders.OrderListHolder;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtWelcome;
    private SharedPrefManager mSharedPref;
    private Context mContext;
    private RecyclerView mRvOrders;
    private CoffeeOrdersAdapter coffeeOrdersAdapter;
    private TextView mOrderNow,mTxtOrdersInfo;
    private DatabaseReference ref2;
    private RelativeLayout mRvOrderInfo;
    private AVLoadingIndicatorView loadingIndicatorView,onlineindicator;


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


        ref2 = FirebaseDatabase.getInstance().getReference()
                .child(mSharedPref.getUserHpass()).child("orders");

    }

    @Override
    protected void onResume() {
        super.onResume();




        coffeeOrdersAdapter = new CoffeeOrdersAdapter(MyOrder.class,R.layout.item_myorder,
                OrderListHolder.class,
                ref2.orderByChild("mUserMobile").equalTo(mSharedPref.getMobileNo()),mContext);
        mRvOrders.setAdapter(coffeeOrdersAdapter);

        ref2.orderByChild("mUserMobile").equalTo(mSharedPref.getMobileNo()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    mTxtOrdersInfo.setText("Your Cart is Empty, Order Something!");
                    loadingIndicatorView.setVisibility(View.INVISIBLE);
                    ViewAnimator.animate(mOrderNow).pulse().start();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        coffeeOrdersAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if(coffeeOrdersAdapter.getItemCount()==0){
                    mTxtOrdersInfo.setText("Your Cart is Empty!");
                }else {
                    int n = coffeeOrdersAdapter.getItemCount();
                    mTxtOrdersInfo.setText("Track your orders in realtime! ("+n+")");
                    loadingIndicatorView.setVisibility(View.INVISIBLE);
                    onlineindicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                if(coffeeOrdersAdapter.getItemCount()==0){
                    mTxtOrdersInfo.setText("Your Cart is Empty!");
                    ViewAnimator.animate(mOrderNow).pulse().start();

                }else {
                    int n = coffeeOrdersAdapter.getItemCount();
                    mTxtOrdersInfo.setText("Track your orders in realtime! ("+n+")");
                    loadingIndicatorView.setVisibility(View.INVISIBLE);
                    onlineindicator.setVisibility(View.VISIBLE);
                }
            }
        });

        ViewAnimator.animate(mOrderNow).pulse().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.yoursettins, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:{
                startActivity(new Intent(this,Settings.class));
            }
            break;
        }
        return true;
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
        loadingIndicatorView = (AVLoadingIndicatorView)findViewById(R.id.AVLoadingIndicatorView);
        onlineindicator = (AVLoadingIndicatorView)findViewById(R.id.AVLoadingIndicatorView2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coffeeOrdersAdapter.cleanup();
    }
}
