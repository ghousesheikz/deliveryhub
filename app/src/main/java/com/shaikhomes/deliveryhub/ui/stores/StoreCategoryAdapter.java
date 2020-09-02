package com.shaikhomes.deliveryhub.ui.stores;

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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.CategoryPojo;
import com.shaikhomes.deliveryhub.ui.orders.CategoryAdapter;
import com.shaikhomes.deliveryhub.utility.RoundedTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class StoreCategoryAdapter extends RecyclerView.Adapter<StoreCategoryAdapter.MyViewHolder> {


    Context context;
    StoreCategoryAdapter.OnItemClickListener itemClickListener;
    List<StoreCategoryPojo.StoreCategoryDetail> mCanList;
    String rupee;
    int mCount = 0;
    DecimalFormat decimalFormat;


    public StoreCategoryAdapter(Activity context, List<StoreCategoryPojo.StoreCategoryDetail> mCanList, StoreCategoryAdapter.OnItemClickListener mItemClickListener) {
        this.context = context;
        this.mCanList = mCanList;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public StoreCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_adapter, parent, false);
        return new StoreCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final StoreCategoryAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);

        if (!TextUtils.isEmpty(mCanList.get(position).getCategoryImage())) {
            if (mCanList.get(position).getCategoryImage().toLowerCase().equalsIgnoreCase("all")) {
                Picasso.get().load(R.drawable.all_categories).resize(300, 300)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .centerCrop().transform(new RoundedTransformation())
                        .into(holder.mImgCategory);
            } else {
                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                int[] COLORS = new int[]{
                        R.color.colorPrimary
                };
                circularProgressDrawable.setColorSchemeColors(COLORS);
                circularProgressDrawable.start();
                String imgUrl = "http://deliveryhub.shaikhomes.com/ImageStorage/" + mCanList.get(position).getCategoryImage();
                Picasso.get().load(imgUrl).resize(300, 300).placeholder(circularProgressDrawable)
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

    public void updateAdapter(List<StoreCategoryPojo.StoreCategoryDetail> updatelist) {
        this.mCanList = updatelist;
        notifyDataSetChanged();
    }

    public List<StoreCategoryPojo.StoreCategoryDetail> getlist() {
        return mCanList;
    }
}
