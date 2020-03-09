package com.shaikhomes.watercan.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.ui.ordercalculation.OrderCalculation;
import com.shaikhomes.watercan.utility.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_can, container, false);
        tinyDB = new TinyDB(getActivity());
        mRecyclerview = view.findViewById(R.id.order_can_list);
        mLinearLayoutmanager = new LinearLayoutManager(getActivity());
        mLinearLayoutmanager.setReverseLayout(true);
        mLinearLayoutmanager.setStackFromEnd(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        List<String> mList = new ArrayList<>();
        mAdapter = new OrderCanAdapter(getActivity(), mList, new OrderCanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String response, int position) {
                Bundle args = new Bundle();
                args.putString("ARG_PARAM1", "ghouse");
                args.putString("ARG_PARAM2", "arguments");
                OrderCalculationPojo mPojo = new OrderCalculationPojo();
                mPojo.setImageURL("");
                mPojo.setLiters("10L");
                mPojo.setPrice("100.00");
                mPojo.setName("Bisleri");
                mPojo.setNoOfCans(1);
                mPojo.setTotalAmount(100.00);
                mPojo.setUnitAmount(100.00);
                try {
                    jsonObject = new JSONObject(new Gson().toJson(mPojo));
                    if (!TextUtils.isEmpty(tinyDB.getString(ORDER_CAN_LIST))) {
                        jsonArray = new JSONArray(tinyDB.getString(ORDER_CAN_LIST));
                    } else {
                        jsonArray = new JSONArray();
                    }
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
}
