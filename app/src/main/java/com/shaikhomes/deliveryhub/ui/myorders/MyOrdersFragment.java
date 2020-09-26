package com.shaikhomes.deliveryhub.ui.myorders;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.pojo.OrderDelivery;
import com.shaikhomes.deliveryhub.ui.BottomSheetView;
import com.shaikhomes.deliveryhub.ui.stores.StoreItemsPojo;
import com.shaikhomes.deliveryhub.ui.stores.StoreOrderItemsPojo;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_MOBILE;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyOrderListAdapter(getActivity(), mList, new MyOrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderDelivery.OrderList response, int position) {
                getOrderItemDetails(response.getOrderId());
            }
        });
        recyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment
        getOrderDetails(tinyDB.getString(USER_MOBILE));
        return view;

    }

    private void getOrderItemDetails(String orderId) {
        try {
            Call<OrderItemsListPojo> call = apiService.getStoresOrderItems(orderId, "");
            call.enqueue(new Callback<OrderItemsListPojo>() {
                @Override
                public void onResponse(Call<OrderItemsListPojo> call, Response<OrderItemsListPojo> response) {
                    OrderItemsListPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getOrderItemsList() != null) {
                            DialogItems(mItemData.getOrderItemsList());
                        }

                    }
                }

                @Override
                public void onFailure(Call<OrderItemsListPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void DialogItems(List<OrderItemsListPojo.OrderItemsList> storeItemsList) {
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.orderitems_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        RecyclerView mRecyclerView = dialog.findViewById(R.id.orderitems_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderItemsAdapter mAdapter = new OrderItemsAdapter(getActivity(), storeItemsList, new OrderItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderItemsListPojo.OrderItemsList response, int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
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
