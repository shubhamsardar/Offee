package com.shubham.tripin1.officehandleruser.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shubham.tripin1.officehandleruser.R;

/**
 * Created by Tripin1 on 6/20/2017.
 */


public class OrderHolder extends RecyclerView.ViewHolder {
    private TextView mNameField;
    private TextView mNumberField;
    private TextView mStatusField;


    public OrderHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.textView_cofee_type);
        mNumberField = (TextView) itemView.findViewById(R.id.textView_coffeNo);
        mStatusField = (TextView) itemView.findViewById(R.id.textViewOrderStatus);

    }

    public TextView getmNameField() {
        return mNameField;
    }

    public void setmNameField(TextView mNameField) {
        this.mNameField = mNameField;
    }

    public TextView getmNumberField() {
        return mNumberField;
    }

    public void setmNumberField(TextView mNumberField) {
        this.mNumberField = mNumberField;
    }

    public TextView getmStatusField() {
        return mStatusField;
    }

    public void setmStatusField(TextView mStatusField) {
        this.mStatusField = mStatusField;
    }
}
