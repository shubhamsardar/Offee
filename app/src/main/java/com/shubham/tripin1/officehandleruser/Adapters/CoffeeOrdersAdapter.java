package com.shubham.tripin1.officehandleruser.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.shubham.tripin1.officehandleruser.Model.MyOrder;
import com.shubham.tripin1.officehandleruser.holders.OrderListHolder;

/**
 * Created by Tripin1 on 6/20/2017.
 */

public class CoffeeOrdersAdapter extends FirebaseRecyclerAdapter<MyOrder,OrderListHolder> {

    private Context mContext;


    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public CoffeeOrdersAdapter(Class<MyOrder> modelClass, int modelLayout, Class<OrderListHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(OrderListHolder viewHolder, MyOrder model, int position) {
        MiniOrderAdapter miniOrderAdapter = new MiniOrderAdapter(model.getmOrderList(),mContext);
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        viewHolder.recyclerView.setAdapter(miniOrderAdapter);
        miniOrderAdapter.notifyDataSetChanged();
        Log.d("debug","populate view holder called.........."+position);
    }
}
