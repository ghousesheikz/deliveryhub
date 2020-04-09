package com.shaikhomes.watercan.ui.orders;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.pojo.ItemPojo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderCanAdapter extends RecyclerView.Adapter<OrderCanAdapter.MyViewHolder> {


    Context context;
    OnItemClickListener itemClickListener;
    List<ItemPojo.Item> mJoblist;
    String rupee;


    public OrderCanAdapter(Activity context, List<ItemPojo.Item> mJoblist, OnItemClickListener mItemClickListener) {
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
        if (!TextUtils.isEmpty(mJoblist.get(position).getItemImage())) {
            String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mJoblist.get(position).getItemImage();
            Picasso.get().load(imgUrl).resize(800, 1200)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.mCanImage);
        }
        holder.mCompName.setText(mJoblist.get(position).getItemName());
        holder.mPrice.setText(rupee +" "+ mJoblist.get(position).getItemPrice()+" ");
        holder.mLiters.setText(mJoblist.get(position).getItemSize());

        holder.mOrderCanLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(mJoblist.get(position), position);
            }
        });
        holder.mCanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(mJoblist.get(position), position);
            }
        });


    }

    public interface OnItemClickListener {
        void onItemClick(ItemPojo.Item response, int position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        // return 12;
        return mJoblist == null ? 0 : mJoblist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mCompName, mPrice, mLiters;
        ImageView mCanImage;
        LinearLayout mOrderCanLL;


        public MyViewHolder(View v) {
            super(v);
            mCompName = v.findViewById(R.id.name_);
            mPrice = v.findViewById(R.id.price_);
            mLiters = v.findViewById(R.id.liters_);
            mCanImage = v.findViewById(R.id.image_);
            mOrderCanLL = v.findViewById(R.id.order_can_ll);
        }

    }

    public void updateAdapter(List<ItemPojo.Item> updatelist) {
        this.mJoblist = updatelist;
        notifyDataSetChanged();
    }

    public List<ItemPojo.Item> getlist() {
        return mJoblist;
    }
}
