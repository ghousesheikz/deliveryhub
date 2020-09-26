package com.shaikhomes.deliveryhub.ui.myorders;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.OrderDelivery;
import com.shaikhomes.deliveryhub.ui.stores.StoreItemsPojo;
import com.shaikhomes.deliveryhub.ui.stores.OrderItemsListPojo.OrderItemsList;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.MyViewHolder> {


    Context context;
    OrderItemsAdapter.OnItemClickListener itemClickListener;
    List<OrderItemsListPojo.OrderItemsList> mCanList;
    String rupee;
    int mCount = 0;
    DecimalFormat decimalFormat;


    public OrderItemsAdapter(Activity context, List<OrderItemsListPojo.OrderItemsList> mCanList, OrderItemsAdapter.OnItemClickListener mItemClickListener) {
        this.context = context;
        this.mCanList = mCanList;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public OrderItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_orderitem_list_adapter, parent, false);
        return new OrderItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final OrderItemsAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);

       
        holder.mItemName.setText(mCanList.get(position).getItemName());
        holder.mQty.setText(mCanList.get(position).getItemQuantity());
        holder.mTotalPrice.setText(mCanList.get(position).getTotalamount()+"");
        holder.mUnitPrice.setText(mCanList.get(position).getItemprice());
        holder.mOrderType.setText(mCanList.get(position).getVendorAddress());
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = format1.parse(mCanList.get(position).getOrderDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mOrderDate.setText(format2.format(date));

    }

    public interface OnItemClickListener {
        void onItemClick(OrderItemsListPojo.OrderItemsList response, int position);
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
        TextView  mItemName, mQty, mTotalPrice, mUnitPrice,
                mOrderType, mOrderDate;
       

        public MyViewHolder(View v) {
            super(v);
           
            mItemName = v.findViewById(R.id.txt_itemname);
            mQty = v.findViewById(R.id.txt_qty);
            mTotalPrice = v.findViewById(R.id.txt_totalprice);
            mUnitPrice = v.findViewById(R.id.txt_unitprice);

            mOrderType = v.findViewById(R.id.txt_ordertype);
            mOrderDate = v.findViewById(R.id.txt_orderdate);
           

        }

    }

    public void updateAdapter(List<OrderItemsListPojo.OrderItemsList> updatelist) {
        this.mCanList = updatelist;
        notifyDataSetChanged();
    }

    public List<OrderItemsListPojo.OrderItemsList> getlist() {
        return mCanList;
    }
}
