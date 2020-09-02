package com.shaikhomes.deliveryhub.ui.vendordashboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.UserRegistrationPojo;

import java.util.List;

public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.MyViewHolder> {


    Context context;
    VendorListAdapter.OnItemClickListener itemClickListener;
    List<UserRegistrationPojo.UserData> mJoblist;
    String rupee;


    public VendorListAdapter(Activity context, List<UserRegistrationPojo.UserData> mJoblist, VendorListAdapter.OnItemClickListener mItemClickListener) {
        this.context = context;
        this.mJoblist = mJoblist;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
    }

    @Override
    public VendorListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_list_adapter, parent, false);
        return new VendorListAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final VendorListAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);
        holder.mName.setText(mJoblist.get(position).getUsername());
        holder.mNumber.setText(mJoblist.get(position).getUsermobileNumber());
        holder.mAddress.setText(mJoblist.get(position).getAddress());
        holder.mStatus.setText(mJoblist.get(position).getActive());
        holder.mUserid.setText(mJoblist.get(position).getUserid());
        if (mJoblist.get(position).getActive().toLowerCase().equalsIgnoreCase("true")) {
            holder.mStatus.setTextColor(Color.GREEN);
        } else {
            holder.mStatus.setTextColor(Color.RED);
        }
        holder.mActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(mJoblist.get(position), position, "True");
            }
        });
        holder.mDeactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(mJoblist.get(position), position, "False");
            }
        });
        holder.mItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemViewClick(mJoblist.get(position), position);
            }
        });

    }

    public interface OnItemClickListener {
        void onItemClick(UserRegistrationPojo.UserData response, int position, String status);

        void onItemViewClick(UserRegistrationPojo.UserData response, int position);
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
        TextView mName, mNumber, mAddress, mStatus, mUserid;
        AppCompatButton mActive, mDeactive;
        ImageView mItemList;

        public MyViewHolder(View v) {
            super(v);
            mName = v.findViewById(R.id.name);
            mNumber = v.findViewById(R.id.number);
            mAddress = v.findViewById(R.id.address_txt);
            mStatus = v.findViewById(R.id.status);
            mActive = v.findViewById(R.id.btn_active);
            mDeactive = v.findViewById(R.id.btn_deactive);
            mUserid = v.findViewById(R.id.userId);
            mItemList = v.findViewById(R.id.item_list_view);
        }

    }

    public void updateAdapter(List<UserRegistrationPojo.UserData> updatelist) {
        this.mJoblist = updatelist;
        notifyDataSetChanged();
    }

    public List<UserRegistrationPojo.UserData> getlist() {
        return mJoblist;
    }
}
