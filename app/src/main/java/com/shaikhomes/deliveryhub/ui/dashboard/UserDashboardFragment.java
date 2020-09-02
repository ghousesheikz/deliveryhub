package com.shaikhomes.deliveryhub.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shaikhomes.deliveryhub.DashboardAdapter;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.interfaces.DashboardOnClick;
import com.shaikhomes.deliveryhub.pojo.CategoryPojo;
import com.shaikhomes.deliveryhub.pojo.DeliveryhubOffersPojo;
import com.shaikhomes.deliveryhub.utility.SliderAdapter;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.deliveryhub.utility.AppConstants.CAT_ID;

public class UserDashboardFragment extends Fragment implements DashboardOnClick {
    private List<String> mImageList;
    private View view;
    private ApiInterface apiService;
    private TinyDB tinyDB;
    private List<CategoryPojo.CategoryDetail> mCatList;
    private DashboardAdapter mAdapter;
    private RecyclerView mRecyclerview;
    private LoopingViewPager viewpager;
    private SliderAdapter sliderAdapter;
    ImageView mAdv_img1, mAdv_img2, mAdv_img3, mAdv_img4;
    String mImgUrl1 = "http://images.shaikhomes.com/subadvimages/adv_slider1.jpg";
    String mImgUrl2 = "http://images.shaikhomes.com/subadvimages/adv_slider2.jpg";
    String mImgUrl3 = "http://images.shaikhomes.com/subadvimages/adv_slider3.jpg";
    String mImgUrl4 = "http://images.shaikhomes.com/subadvimages/adv_slider4.jpg";
    List<DeliveryhubOffersPojo.OffersList> mOfferList;
    List<DeliveryhubOffersPojo.OffersList> mAdvList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mOfferList = new ArrayList<>();
        mAdvList = new ArrayList<>();
        tinyDB = new TinyDB(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        mImageList = new ArrayList<>();
        mCatList = new ArrayList<>();
        mImageList.add("http://images.shaikhomes.com/shoppingimages/slider_1.jpg");
        mImageList.add("http://images.shaikhomes.com/shoppingimages/slider_2.jpg");
        mImageList.add("http://images.shaikhomes.com/shoppingimages/slider_3.jpg");
        mImageList.add("http://images.shaikhomes.com/shoppingimages/slider_4.jpg");
        mImageList.add("http://images.shaikhomes.com/shoppingimages/slider_5.jpg");
        mImageList.add("http://images.shaikhomes.com/shoppingimages/slider_6.jpg");
        getCategoryDetails();

    }

    private final GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return mAdapter.getItemViewType(position) == 2 ? 2 : 6;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.userdashboard, container, false);
        mRecyclerview = view.findViewById(R.id.dashboard_menu);
        mAdv_img1 = view.findViewById(R.id.adv_img1);
        mAdv_img2 = view.findViewById(R.id.adv_img2);
        mAdv_img3 = view.findViewById(R.id.adv_img3);
        mAdv_img4 = view.findViewById(R.id.adv_img4);
        viewpager = view.findViewById(R.id.viewpager);


        GridLayoutManager
                mGridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        // mGridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setItemViewCacheSize(20);
        mRecyclerview.setLayoutManager(mGridLayoutManager);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DashboardAdapter(mImageList, mCatList, this);
        mRecyclerview.setAdapter(mAdapter);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getActivity());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        int[] COLORS = new int[]{
                getActivity().getResources().getColor(R.color.colorPrimaryDark),
                getActivity().getResources().getColor(R.color.red)
        };
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();
        getAdvList();
        getOffersData();
        return view;
    }


    private void getCategoryDetails() {
        try {
            Call<CategoryPojo> call = apiService.GetCategoryDetails("");
            call.enqueue(new Callback<CategoryPojo>() {
                @Override
                public void onResponse(Call<CategoryPojo> call, Response<CategoryPojo> response) {
                    CategoryPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getCategoryDetails() != null) {
                            if (mItemData.getCategoryDetails().size() > 0) {
                                if (mCatList.size() > 0) {
                                    mCatList.clear();
                                }
                                mCatList.add(new CategoryPojo.CategoryDetail("-1", "All", "ALL"));
                                for (int i = 0; i < mItemData.getCategoryDetails().size(); i++) {
                                    mCatList.add(new CategoryPojo.CategoryDetail(mItemData.getCategoryDetails().get(i).getId(), mItemData.getCategoryDetails().get(i).getCategoryImage(), mItemData.getCategoryDetails().get(i).getCategoryName()));
                                }
                                mAdapter.UpdateDashboardAdapter(mImageList, mCatList);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<CategoryPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void clickHeader(int c, int subCategoryID) {

    }

    @Override
    public void onItemClick(CategoryPojo.CategoryDetail categoryDetail, int position) {
        tinyDB.remove(CAT_ID);
        tinyDB.putString(CAT_ID, "CAT__" + categoryDetail.getId());

        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_insta_services);

    }


    private void getAdvList() {
        try {
            Call<DeliveryhubOffersPojo> call = apiService.GetAppOffers("adv");
            call.enqueue(new Callback<DeliveryhubOffersPojo>() {
                @Override
                public void onResponse(Call<DeliveryhubOffersPojo> call, Response<DeliveryhubOffersPojo> response) {
                    DeliveryhubOffersPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        if(mImageList.size()>0){
                            mImageList.clear();
                        }
                        for (int i = 0; i < mPojo.getOffersList().size(); i++) {
                            mAdvList.add(mPojo.getOffersList().get(i));
                            mImageList.add(mPojo.getOffersList().get(i).getImage());
                        }

                        sliderAdapter = new SliderAdapter(getActivity(), mImageList, new SliderAdapter.ClickTopSlider() {
                            @Override
                            public void onClick(int position, int catID) {
                            }
                        });
                        viewpager.setAdapter(sliderAdapter);

                    } else {
                        Toasty.error(getActivity(), mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeliveryhubOffersPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void getOffersData() {
        try {
            Call<DeliveryhubOffersPojo> call = apiService.GetAppOffers("offer");
            call.enqueue(new Callback<DeliveryhubOffersPojo>() {
                @Override
                public void onResponse(Call<DeliveryhubOffersPojo> call, Response<DeliveryhubOffersPojo> response) {
                    DeliveryhubOffersPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        for (int i = 0; i < mPojo.getOffersList().size(); i++) {
                            mOfferList.add(mPojo.getOffersList().get(i));
                        }
                        if (mOfferList.size() > 0) {
                            fillImageData();
                        }
                    } else {
                        Toasty.error(getActivity(), mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeliveryhubOffersPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void fillImageData() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getActivity());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        int[] COLORS = new int[]{
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.red)
        };
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();

        Glide.with(getActivity()).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(0).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdv_img1);
        Glide.with(getActivity()).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(1).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdv_img2);
        Glide.with(getActivity()).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(2).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdv_img3);
        Glide.with(getActivity()).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(3).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdv_img4);
    }
}
