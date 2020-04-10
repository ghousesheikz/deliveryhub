package com.shaikhomes.watercan.ui.orders;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.pojo.CategoryPojo;
import com.shaikhomes.watercan.pojo.OrderDelivery;
import com.shaikhomes.watercan.utility.RoundedTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {


    Context context;
    CategoryAdapter.OnItemClickListener itemClickListener;
    List<CategoryPojo.CategoryDetail> mCanList;
    String rupee;
    int mCount = 0;
    DecimalFormat decimalFormat;


    public CategoryAdapter(Activity context, List<CategoryPojo.CategoryDetail> mCanList, CategoryAdapter.OnItemClickListener mItemClickListener) {
        this.context = (Activity) context;
        this.mCanList = mCanList;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_adapter, parent, false);
        return new CategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);

        if (!TextUtils.isEmpty(mCanList.get(position).getCategoryImage())) {
            if (mCanList.get(position).getCategoryImage().toLowerCase().equalsIgnoreCase("all")) {
                Picasso.get().load(R.drawable.all_categories).resize(300, 300)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .centerCrop().transform(new RoundedTransformation())
                        .into(holder.mImgCategory);
            } else {
                String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mCanList.get(position).getCategoryImage();
                Picasso.get().load(imgUrl).resize(300, 300)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .centerCrop().transform(new RoundedTransformation())
                        .into(holder.mImgCategory);
            }
        }
        holder.mTxtCategory.setText(mCanList.get(position).getCategoryName());
        holder.mCatLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(mCanList.get(position), position);
            }
        });


       /* holder.mOrderId.setText("DH00"+mCanList.get(position).getOrderId());
        holder.mItemName.setText(mCanList.get(position).getItemName());
        holder.mNoofCans.setText(mCanList.get(position).getItemQuantity());
        holder.mTotalPrice.setText(mCanList.get(position).getTotalamount());
        holder.mUnitPrice.setText(mCanList.get(position).getItemprice());
        holder.mOrderType.setText(mCanList.get(position).getTypeoforder());
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = format1.parse(mCanList.get(position).getOrderDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mOrderDate.setText(format2.format(date));
        holder.mPaymentStatus.setText(mCanList.get(position).getPaidStatus());
        holder.mOrderStatus.setText(mCanList.get(position).getOrderStatus());
        holder.mPaymentMethod.setText(mCanList.get(position).getPaymentType());*/

    }

    public interface OnItemClickListener {
        void onItemClick(CategoryPojo.CategoryDetail response, int position);
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
        TextView mTxtCategory;
        ImageView mImgCategory;
        RelativeLayout mCatLL;


        public MyViewHolder(View v) {
            super(v);
            mTxtCategory = v.findViewById(R.id.category_txt);
            mImgCategory = v.findViewById(R.id.category_img);
            mCatLL = v.findViewById(R.id.category_);


        }

    }

    public void updateAdapter(List<CategoryPojo.CategoryDetail> updatelist) {
        this.mCanList = updatelist;
        notifyDataSetChanged();
    }

    public List<CategoryPojo.CategoryDetail> getlist() {
        return mCanList;
    }
}
