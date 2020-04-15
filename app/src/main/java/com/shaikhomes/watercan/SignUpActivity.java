package com.shaikhomes.watercan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shaikhomes.watercan.api_services.ApiClient;
import com.shaikhomes.watercan.api_services.ApiInterface;
import com.shaikhomes.watercan.pojo.PostResponsePojo;
import com.shaikhomes.watercan.pojo.UserRegistrationPojo;
import com.shaikhomes.watercan.ui.item.AddItemActivity;
import com.shaikhomes.watercan.ui.vendordashboard.VendorDashboard;
import com.shaikhomes.watercan.utility.TinyDB;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mFirstName, mLastName, mMobileNumber, mShopName, mAddress;
    private CheckBox mTermsChk;
    private AppCompatButton mSignup;
    public ApiInterface apiService;
    private TinyDB tinyDB;
    private String isAdmin = "", vendorid = "", mUserType = "";
    private TextView mBtnUser, mBtnRetailer;
    private LinearLayout mBtnLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        tinyDB = new TinyDB(this);
        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        mFirstName = findViewById(R.id.signup_firstname);
        mLastName = findViewById(R.id.signup_lastname);
        mBtnLL = findViewById(R.id.btn_users_ll);
        mShopName = findViewById(R.id.edt_shopname);
        mAddress = findViewById(R.id.edt_address);
        mMobileNumber = findViewById(R.id.signup_mobile);
        mTermsChk = findViewById(R.id.chk_terms_condition);
        mSignup = findViewById(R.id.btn_signup);
        mBtnUser = findViewById(R.id.btn_user);
        mBtnRetailer = findViewById(R.id.btn_retailer);
        mSignup.setOnClickListener(this);
        if (getIntent().getStringExtra("isadmin") != null) {
            isAdmin = getIntent().getStringExtra("isadmin");
            mBtnLL.setVisibility(View.GONE);
            mUserType = "user";
            vendorid = tinyDB.getString(USER_ID);
        } else {
            isAdmin = "4";

            mBtnLL.setVisibility(View.VISIBLE);
            mUserType = "user";
        }

        mBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserType = "user";
                mBtnUser.setBackgroundResource(R.color.colorPrimaryDark);
                mBtnRetailer.setBackgroundResource(R.color.light_grey);
                mBtnUser.setTextColor(Color.WHITE);
                mBtnRetailer.setTextColor(Color.BLACK);
                mShopName.setVisibility(View.GONE);
                mAddress.setVisibility(View.GONE);
            }
        });
        mBtnRetailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserType = "retailer";

                mBtnUser.setBackgroundResource(R.color.light_grey);
                mBtnRetailer.setBackgroundResource(R.color.colorPrimaryDark);
                mBtnUser.setTextColor(Color.BLACK);
                mBtnRetailer.setTextColor(Color.WHITE);
                mShopName.setVisibility(View.VISIBLE);
                mAddress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // if (mTermsChk.isChecked() == true) {
        if (TextUtils.isEmpty(mFirstName.getText().toString())) {
            Toasty.error(SignUpActivity.this, "Please enter firt name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mLastName.getText().toString())) {
            Toasty.error(SignUpActivity.this, "Please enter last name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mMobileNumber.getText().toString())) {
            Toasty.error(SignUpActivity.this, "Please enter mobilenumber", Toast.LENGTH_SHORT).show();
        } else {
            if (mUserType.equalsIgnoreCase("user")) {
                String mName = mFirstName.getText().toString() + " " + mLastName.getText().toString();
                UserRegistrationPojo.UserData mPostData = new UserRegistrationPojo.UserData(mName, mMobileNumber.getText().toString(), isAdmin, "True", "", "", "", "", "", vendorid);
                Call<PostResponsePojo> call = apiService.PostUserDetails(mPostData);
                call.enqueue(new Callback<PostResponsePojo>() {
                    @Override
                    public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                        PostResponsePojo pojo = response.body();
                        if (pojo != null)
                            if (pojo.getStatus().equalsIgnoreCase("200")) {
                                clearData();
                                if (isAdmin.equalsIgnoreCase("4")) {
                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    finish();
                                }
                                Toasty.success(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT, true).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                    }
                });
            } else if (mUserType.equalsIgnoreCase("retailer")) {
                if (TextUtils.isEmpty(mShopName.getText().toString())) {
                    Toasty.error(SignUpActivity.this, "Please enter shop name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mAddress.getText().toString())) {
                    Toasty.error(SignUpActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
                }else {
                    String mName = mFirstName.getText().toString() + " " + mLastName.getText().toString();
                    String maddress = mShopName.getText().toString() + ", " + mAddress.getText().toString();
                    UserRegistrationPojo.UserData mPostData = new UserRegistrationPojo.UserData(mName, mMobileNumber.getText().toString(), "2", "True", maddress, "", "", "", "", "");
                    Call<PostResponsePojo> call = apiService.PostUserDetails(mPostData);
                    call.enqueue(new Callback<PostResponsePojo>() {
                        @Override
                        public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                            PostResponsePojo pojo = response.body();
                            if (pojo != null)
                                if (pojo.getStatus().equalsIgnoreCase("200")) {
                                    clearData();

                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    finish();

                                    Toasty.success(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT, true).show();
                                }
                        }

                        @Override
                        public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                        }
                    });
                }
            }
        }
       /* } else {
            Toasty.error(SignUpActivity.this, "Please Check the terms and conditions", Toast.LENGTH_SHORT).show();
        }*/
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/



    private void clearData() {
        mFirstName.setText("");
        mLastName.setText("");
        mMobileNumber.setText("");

    }
}
