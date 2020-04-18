package com.shaikhomes.watercan.ui.employeedashboard;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.pojo.OrderDelivery;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeOrderAdapter extends RecyclerView.Adapter<EmployeeOrderAdapter.MyViewHolder> {


    Context context;
    EmployeeOrderAdapter.OnItemClickListener itemClickListener;
    List<OrderDelivery.OrderList> mCanList;
    String rupee;
    int mCount = 0;
    DecimalFormat decimalFormat;


    public EmployeeOrderAdapter(Activity context, List<OrderDelivery.OrderList> mCanList, EmployeeOrderAdapter.OnItemClickListener mItemClickListener) {
        this.context = (Activity) context;
        this.mCanList = mCanList;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public EmployeeOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_order_list_adapter, parent, false);
        return new EmployeeOrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final EmployeeOrderAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);

        holder.mOrderId.setText("DH00" + mCanList.get(position).getOrderId());
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
        holder.mPaymentMethod.setText(mCanList.get(position).getPaymentType());
        holder.mPaymentTxnId.setText(mCanList.get(position).getPaymentTxnId());

        holder.TxtDeliverOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(mCanList.get(position), position);
            }
        });



    }

    public interface OnItemClickListener {
        void onItemClick(OrderDelivery.OrderList response,int position);

        void onAssignEmp(String status, int position, TextView assignEmp);
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
        TextView mOrderId, mItemName, mNoofCans, mTotalPrice, mUnitPrice,
                mOrderType, mOrderDate, mPaymentStatus, mOrderStatus, mPaymentMethod, mPaymentTxnId;
        AppCompatButton TxtDeliverOrder;

        public MyViewHolder(View v) {
            super(v);
            mOrderId = v.findViewById(R.id.txt_orderid);
            mItemName = v.findViewById(R.id.txt_itemname);
            mNoofCans = v.findViewById(R.id.txt_noofcans);
            mTotalPrice = v.findViewById(R.id.txt_totalprice);
            mUnitPrice = v.findViewById(R.id.txt_unitprice);

            mOrderType = v.findViewById(R.id.txt_ordertype);
            mOrderStatus = v.findViewById(R.id.txt_orderstatus);
            mOrderDate = v.findViewById(R.id.txt_orderdate);
            mPaymentStatus = v.findViewById(R.id.txt_paymentstatus);
            mPaymentMethod = v.findViewById(R.id.txt_paymentmethod);
            mPaymentTxnId = v.findViewById(R.id.txt_paymenttxnid);

            TxtDeliverOrder = v.findViewById(R.id.txt_deliverorder);
        }

    }

    public void updateAdapter(List<OrderDelivery.OrderList> updatelist) {
        this.mCanList = updatelist;
        notifyDataSetChanged();
    }

    public List<OrderDelivery.OrderList> getlist() {
        return mCanList;
    }
}
