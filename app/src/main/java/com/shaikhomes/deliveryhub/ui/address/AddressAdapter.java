package com.shaikhomes.deliveryhub.ui.address;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.AddressPojo;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {


    Context context;
    AddressAdapter.OnItemClickListener itemClickListener;
    List<AddressPojo> mJoblist;
    String rupee;


    public AddressAdapter(Activity context, List<AddressPojo> mJoblist, AddressAdapter.OnItemClickListener mItemClickListener) {
        this.context = context;
        this.mJoblist = mJoblist;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
    }

    @Override
    public AddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list_adapter, parent, false);
        return new AddressAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final AddressAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);
        holder.mAddType.setText(mJoblist.get(position).getAddressType());

        holder.mAddDetails.setText(mJoblist.get(position).getFlatNumber() + ", " + mJoblist.get(position).getApartmentName() + ", " + mJoblist.get(position).getLandmark() + ", " +
                mJoblist.get(position).getAreaName() + ", " + mJoblist.get(position).getCityName());

        holder.mSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(mJoblist.get(position), position);
            }
        });

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemDelete(mJoblist.get(position), position);
            }
        });


    }

    public interface OnItemClickListener {
        void onItemClick(AddressPojo response, int position);
        void onItemDelete(AddressPojo response, int position);
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
        TextView mAddType, mAddDetails;
        LinearLayout mSelectAddress;
        ImageView mDelete;

        public MyViewHolder(View v) {
            super(v);
            mAddType = v.findViewById(R.id.add_type);
            mAddDetails = v.findViewById(R.id.add_details);
            mSelectAddress = v.findViewById(R.id.select_address_ll);
            mDelete = v.findViewById(R.id.delete_address);
        }

    }

    public void updateAdapter(List<AddressPojo> updatelist) {
        this.mJoblist = updatelist;
        notifyDataSetChanged();
    }

    public List<AddressPojo> getlist() {
        return mJoblist;
    }
}
