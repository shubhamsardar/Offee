package com.shubham.tripin1.officehandleruser.Activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubham.tripin1.officehandleruser.Adapters.CoffrrPrefRecyAdapter;
import com.shubham.tripin1.officehandleruser.Communicators.OnCoffeeSelected;
import com.shubham.tripin1.officehandleruser.Managers.SharedPrefManager;
import com.shubham.tripin1.officehandleruser.Model.CoffeeOrder;
import com.shubham.tripin1.officehandleruser.Model.MyOrder;
import com.shubham.tripin1.officehandleruser.Model.OrderNotification;
import com.shubham.tripin1.officehandleruser.R;
import com.shubham.tripin1.officehandleruser.holders.PrefCoffeeHolder;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class CoffeePrefActivity extends AppCompatActivity implements OnCoffeeSelected {

    private RecyclerView mRvOrders;
    private CoffrrPrefRecyAdapter mPrefAdapter;
    private SharedPrefManager mSharedPref;
    private Context mContext;
    private List<CoffeeOrder> mFullList,mSelectedList;
    private Button mPlaceOrder;
    private DatabaseReference ref2, refNofication;
    private AVLoadingIndicatorView loadingIndicatorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_pref);


        mFullList = new ArrayList<>();
        mSelectedList = new ArrayList<>();

        mContext = getApplicationContext();
        mSharedPref = new SharedPrefManager(mContext);
        intView();
        setListners();
        mPlaceOrder.setText("Fetching List...");
        getSupportActionBar().setTitle("Offee - Select your stuff!");

        mRvOrders = (RecyclerView) findViewById(R.id.rv_preflist);
        mRvOrders.setLayoutManager(new LinearLayoutManager(this));
        mPrefAdapter = new CoffrrPrefRecyAdapter(mContext,mFullList,this);
        mRvOrders.setAdapter(mPrefAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(mSharedPref.getUserHpass()).child("available_coffee");

        ref2 = FirebaseDatabase.getInstance().getReference()
                .child(mSharedPref.getUserHpass()).child("orders");
        refNofication = FirebaseDatabase.getInstance().getReference()
                .child("orders");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    mPlaceOrder.setText("Select Something!");
                    ViewAnimator.animate(mPlaceOrder).pulse().start();
                    loadingIndicatorView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CoffeeOrder c = dataSnapshot.getValue(CoffeeOrder.class);
                mFullList.add(c);
                Log.d("Firebase","on Chlild added");
                mPrefAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                CoffeeOrder c = dataSnapshot.getValue(CoffeeOrder.class);
                mFullList.remove(c);
                Log.d("Firebase","on Chlild removed");
                mPrefAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void intView() {

        mPlaceOrder = (Button)findViewById(R.id.button_placeorder);
        loadingIndicatorView = (AVLoadingIndicatorView)findViewById(R.id.prefLoder);

    }


    private void setListners() {

        mPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPlaceOrder.setText("Sending Request!");

                if(mSelectedList.size()!=0){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateTime = dateFormat.format(new Date()); // Find todays date
                    MyOrder order = new MyOrder(mSharedPref.getUserName(),
                            mSharedPref.getMobileNo(),mSelectedList,currentDateTime);
                    OrderNotification orderNotification = new OrderNotification(mSharedPref.getUserName(),mSharedPref.getUserHpass());
                    refNofication.push().setValue(orderNotification);
                    ref2.push().setValue(order);
                    mPlaceOrder.setText("Order Placed!");
                    ViewAnimator.animate(mPlaceOrder).pulse().duration(1000).start().onStop(new AnimationListener.Stop() {
                        @Override
                        public void onStop() {
                            finish();
                        }
                    });


                }else {
                    mPlaceOrder.setText("Kuch to Select!");
                }

            }
        });
    }


    @Override
    public void onCoffeeSelected(CoffeeOrder coffeeOrder) {

        Boolean isNew = true;

        for(int i=0;i<mSelectedList.size();i++){
            CoffeeOrder c = mSelectedList.get(i);
            if(c.getmCoffeeName().equalsIgnoreCase(coffeeOrder.getmCoffeeName())){
                if(coffeeOrder.getmCoffeeNumber().equalsIgnoreCase("0")){
                    mSelectedList.remove(i);
                }else {
                    c.setmCoffeeNumber(coffeeOrder.getmCoffeeNumber());
                    mSelectedList.set(i,c);
                }

                isNew = false;
            }
        }

        if(isNew){
            mSelectedList.add(coffeeOrder);
        }

        if(mSelectedList.size()!=0){
            mPlaceOrder.setText("Place Order!");

        }else {
            mPlaceOrder.setText("Select Items!");
        }

        Log.v("Selected List : ", mSelectedList.toString());

    }
}
