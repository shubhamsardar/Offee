package com.shubham.tripin1.officehandleruser.holders;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shubham.tripin1.officehandleruser.R;

/**
 * Created by Tripin1 on 6/20/2017.
 */

public class PrefCoffeeHolder  extends RecyclerView.ViewHolder  {
    private TextView mNameField;
    private TextView mNumberField;
    public TextView mTapInfo;
    public TextView mPrice;


    public FloatingActionButton fabAdd,fabMinus;
    public LinearLayout llNumber;
    public ImageView imgThumb;


    public PrefCoffeeHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.textView_name);
        mNumberField = (TextView) itemView.findViewById(R.id.textView_number);
        fabAdd = (FloatingActionButton)itemView.findViewById(R.id.fab_plus);
        fabMinus = (FloatingActionButton)itemView.findViewById(R.id.fab_minus);
        llNumber = (LinearLayout)itemView.findViewById(R.id.ll_numbers);
        imgThumb = (ImageView)itemView.findViewById(R.id.imageView_thumb);
        mTapInfo = (TextView)itemView.findViewById(R.id.textViewClickInfo);
        mPrice = (TextView)itemView.findViewById(R.id.textViewPrice);
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

  }
