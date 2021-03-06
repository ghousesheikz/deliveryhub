package com.shaikhomes.deliveryhub.views;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import static com.shaikhomes.deliveryhub.utility.AppConstants.LOGIN_ENABLED;


public class MyCustomDialog extends Activity {
    TextView mtxtAddress;
    String phone_no, mName, mAddress, mIsAdmin, mID, mServiceId;
    Button dialog_accept, dialog_decline;
    TextView relation;
    RelativeLayout mOKLL;
    LinearLayout mAcceptLL;
    private Ringtone ringtone;
    public TinyDB tinyDB;
    TextToSpeech t1;
    private TextToSpeech textToSpeech;
    public ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int screenWidth = displaymetrics.widthPixels;
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setLayout(screenWidth, WindowManager.LayoutParams.MATCH_PARENT);
           /* getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | PixelFormat.TRANSLUCENT);*/
            getWindow().setGravity(Gravity.CENTER);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            this.setFinishOnTouchOutside(false);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_logout);
            initializeContent();
            StringBuffer sb = new StringBuffer();
            tinyDB = new TinyDB(this);
            apiService = ApiClient.getClient(this).create(ApiInterface.class);
            //  Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

            dialog_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tinyDB.putBoolean(LOGIN_ENABLED, false);
                    finishAffinity();
                }
            });
            dialog_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }
    }


    private void initializeContent() {
        dialog_accept = findViewById(R.id.dialog_accept);
        dialog_decline = findViewById(R.id.dialog_decline);

        mtxtAddress = findViewById(R.id.txt_address);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

       /* if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}