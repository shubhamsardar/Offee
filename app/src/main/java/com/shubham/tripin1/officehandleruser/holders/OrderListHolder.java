package com.shubham.tripin1.officehandleruser.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shubham.tripin1.officehandleruser.R;

/**
 * Created by Tripin1 on 6/21/2017.
 */

public class OrderListHolder extends RecyclerView.ViewHolder {

    public RecyclerView recyclerView;

    public OrderListHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView)itemView.findViewById(R.id.rv_myorderlists);
    }

}