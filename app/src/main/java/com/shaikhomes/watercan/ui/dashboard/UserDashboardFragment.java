package com.shaikhomes.watercan.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.shaikhomes.watercan.DashboardAdapter;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.api_services.ApiClient;
import com.shaikhomes.watercan.api_services.ApiInterface;
import com.shaikhomes.watercan.interfaces.DashboardOnClick;
import com.shaikhomes.watercan.pojo.CategoryPojo;
import com.shaikhomes.watercan.utility.SliderAdapter;
import com.shaikhomes.watercan.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.CAT_ID;
import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        viewpager = view.findViewById(R.id.viewpager);
        sliderAdapter = new SliderAdapter(getActivity(), mImageList, new SliderAdapter.ClickTopSlider() {
            @Override
            public void onClick(int position, int catID) {
            }
        });
        viewpager.setAdapter(sliderAdapter);

        GridLayoutManager
                mGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        // mGridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setItemViewCacheSize(20);
        mRecyclerview.setLayoutManager(mGridLayoutManager);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DashboardAdapter(mImageList, mCatList, this);
        mRecyclerview.setAdapter(mAdapter);
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
        tinyDB.putString(CAT_ID, categoryDetail.getId());

        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_insta_services);

    }
}
