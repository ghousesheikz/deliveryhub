package com.shaikhomes.watercan.ui.ordercalculation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.ui.orders.OrderCanAdapter;
import com.shaikhomes.watercan.ui.orders.OrderListAdapter;
import com.shaikhomes.watercan.utility.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.shaikhomes.watercan.utility.UtilityConstants.ORDER_CAN_LIST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCalculation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCalculation extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_IMAGE = "image";
    private static final String ARG_LITERS = "liters";
    private static final String ARG_NAME = "name";
    private static final String ARG_NOOFCANS = "noofcans";
    private static final String ARG_TOTAMT = "totamt";
    private static final String ARG_UNITAMT = "unitamt";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<OrderCalculationPojo> mOrdersList = new ArrayList<>();
    private TinyDB tinyDB;
    private JSONArray jsonArray;
    private OrderCalculationPojo mOrderPojo;
    private OrderListAdapter mAdapter;
    private View view;
    private RecyclerView mRecyclerView;
    private TextView mtxtTotalAmt;
    private FloatingActionButton mOrderProceedFab;

    public OrderCalculation() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrderCalculation newInstance(String imageURL, String liters, String name, int noOfCans, double totalAmount, double unitAmount) {
        OrderCalculation fragment = new OrderCalculation();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE, imageURL);
        args.putString(ARG_LITERS, liters);
        args.putString(ARG_NAME, name);
        args.putInt(ARG_NOOFCANS, noOfCans);
        args.putDouble(ARG_TOTAMT, totalAmount);
        args.putDouble(ARG_UNITAMT, unitAmount);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(getActivity());
        try {
            jsonArray = new JSONArray(tinyDB.getString(ORDER_CAN_LIST));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonArray != null) {
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    mOrderPojo = new OrderCalculationPojo();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        mOrderPojo.setImageURL(jsonObject.getString("imageURL"));
                        mOrderPojo.setName(jsonObject.getString("Name"));
                        mOrderPojo.setPrice(jsonObject.getString("Price"));
                        mOrderPojo.setLiters(jsonObject.getString("Liters"));
                        mOrderPojo.setNoOfCans(jsonObject.getInt("NoOfCans"));
                        mOrderPojo.setUnitAmount(jsonObject.getDouble("unitAmount"));
                        mOrderPojo.setTotalAmount(jsonObject.getDouble("TotalAmount"));
                        mOrdersList.add(mOrderPojo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_calculation, container, false);
        mRecyclerView = view.findViewById(R.id.order_cal_list);
        mtxtTotalAmt = view.findViewById(R.id.total_amount);
        mOrderProceedFab = view.findViewById(R.id.proceed_order);
        mOrderProceedFab.setOnClickListener(this);
        if (mOrdersList.size() > 0) {
            double totAmt = 0;
            for (int i = 0; i < mOrdersList.size(); i++) {
                totAmt += mOrdersList.get(i).getUnitAmount();
            }
            mtxtTotalAmt.setText(totAmt + " /-");
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter = new OrderListAdapter(getActivity(), mOrdersList, new OrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<OrderCalculationPojo> response, int position) {
                tinyDB.remove(ORDER_CAN_LIST);
                tinyDB.putString(ORDER_CAN_LIST, new Gson().toJson(response));
                mAdapter.updateAdapter(response);
                mOrdersList = response;
                double totAmt = 0;
                for (int i = 0; i < response.size(); i++) {
                    totAmt += response.get(i).getUnitAmount();
                }
                mtxtTotalAmt.setText(totAmt + " /-");
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.proceed_order) {
            tinyDB.remove(ORDER_CAN_LIST);
            Toasty.success(getActivity(), "Your Total Order AMount is " + mtxtTotalAmt.getText().toString().trim(), Toast.LENGTH_SHORT).show();
        }
    }
}
