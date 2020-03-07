package com.shaikhomes.watercan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.ui.maps.MapsFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class LoginActivity extends AppCompatActivity {
    private AppCompatButton mLoginBtn;
    private TextView mRegisterUser;

    private double mLatitude = 0.00, mLongitude = 0.00;
    private Context context;
    private EditText mEdtMobNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginBtn = findViewById(R.id.btn_login);
        context = this;
        mRegisterUser = findViewById(R.id.btn_register_user);
        mEdtMobNumber = findViewById(R.id.edt_login_mob);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        // authenticateuser(mEdtMobNumber.getText().toString().trim());
                    }
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

    private void authenticateuser(String mobnumber) {
      /*  mLoginBtn.stopAnimation();
        mLoginBtn.revertAnimation();
        try {

            Call<LoginAuthentication> call = apiService.authenticateUser(mobnumber);
            call.enqueue(new Callback<LoginAuthentication>() {
                @Override
                public void onResponse(Call<LoginAuthentication> call, Response<LoginAuthentication> response) {
                    LoginAuthentication mData = response.body();
                    samsProgressDialog.close();
                    mLoginBtn.stopAnimation();
                    mLoginBtn.revertAnimation();
                    if (mData.getStatus().equalsIgnoreCase("200") && mData.getIsuser() != 0 && mData.getActive().toLowerCase().equalsIgnoreCase("true")) {
                        if (mData.getIsuser() == 1) {
                            Toasty.success(LoginActivity.this, "Admin Login \n"+mData.getName()+"\n"+mData.getAddress(), Toasty.LENGTH_SHORT).show();
                        } else if (mData.getIsuser() == 2) {
                            Toasty.success(LoginActivity.this, "User Login \n"+mData.getName()+"\n"+mData.getAddress(), Toasty.LENGTH_SHORT).show();
                        } else if (mData.getIsuser() == 3) {
                            Toasty.success(LoginActivity.this, "Employee Login \n"+mData.getName()+"\n"+mData.getAddress(), Toasty.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(LoginActivity.this, UserRequest.class));
                        finish();
                    } else if (mData.getIsuser() == 0) {
                        startActivity(new Intent(LoginActivity.this, UserRegistration.class));
                        finish();
                        Toasty.error(LoginActivity.this, "Please Register user", Toasty.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<LoginAuthentication> call, Throwable t) {
                    mLoginBtn.stopAnimation();
                    mLoginBtn.revertAnimation();
                    samsProgressDialog.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


}
