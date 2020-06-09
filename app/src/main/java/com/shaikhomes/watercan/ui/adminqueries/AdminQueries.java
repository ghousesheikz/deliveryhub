package com.shaikhomes.watercan.ui.adminqueries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaikhomes.watercan.LoginActivity;
import com.shaikhomes.watercan.OTPAuthentication;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.SignUpActivity;
import com.shaikhomes.watercan.api_services.ApiClient;
import com.shaikhomes.watercan.api_services.ApiInterface;
import com.shaikhomes.watercan.pojo.PostResponsePojo;
import com.shaikhomes.watercan.pojo.UserRegistrationPojo;
import com.shaikhomes.watercan.ui.customercare.CustomerCareActivity;
import com.shaikhomes.watercan.ui.ordercalculation.ChatAdapter;
import com.shaikhomes.watercan.ui.ordercalculation.ItemQueriesPojo;
import com.shaikhomes.watercan.ui.userquery.QueriesAdapter;
import com.shaikhomes.watercan.utility.RoundedTransformation;
import com.shaikhomes.watercan.utility.TinyDB;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Html.fromHtml;
import static com.shaikhomes.watercan.utility.AppConstants.ADDRESS_LIST;
import static com.shaikhomes.watercan.utility.AppConstants.IS_ADMIN;
import static com.shaikhomes.watercan.utility.AppConstants.OTP_ENABLED;
import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;
import static com.shaikhomes.watercan.utility.AppConstants.VENDOR_ID;

public class AdminQueries extends AppCompatActivity {
    private RecyclerView mRecyclerview;
    QueriesAdapter mAdapter;
    TinyDB tinyDB;

