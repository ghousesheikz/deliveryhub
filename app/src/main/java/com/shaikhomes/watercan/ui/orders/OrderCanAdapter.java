package com.shaikhomes.watercan.ui.orders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.shaikhomes.watercan.R;

import java.util.List;

public class OrderCanAdapter extends RecyclerView.Adapter<OrderCanAdapter.MyViewHolder> {


    Context context;
    OnItemClickListener itemClickListener;
    List<String> mJoblist;
    String rupee;


    public OrderCanAdapter(Activity context, List<String> mJoblist, OnItemClickListener mItemClickListener) {
        this.context = (Activity) context;
        this.mJoblist = mJoblist;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_can_list_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);


    }

    public interface OnItemClickListener {
        void onItemClick(String response, int position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return 12;
      //  return mJoblist == null ? 0 : mJoblist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mCompName, mPrice, mLiters;
        ImageView mCanImage;


        public MyViewHolder(View v) {
            super(v);
            mCompName = v.findViewById(R.id.name_);
            mPrice = v.findViewById(R.id.price_);
            mLiters = v.findViewById(R.id.liters_);
            mCanImage = v.findViewById(R.id.image_);

        }

    }

    public void updateAdapter(List<String> updatelist) {
        this.mJoblist = updatelist;
        notifyDataSetChanged();
    }

    public List<String> getlist() {
        return mJoblist;
    }
}
