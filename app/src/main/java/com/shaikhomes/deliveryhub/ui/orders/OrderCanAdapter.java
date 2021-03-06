package com.shaikhomes.deliveryhub.ui.orders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.ItemPojo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class OrderCanAdapter extends RecyclerView.Adapter<OrderCanAdapter.MyViewHolder> {


    Context context;
    OnItemClickListener itemClickListener;
    List<ItemPojo.Item> mJoblist;
    String rupee;


    public OrderCanAdapter(Activity context, List<ItemPojo.Item> mJoblist, OnItemClickListener mItemClickListener) {
        this.context = context;
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
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            int[] COLORS = new int[]{
                    R.color.colorPrimary
            };
            circularProgressDrawable.setColorSchemeColors(COLORS);
            circularProgressDrawable.start();
            String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mJoblist.get(position).getItemImage();
            Picasso.get().load(imgUrl).placeholder(circularProgressDrawable).resize(800, 1200)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.mCanImage);
        }
        holder.mCompName.setText(mJoblist.get(position).getItemName().trim());
        holder.mPrice.setText(rupee + " " + mJoblist.get(position).getItemPrice() + " ");
        holder.mLiters.setText(mJoblist.get(position).getItemSize());
        holder.mItemDsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mJoblist.get(position).getItemDescription())) {
                    new AlertDialog.Builder(context).setTitle("Item Info").setMessage(mJoblist.get(position).getItemDescription()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).create().show();
                } else {
                    Toasty.error(context, "Item description not mentioned", Toasty.LENGTH_SHORT).show();
                }
            }
        });
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
        ImageView mCanImage, mItemDsc;
        LinearLayout mOrderCanLL;


        public MyViewHolder(View v) {
            super(v);
            mCompName = v.findViewById(R.id.name_);
            mPrice = v.findViewById(R.id.price_);
            mLiters = v.findViewById(R.id.liters_);
            mCanImage = v.findViewById(R.id.image_);
            mOrderCanLL = v.findViewById(R.id.order_can_ll);
            mItemDsc = v.findViewById(R.id.item_desc);
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
