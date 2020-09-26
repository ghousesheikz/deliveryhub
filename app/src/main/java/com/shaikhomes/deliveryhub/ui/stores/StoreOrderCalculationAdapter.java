package com.shaikhomes.deliveryhub.ui.stores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.utility.RoundedTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class StoreOrderCalculationAdapter extends RecyclerView.Adapter<StoreOrderCalculationAdapter.MyViewHolder> {


    Context context;
    StoreCategoryAdapter.OnItemClickListener itemClickListener;
    List<StoreOrderItemsPojo> mJoblist;
    String rupee;
    int mCount = 0;
    DecimalFormat decimalFormat;


    public StoreOrderCalculationAdapter(Activity context, List<StoreOrderItemsPojo> mCanList, StoreCategoryAdapter.OnItemClickListener mItemClickListener) {
        this.context = context;
        this.mJoblist = mCanList;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public StoreOrderCalculationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ordercal_list_adapter, parent, false);
        return new StoreOrderCalculationAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final StoreOrderCalculationAdapter.MyViewHolder holder, final int position) {
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
            Picasso.get().load(imgUrl).placeholder(circularProgressDrawable)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).fit()
                    .into(holder.mCanImage);
        }
        holder.mCompName.setText(mJoblist.get(position).getItemName().trim());
        holder.mQty.setText("Qty : "+mJoblist.get(position).getItemCount().trim());
         holder.mPrice.setText("₹ " +  mJoblist.get(position).getItemprice() + " ");
        holder.mPrice.setPaintFlags(holder.mPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.mSellingPrice.setText("₹ " + mJoblist.get(position).getSellingPrice());
         holder.mTotalPrice.setText("Total : "+rupee + " " + mJoblist.get(position).getTotalamount() + " ");
        holder.mItemDsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public interface OnItemClickListener {
        void onItemClick(StoreCategoryPojo.StoreCategoryDetail response, int position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return mJoblist.size();
        //  return mCanList == null ? 0 : mCanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mCompName, mPrice, mTotalPrice,mQty,mSellingPrice;
        ImageView mPlusCnt, mMinusCnt;
        ImageView mCanImage, mItemDsc;


        public MyViewHolder(View v) {
            super(v);
            mCompName = v.findViewById(R.id.name_);
            mQty = v.findViewById(R.id.qty_);
            mPrice = v.findViewById(R.id.price_);
            mCanImage = v.findViewById(R.id.image_);
            mTotalPrice = v.findViewById(R.id.price_total);
            mItemDsc = v.findViewById(R.id.item_desc);
            mSellingPrice = v.findViewById(R.id.sellingprice_);


        }

    }

    public void updateAdapter(List<StoreOrderItemsPojo> updatelist) {
        this.mJoblist = updatelist;
        notifyDataSetChanged();
    }

    public List<StoreOrderItemsPojo> getlist() {
        return mJoblist;
    }


}
