package com.shaikhomes.watercan.ui.ordercalculation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.ui.orders.OrderListAdapter;

import java.text.DecimalFormat;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {


    Context context;
    OrderListAdapter.OnItemClickListener itemClickListener;
    List<String> mCanList;
    String rupee;
    int mCount = 0;
    DecimalFormat decimalFormat;


    public ChatAdapter(Activity context, List<String> mCanList) {
        this.context = (Activity) context;
        this.mCanList = mCanList;
        rupee = context.getResources().getString(R.string.Rs);
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_adapter, parent, false);
        return new ChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final ChatAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);
        holder.mTxtMessage.setText(mCanList.get(position));
        if (position % 2 == 1) {
            holder.mCardLL.setBackgroundColor(Color.parseColor("#FFFFFF"));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(3, 10, 30, 10);
            holder.mCardLL.setLayoutParams(params);
            holder.mTxtMessage.setTextColor(Color.parseColor("#0f0f0f"));
        } else {
            holder.mCardLL.setBackgroundColor(Color.parseColor("#18E1CD"));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(30, 10, 3, 10);
            holder.mCardLL.setLayoutParams(params);
            holder.mTxtMessage.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(List<OrderCalculationPojo> response, int position);

        void onFabClick(OrderCalculationPojo response, int position);

        void onImageClick(OrderCalculationPojo response, int position, String imageno, ImageView imageView);
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
        TextView mTxtMessage;
        CardView mCardLL;


        public MyViewHolder(View v) {
            super(v);
            mTxtMessage = v.findViewById(R.id.txt_chat);
            mCardLL = v.findViewById(R.id.card_ll);


        }

    }

    public void updateAdapter(List<String> updatelist) {
        this.mCanList = updatelist;
        notifyDataSetChanged();
    }

    public List<String> getlist() {
        return mCanList;
    }
}
