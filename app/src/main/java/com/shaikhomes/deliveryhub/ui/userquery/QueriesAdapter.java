package com.shaikhomes.deliveryhub.ui.userquery;

import android.app.Activity;
import android.content.Context;
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
import com.shaikhomes.deliveryhub.ui.ordercalculation.ItemQueriesPojo;
import com.shaikhomes.deliveryhub.utility.RoundedTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class QueriesAdapter extends RecyclerView.Adapter<QueriesAdapter.MyViewHolder> {


    Context context;
    QueriesAdapter.OnItemClickListener itemClickListener;
    List<ItemQueriesPojo.QueryList> mCanList;
    String rupee;
    int mCount = 0;
    DecimalFormat decimalFormat;
    String isadmin;

    public QueriesAdapter(Activity context, String isadmin, List<ItemQueriesPojo.QueryList> mCanList, QueriesAdapter.OnItemClickListener itemClickListener) {
        this.context = context;
        this.mCanList = mCanList;
        this.itemClickListener = itemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
        decimalFormat = new DecimalFormat("0.00");
        this.isadmin = isadmin;
    }

    @Override
    public QueriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.queries_adapter, parent, false);
        return new QueriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final QueriesAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);
       /* if (!TextUtils.isEmpty(mCanList.get(position).getAnswer4())) {
            holder.mTxtMessage.setText(mCanList.get(position).getAnswer4());
        } else if (!TextUtils.isEmpty(mCanList.get(position).getQuestion4())) {
            holder.mTxtMessage.setText(mCanList.get(position).getQuestion4());
        } else if (!TextUtils.isEmpty(mCanList.get(position).getAnswer3())) {
            holder.mTxtMessage.setText(mCanList.get(position).getAnswer3());
        } else if (!TextUtils.isEmpty(mCanList.get(position).getQuestion3())) {
            holder.mTxtMessage.setText(mCanList.get(position).getQuestion3());
        } else if (!TextUtils.isEmpty(mCanList.get(position).getAnswer2())) {
            holder.mTxtMessage.setText(mCanList.get(position).getAnswer2());
        } else if (!TextUtils.isEmpty(mCanList.get(position).getQuestion2())) {
            holder.mTxtMessage.setText(mCanList.get(position).getQuestion2());
        } else if (!TextUtils.isEmpty(mCanList.get(position).getAnswer1())) {
            holder.mTxtMessage.setText(mCanList.get(position).getAnswer1());
        } else if (!TextUtils.isEmpty(mCanList.get(position).getQuestion1())) {

        }*/
        holder.mTxtMessage.setText(mCanList.get(position).getDescription());
        if (isadmin.equalsIgnoreCase("1")) {
            holder.mTxtName.setText(mCanList.get(position).getUserName());
        }else{
            holder.mTxtName.setText(mCanList.get(position).getItemName());
        }
        holder.mChatLL.setOnClickListener(v -> {
            itemClickListener.onItemClick(mCanList.get(position), position);
        });
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        int[] COLORS = new int[]{
                R.color.colorPrimary
        };
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();
        String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mCanList.get(position).getQueryImage();
        Picasso.get().load(imgUrl).resize(100, 100)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .centerCrop().transform(new RoundedTransformation()).into(holder.mImageView);
    }

    public interface OnItemClickListener {
        void onItemClick(ItemQueriesPojo.QueryList response, int position);


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
        TextView mTxtName, mTxtMessage;
        LinearLayout mChatLL;
        ImageView mImageView;

        public MyViewHolder(View v) {
            super(v);
            mTxtMessage = v.findViewById(R.id.txt_msg);
            mTxtName = v.findViewById(R.id.txt_name);
            mImageView = v.findViewById(R.id.item_image);
            mChatLL = v.findViewById(R.id.chat_ll);


        }

    }

    public void updateAdapter(List<ItemQueriesPojo.QueryList> updatelist) {
        if (mCanList.size() > 0) {
            mCanList.clear();
        }
        this.mCanList = updatelist;
        notifyDataSetChanged();
    }

    public List<ItemQueriesPojo.QueryList> getlist() {
        return mCanList;
    }
}
