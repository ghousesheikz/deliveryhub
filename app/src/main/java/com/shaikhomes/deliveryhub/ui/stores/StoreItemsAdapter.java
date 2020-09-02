package com.shaikhomes.deliveryhub.ui.stores;

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
import com.shaikhomes.deliveryhub.ui.orders.OrderCanAdapter;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class StoreItemsAdapter extends RecyclerView.Adapter<StoreItemsAdapter.MyViewHolder> {


    Context context;
    StoreItemsAdapter.OnItemClickListener itemClickListener;
    List<StoreItemsPojo.StoreItemsList> mJoblist;
    String rupee;
    int mCount = 0;
    DecimalFormat df ;


    public StoreItemsAdapter(Activity context, List<StoreItemsPojo.StoreItemsList> mJoblist, StoreItemsAdapter.OnItemClickListener mItemClickListener) {
        this.context = context;
        this.mJoblist = mJoblist;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
        df = new DecimalFormat("0.00");
    }

    @Override
    public StoreItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_itemslist_adapter, parent, false);
        return new StoreItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final StoreItemsAdapter.MyViewHolder holder, final int position) {
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
            String imgUrl = "http://deliveryhub.shaikhomes.com/ImageStorage/" + mJoblist.get(position).getItemImage();
            Picasso.get().load(imgUrl).placeholder(circularProgressDrawable)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).fit()
                    .into(holder.mCanImage);
        }
        holder.mCompName.setText(mJoblist.get(position).getItemName().trim());
       // holder.mPrice.setText(rupee + " " + mJoblist.get(position).getItemPrice() + " ");
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
       /* holder.mOrderCanLL.setOnClickListener(new View.OnClickListener() {
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
        });*/

        mCount = mJoblist.get(position).getItemCount();
        holder.mPlusCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount = mJoblist.get(position).getItemCount();
                mCount = mCount + 1;
                int amt = Integer.parseInt(mJoblist.get(position).getItemPrice());
                amt = mCount * amt;
                mJoblist.get(position).setTotalAmt(amt);
                mJoblist.get(position).setItemCount(mCount);
              //  holder.mPrice.setText("₹ " + amt + " ");
                holder.mCanCount.setText(String.valueOf(mCount));
                itemClickListener.onItemClick(mJoblist, position);
            }
        });
        int amt = Integer.parseInt(mJoblist.get(position).getItemPrice());
        holder.mPrice.setText("₹ " + df.format(amt));
        holder.mMinusCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount = mJoblist.get(position).getItemCount();
                mCount = mCount - 1;
                if (mCount < 0) {
                    mCount = 0;
                    int amt = Integer.parseInt(mJoblist.get(position).getItemPrice());
                    amt = mCount * amt;
                    mJoblist.get(position).setTotalAmt(amt);
                    mJoblist.get(position).setItemCount(mCount);

                    holder.mCanCount.setText(String.valueOf(mCount));
                    itemClickListener.onItemClick(mJoblist, position);
                } else {
                    int amt = Integer.parseInt(mJoblist.get(position).getItemPrice());
                    amt = mCount * amt;
                    mJoblist.get(position).setTotalAmt(amt);
                    mJoblist.get(position).setItemCount(mCount);
                   // holder.mPrice.setText("₹ " + df.format(amt) + " ");
                    holder.mCanCount.setText(String.valueOf(mCount));
                    itemClickListener.onItemClick(mJoblist, position);
                }
            }
        });




    }

    public interface OnItemClickListener {
        void onItemClick(List<StoreItemsPojo.StoreItemsList> response, int position);
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
        TextView mCompName, mPrice,mCanCount;
        ImageView mPlusCnt, mMinusCnt;
        ImageView mCanImage, mItemDsc;
        LinearLayout mOrderCanLL;


        public MyViewHolder(View v) {
            super(v);
            mCompName = v.findViewById(R.id.name_);
            mPrice = v.findViewById(R.id.price_);
            mCanImage = v.findViewById(R.id.image_);
            mOrderCanLL = v.findViewById(R.id.order_can_ll);
            mItemDsc = v.findViewById(R.id.item_desc);
            mCanCount = v.findViewById(R.id.can_count);
            mPlusCnt = v.findViewById(R.id.plus_count);
            mMinusCnt = v.findViewById(R.id.minus_count);
        }

    }

    public void updateAdapter(List<StoreItemsPojo.StoreItemsList> updatelist) {
        this.mJoblist = updatelist;
        notifyDataSetChanged();
    }

    public List<StoreItemsPojo.StoreItemsList> getlist() {
        return mJoblist;
    }
}
