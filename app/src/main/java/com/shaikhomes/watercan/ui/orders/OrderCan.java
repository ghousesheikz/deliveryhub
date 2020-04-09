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
import com.shaikhomes.watercan.pojo.ItemPojo;
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
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearLayoutmanager;
    private OrderCanAdapter mAdapter;
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
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    View view;
    List<ItemPojo.Item> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_can, container, false);
        tinyDB = new TinyDB(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        mRecyclerview = view.findViewById(R.id.order_can_list);
        mLinearLayoutmanager = new LinearLayoutManager(getActivity());
        mLinearLayoutmanager.setReverseLayout(true);
        mLinearLayoutmanager.setStackFromEnd(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mList = new ArrayList<>();
        mAdapter = new OrderCanAdapter(getActivity(), mList, new OrderCanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemPojo.Item response, int position) {
                Bundle args = new Bundle();
                args.putString("ARG_PARAM1", "");
                args.putString("ARG_PARAM2", "arguments");
                OrderCalculationPojo mPojo = new OrderCalculationPojo();
                mPojo.setImageURL(mList.get(position).getItemImage());
                mPojo.setLiters(mList.get(position).getItemSize());
                mPojo.setPrice(mList.get(position).getItemPrice());
                mPojo.setName(mList.get(position).getItemName());
                mPojo.setVendorId(mList.get(position).getVendorId());
                mPojo.setVendorName(mList.get(position).getVendorName());
                mPojo.setMinQty(mList.get(position).getMinqty());
                if(mList.get(position).getMinqty().equalsIgnoreCase("0")) {
                    mPojo.setNoOfCans(1);
                }else{
                    mPojo.setNoOfCans(Integer.parseInt(mList.get(position).getMinqty()));
                }
                mPojo.setTotalAmount(Integer.parseInt(mList.get(position).getItemPrice()));
                int amt = Integer.parseInt(mList.get(position).getMinqty())*Integer.parseInt(mList.get(position).getItemPrice());
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
                                if(mList.size()>0){
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
}
