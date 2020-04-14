package com.shaikhomes.watercan.ui.venodrorderdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shaikhomes.watercan.BaseActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.pojo.OrderDelivery;
import com.shaikhomes.watercan.pojo.PostResponsePojo;
import com.shaikhomes.watercan.pojo.UpdateOrderPojo;
import com.shaikhomes.watercan.ui.item.ViewItemsActivity;
import com.shaikhomes.watercan.ui.myorders.MyOrderListAdapter;
import com.shaikhomes.watercan.utility.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;

public class UpdateOrderDetailsActivity extends BaseActivity {

    RecyclerView recyclerView;
    private TinyDB tinyDB;
    private List<OrderDelivery.OrderList> mList;
    UpdateOrderAdapter mAdapter;
    JSONObject jsonObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tinyDB = new TinyDB(this);
        mList = new ArrayList<>();
        recyclerView = findViewById(R.id.my_order_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new UpdateOrderAdapter(this, mList, new UpdateOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderDelivery.OrderList response, String status, int position) {
               /* UpdateOrderPojo mPojo = new UpdateOrderPojo();
                mPojo.setOrderId(response.getOrderId());
                mPojo.setUpdate("orderstatus");
                mPojo.setOrderStatus(status);

                mPojo.setDeliveredBy("");mPojo.setDeliveredDate("");
                UpdateOrderStatus(mPojo);*/


                jsonObj = new JSONObject();
                try {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                    jsonObj.put("OrderId", response.getOrderId());
                    jsonObj.put("Update", "orderstatus");
                    jsonObj.put("OrderStatus", status);
                    jsonObj.put("DeliveredBy", "");
                    jsonObj.put("DeliveredDate", "");
                    RetreiveFeedTask feedTask = new RetreiveFeedTask();
                    feedTask.execute();
                } catch (Exception ex) {
                }

            }
        });
        recyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment
        getOrderDetails(tinyDB.getString(USER_ID));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void UpdateOrderStatus(UpdateOrderPojo mPojo) {
        Call<PostResponsePojo> call = apiService.UpdateOrderDetails(mPojo);
        call.enqueue(new Callback<PostResponsePojo>() {
            @Override
            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                PostResponsePojo pojo = response.body();
                if (pojo != null)
                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                        getOrderDetails(tinyDB.getString(USER_ID));
                        Toasty.success(UpdateOrderDetailsActivity.this, "Order updated successwfully", Toasty.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
            }
        });
    }

    private void getOrderDetails(String id) {
        try {
            Call<OrderDelivery> call = apiService.GetVendorOrderDetails(id, "");
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

    class RetreiveFeedTask extends AsyncTask<String, Void, Integer> {
        URL url;
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String mEmpId = "", mFeedback = "";


        private Exception exception;

        protected Integer doInBackground(String... datas) {


            final String serverURL = "https://delapi.shaikhomes.com/api/UpdateOrder?";

            RequestBody body = RequestBody.create(mediaType, jsonObj.toString());
            /*Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();*/
            Request request = new Request.Builder()
                    .url(serverURL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "74edb4b6-3c73-4cbd-85f9-357f8a160d55")
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String some = response.body().string();
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(some);
                        if (jsonObject1.getString("status").equalsIgnoreCase("200")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getOrderDetails(tinyDB.getString(USER_ID));
                                    Toasty.success(UpdateOrderDetailsActivity.this, "Order updated successwfully", Toasty.LENGTH_SHORT).show();
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 1;
        }

        protected void onPostExecute(Void void1) {
            // do nothing;

            //  new AsyncHttpSendWhatsapp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
            //  feedbackDialog(feed_saleslist, "", mEnquiryMessage, spSalesId.getSelectedItemPosition(), edCustName.getText().toString().trim(), mEnquiryMessage);
        }

        public RetreiveFeedTask() {
        }

        public Exception getException() {
            return exception;
        }
    }


}
