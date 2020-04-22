package com.shaikhomes.watercan.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.api_services.ApiClient;
import com.shaikhomes.watercan.api_services.ApiInterface;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.pojo.CategoryPojo;
import com.shaikhomes.watercan.pojo.ItemPojo;
import com.shaikhomes.watercan.pojo.Spinner_global_model;
import com.shaikhomes.watercan.utility.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.UtilityConstants.ORDER_CAN_LIST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCan extends Fragment {
    private RecyclerView mRecyclerview, mCatRecyclerview;
    private LinearLayoutManager mLinearLayoutmanager;
    private OrderCanAdapter mAdapter;
    private CategoryAdapter mCatAdapter;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private TinyDB tinyDB;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ApiInterface apiService;

    public OrderCan() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrderCan newInstance(String param1, String param2) {
        OrderCan fragment = new OrderCan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);

    }

    View view;
    List<ItemPojo.Item> mList;
    private List<CategoryPojo.CategoryDetail> mCatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_can, container, false);
        mRecyclerview = view.findViewById(R.id.order_can_list);
        mCatRecyclerview = view.findViewById(R.id.item_menu_list);
        mCatRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mLinearLayoutmanager = new LinearLayoutManager(getActivity());
        mLinearLayoutmanager.setReverseLayout(true);
        mLinearLayoutmanager.setStackFromEnd(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mList = new ArrayList<>();
        mCatList = new ArrayList<>();
        mAdapter = new OrderCanAdapter(getActivity(), mList, new OrderCanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemPojo.Item response, int position) {
                Bundle args = new Bundle();
                args.putString("ARG_PARAM1", "");
                args.putString("ARG_PARAM2", "arguments");
                OrderCalculationPojo mPojo = new OrderCalculationPojo();
                mPojo.setImageURL(response.getItemImage());
                mPojo.setLiters(response.getItemSize());
                mPojo.setPrice(response.getItemPrice());
                mPojo.setName(response.getItemName());
                mPojo.setVendorId(response.getVendorId());
                mPojo.setVendorName(response.getVendorName());
                mPojo.setMinQty(response.getMinqty());
                mPojo.setCategoryId(response.getCategoryId());
                mPojo.setDescription(response.getItemDescription());
                if (response.getMinqty().equalsIgnoreCase("0")) {
                    mPojo.setNoOfCans(1);
                } else {
                    mPojo.setNoOfCans(Integer.parseInt(response.getMinqty()));
                }
                mPojo.setTotalAmount(Integer.parseInt(response.getItemPrice()));
                int amt = Integer.parseInt(response.getMinqty()) * Integer.parseInt(response.getItemPrice());
                mPojo.setUnitAmount(amt);
                try {

                    jsonObject = new JSONObject(new Gson().toJson(mPojo));
                    jsonArray = new JSONArray();
                    jsonArray.put(jsonObject);
                    tinyDB.remove(ORDER_CAN_LIST);
                    tinyDB.putString(ORDER_CAN_LIST, jsonArray.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // new OrderCalculation().newInstance(mPojo.getImageURL(), mPojo.getLiters(), mPojo.getName(), mPojo.getNoOfCans(), mPojo.getTotalAmount(), mPojo.getUnitAmount());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_order_cal, args);
            }
        });
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mCatAdapter = new CategoryAdapter(getActivity(), mCatList, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoryPojo.CategoryDetail response, int position) {
                List<ItemPojo.Item> mFilterList = new ArrayList<>();
                if (response.getId().equalsIgnoreCase("-1")) {
                    mAdapter.updateAdapter(mList);
                } else {
                    for (int i = 0; i < mList.size(); i++) {
                        if (response.getId().equalsIgnoreCase(mList.get(i).getCategoryId())) {
                            mFilterList.add(mList.get(i));
                        }
                    }
                    if (mFilterList.size() > 0) {
                        mAdapter.updateAdapter(mFilterList);
                    }else{
                        mAdapter.updateAdapter(mFilterList);
                    }
                }
            }
        });
        mCatRecyclerview.setAdapter(mCatAdapter);
        mCatAdapter.notifyDataSetChanged();
        getCategoryDetails("");
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getItemData();
    }

    private void getItemData() {
        try {
            Call<ItemPojo> call = apiService.GetItemList("", "True");
            call.enqueue(new Callback<ItemPojo>() {
                @Override
                public void onResponse(Call<ItemPojo> call, Response<ItemPojo> response) {
                    ItemPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getItemList() != null) {
                            if (mItemData.getItemList().size() > 0) {
                                if (mList.size() > 0) {
                                    mList.clear();
                                }
                                mList = mItemData.getItemList();
                                mAdapter.updateAdapter(mItemData.getItemList());
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<ItemPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void getCategoryDetails(String number) {
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
                                // mCatList = mItemData.getCategoryDetails();
                                if (mCatList.size() > 0) {
                                    mCatAdapter.updateAdapter(mCatList);
                                }
                                // mAdapter.updateAdapter(mItemData.getOrderList());
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
}
