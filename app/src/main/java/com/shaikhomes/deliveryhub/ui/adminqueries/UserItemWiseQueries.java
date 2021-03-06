package com.shaikhomes.deliveryhub.ui.adminqueries;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo;
import com.shaikhomes.deliveryhub.pojo.UserRegistrationPojo;
import com.shaikhomes.deliveryhub.ui.ordercalculation.ChatAdapter;
import com.shaikhomes.deliveryhub.ui.ordercalculation.ItemQueriesPojo;
import com.shaikhomes.deliveryhub.ui.userquery.QueriesAdapter;
import com.shaikhomes.deliveryhub.utility.RoundedTransformation;
import com.shaikhomes.deliveryhub.utility.TinyDB;
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
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_ID;

public class UserItemWiseQueries extends AppCompatActivity {
    private RecyclerView mRecyclerview;
    QueriesAdapter mAdapter;
    TinyDB tinyDB;
    private ApiInterface apiService;
    List<ItemQueriesPojo.QueryList> mQueryList;
    ChatAdapter mChatAdapter;
    String mUserId = "";
    TextView mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_item_wise_queries);
        tinyDB = new TinyDB(this);
        mUserId = getIntent().getStringExtra("userid");
        mUserName = findViewById(R.id.txt_username);
        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        mQueryList = new ArrayList<>();
        mRecyclerview = findViewById(R.id.queries_list);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new ChatAdapter(this, mQueryList);
        mRecyclerview.setAdapter(mChatAdapter);

        mAdapter = new QueriesAdapter(this, "0", mQueryList, new QueriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemQueriesPojo.QueryList response, int position) {
                getUserQueryData(response, position);
            }
        });
        mRecyclerview.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getQueryData();
    }

    private void getQueryData() {
        try {
            Call<ItemQueriesPojo> call = apiService.getQueries("", mUserId, "user", "");
            call.enqueue(new Callback<ItemQueriesPojo>() {
                @Override
                public void onResponse(Call<ItemQueriesPojo> call, Response<ItemQueriesPojo> response) {
                    ItemQueriesPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        mQueryList = response.body().getQueryList();
                        mAdapter.updateAdapter(response.body().getQueryList());
                        if (response.body().getQueryList().size() > 0) {
                            mUserName.setText(response.body().getQueryList().get(0).getUserName());
                        }
                        // mRecyclerview.scrollToPosition(mChatAdapter.getlist().size() - 1);
                        //   chatDialog(response.body().getQueryList().get(0),1);
                    } else {
                        Toasty.error(UserItemWiseQueries.this, mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
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

    private void getUserQueryData(ItemQueriesPojo.QueryList data, int position) {
        try {
            Call<ItemQueriesPojo> call = apiService.getQueries("", data.getUserId(), "1", data.getItemId());
            call.enqueue(new Callback<ItemQueriesPojo>() {
                @Override
                public void onResponse(Call<ItemQueriesPojo> call, Response<ItemQueriesPojo> response) {
                    ItemQueriesPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        if (response.body().getQueryList().size() > 0) {

                            chatDialog(response.body().getQueryList(), position);
                        }
                    } else {
                        Toasty.error(UserItemWiseQueries.this, mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
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

    private void chatDialog(List<ItemQueriesPojo.QueryList> response, int position) {


        final Dialog dialog = new Dialog(UserItemWiseQueries.this, R.style.Theme_AppCompat_DayNight_Dialog);
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
        getVendorDeails(response.get(response.size() - 1).getVendorId(), mVendorName, mCallVendor);
        mItemName.setText(fromHtml(getColoredSpanned("ItemName - ", "#F73D81")
                + response.get(response.size() - 1).getItemName()
        ));
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(UserItemWiseQueries.this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        int[] COLORS = new int[]{
                R.color.colorPrimary
        };
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();
        String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + response.get(response.size() - 1).getQueryImage();
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
        ChatAdapter mAdapter = new ChatAdapter(UserItemWiseQueries.this, response);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(UserItemWiseQueries.this));
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.scrollToPosition(mAdapter.getlist().size() - 1);
        mFabSend.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mChatMsg.getText().toString().trim())) {

                int pos = response.size() - 1;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();

                ItemQueriesPojo.QueryList mPojo = new ItemQueriesPojo.QueryList();
                mPojo.setItemId(response.get(pos).getItemId());
                mPojo.setItemName(response.get(pos).getItemName());
                mPojo.setUserId(response.get(pos).getUserId());
                mPojo.setVendorId(response.get(pos).getVendorId());
                mPojo.setUserName(response.get(pos).getUserName());
                mPojo.setQueryDate(dateFormat.format(cal.getTime()));
                mPojo.setQueryImage(response.get(pos).getQueryImage());
                mPojo.setDescription(mChatMsg.getText().toString().trim());
                mPojo.setType("A");


                Call<PostResponsePojo> call = apiService.PostItemQuery(mPojo);
                call.enqueue(new Callback<PostResponsePojo>() {
                    @Override
                    public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> latresponse) {
                        PostResponsePojo pojo = latresponse.body();
                        if (pojo != null)
                            if (pojo.getStatus().equalsIgnoreCase("200")) {
                                mChatMsg.setText("");
                                response.add(mPojo);
                                mAdapter.updateAdapter(response);
                                mRecyclerview.scrollToPosition(mAdapter.getlist().size() - 1);
                                Toasty.success(UserItemWiseQueries.this, "Query sent Successfully", Toast.LENGTH_SHORT, true).show();
                            } else {
                                Toasty.error(UserItemWiseQueries.this, pojo.getMessage(), Toast.LENGTH_SHORT, true).show();

                            }
                    }

                    @Override
                    public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                    }
                });
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
                                if (ContextCompat.checkSelfPermission(UserItemWiseQueries.this, Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    Number = "+91" + Number;
                                    intent.setData(Uri.parse("tel:" + Number));
                                    if (ActivityCompat.checkSelfPermission(UserItemWiseQueries.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                                    Toasty.info(UserItemWiseQueries.this, "Please give permission for calling", Toasty.LENGTH_SHORT).show();
                                    ActivityCompat.requestPermissions(UserItemWiseQueries.this,
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
