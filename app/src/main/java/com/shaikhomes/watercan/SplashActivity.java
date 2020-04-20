package com.shaikhomes.watercan;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;

import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;


import com.shaikhomes.watercan.pojo.UserRegistrationPojo;
import com.shaikhomes.watercan.ui.employeedashboard.EmployeeDashboard;
import com.shaikhomes.watercan.ui.vendordashboard.VendorDashboard;
import com.shaikhomes.watercan.utility.TinyDB;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.LOGIN_ENABLED;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;

public class SplashActivity extends BaseActivity {
    private final int SPLASH_DISPLAY_LENGTH = 5000;


    private boolean isDisableExitConfirmation = false;


    //service

    private static final String TAG = "resPMain";

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    // Tracks the bound state of the service.
    private boolean mBound = false;
    private TinyDB tinyDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Fabric.with(this, new Crashlytics());*/
        setContentView(R.layout.activity_splash);
        tinyDB = new TinyDB(this);
        // getSupportActionBar().hide();
        ActivityCompat.requestPermissions(SplashActivity.this,
                new String[]{Manifest.permission.WAKE_LOCK, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE},
                1);
        if (!tinyDB.getBoolean(LOGIN_ENABLED)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            if (checkInternetConnection()) {

                Call<UserRegistrationPojo> call = apiService.GetUserbyNumber("", tinyDB.getString(USER_MOBILE));
                call.enqueue(new Callback<UserRegistrationPojo>() {
                    @Override
                    public void onResponse
                            (Call<UserRegistrationPojo> call, Response<UserRegistrationPojo> response) {
                        UserRegistrationPojo.UserData pojo = new UserRegistrationPojo.UserData();
                        if (response.body().getData() != null)
                            if (response.body().getData().size() > 0) {
                                pojo = response.body().getData().get(0);
                                if (tinyDB.getString(USER_MOBILE).equalsIgnoreCase(pojo.getUsermobileNumber()) && pojo.getActive().toLowerCase().equalsIgnoreCase("true")) {
                                    if (pojo.getIsadmin().equalsIgnoreCase("1")) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(SplashActivity.this, VendorDashboard.class);
                                                intent.putExtra("admin","1");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, SPLASH_DISPLAY_LENGTH);

                                    } else if (pojo.getIsadmin().equalsIgnoreCase("2")) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(SplashActivity.this, VendorDashboard.class);
                                                intent.putExtra("admin","2");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, SPLASH_DISPLAY_LENGTH);

                                    } else if (pojo.getIsadmin().equalsIgnoreCase("3")) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(SplashActivity.this, EmployeeDashboard.class));
                                                finish();
                                            }
                                        }, SPLASH_DISPLAY_LENGTH);

                                    } else if (pojo.getIsadmin().equalsIgnoreCase("4")) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }, SPLASH_DISPLAY_LENGTH);

                                    }
                                } else {

                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<UserRegistrationPojo> call, Throwable t) {
                        Toasty.error(SplashActivity.this, t.getMessage(), Toasty.LENGTH_LONG).show();
                    }
                });
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
