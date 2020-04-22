package com.shaikhomes.watercan.ui.orders;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {


    Context context;
    OrderListAdapter.OnItemClickListener itemClickListener;
    List<OrderCalculationPojo> mCanList;
    String rupee;
    int mCount = 0;
    DecimalFormat decimalFormat;


    public OrderListAdapter(Activity context, List<OrderCalculationPojo> mCanList, OrderListAdapter.OnItemClickListener mItemClickListener) {
        this.context = (Activity) context;
        this.mCanList = mCanList;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_adapter, parent, false);
        return new OrderListAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final OrderListAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);
        if (!TextUtils.isEmpty(mCanList.get(position).getImageURL())) {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            int[] COLORS = new int[]{
                    R.color.colorPrimary
            };
            circularProgressDrawable.setColorSchemeColors(COLORS);
            circularProgressDrawable.start();
            String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mCanList.get(position).getImageURL();
            Picasso.get().load(imgUrl).resize(800, 1200).placeholder(circularProgressDrawable)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.mCanImage);
        }
        holder.mItemName.setText(mCanList.get(position).getName());
        holder.mItemDesc.setText(mCanList.get(position).getDescription());
        holder.mTxtDistributor.setText(mCanList.get(position).getVendorName());
        mCount = mCanList.get(position).getNoOfCans();

        holder.mCanCount.setText(String.valueOf(mCount));
        holder.mCanAmt.setText("Amount : ₹" + mCanList.get(position).getUnitAmount());
        holder.mPlusCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount = mCanList.get(position).getNoOfCans();
                mCount = mCount + 1;
                int amt = Integer.parseInt(mCanList.get(position).getPrice());
                amt = mCount * amt;
                mCanList.get(position).setUnitAmount(amt);
                mCanList.get(position).setNoOfCans(mCount);
                holder.mCanAmt.setText("Amount : ₹" + amt + " ");
                holder.mCanCount.setText(String.valueOf(mCount));
                itemClickListener.onItemClick(mCanList, position);
            }
        });

        holder.mMinusCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount = mCanList.get(position).getNoOfCans();
                mCount = mCount - 1;
                if(mCount<Integer.parseInt(mCanList.get(position).getMinQty())){
                    mCount=Integer.parseInt(mCanList.get(position).getMinQty());
                }
                if (mCount < 0) {
                    mCount = 0;
                    int amt = Integer.parseInt(mCanList.get(position).getPrice());
                    amt = mCount * amt;
                    mCanList.get(position).setUnitAmount(amt);
                    mCanList.get(position).setNoOfCans(mCount);
                    holder.mCanAmt.setText("Amount : ₹" + decimalFormat.format(amt) + " ");
                    holder.mCanCount.setText(String.valueOf(mCount));
                    mCanList.get(position).setItemcount(mCount);
                    itemClickListener.onItemClick(mCanList, position);
                } else {
                    int amt = Integer.parseInt(mCanList.get(position).getPrice());
                    amt = mCount * amt;
                    mCanList.get(position).setUnitAmount(amt);
                    mCanList.get(position).setNoOfCans(mCount);
                    holder.mCanAmt.setText("Amount : ₹" + decimalFormat.format(amt) + " ");
                    holder.mCanCount.setText(String.valueOf(mCount));
                    itemClickListener.onItemClick(mCanList, position);
                }
            }
        });


    }

    public interface OnItemClickListener {
        void onItemClick(List<OrderCalculationPojo> response, int position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return mCanList.size();
        //  return mCanList == null ? 0 : mCanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mCanCount, mCanAmt,mTxtDistributor,mItemName,mItemDesc;
        ImageView mPlusCnt, mMinusCnt, mCanImage;

        public MyViewHolder(View v) {
            super(v);
            mCanCount = v.findViewById(R.id.can_count);
            mPlusCnt = v.findViewById(R.id.plus_count);
            mMinusCnt = v.findViewById(R.id.minus_count);
            mCanAmt = v.findViewById(R.id.txt_amt);
            mCanImage = v.findViewById(R.id.image_);
            mTxtDistributor = v.findViewById(R.id.distributor_name);
            mItemName = v.findViewById(R.id.item_name);
            mItemDesc = v.findViewById(R.id.item_desc);

        }

    }

    public void updateAdapter(List<OrderCalculationPojo> updatelist) {
        this.mCanList = updatelist;
        notifyDataSetChanged();
    }

    public List<OrderCalculationPojo> getlist() {
        return mCanList;
    }
}
