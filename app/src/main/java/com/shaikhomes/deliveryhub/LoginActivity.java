package com.shaikhomes.deliveryhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.pojo.UserRegistrationPojo;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.deliveryhub.ui.maps.MapsFragment.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.shaikhomes.deliveryhub.utility.AppConstants.ADDRESS_LIST;
import static com.shaikhomes.deliveryhub.utility.AppConstants.IS_ADMIN;
import static com.shaikhomes.deliveryhub.utility.AppConstants.OTP_ENABLED;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_ID;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_NAME;
import static com.shaikhomes.deliveryhub.utility.AppConstants.VENDOR_ID;

public class LoginActivity extends AppCompatActivity {

    private TextView mRegisterUser;

    private double mLatitude = 0.00, mLongitude = 0.00;
    private Context context;
    private EditText mEdtMobNumber;
    public ApiInterface apiService;
    private TinyDB tinyDB;
    CircularProgressButton mLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        tinyDB = new TinyDB(this);
        mLoginBtn =  findViewById(R.id.btn_id);
        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        mRegisterUser = findViewById(R.id.btn_register_user);
        mEdtMobNumber = findViewById(R.id.edt_login_mob);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_LOCATION);

        mRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEdtMobNumber.getText().toString().trim())) {
                    if (mEdtMobNumber.getText().toString().length() == 10) {
                       /* startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();*/
                        mLoginBtn.startAnimation();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                authenticateuser(mEdtMobNumber.getText().toString().trim());
                            }
                        }, 2000);

                    }
                } else {

                    Toasty.error(LoginActivity.this, "Please enter 10digit mobile number", Toast.LENGTH_SHORT).show();
                }


               /* new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 3 seconds
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.water_can);
                                // mrequestButton.doneLoadingAnimation(R.color.colorPrimary, icon);

                                Intent intent = new Intent(LoginActivity.this, UserRequest.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                }, 3000);*/
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginBtn.dispose();
    }

    private void authenticateuser(String mobnumber) {
        try {
            Call<UserRegistrationPojo> call = apiService.GetUserbyNumber("", mobnumber);
            call.enqueue(new Callback<UserRegistrationPojo>() {
                @Override
                public void onResponse(Call<UserRegistrationPojo> call, Response<UserRegistrationPojo> response) {
                    mLoginBtn.stopAnimation();
                    UserRegistrationPojo mUserData = response.body();
                    if (mUserData.getStatus().equalsIgnoreCase("200")) {
                        if (mUserData.getData() != null && mUserData.getData().size() > 0) {
                            UserRegistrationPojo.UserData mData = mUserData.getData().get(0);
                            if (mData.getActive().toLowerCase().equalsIgnoreCase("true")) {
                                if (mData.getIsadmin().equalsIgnoreCase("1")) {
                                    tinyDB.putString(USER_MOBILE, mData.getUsermobileNumber());
                                    tinyDB.putString(IS_ADMIN, mData.getIsadmin());
                                    tinyDB.putString(USER_ID, mData.getUserid());
                                    tinyDB.putString(USER_NAME, mData.getUsername());
                                    tinyDB.putString(ADDRESS_LIST, mData.getAddress());
                                    tinyDB.putBoolean(OTP_ENABLED, true);
                                    startActivity(new Intent(LoginActivity.this, OTPAuthentication.class));
                                    finish();
                                } else if (mData.getIsadmin().equalsIgnoreCase("2")) {
                                    tinyDB.putString(USER_MOBILE, mData.getUsermobileNumber());
                                    tinyDB.putString(IS_ADMIN, mData.getIsadmin());
                                    tinyDB.putString(USER_ID, mData.getUserid());
                                    tinyDB.putString(USER_NAME, mData.getUsername());
                                    tinyDB.putString(ADDRESS_LIST, mData.getAddress());
                                    tinyDB.putBoolean(OTP_ENABLED, true);
                                    startActivity(new Intent(LoginActivity.this, OTPAuthentication.class));
                                    finish();
                                } else if (mData.getIsadmin().equalsIgnoreCase("3")) {
                                    tinyDB.putString(USER_MOBILE, mData.getUsermobileNumber());
                                    tinyDB.putString(IS_ADMIN, mData.getIsadmin());
                                    tinyDB.putString(USER_ID, mData.getUserid());
                                    tinyDB.putString(USER_NAME, mData.getUsername());
                                    tinyDB.putBoolean(OTP_ENABLED, true);
                                    tinyDB.putString(VENDOR_ID, mData.getVendorid());
                                    startActivity(new Intent(LoginActivity.this, OTPAuthentication.class));
                                    finish();
                                } else if (mData.getIsadmin().equalsIgnoreCase("4")) {
                                    tinyDB.putString(USER_MOBILE, mData.getUsermobileNumber());
                                    tinyDB.putString(IS_ADMIN, mData.getIsadmin());
                                    tinyDB.putString(USER_ID, mData.getUserid());
                                    tinyDB.putString(USER_NAME, mData.getUsername());
                                    tinyDB.putString(ADDRESS_LIST, mData.getAddress());
                                    tinyDB.putBoolean(OTP_ENABLED, true);
                                    startActivity(new Intent(LoginActivity.this, OTPAuthentication.class));
                                    finish();
                                }

                            }else{
                                mLoginBtn.revertAnimation();
                                Toasty.error(LoginActivity.this,"User is not active",Toasty.LENGTH_SHORT).show();
                            }
                        } else {
                            mLoginBtn.stopAnimation();
                            mLoginBtn.revertAnimation();
                            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                            finish();
                            Toasty.error(LoginActivity.this, "Please Register user", Toasty.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserRegistrationPojo> call, Throwable t) {
                    mLoginBtn.stopAnimation();
                    mLoginBtn.revertAnimation();
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }


}
