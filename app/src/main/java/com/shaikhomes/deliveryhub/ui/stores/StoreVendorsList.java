package com.shaikhomes.deliveryhub.ui.stores;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaikhomes.deliveryhub.MainActivity;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.pojo.ItemPojo;
import com.shaikhomes.deliveryhub.pojo.UserRegistrationPojo;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreVendorsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreVendorsList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreVendorsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreVendorsList.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreVendorsList newInstance(String param1, String param2) {
        StoreVendorsList fragment = new StoreVendorsList();
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
    RecyclerView recyclerView;
    StoreListAdapter mAdapter;
    private ApiInterface apiService;
    private TinyDB tinyDB;
    private List<StoreListPojo.ShopsList> mShopList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store_vendors_list, container, false);
        // Inflate the layout for this fragment
        tinyDB = new TinyDB(getActivity());
        mShopList = new ArrayList<>();
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        recyclerView = view.findViewById(R.id.store_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new StoreListAdapter(getActivity(), mShopList, new StoreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StoreListPojo.ShopsList response, int position) {
                tinyDB.putString("SVENDOR", response.getVendorUserId());
                tinyDB.putInt("MINAMT", response.getMinOrderAmt().intValue());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_stores_items);
            }

        });
        recyclerView.setAdapter(mAdapter);
        getStoresList();
        return view;
    }

    private void getStoresList() {
        try {
            Call<StoreListPojo> call = apiService.getStoresList("", "True", tinyDB.getDouble("LAT", 0.0), tinyDB.getDouble("LANG", 0.0));
            call.enqueue(new Callback<StoreListPojo>() {
                @Override
                public void onResponse(Call<StoreListPojo> call, Response<StoreListPojo> response) {
                    StoreListPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getShopsList() != null) {
                            if (mItemData.getShopsList().size() > 0) {

                                mAdapter.updateAdapter(mItemData.getShopsList());
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<StoreListPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }
}
