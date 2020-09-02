package com.shaikhomes.deliveryhub.ui.stores;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.utility.MyScrollController;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreItemsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreItemsList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreItemsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreItemsList.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreItemsList newInstance(String param1, String param2) {
        StoreItemsList fragment = new StoreItemsList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    private ApiInterface apiService;
    private TinyDB tinyDB;
    RecyclerView mCategoryRecyclerView, mItemsRecyclerview;
    StoreCategoryAdapter mCatAdapter;
    StoreItemsAdapter mItemAdapter;
    List<StoreCategoryPojo.StoreCategoryDetail> mCatList;
    List<StoreItemsPojo.StoreItemsList> mItemList;
    TextView txt_totalprice;
    int minamt = 0;
    double totalAmt = 0;
    boolean mProceedflag = false;
    DecimalFormat df;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store_items_list, container, false);
        tinyDB = new TinyDB(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        mCatList = new ArrayList<>();
        mItemList = new ArrayList<>();
        df = new DecimalFormat("0.00");
        minamt = tinyDB.getInt("MINAMT");
        txt_totalprice = view.findViewById(R.id.txt_totalprice);
        mCategoryRecyclerView = view.findViewById(R.id.store_category_list);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mItemsRecyclerview = view.findViewById(R.id.store_items_list);
        mItemsRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mItemsRecyclerview.addOnScrollListener(new MyScrollController() {
            @SuppressLint("RestrictedApi")
            @Override
            public void show() {
               /* txt_totalprice.setVisibility(View.VISIBLE);
                txt_totalprice.animate().translationY(0).setStartDelay(200).setInterpolator(new DecelerateInterpolator(2)).start();
          */
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void hide() {
               /* txt_totalprice.animate().translationY(txt_totalprice.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                txt_totalprice.setVisibility(View.GONE);
          */
            }
        });
        mCatAdapter = new StoreCategoryAdapter(getActivity(), mCatList, new StoreCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StoreCategoryPojo.StoreCategoryDetail response, int position) {
                getItemsDetails(response.getCategoryId(), "");
            }
        });
        mCategoryRecyclerView.setAdapter(mCatAdapter);
        mItemAdapter = new StoreItemsAdapter(getActivity(), mItemList, new StoreItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<StoreItemsPojo.StoreItemsList> response, int position) {
                totalAmt = 0;
                for (int i = 0; i < response.size(); i++) {
                    if (response.get(i).getTotalAmt() > 0) {
                        totalAmt = totalAmt + response.get(i).getTotalAmt();
                        if (totalAmt >= minamt) {
                            mProceedflag = true;
                            txt_totalprice.setBackgroundResource(R.drawable.button_background);
                            txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                        } else {
                            mProceedflag = false;
                            txt_totalprice.setBackgroundResource(R.drawable.button_background_grey);
                            txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                        }
                    }
                }
            }
        });
        mItemsRecyclerview.setAdapter(mItemAdapter);
        getCategoryDetails(tinyDB.getString("SVENDOR"));
        getItemsDetails(tinyDB.getString("SVENDOR"), "");
        txt_totalprice.setOnClickListener(v -> {
            if (mProceedflag) {
                proceedOrder();

            } else {
                Toasty.error(getActivity(), "Minimum order amount is : " + df.format(minamt), Toasty.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void proceedOrder() {
        List<StoreOrderItemsPojo> mStoreItemsPojoList = new ArrayList<>();
        StoreOrderItemsPojo mDataPojo;
        if (mItemAdapter.getlist().size() > 0) {
            for (int i = 0; i <= mItemAdapter.getlist().size(); i++) {
                mDataPojo = new StoreOrderItemsPojo();
                mDataPojo.setItemId(mItemAdapter.getlist().get(i).getItemId());
                mDataPojo.setItemName(mItemAdapter.getlist().get(i).getItemName());
                mDataPojo.setItemQuantity(mItemAdapter.getlist().get(i).getItemQuantity());
                mDataPojo.setItemCategory(mItemAdapter.getlist().get(i).getCategoryId());
                mDataPojo.setItemprice(mItemAdapter.getlist().get(i).getItemPrice());
                mDataPojo.setTotalamount(mItemAdapter.getlist().get(i).getTotalAmt() + "");
                mDataPojo.setOrderType("stores");
                mStoreItemsPojoList.add(mDataPojo);
            }
        }
    }

    private void getItemsDetails(String svendor, String category) {
        try {
            Call<StoreItemsPojo> call = apiService.getStoresItems(category, svendor, "True");
            call.enqueue(new Callback<StoreItemsPojo>() {
                @Override
                public void onResponse(Call<StoreItemsPojo> call, Response<StoreItemsPojo> response) {
                    StoreItemsPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getStoreItemsList() != null) {
                            if (mItemData.getStoreItemsList().size() > 0) {

                                mItemAdapter.updateAdapter(mItemData.getStoreItemsList());
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<StoreItemsPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void getCategoryDetails(String svendor) {
        try {
            Call<StoreCategoryPojo> call = apiService.getStoresCategory(svendor);
            call.enqueue(new Callback<StoreCategoryPojo>() {
                @Override
                public void onResponse(Call<StoreCategoryPojo> call, Response<StoreCategoryPojo> response) {
                    StoreCategoryPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getStoreCategoryDetails() != null) {
                            if (mItemData.getStoreCategoryDetails().size() > 0) {

                                mCatAdapter.updateAdapter(mItemData.getStoreCategoryDetails());
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<StoreCategoryPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }
}
