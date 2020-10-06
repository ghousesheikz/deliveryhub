package com.shaikhomes.deliveryhub.utility;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shaikhomes.deliveryhub.R;

import java.util.List;


public class SliderAdapter extends LoopingPagerAdapter<String> {


    private final ClickTopSlider clickTopSlider;

    public SliderAdapter(Context context, List<String> itemList, ClickTopSlider clickTopSlider) {
        super(context, itemList, false);
        this.clickTopSlider = clickTopSlider;
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.item_slider_adapter_, container, false);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        ImageView imageView = convertView.findViewById(R.id.imageview);
        /* RequestOptions requestOption = new RequestOptions().placeholder(R.drawable.tvs_logo_s).centerInside();*/

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(convertView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        int[] COLORS = new int[]{
                convertView.getContext().getResources().getColor(R.color.colorPrimaryDark),
                convertView.getContext().getResources().getColor(R.color.red)
        };
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();
        Glide.with(convertView.getContext()).load("http://delapi.shaikhomes.com/Offers/" + itemList.get(listPosition))
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(imageView);

        imageView.setOnClickListener(v -> {
            if (listPosition == 0) {
                clickTopSlider.onClick("18");
            }
        });


    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }


    public interface ClickTopSlider {
        void onClick(String position);
    }
}
