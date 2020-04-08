package com.shaikhomes.watercan;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.shaikhomes.watercan.api_services.ApiClient;
import com.shaikhomes.watercan.api_services.ApiInterface;
import com.shaikhomes.watercan.utility.AppEnvironment;
import com.shaikhomes.watercan.utility.BaseApplication;
import com.shaikhomes.watercan.utility.TinyDB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public abstract class BaseActivity extends AppCompatActivity {
    static ProgressDialog pd;
    public static String API_KEY = "ef9d2d6277144a99b2c8164fbf9681fd";
    public static Context Appcontext;
    public static final int REQUEST_CHECK_SETTINGS = 3;
    public static final String OK = "OK";

    public static final String CANCEL = "CANCEL";
    public ApiInterface apiService;
    public static final int MY_PERMISSIONS_REQUEST_ = 2;
    public static String mLeadNumber;
    public static String mLeadName;
    public ProgressDialog bar;
    public String PATH = Environment.getExternalStorageDirectory() + "/Download/";
    public TinyDB tinyDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        Appcontext = getApplicationContext();
        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        tinyDB = new TinyDB(this);
    }

    public void showToast(final Context activity, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static boolean isAnyStringNullOrEmpty(String... strings) {
        for (String s : strings)
            if (s == null || s.isEmpty())
                return true;
        return false;
    }

    public static void showProgress(Activity activity, String title, String msg) {
        try {
            pd = new ProgressDialog(activity);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setTitle(title);
            pd.setMessage(msg);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        } catch (Exception e) {
            pd.dismiss();
        }
    }

    public static boolean permis_check_loc(Context context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }

    }


    public static boolean permis_check_cam(Context context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }

    }

    public static void hideProgress() {
        pd.dismiss();
    }

    public boolean checkInternetConnection() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] inf = connectivity.getAllNetworkInfo();
                if (inf != null)
                    for (int i = 0; i < inf.length; i++)
                        if (inf[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
            }
        } catch (Exception e) {
            showToast(getBaseContext(), "Internet connection dropped.");
        }
        return false;
    }

    public static String hashCal(String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    public PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }
}