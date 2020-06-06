package com.shaikhomes.watercan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shaikhomes.watercan.custom_views.PassCodeView;
import com.shaikhomes.watercan.pojo.SMSResponse;
import com.shaikhomes.watercan.ui.employeedashboard.EmployeeDashboard;
import com.shaikhomes.watercan.ui.vendordashboard.VendorDashboard;
import com.shaikhomes.watercan.utility.TinyDB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import es.dmoral.toasty.Toasty;

import static com.shaikhomes.watercan.utility.AppConstants.IS_ADMIN;
import static com.shaikhomes.watercan.utility.AppConstants.LOGIN_ENABLED;
import static com.shaikhomes.watercan.utility.AppConstants.OTP_ENABLED;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;

public class OTPAuthentication extends AppCompatActivity {
    private PassCodeView passCodeView;
    private String mOtp = "";
    ImageView mBackArrow;
    TinyDB tinyDB;
    SMSResponse getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_authentication);
        tinyDB = new TinyDB(this);
        mBackArrow = findViewById(R.id.back_arrow);
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinyDB.putBoolean(OTP_ENABLED, false);
                startActivity(new Intent(OTPAuthentication.this, LoginActivity.class));
                finish();
            }
        });
        mOtp = generateOTP();
        //        sendSms("91"+tinyDB.getString(USER_MOBILE),"Thank you for registering with DELIVERY HUB.Please Enter OTP : " + mOtp + "  for Login.");
        sendSms("91"+tinyDB.getString(USER_MOBILE),"Thank you for registering with DELIVERY HUB. Please Enter OTP : " + mOtp + "for Login.");
       // Toast.makeText(OTPAuthentication.this, mOtp, Toast.LENGTH_SHORT).show();
        passCodeView = findViewById(R.id.pass_code_view);
        passCodeView.setOnTextChangeListener(text -> {
            if (text.length() == 4) {
                if (mOtp.equalsIgnoreCase(text.toString())) {
                    tinyDB.putBoolean(LOGIN_ENABLED, true);
                    if (tinyDB.getString(IS_ADMIN).equalsIgnoreCase("1")) {
                        Intent intent = new Intent(OTPAuthentication.this, AdminDashboard.class);
                        intent.putExtra("admin","1");
                        startActivity(intent);
                        finish();
                    } else if (tinyDB.getString(IS_ADMIN).equalsIgnoreCase("2")) {
                        Intent intent = new Intent(OTPAuthentication.this, VendorDashboard.class);
                        intent.putExtra("admin","2");
                        startActivity(intent);
                        finish();
                    } else if (tinyDB.getString(IS_ADMIN).equalsIgnoreCase("3")) {
                        startActivity(new Intent(OTPAuthentication.this, EmployeeDashboard.class));
                        finish();
                    } else if (tinyDB.getString(IS_ADMIN).equalsIgnoreCase("4")) {
                        startActivity(new Intent(OTPAuthentication.this, MainActivity.class));
                        finish();
                    }
                }
            }
        });

    }

    private String generateOTP() {
        Random random = new Random();
        String id = String.format("%04d", random.nextInt(10000));
        return id;
    }

    public void sendSms(String number, String msg) {
        try {
            // Construct data
            String apiKey = "apikey=" + "rHjrP/vVGC0-6QwewVQbHjh1xnWwlnoMWXiC4ofDcK";
            String message = "&message=" + msg;
            String sender = "&sender=" + "SHAIKH";
            String numbers = "&numbers=" + number;

            // Send data
            String url = apiKey + numbers + message + sender;
            new RetrieveFeedTask().execute(url);
        } catch (Exception e) {


        }
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
                String data = urls[0];
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                conn.getOutputStream().write(data.getBytes("UTF-8"));
                final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    stringBuffer.append(line);
                }
                rd.close();
                JsonParser parser = new JsonParser();
                JsonElement mJson = parser.parse(stringBuffer.toString());
                Gson gson = new Gson();
                getData = gson.fromJson(mJson, SMSResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getData.getStatus().toLowerCase().equalsIgnoreCase("success")) {
                            Toasty.success(OTPAuthentication.this, getData.getStatus(), Toasty.LENGTH_LONG).show();
                        } else {
                            Toasty.error(OTPAuthentication.this, getData.getStatus(), Toasty.LENGTH_LONG).show();

                        }
                    }
                });
                Log.v("response", stringBuffer.toString());
                return stringBuffer.toString();
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }
}
