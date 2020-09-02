package com.shaikhomes.deliveryhub.ui.stores;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.UserRegistrationPojo;
import com.shaikhomes.deliveryhub.ui.vendordashboard.VendorListAdapter;
import com.shaikhomes.deliveryhub.utility.RoundedTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.MyViewHolder> {


    Context context;
    StoreListAdapter.OnItemClickListener itemClickListener;
    List<StoreListPojo.ShopsList> mJoblist;
    String rupee;


    public StoreListAdapter(Activity context, List<StoreListPojo.ShopsList> mJoblist, StoreListAdapter.OnItemClickListener mItemClickListener) {
        this.context = context;
        this.mJoblist = mJoblist;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
    }

    @Override
    public StoreListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list_adapter, parent, false);
        return new StoreListAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final StoreListAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);
        holder.mName.setText("" + mJoblist.get(position).getShopName());
        holder.mMinOrder.setText("Min Order : " + mJoblist.get(position).getMinOrderAmt());
        holder.mAddress.setText(mJoblist.get(position).getAddress());
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        int[] COLORS = new int[]{
                R.color.colorPrimary
        };
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();
        String imgUrl = "http://deliveryhub.shaikhomes.com/ImageStorage/" + mJoblist.get(position).getShopImg();
        Picasso.get().load(imgUrl).placeholder(circularProgressDrawable)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .fit()
                .into(holder.mStoreImage);
        holder.mStoreLL.setOnClickListener(v -> {
            itemClickListener.onItemClick(mJoblist.get(position), position);
        });
    }

    public interface OnItemClickListener {
        void onItemClick(StoreListPojo.ShopsList response, int position);

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
        TextView mName, mAddress, mMinOrder;
        CardView mStoreLL;
        ImageView mStoreImage;

        public MyViewHolder(View v) {
            super(v);
            mName = v.findViewById(R.id.store_name);
            mStoreLL = v.findViewById(R.id.store_layout);
            mAddress = v.findViewById(R.id.store_address);
            mMinOrder = v.findViewById(R.id.store_minorder);
            mStoreImage = v.findViewById(R.id.store_image);
        }

    }

    public void updateAdapter(List<StoreListPojo.ShopsList> updatelist) {
        this.mJoblist = updatelist;
        notifyDataSetChanged();
    }

    public List<StoreListPojo.ShopsList> getlist() {
        return mJoblist;
    }
}
