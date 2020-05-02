package com.shaikhomes.watercan.ui.orders;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.utility.MySpannable;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {


    Context context;
    OrderListAdapter.OnItemClickListener itemClickListener;
    List<OrderCalculationPojo> mCanList;
    String rupee;
    int mCount = 0;
    DecimalFormat decimalFormat;


    public OrderListAdapter(Activity context, List<OrderCalculationPojo> mCanList, OrderListAdapter.OnItemClickListener mItemClickListener) {
        this.context = (Activity) context;
        this.mCanList = mCanList;
        this.itemClickListener = mItemClickListener;
        rupee = context.getResources().getString(R.string.Rs);
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_adapter, parent, false);
        return new OrderListAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. View More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @Override
    public void onBindViewHolder(final OrderListAdapter.MyViewHolder holder, final int position) {
        //  animator.animateAdd(holder);
        if (!TextUtils.isEmpty(mCanList.get(position).getImageURL())) {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            int[] COLORS = new int[]{
                    R.color.colorPrimary
            };
            circularProgressDrawable.setColorSchemeColors(COLORS);
            circularProgressDrawable.start();
            String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mCanList.get(position).getImageURL();
            Picasso.get().load(imgUrl).resize(800, 1200).placeholder(circularProgressDrawable)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.mCanImage);
            if (!TextUtils.isEmpty(mCanList.get(position).getImageURL2())) {
                String imgUrl2 = "http://delapi.shaikhomes.com/ImageStorage/" + mCanList.get(position).getImageURL2();
                Picasso.get().load(imgUrl2).resize(800, 1200).placeholder(circularProgressDrawable)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(holder.mCanImage2);
            }
            if (!TextUtils.isEmpty(mCanList.get(position).getImageURL3())) {
                String imgUrl3 = "http://delapi.shaikhomes.com/ImageStorage/" + mCanList.get(position).getImageURL3();
                Picasso.get().load(imgUrl3).resize(800, 1200).placeholder(circularProgressDrawable)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(holder.mCanImage3);
            }
            if (!TextUtils.isEmpty(mCanList.get(position).getImageURL4())) {
                String imgUrl4 = "http://delapi.shaikhomes.com/ImageStorage/" + mCanList.get(position).getImageURL4();
                Picasso.get().load(imgUrl4).resize(800, 1200).placeholder(circularProgressDrawable)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(holder.mCanImage4);
            }
        }
        holder.mItemName.setText(mCanList.get(position).getName());

        if(!TextUtils.isEmpty(mCanList.get(position).getDescription())) {
            holder.mItemDesc.setText(mCanList.get(position).getDescription());
            makeTextViewResizable(holder.mItemDesc, 2, "View More", true);
        }else{
            holder.mItemDesc.setText("Not Available");
        }
        holder.mTxtDistributor.setText(mCanList.get(position).getVendorName());
        mCount = mCanList.get(position).getNoOfCans();

        holder.mCanCount.setText(String.valueOf(mCount));
        holder.mCanAmt.setText("Price : ₹" + mCanList.get(position).getPrice());
        holder.mPlusCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount = mCanList.get(position).getNoOfCans();
                mCount = mCount + 1;
                int amt = Integer.parseInt(mCanList.get(position).getPrice());
                amt = mCount * amt;
                mCanList.get(position).setUnitAmount(amt);
                mCanList.get(position).setNoOfCans(mCount);
                holder.mCanAmt.setText("Amount : ₹" + amt + " ");
                holder.mCanCount.setText(String.valueOf(mCount));
                itemClickListener.onItemClick(mCanList, position);
            }
        });

        holder.mMinusCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount = mCanList.get(position).getNoOfCans();
                mCount = mCount - 1;
                if (mCount < Integer.parseInt(mCanList.get(position).getMinQty())) {
                    mCount = Integer.parseInt(mCanList.get(position).getMinQty());
                }
                if (mCount < 0) {
                    mCount = 0;
                    int amt = Integer.parseInt(mCanList.get(position).getPrice());
                    amt = mCount * amt;
                    mCanList.get(position).setUnitAmount(amt);
                    mCanList.get(position).setNoOfCans(mCount);
                    holder.mCanAmt.setText("Amount : ₹" + decimalFormat.format(amt) + " ");
                    holder.mCanCount.setText(String.valueOf(mCount));
                    mCanList.get(position).setItemcount(mCount);
                    itemClickListener.onItemClick(mCanList, position);
                } else {
                    int amt = Integer.parseInt(mCanList.get(position).getPrice());
                    amt = mCount * amt;
                    mCanList.get(position).setUnitAmount(amt);
                    mCanList.get(position).setNoOfCans(mCount);
                    holder.mCanAmt.setText("Amount : ₹" + decimalFormat.format(amt) + " ");
                    holder.mCanCount.setText(String.valueOf(mCount));
                    itemClickListener.onItemClick(mCanList, position);
                }
            }
        });

        holder.mCanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onImageClick(mCanList.get(position), position, "1", holder.mCanImage);
            }
        });
        holder.mCanImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onImageClick(mCanList.get(position), position, "2", holder.mCanImage2);
            }
        });
        holder.mCanImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onImageClick(mCanList.get(position), position, "3", holder.mCanImage3);
            }
        });
        holder.mCanImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onImageClick(mCanList.get(position), position, "4", holder.mCanImage4);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(List<OrderCalculationPojo> response, int position);

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
        TextView mCanCount, mCanAmt, mTxtDistributor, mItemName, mItemDesc;
        ImageView mPlusCnt, mMinusCnt, mCanImage, mCanImage2, mCanImage3, mCanImage4;

        public MyViewHolder(View v) {
            super(v);
            mCanCount = v.findViewById(R.id.can_count);
            mPlusCnt = v.findViewById(R.id.plus_count);
            mMinusCnt = v.findViewById(R.id.minus_count);
            mCanAmt = v.findViewById(R.id.txt_amt);
            mCanImage = v.findViewById(R.id.image_);
            mCanImage2 = v.findViewById(R.id.image_2);
            mCanImage3 = v.findViewById(R.id.image_3);
            mCanImage4 = v.findViewById(R.id.image_4);
            mTxtDistributor = v.findViewById(R.id.distributor_name);
            mItemName = v.findViewById(R.id.item_name);
            mItemDesc = v.findViewById(R.id.item_desc);

        }

    }

    public void updateAdapter(List<OrderCalculationPojo> updatelist) {
        this.mCanList = updatelist;
        notifyDataSetChanged();
    }

    public List<OrderCalculationPojo> getlist() {
        return mCanList;
    }
}
