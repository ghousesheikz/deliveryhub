package com.shaikhomes.watercan.ui.venodrorderdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shaikhomes.watercan.BaseActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.pojo.EmployeeDetailsPojo;
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
import okhttp3.ResponseBody;
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
    private List<EmployeeDetailsPojo.Datum> mUsersList;
    private String mSelectedEmpId = "", mSelectedVendorId = "";
    EmployeeDetailsPojo getData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tinyDB = new TinyDB(this);
        mList = new ArrayList<>();
        recyclerView = findViewById(R.id.my_order_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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

            @Override
            public void onAssignEmp(String orderid, int position, TextView assignEmp) {
                showdialogUsers(position, assignEmp, orderid);
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

    private void showdialogUsers(int pos, TextView assignEmp, String orderid) {

        try {

            Call<ResponseBody> call = apiService.GetEmployeeDetails("", tinyDB.getString(USER_ID), "");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response != null) {
                        if (response.body() != null)

                            try {
                                String result = null;
                                result = response.body().string();
                                JsonParser parser = new JsonParser();
                                JsonElement mJson = parser.parse(result);
                                Gson gson = new Gson();
                                getData = gson.fromJson(mJson, EmployeeDetailsPojo.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (getData.getStatus().equalsIgnoreCase("200"))
                                mUsersList = getData.getData();
                            try {
                                DisplayMetrics displaymetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                                int screenWidth = displaymetrics.widthPixels;
                                final Dialog dialogcust = new Dialog(UpdateOrderDetailsActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
                                dialogcust.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialogcust.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialogcust.setContentView(R.layout.dialog_reasons);
                                dialogcust.setCancelable(true);
                                dialogcust.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialogcust.getWindow().setLayout(screenWidth, WindowManager.LayoutParams.MATCH_PARENT);
                                dialogcust.getWindow().setGravity(Gravity.CENTER);
                                dialogcust.show();
                                LinearLayout mReasonLayout = dialogcust.findViewById(R.id.reason_ll);
                                mReasonLayout.removeAllViews();
                                for (int j = 0; j < mUsersList.size(); j++) {
                                    LayoutInflater layoutInflater = (LayoutInflater) UpdateOrderDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View view = layoutInflater.inflate(R.layout.reason_text, null);
                                    final TextView mReasonTxt = view.findViewById(R.id.reason_txt);
                                    mReasonTxt.setPadding(8, 8, 8, 8);
                                    mReasonTxt.setText(mUsersList.get(j).getUsername());
                                    mReasonTxt.setTextSize(20.0f);
                                    mReasonTxt.setTextColor(Color.parseColor("#FF4500"));
                                    mReasonLayout.addView(view);
                                    final int finalJ = j;
                                    int finalJ1 = j;
                                    mReasonTxt.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            mSelectedEmpId = "";
                                            mSelectedEmpId = mUsersList.get(finalJ1).getUserid();
                                            mSelectedVendorId = mUsersList.get(finalJ1).getVendorid();
                                            assignEmp.setText(mUsersList.get(finalJ1).getUsername());
                                            mReasonTxt.setBackgroundColor(Color.parseColor("#35BF34"));
                                            mReasonTxt.setTextColor(Color.parseColor("#FFFFFF"));
                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    jsonObj = new JSONObject();
                                                    try {
                                                        Date c = Calendar.getInstance().getTime();
                                                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                                                        jsonObj.put("OrderId", orderid);
                                                        jsonObj.put("Update", "assign");
                                                        jsonObj.put("OrderStatus", "");
                                                        jsonObj.put("DeliveredBy", mSelectedEmpId + "_" + mUsersList.get(finalJ1).getUsername());
                                                        jsonObj.put("DeliveredDate", "");
                                                        RetreiveFeedTask feedTask = new RetreiveFeedTask();
                                                        feedTask.execute();
                                                        dialogcust.dismiss();
                                                        // updateData(mData, mUsersList.get(finalJ).getUsername(), mUsersList.get(finalJ).getUsermobileNumber(), pos);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }, 200);

                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure (Call < ResponseBody > call, Throwable t){

                    }
                });
            } catch(Exception e){
                e.printStackTrace();
            }

        }


    }
