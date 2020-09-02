package com.shaikhomes.deliveryhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shaikhomes.deliveryhub.custom_views.PassCodeView;
import com.shaikhomes.deliveryhub.pojo.SMSResponse;
import com.shaikhomes.deliveryhub.ui.employeedashboard.EmployeeDashboard;
import com.shaikhomes.deliveryhub.ui.vendordashboard.VendorDashboard;
import com.shaikhomes.deliveryhub.utility.HttpRequestRestAPI;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import es.dmoral.toasty.Toasty;

import static com.shaikhomes.deliveryhub.utility.AppConstants.IS_ADMIN;
import static com.shaikhomes.deliveryhub.utility.AppConstants.LOGIN_ENABLED;
import static com.shaikhomes.deliveryhub.utility.AppConstants.OTP_ENABLED;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_MOBILE;

public class OTPAuthentication extends AppCompatActivity  {
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
        /*SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String text) {

                if (text.length() == 4) {
                    if (mOtp.equalsIgnoreCase(text)) {
                        Toasty.success(OTPAuthentication.this, "OTP verified", Toast.LENGTH_SHORT).show();
                        tinyDB.putBoolean(LOGIN_ENABLED, true);
                        if (tinyDB.getString(IS_ADMIN).equalsIgnoreCase("1")) {
                            Intent intent = new Intent(OTPAuthentication.this, AdminDashboard.class);
                            intent.putExtra("admin", "1");
                            startActivity(intent);
                            finish();
                        } else if (tinyDB.getString(IS_ADMIN).equalsIgnoreCase("2")) {
                            Intent intent = new Intent(OTPAuthentication.this, VendorDashboard.class);
                            intent.putExtra("admin", "2");
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
            }
        });*/
        mOtp = generateOTP();
        //  Toasty.info(OTPAuthentication.this,mOtp,Toasty.LENGTH_SHORT).show();
        sendSmsDatagen("91" + tinyDB.getString(USER_MOBILE), "Thank you for registering with DELIVERY HUB. Please Enter OTP : " + mOtp + " for Login.");
        //sendTxtSMS("91"+tinyDB.getString(USER_MOBILE),"Thank you for registering with DELIVERY HUB. Please Enter OTP : " + mOtp + "for Login.");
        //        sendSms("91"+tinyDB.getString(USER_MOBILE),"Thank you for registering with DELIVERY HUB.Please Enter OTP : " + mOtp + "  for Login.");
        //sendSms("91"+tinyDB.getString(USER_MOBILE),"Thank you for registering with DELIVERY HUB. Please Enter OTP : " + mOtp + "for Login.");
        // Toast.makeText(OTPAuthentication.this, mOtp, Toast.LENGTH_SHORT).show();
        passCodeView = findViewById(R.id.pass_code_view);
        passCodeView.setOnTextChangeListener(text -> {
            if (text.length() == 4) {
                if (mOtp.equalsIgnoreCase(text)) {
                    tinyDB.putBoolean(LOGIN_ENABLED, true);
                    if (tinyDB.getString(IS_ADMIN).equalsIgnoreCase("1")) {
                        Intent intent = new Intent(OTPAuthentication.this, AdminDashboard.class);
                        intent.putExtra("admin", "1");
                        startActivity(intent);
                        finish();
                    } else if (tinyDB.getString(IS_ADMIN).equalsIgnoreCase("2")) {
                        Intent intent = new Intent(OTPAuthentication.this, VendorDashboard.class);
                        intent.putExtra("admin", "2");
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    /*public void sendTxtSMS(String phoneNo, String msg) {
            try {
                String messageToSend = msg;
                String number = phoneNo;

                SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }*/
    private String generateOTP() {
        Random random = new Random();
        String id = String.format("%04d", random.nextInt(10000));
        return id;
    }


    public void sendSmsDatagen(String number, String msg) {
        try {
            // Construct data
            String auth = "auth=" + "D!~3977UHlCfD0xtt";
            String message = "&message=" + msg;
            String sender = "&senderid=" + "SHAIKH";
            String numbers = "&msisdn=" + number;

            // Send data
            String url = "https://global.datagenit.com/API/sms-api.php?" + auth + sender + numbers + message;
            new RetrieveFeedDatagenTask().execute(url);
        } catch (Exception e) {


        }
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
                conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
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

    class RetrieveFeedDatagenTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpRequestRestAPI httpRequestRestAPI = new HttpRequestRestAPI();


                JSONObject jsonObject = httpRequestRestAPI.commonJsonObject(url);
                if (jsonObject != null)
                    getData = new Gson().fromJson(jsonObject.toString(), SMSResponse.class);
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
                Log.v("response", jsonObject.toString());
                return jsonObject.toString();
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
