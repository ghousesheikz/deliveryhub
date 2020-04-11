package com.shaikhomes.watercan.ui.myorders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.api_services.ApiClient;
import com.shaikhomes.watercan.api_services.ApiInterface;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.pojo.ItemPojo;
import com.shaikhomes.watercan.pojo.OrderDelivery;
import com.shaikhomes.watercan.ui.BottomSheetView;
import com.shaikhomes.watercan.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ApiInterface apiService;
    private TinyDB tinyDB;
    private List<OrderDelivery.OrderList> mList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyOrderListAdapter mAdapter;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        tinyDB = new TinyDB(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    View view;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    BottomSheetView bottomSheetView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        bottomSheetView = (BottomSheetView) view.getContext();
        bottomSheetView.BottomSheetDesignView("hide");
        mList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.my_order_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyOrderListAdapter(getActivity(), mList, new MyOrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<OrderCalculationPojo> response, int position) {

            }
        });
        recyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment
        getOrderDetails(tinyDB.getString(USER_MOBILE));
        return view;

    }

    private void getOrderDetails(String number) {
        try {
            Call<OrderDelivery> call = apiService.GetOrderDetails(number, "");
            call.enqueue(new Callback<OrderDelivery>() {
                @Override
                public void onResponse(Call<OrderDelivery> call, Response<OrderDelivery> response) {
                    OrderDelivery mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getOrderList() != null) {
                            if (mItemData.getOrderList().size() > 0) {
                                if (mList.size() > 0) {
                                    mList.clear();
                                }
                                mList = mItemData.getOrderList();
                                mAdapter.updateAdapter(mItemData.getOrderList());
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<OrderDelivery> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }
}
