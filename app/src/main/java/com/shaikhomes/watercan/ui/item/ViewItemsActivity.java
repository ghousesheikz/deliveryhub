package com.shaikhomes.watercan.ui.item;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shaikhomes.watercan.BaseActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.api_services.ApiClient;
import com.shaikhomes.watercan.api_services.ApiInterface;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.pojo.ItemPojo;
import com.shaikhomes.watercan.pojo.PostResponsePojo;
import com.shaikhomes.watercan.ui.orders.OrderCanAdapter;
import com.shaikhomes.watercan.utility.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;
import static com.shaikhomes.watercan.utility.UtilityConstants.ORDER_CAN_LIST;

public class ViewItemsActivity extends BaseActivity {
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearLayoutmanager;
    private OrderCanAdapter mAdapter;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private TinyDB tinyDB;
    private ApiInterface apiService;
    List<ItemPojo.Item> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tinyDB= new TinyDB(this);
        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        mRecyclerview = findViewById(R.id.order_item_list);
        mLinearLayoutmanager = new LinearLayoutManager(this);
        mLinearLayoutmanager.setReverseLayout(true);
        mLinearLayoutmanager.setStackFromEnd(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        mList = new ArrayList<>();
        mAdapter = new OrderCanAdapter(this, mList, new OrderCanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemPojo.Item response, int position) {

                new AlertDialog.Builder(ViewItemsActivity.this).setTitle("Edit Item").setMessage("").setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            Intent intent = new Intent(ViewItemsActivity.this, AddItemActivity.class);
                            intent.putExtra("edititem", new Gson().toJson(response));
                            startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteItem(response, position);
                    }
                }).create().show();




            }
        });
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        getItemData();
    }

    private void getItemData() {
        try {
            Call<ItemPojo> call = apiService.GetItemList(tinyDB.getString(USER_ID), "");
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

    public void deleteItem(ItemPojo.Item mPostItem, int position) {
        mPostItem.setUpdate("delete");
        Call<PostResponsePojo> call = apiService.UpdateItemDetails(mPostItem);
        call.enqueue(new Callback<PostResponsePojo>() {
            @Override
            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                PostResponsePojo pojo = response.body();
                if (pojo != null)
                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                        mList.remove(position);
                        mAdapter.updateAdapter(mList);
                        Toasty.success(ViewItemsActivity.this, "Item deleted successwfully", Toasty.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
            }
        });
    }
}
