package com.shaikhomes.deliveryhub.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.model.OrderCalculationPojo;
import com.shaikhomes.deliveryhub.pojo.CategoryPojo;
import com.shaikhomes.deliveryhub.pojo.ItemPojo;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.deliveryhub.utility.AppConstants.CAT_ID;
import static com.shaikhomes.deliveryhub.utility.UtilityConstants.ORDER_CAN_LIST;

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
    private String mVendoprId = "";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "CAT_ID";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static String mParam1 = "";
    private String mParam2;
    private ApiInterface apiService;
    private String mCatId = "";

    public OrderCan() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrderCan newInstance(String Search) {
        OrderCan fragment = new OrderCan();
        mParam1 = Search;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        //  mCatId = tinyDB.getString(CAT_ID);

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
                mPojo.setItemid(response.getItemId());
                mPojo.setPrice(response.getItemPrice());
                mPojo.setName(response.getItemName());
                mPojo.setVendorId(response.getVendorId());
                mPojo.setVendorName(response.getVendorName());
                mPojo.setMinQty(response.getMinqty());
                mPojo.setCategoryId(response.getCategoryId());
                if (TextUtils.isEmpty(response.getItemDescription())) {
                    mPojo.setDescription("");
                } else {
                    mPojo.setDescription(response.getItemDescription());
                }
                if (TextUtils.isEmpty(response.getImage1())) {
                    mPojo.setImageURL2("");
                } else {
                    mPojo.setImageURL2(response.getImage1());
                }
                if (TextUtils.isEmpty(response.getImage2())) {
                    mPojo.setImageURL3("");
                } else {
                    mPojo.setImageURL3(response.getImage2());
                }
                if (TextUtils.isEmpty(response.getImage3())) {
                    mPojo.setImageURL4("");
                } else {
                    mPojo.setImageURL4(response.getImage3());
                }

                if (response.getMinqty().equalsIgnoreCase("0")) {
                    mPojo.setNoOfCans(1);
                } else {
                    mPojo.setNoOfCans(Integer.parseInt(response.getMinqty()));
                }
                mPojo.setTotalAmount(Integer.parseInt(response.getItemPrice()));
              //  mPojo.setItemid(response.getItemId());
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
                    } else {
                        mAdapter.updateAdapter(mFilterList);
                    }
                }
            }
        });
        mCatRecyclerview.setAdapter(mCatAdapter);
        mCatAdapter.notifyDataSetChanged();
        //getCategoryDetails("");
        if (tinyDB.getString(CAT_ID).split("__")[0].equalsIgnoreCase("SEA")) {
            getSearchItemData(tinyDB.getString(CAT_ID).split("__")[1]);
        }
        if (tinyDB.getString(CAT_ID).split("__")[0].equalsIgnoreCase("CAT")) {
            if (tinyDB.getString(CAT_ID).split("__")[1].equalsIgnoreCase("-1")) {
                getAllItemData();
            } else {
                getItemData(tinyDB.getString(CAT_ID).split("__")[1]);
            }
        }
        // Inflate the layout for this fragment
        return view;
    }

    private void getSearchItemData(String query) {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getItemData(String catid) {
        try {
            Call<ItemPojo> call = apiService.GetItemListByCategory(catid, "",tinyDB.getDouble("LAT",0.0),tinyDB.getDouble("LANG",0.0));
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

    private void getAllItemData() {
        try {
            Call<ItemPojo> call = apiService.GetItemList("", "True",tinyDB.getDouble("LAT",0.0),tinyDB.getDouble("LANG",0.0));
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
