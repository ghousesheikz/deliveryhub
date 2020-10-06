package com.shaikhomes.deliveryhub;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.shaikhomes.deliveryhub.interfaces.DashboardOnClick;
import com.shaikhomes.deliveryhub.pojo.CategoryPojo;
import com.shaikhomes.deliveryhub.utility.RoundedTransformation;
import com.shaikhomes.deliveryhub.utility.SliderAdapter;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;

    private static final int TYPE_HEADER_SLIDER = 0;
    private static final int TYPE_MENU = 1;
    private static final int TYPE_VEHICLE = 2;
    private static final int TYPE_OTHER_CATEGORY = 3;
    private static final int TYPE_PRODUCT_LIST_TITLE = 4;
    private static final int TYPE_PRODUCT_LIST = 5;

    private ArrayList<String> lstProducts;
    private List<String> topBar;
    private List<CategoryPojo.CategoryDetail> category;


    private final DashboardOnClick onClick;


    public DashboardAdapter(
            List<String> topBar,
            List<CategoryPojo.CategoryDetail> category,

            DashboardOnClick onClick) {

        this.topBar = topBar;
        this.category = category;

        this.onClick = onClick;
    }

    public void UpdateDashboardAdapter(
            List<String> topBar,
            List<CategoryPojo.CategoryDetail> category) {
        this.topBar = topBar;
        this.category = category;
        this.notifyDataSetChanged();
        if (sliderAdapter != null) {
            sliderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_SLIDER;
        } else if (position == 1) {
            return TYPE_MENU;
        } else if (position == 2) {
            return TYPE_VEHICLE;
        } else if (position == 3) {
            return TYPE_OTHER_CATEGORY;
        } else if (position == 4) {
            return TYPE_PRODUCT_LIST_TITLE;
        } else {
            return TYPE_PRODUCT_LIST;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null)
            inflater = (LayoutInflater) viewGroup.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       /* if (i == TYPE_HEADER_SLIDER) {
            View v = inflater.inflate(R.layout.item_header_slider, viewGroup, false);
            return new HeaderSliderViewHolder(v);
        } else  {*/
        View v = inflater.inflate(R.layout.category_list_adapter, viewGroup, false);
        return new MenuViewHolder(v);
         /*else if (i == TYPE_VEHICLE) {
            View v = inflater.inflate(R.layout.item_vehicle_, viewGroup, false);
            return new VehicleViewHolder(v);
        }*/

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

       /* if (viewHolder instanceof ProductListViewHolder) {
            ProductListViewHolder viewHolder0 = (ProductListViewHolder) viewHolder;
           *//* LstProduct data = lstProducts.get(viewHolder.getAdapterPosition() - 5);
            viewHolder0.mrp.setText("₹" + data.getMrp());
            viewHolder0.selling_price.setText("₹" + data.getSellingPrice());
            //viewHolder0.ndp.setText("NDP - Rs." + String.valueOf(data.getNdp()));
            viewHolder0.product_name.setText(String.valueOf(data.getProductName()) + " ("+ data.getProductCode() + ")");
*//*
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(viewHolder.itemView.getContext());
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            int[] COLORS = new int[]{
                    viewHolder.itemView.getContext().getResources().getColor(R.color.colorPrimaryDark),
                    viewHolder.itemView.getContext().getResources().getColor(R.color.red)
            };
            circularProgressDrawable.setColorSchemeColors(COLORS);
            circularProgressDrawable.start();

           *//* Glide.with(viewHolder.itemView).load(data.getImageLink()).placeholder(circularProgressDrawable)
                    .fitCenter()
                    .error(R.drawable.ic_no_image).into(viewHolder0.product_image);*//*

         *//*  viewHolder0.parent.setOnClickListener(view->{
                onClick.clickProduct(data.getProductCode());
            });*//*






        } else */
        if (viewHolder instanceof MenuViewHolder) {
            MenuViewHolder menuViewHolder = (MenuViewHolder) viewHolder;
            if (!TextUtils.isEmpty(category.get(position).getCategoryImage())) {
                if (category.get(position).getCategoryImage().toLowerCase().equalsIgnoreCase("all")) {
                    Picasso.get().load(R.drawable.all_categories).resize(300, 300)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .centerCrop().transform(new RoundedTransformation())
                            .into(menuViewHolder.mImgCategory);
                } else {
                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(menuViewHolder.mImgCategory.getContext());
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    int[] COLORS = new int[]{
                            R.color.colorPrimary
                    };
                    circularProgressDrawable.setColorSchemeColors(COLORS);
                    circularProgressDrawable.start();
                    String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + category.get(position).getCategoryImage();
                    Picasso.get().load(imgUrl).resize(300, 300).placeholder(circularProgressDrawable)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .centerCrop().transform(new RoundedTransformation())
                            .into(menuViewHolder.mImgCategory);
                }
            }
            menuViewHolder.mTxtCategory.setText(category.get(position).getCategoryName());
            menuViewHolder.mCatLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onItemClick(category.get(position), position);
                }
            });

        } else if (viewHolder instanceof HeaderSliderViewHolder) {

            HeaderSliderViewHolder headerSliderViewHolder = (HeaderSliderViewHolder) viewHolder;

        }
    }

    @Override
    public int getItemCount() {

        int value = category.size();


        return value;
    }

    private SliderAdapter sliderAdapter;

    private class HeaderSliderViewHolder extends RecyclerView.ViewHolder {

        private final LoopingViewPager viewpager;


        private HeaderSliderViewHolder(View v) {
            super(v);

            viewpager = itemView.findViewById(R.id.viewpager);
            init();

        }

        private void init() {
            sliderAdapter = new SliderAdapter(itemView.getContext(), topBar, new SliderAdapter.ClickTopSlider() {
                @Override
                public void onClick(String position) {
                    onClick.clickHeader(position);
                }
            });
            viewpager.setAdapter(sliderAdapter);
        }

    }

    private class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView mTxtCategory;
        ImageView mImgCategory;
        RelativeLayout mCatLL;


        private MenuViewHolder(View v) {
            super(v);

            mTxtCategory = v.findViewById(R.id.category_txt);
            mImgCategory = v.findViewById(R.id.category_img);
            mCatLL = v.findViewById(R.id.category_);


        }
    }





   /* private class ProductListViewHolder extends RecyclerView.ViewHolder {

        final TextView product_name;
        final TextView mrp;
        final TextView selling_price;
        final TextView ndp;
        final ImageView product_image;
        final LinearLayout parent_price;

        final CardView parent;

        private ProductListViewHolder(View v) {
            super(v);
            product_name = itemView.findViewById(R.id.product_name);
            mrp = itemView.findViewById(R.id.mrp);
            selling_price = itemView.findViewById(R.id.selling_price);
            ndp = itemView.findViewById(R.id.ndp);
            product_image = itemView.findViewById(R.id.image_1);

            parent_price = itemView.findViewById(R.id.parent_price);

            parent = itemView.findViewById(R.id.parent);
        }
    }*/


}
