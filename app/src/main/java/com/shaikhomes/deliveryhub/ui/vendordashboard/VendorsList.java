package com.shaikhomes.deliveryhub.ui.vendordashboard;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shaikhomes.deliveryhub.BaseActivity;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo;
import com.shaikhomes.deliveryhub.pojo.UserRegistrationPojo;
import com.shaikhomes.deliveryhub.ui.item.ViewItemsActivity;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorsList extends BaseActivity {
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearLayoutmanager;
    private TinyDB tinyDB;
    private VendorListAdapter mAdapter;
    List<UserRegistrationPojo.UserData> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerview = findViewById(R.id.vendors_lsit);

        mList = new ArrayList<>();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new VendorListAdapter(this, mList, new VendorListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserRegistrationPojo.UserData response, int position, String status) {
                UpdateVendor(response.getUserid(), position, status);
            }

            @Override
            public void onItemViewClick(UserRegistrationPojo.UserData response, int position) {
                Intent mIntent = new Intent(VendorsList.this, ViewItemsActivity.class);
                mIntent.putExtra("vendorid", response.getUserid());
                mIntent.putExtra("vendorname", response.getUsername());
                mIntent.putExtra("vendoraddress", response.getAddress());
                startActivity(mIntent);
            }
        });
        mRecyclerview.setAdapter(mAdapter);
        getVendorList();
    }

    private void UpdateVendor(String userid, int position, String status) {
        try {
            Call<PostResponsePojo> call = apiService.UpdateVendor(userid, status);
            call.enqueue(new Callback<PostResponsePojo>() {
                @Override
                public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                    PostResponsePojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        Toasty.success(VendorsList.this, "Updated Successfully", Toasty.LENGTH_SHORT).show();
                        mList.get(position).setActive(status);
                        mAdapter.updateAdapter(mList);
                    } else {
                        Toasty.error(VendorsList.this, mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void getVendorList() {
        if (checkInternetConnection()) {

            Call<UserRegistrationPojo> call = apiService.GetUserbyNumber("2", "");
            call.enqueue(new Callback<UserRegistrationPojo>() {
                @Override
                public void onResponse
                        (Call<UserRegistrationPojo> call, Response<UserRegistrationPojo> response) {
                    UserRegistrationPojo.UserData pojo = new UserRegistrationPojo.UserData();
                    if (response.body().getData() != null) {
                        if (response.body().getData().size() > 0) {
                            mList = response.body().getData();
                            mAdapter.updateAdapter(mList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserRegistrationPojo> call, Throwable t) {
                    Toasty.error(VendorsList.this, t.getMessage(), Toasty.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