    private ApiInterface apiService;
    List<ItemQueriesPojo.QueryList> mQueryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_queries);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tinyDB = new TinyDB(this);
        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        mRecyclerview = findViewById(R.id.queries_list);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mQueryList = new ArrayList<>();
        mAdapter = new QueriesAdapter(this, mQueryList, new QueriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemQueriesPojo.QueryList response, int position) {
                chatDialog(response, position);
            }
        });
        mRecyclerview.setAdapter(mAdapter);
        getQueryData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getQueryData() {
        try {

            Call<ItemQueriesPojo> call = apiService.getQueries("", "", "", "all");
            call.enqueue(new Callback<ItemQueriesPojo>() {
                @Override
                public void onResponse(Call<ItemQueriesPojo> call, Response<ItemQueriesPojo> response) {
                    ItemQueriesPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        mAdapter.updateAdapter(response.body().getQueryList());
                    } else {
                        Toasty.error(AdminQueries.this, mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ItemQueriesPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }
    @NonNull
    private String getColoredSpanned(String text, String color) {
        return "<font color=" + color + ">" + text + "</font>";
    }


    private void chatDialog(ItemQueriesPojo.QueryList response, int position) {
        List<String> mChatList = new ArrayList<>();
        if (!TextUtils.isEmpty(response.getQuestion1())) {
            mChatList.add(response.getQuestion1());
        }
        if (!TextUtils.isEmpty(response.getAnswer1())) {
            mChatList.add(response.getAnswer1());
        }
        if (!TextUtils.isEmpty(response.getQuestion2())) {
            mChatList.add(response.getQuestion2());
        }
        if (!TextUtils.isEmpty(response.getAnswer2())) {
            mChatList.add(response.getAnswer2());
        }
        if (!TextUtils.isEmpty(response.getQuestion3())) {
            mChatList.add(response.getQuestion3());
        }
        if (!TextUtils.isEmpty(response.getAnswer3())) {
            mChatList.add(response.getAnswer3());
        }
        if (!TextUtils.isEmpty(response.getQuestion4())) {
            mChatList.add(response.getQuestion4());
        }
        if (!TextUtils.isEmpty(response.getAnswer4())) {
            mChatList.add(response.getAnswer4());
        }
        final int chatsize = mChatList.size();
        final Dialog dialog = new Dialog(AdminQueries.this, R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.queries_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        RelativeLayout mAdminLL = dialog.findViewById(R.id.admin_layout);
        mAdminLL.setVisibility(View.VISIBLE);
        ImageView mImage = dialog.findViewById(R.id.item_image);
        TextView mItemName = dialog.findViewById(R.id.itemName);
        TextView mVendorName = dialog.findViewById(R.id.VendorName);
        ImageView mCallVendor = dialog.findViewById(R.id.call_vendor);
        getVendorDeails(response.getVendorId(), mVendorName, mCallVendor);
        mItemName.setText( fromHtml(getColoredSpanned("ItemName - ", "#F73D81")
                + response.getItemName()
        ));
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(AdminQueries.this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        int[] COLORS = new int[]{
                R.color.colorPrimary
        };
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();
        String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + response.getQueryImage();
        Picasso.get().load(imgUrl).resize(100, 100)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .centerCrop().transform(new RoundedTransformation()).into(mImage);
        EditText mChatMsg = dialog.findViewById(R.id.edt_msg);
        FloatingActionButton mFabSend = dialog.findViewById(R.id.btn_send);
        ImageView mCloseBtn = dialog.findViewById(R.id.close_dialog);
        mCloseBtn.setOnClickListener(v -> {
            dialog.dismiss();
            getQueryData();
        });
        RecyclerView mRecyclerview = dialog.findViewById(R.id.chat_msg_list);
        ChatAdapter mAdapter = new ChatAdapter(AdminQueries.this, mChatList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(AdminQueries.this));
        mRecyclerview.setAdapter(mAdapter);

        if (!TextUtils.isEmpty(response.getAnswer4())) {
            mFabSend.setEnabled(false);
        }
        mFabSend.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mChatMsg.getText().toString().trim())) {
                if (mChatList.size() == chatsize) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();

                    ItemQueriesPojo.QueryList mPojo = new ItemQueriesPojo.QueryList();
                    mPojo.setItemId(response.getItemId());
                    mPojo.setItemName(response.getItemName());
                    mPojo.setUserId(response.getUserId());
                    mPojo.setVendorId(response.getVendorId());
                    mPojo.setUserName(tinyDB.getString(USER_NAME));
                    mPojo.setQueryDate(dateFormat.format(cal.getTime()));
                    mPojo.setQueryImage(response.getQueryImage());
                    mPojo.setQueryId(response.getQueryId());
                    boolean mSendUpdate = false;
                    if (!TextUtils.isEmpty(response.getQuestion1()) && TextUtils.isEmpty(response.getAnswer1())) {
                        mSendUpdate = true;
                        mFabSend.setEnabled(false);
                        mChatList.add(mChatMsg.getText().toString().trim());
                        mAdapter.updateAdapter(mChatList);
                        mPojo.setQuestion1(response.getQuestion1());
                        mPojo.setAnswer1(mChatMsg.getText().toString().trim());

                    } else if (!TextUtils.isEmpty(response.getQuestion2()) && TextUtils.isEmpty(response.getAnswer2())) {
                        mSendUpdate = true;
                        mFabSend.setEnabled(false);
                        mChatList.add(mChatMsg.getText().toString().trim());
                        mAdapter.updateAdapter(mChatList);
                        mPojo.setQuestion1(response.getQuestion1());
                        mPojo.setAnswer1(response.getAnswer1());
                        mPojo.setQuestion2(response.getQuestion2());
                        mPojo.setAnswer2(mChatMsg.getText().toString().trim());
                    } else if (!TextUtils.isEmpty(response.getQuestion3()) && TextUtils.isEmpty(response.getAnswer3())) {
                        mSendUpdate = true;
                        mFabSend.setEnabled(false);
                        mChatList.add(mChatMsg.getText().toString().trim());
                        mAdapter.updateAdapter(mChatList);
                        mPojo.setQuestion1(response.getQuestion1());
                        mPojo.setAnswer1(response.getAnswer1());
                        mPojo.setQuestion2(response.getQuestion2());
                        mPojo.setAnswer2(response.getAnswer2());
                        mPojo.setQuestion3(response.getQuestion3());
                        mPojo.setAnswer3(mChatMsg.getText().toString().trim());
                    } else if (!TextUtils.isEmpty(response.getQuestion4()) && TextUtils.isEmpty(response.getAnswer4())) {
                        mSendUpdate = true;
                        mFabSend.setEnabled(false);
                        mChatList.add(mChatMsg.getText().toString().trim());
                        mAdapter.updateAdapter(mChatList);
                        mPojo.setQuestion1(response.getQuestion1());
                        mPojo.setAnswer1(response.getAnswer1());
                        mPojo.setQuestion2(response.getQuestion2());
                        mPojo.setAnswer2(response.getAnswer2());
                        mPojo.setQuestion3(response.getQuestion3());
                        mPojo.setAnswer3(response.getAnswer3());
                        mPojo.setQuestion4(response.getQuestion4());
                        mPojo.setQuestion4(mChatMsg.getText().toString().trim());
                    }
                    mPojo.setUpdate("update");
                    if (mSendUpdate) {
                        Call<PostResponsePojo> call = apiService.PostItemQuery(mPojo);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        mChatMsg.setText("");
                                        Toasty.success(AdminQueries.this, "Query sent Successfully", Toast.LENGTH_SHORT, true).show();
                                    } else {
                                        Toasty.error(AdminQueries.this, pojo.getMessage(), Toast.LENGTH_SHORT, true).show();

                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                            }
                        });
                    } else {
                        Toasty.error(AdminQueries.this, "You cannot answer multiple times", Toasty.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(AdminQueries.this, "Please enter message", Toasty.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getVendorDeails(String vendorId, TextView mVendorName, ImageView mCallVendor) {
        try {
            Call<UserRegistrationPojo> call = apiService.GetUserbyuserid(vendorId);
            call.enqueue(new Callback<UserRegistrationPojo>() {
                @Override
                public void onResponse(Call<UserRegistrationPojo> call, Response<UserRegistrationPojo> response) {
                    UserRegistrationPojo mUserData = response.body();
                    if (mUserData.getStatus().equalsIgnoreCase("200")) {


                        mVendorName.setText(fromHtml(getColoredSpanned("VendorName - ", "#F73D81")
                                + mUserData.getData().get(0).getUsername()
                        ));
                        mCallVendor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String Number = mUserData.getData().get(0).getUsermobileNumber();
                                if (ContextCompat.checkSelfPermission(AdminQueries.this, Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    Number = "+91" + Number;
                                    intent.setData(Uri.parse("tel:" + Number));
                                    if (ActivityCompat.checkSelfPermission(AdminQueries.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    startActivity(intent);

                                } else {
                                    Toasty.info(AdminQueries.this, "Please give permission for calling", Toasty.LENGTH_SHORT).show();
                                    ActivityCompat.requestPermissions(AdminQueries.this,
                                            new String[]{Manifest.permission.CALL_PHONE},
                                            1);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<UserRegistrationPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }
}
