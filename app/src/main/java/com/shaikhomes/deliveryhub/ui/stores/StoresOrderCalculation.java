package com.shaikhomes.deliveryhub.ui.stores;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.shaikhomes.deliveryhub.MainActivity;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.pojo.AddressPojo;
import com.shaikhomes.deliveryhub.pojo.OrderDelivery;
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo;
import com.shaikhomes.deliveryhub.pojo.SMSResponse;
import com.shaikhomes.deliveryhub.pojo.UpdateWalletPojo;
import com.shaikhomes.deliveryhub.pojo.UserRegistrationPojo;
import com.shaikhomes.deliveryhub.ui.adminqueries.AdminQueries;
import com.shaikhomes.deliveryhub.utility.AppEnvironment;
import com.shaikhomes.deliveryhub.utility.BaseApplication;
import com.shaikhomes.deliveryhub.utility.HttpRequestRestAPI;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.ezdialoglib.EZDialog;
import spencerstudios.com.ezdialoglib.EZDialogListener;

import static android.text.Html.fromHtml;
import static com.shaikhomes.deliveryhub.BaseActivity.hashCal;
import static com.shaikhomes.deliveryhub.utility.AppConstants.DELIVERY_TYPE;
import static com.shaikhomes.deliveryhub.utility.AppConstants.ORDER_TYPE;
import static com.shaikhomes.deliveryhub.utility.AppConstants.STORE_ITEMS_ORDER_DATA;
import static com.shaikhomes.deliveryhub.utility.AppConstants.STORE_ORDER_DATA;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_ADDRESS;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_NAME;
import static com.shaikhomes.deliveryhub.utility.UtilityConstants.ORDER_CAN_LIST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoresOrderCalculation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoresOrderCalculation extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoresOrderCalculation() {
        // Required empty public constructor
    }

    static List<StoreOrderItemsPojo> mStoreItemsPojoList;
    static double totalAMt;
    static String mVendor;
    static double MrptotalAmt;

    // TODO: Rename and change types and number of parameters
    public static StoresOrderCalculation newInstance(List<StoreOrderItemsPojo> mPojoList, double totalAmt, double mrptotal, String mVendorId) {
        StoresOrderCalculation fragment = new StoresOrderCalculation();
        mStoreItemsPojoList = mPojoList;
        totalAMt = totalAmt;
        MrptotalAmt = mrptotal;
        mVendor = mVendorId;
        return fragment;
    }

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    View view;
    SMSResponse getData;
    RecyclerView orderitem_cal_list;
    StoreOrderCalculationAdapter mAdapter;
    private TextView mScheduledDate;
    private RadioGroup mRadioGroup;
    private RadioButton radioButton;
    private String mPaymentChk = "";
    private LinearLayout mScheduleLL;
    //private DecimalFormat decimalFormat;
    public ApiInterface apiService;
    @Nullable
    DatePickerDialog dialouge;
    int year, month, day;
    private boolean isDisableExitConfirmation = false;
    Calendar c;
    TinyDB tinyDB;
    private RecyclerView mRecyclerView;
    private TextView mtxtTotalAmt, mTxtCanCount, txt_discount, mTxtMrpPrice, mMasterTotal, mTxtAddressType, mTxtAddress;
    private RadioButton mOnlinePay, mCashPay;
    DecimalFormat df;
    private CircularProgressButton mOrderProceedFab;
    private double latitiude = 0.0, longitude = 0.0;
    OrderItemsListPojo mPublicOrderList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //payment environment
        ((BaseApplication) getActivity().getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stores_order_calculation, container, false);
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        tinyDB = new TinyDB(getActivity());
        df = new DecimalFormat("0.00");
        mRadioGroup = view.findViewById(R.id.radiogroup);
        mMasterTotal = view.findViewById(R.id.master_total_amount);
        mOnlinePay = view.findViewById(R.id.radio_online_pay);
        mCashPay = view.findViewById(R.id.radio_cash_on_delivery);
        mTxtAddressType = view.findViewById(R.id.txt_address_type);
        mTxtAddress = view.findViewById(R.id.txt_address);
        mTxtMrpPrice = view.findViewById(R.id.mrp_amount);
        mScheduledDate = view.findViewById(R.id.txt_scheduledate);
        mScheduleLL = view.findViewById(R.id.schedule_ll);
        mParam1 = tinyDB.getString(DELIVERY_TYPE);
        if (mParam1.equalsIgnoreCase("instant")) {
            mScheduleLL.setVisibility(View.GONE);
        } else if (mParam1.equalsIgnoreCase("schedule")) {
            mScheduleLL.setVisibility(View.VISIBLE);
        }
        mMasterTotal = view.findViewById(R.id.master_total_amount);
        mtxtTotalAmt = view.findViewById(R.id.total_amount);
        txt_discount = view.findViewById(R.id.txt_discount);
        mOrderProceedFab = view.findViewById(R.id.proceed_order);
        mScheduledDate.setOnClickListener(this);
        mOrderProceedFab.setOnClickListener(this);
        // Inflate the layout for this fragment

        String mAddress = tinyDB.getString(USER_ADDRESS);
        AddressPojo mAddPojo = new Gson().fromJson(mAddress, AddressPojo.class);
        if (mAddPojo != null) {
            mTxtAddressType.setText(mAddPojo.getAddressType());
        }
        if (mAddPojo != null) {
            if (!mAddPojo.getLandmark().equalsIgnoreCase("NO")) {
                mTxtAddress.setText(mAddPojo.getFlatNumber() + ", " + mAddPojo.getApartmentName() + ", " + mAddPojo.getLandmark() + ", " + mAddPojo.getAreaName() + ", " + mAddPojo.getCityName());
            } else {
                mTxtAddress.setText(mAddPojo.getFlatNumber() + ", " + mAddPojo.getApartmentName() + ", " + mAddPojo.getAreaName() + ", " + mAddPojo.getCityName());

            }
            if (!TextUtils.isEmpty(mAddPojo.getLat())) {
                latitiude = Double.parseDouble(mAddPojo.getLat());
            }

            if (!TextUtils.isEmpty(mAddPojo.getLang())) {
                longitude = Double.parseDouble(mAddPojo.getLang());
            }
        }
        orderitem_cal_list = view.findViewById(R.id.orderitem_cal_list);
        orderitem_cal_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new StoreOrderCalculationAdapter(getActivity(), mStoreItemsPojoList, new StoreCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StoreCategoryPojo.StoreCategoryDetail response, int position) {

            }
        });
        orderitem_cal_list.setAdapter(mAdapter);

        mtxtTotalAmt.setText("₹ " + df.format(totalAMt) + " ");
        mMasterTotal.setText("₹ " + df.format(totalAMt) + " ");
        double discount = MrptotalAmt - totalAMt;
        txt_discount.setText("₹ " + df.format(discount) + " ");
        mTxtMrpPrice.setText("₹ " + df.format(MrptotalAmt) + " ");
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_scheduledate) {
            dialouge = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                                String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                Date date = formatter.parse(dateInString);
                                formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                mScheduledDate.setText("");
                                mScheduledDate.setText(String.format("%s%s", mScheduledDate.getText().toString(), formatter.format(date)));

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, year, month, day);
            dialouge.getDatePicker().setMinDate(c.getTimeInMillis());
            dialouge.show();

        }
        if (v.getId() == R.id.proceed_order) {

            tinyDB.remove(ORDER_CAN_LIST);
            int selectedId = mRadioGroup.getCheckedRadioButtonId();

            if (validInputs()) {
                if (selectedId != -1) {
                    mOrderProceedFab.startAnimation();
                    radioButton = view.findViewById(selectedId);
                    if (radioButton.getText().toString().toLowerCase().equalsIgnoreCase("online payment")) {
                        mPaymentChk = "online";
                        Toasty.success(getActivity(), "Your Total Order AMount is " + mtxtTotalAmt.getText().toString().trim() + mPaymentChk, Toast.LENGTH_SHORT).show();
                        try {
                            String mOtp = generateOTP();

                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            OrderDelivery.OrderList mPostData = new OrderDelivery.OrderList();
                            String mItems = "";
                            for (int i = 0; i < mStoreItemsPojoList.size(); i++) {
                                mItems = mItems + mStoreItemsPojoList.get(i).getItemName() + " - ";
                                mStoreItemsPojoList.get(i).setUserName(tinyDB.getString(USER_NAME));
                                mStoreItemsPojoList.get(i).setUserMobileNo(tinyDB.getString(USER_MOBILE));
                                mStoreItemsPojoList.get(i).setVendorName(tinyDB.getString("SVENDOR"));
                                mStoreItemsPojoList.get(i).setVendorAddress(tinyDB.getString("SVENDORNAME"));
                                mStoreItemsPojoList.get(i).setAddress(mTxtAddressType.getText().toString() + "_" + mTxtAddress.getText().toString());
                                mStoreItemsPojoList.get(i).setOrderDate(df.format(c));
                                mStoreItemsPojoList.get(i).setDeliveredDate(df.format(c));
                                mStoreItemsPojoList.get(i).setItemQuantity(mStoreItemsPojoList.get(i).getItemCount());
                            }
                            mPostData.setItemName(mItems);
                            mPostData.setItemprice(totalAMt + "");

                            mPostData.setItemQuantity(String.valueOf(1));
                            mPostData.setOTP(mOtp);
                            mPostData.setUserMobileNo(tinyDB.getString(USER_MOBILE));
                            mPostData.setUserName(tinyDB.getString(USER_NAME));

                            mPostData.setVendorName(tinyDB.getString("SVENDOR"));
                            mPostData.setVendorAddress(tinyDB.getString("SVENDORNAME"));
                            mPostData.setAddress(mTxtAddressType.getText().toString() + "_" + mTxtAddress.getText().toString() + "_" + latitiude + "_" + longitude);
                            mPostData.setTotalamount(String.valueOf(totalAMt));
                            mPostData.setPaidStatus("");
                            mPostData.setTypeoforder("instant");

                            mPostData.setOrderDate(df.format(c));
                            mPostData.setOrderStatus("Pending");
                            mPostData.setOrderType("stores");
                            mPostData.setPaymentType("Online");
                            mPostData.setItemCategory("0");
                            mPostData.setDeliveredDate(null);
                            mPostData.setDeliveredBy("");
                            tinyDB.putString(ORDER_TYPE, "stores");
                            tinyDB.putString(STORE_ORDER_DATA, new Gson().toJson(mPostData));
                            OrderItemsListPojo mOrderList = new OrderItemsListPojo();
                            mOrderList.setOrderItemsList(mStoreItemsPojoList);
                            tinyDB.putString(STORE_ITEMS_ORDER_DATA, new Gson().toJson(mOrderList));
                            mOrderProceedFab.stopAnimation();
                            launchPayUMoneyFlow(tinyDB.getString(USER_NAME), tinyDB.getString(USER_MOBILE), "stores" + "DeliveryHUB", String.valueOf(totalAMt));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (radioButton.getText().toString().toLowerCase().equalsIgnoreCase("cash on delivery")) {
                        mPaymentChk = "cash";

                        Toasty.success(getActivity(), "Your Total Order AMount is " + mtxtTotalAmt.getText().toString().trim() + mPaymentChk, Toast.LENGTH_SHORT).show();
                        try {
                            String mOtp = generateOTP();

                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            OrderDelivery.OrderList mPostData = new OrderDelivery.OrderList();
                            String mItems = "";
                            for (int i = 0; i < mStoreItemsPojoList.size(); i++) {
                                mItems = mItems + mStoreItemsPojoList.get(i).getItemName() + " - ";
                                mStoreItemsPojoList.get(i).setUserName(tinyDB.getString(USER_NAME));
                                mStoreItemsPojoList.get(i).setUserMobileNo(tinyDB.getString(USER_MOBILE));
                                mStoreItemsPojoList.get(i).setVendorName(tinyDB.getString("SVENDOR"));
                                mStoreItemsPojoList.get(i).setVendorAddress(tinyDB.getString("SVENDORNAME"));
                                mStoreItemsPojoList.get(i).setAddress(mTxtAddressType.getText().toString() + "_" + mTxtAddress.getText().toString());
                                mStoreItemsPojoList.get(i).setOrderDate(df.format(c));
                                mStoreItemsPojoList.get(i).setDeliveredDate(df.format(c));
                                mStoreItemsPojoList.get(i).setItemQuantity(mStoreItemsPojoList.get(i).getItemCount());
                            }
                            mPostData.setItemName(mItems);
                            mPostData.setItemprice(totalAMt + "");

                            mPostData.setItemQuantity(String.valueOf(1));
                            mPostData.setOTP(mOtp);
                            mPostData.setUserMobileNo(tinyDB.getString(USER_MOBILE));
                            mPostData.setUserName(tinyDB.getString(USER_NAME));
                            mPostData.setVendorName(tinyDB.getString("SVENDOR"));
                            mPostData.setVendorAddress(tinyDB.getString("SVENDORNAME"));
                            mPostData.setAddress(mTxtAddressType.getText().toString() + "_" + mTxtAddress.getText().toString() + "_" + latitiude + "_" + longitude);
                            mPostData.setTotalamount(String.valueOf(totalAMt));
                            mPostData.setPaidStatus("");
                            mPostData.setTypeoforder("instant");

                            mPostData.setOrderDate(df.format(c));

                            mPostData.setOrderStatus("Pending");
                            mPostData.setOrderType("stores");
                            mPostData.setPaymentType("COD");
                            mPostData.setItemCategory("0");
                            mPostData.setDeliveredDate(null);
                            mPostData.setDeliveredBy("");
                            tinyDB.putString(ORDER_TYPE, "stores");
                            tinyDB.putString(STORE_ORDER_DATA, new Gson().toJson(mPostData));
                            OrderItemsListPojo mOrderList = new OrderItemsListPojo();
                            mOrderList.setOrderItemsList(mStoreItemsPojoList);
                            tinyDB.putString(STORE_ITEMS_ORDER_DATA, new Gson().toJson(mOrderList));
                            Call<PostResponsePojo> call = apiService.PostOrder(mPostData);
                            call.enqueue(new Callback<PostResponsePojo>() {
                                @Override
                                public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                                    mOrderProceedFab.stopAnimation();
                                    PostResponsePojo pojo = response.body();
                                    if (pojo != null)
                                        if (pojo.getStatus().equalsIgnoreCase("200")) {
                                            //clearData();

                                            mPublicOrderList = new OrderItemsListPojo();
                                            mPublicOrderList.setOrderItemsList(mStoreItemsPojoList);

                                            String mData = new Gson().toJson(mPublicOrderList.getOrderItemsList());
                                            new RetrieveFeedTask().execute(mData, mOtp, mPostData.getVendorName(), mPostData.getTotalamount());
                                            // PostOrderItems(mOrderList.getOrderItemsList(), mOtp);


                                        }
                                }

                                @Override
                                public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                                    mOrderProceedFab.stopAnimation();
                                    mOrderProceedFab.revertAnimation();
                                    // hud.dismiss();
                                }
                            });
                            //  launchPayUMoneyFlow(tinyDB.getString(USER_NAME), tinyDB.getString(USER_MOBILE), mOrdersList.get(0).getName() + "DeliveryHUB", String.valueOf(mTotalPrice));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // tinyDB.remove(USER_ADDRESS);
                }
            } else {
                mOrderProceedFab.stopAnimation();
            }
        }
    }

    class RetrieveFeedTask extends AsyncTask<String, String, String> {
        String mOtp = "", mVendorId = "", mTotalAMt = "";
        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String data = urls[0];
                mOtp = urls[1];
                mVendorId = urls[2];
                mTotalAMt = urls[3];
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, data);
                Request request = new Request.Builder()
                        .url("http://delapi.shaikhomes.com/api/OrderItems?")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                String ret = "";
                if (response.isSuccessful()) {
                    String some = response.body().string();
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(some);
                        if (jsonObject1.getString("status").equalsIgnoreCase("200")) {
                            ret = "1";
                        } else {
                            ret = "0";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return ret;
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            if (feed.equalsIgnoreCase("1")) {
                tinyDB.remove("OFFSTORES");
                new EZDialog.Builder(getActivity())
                        .setTitle("SUCCESS")
                        .setMessage("Order Created successfully")
                        .setPositiveBtnText("Ok")
                        .setCancelableOnTouchOutside(false)
                        .setHeaderColor(Color.parseColor("#11CAB8"))

                        .setTitleTextColor(Color.parseColor("#FFFFFF"))
                        .OnPositiveClicked(new EZDialogListener() {
                            @Override
                            public void OnClick() {
                                try {

                                    UpdateWalletPojo.WalletDetail mPojo = new UpdateWalletPojo.WalletDetail();
                                    mPojo.setVendorId(mVendorId);
                                    mPojo.setOnlineAmount(mTotalAMt);
                                    UpdateWalletItemsStatus(mPojo, mOtp);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .build();
            }
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    private void UpdateWalletItemsStatus(UpdateWalletPojo.WalletDetail mPojo, String mOtp) {
        Call<PostResponsePojo> call = apiService.UpdateWalletDetails(mPojo);
        call.enqueue(new Callback<PostResponsePojo>() {
            @Override
            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                PostResponsePojo pojo = response.body();
                if (pojo != null)
                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                        tinyDB.remove(STORE_ORDER_DATA);
                        Toasty.success(getActivity(), "Item Ordered Successfully", Toast.LENGTH_SHORT, true).show();
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);

                        //  Toasty.info(OTPAuthentication.this,mOtp,Toasty.LENGTH_SHORT).show();
                        sendSmsDatagen("91" + tinyDB.getString(USER_MOBILE), "Thank you for ordering with DELIVERY HUB. Please share OTP : " + mOtp + " for confirmation.");
                        sendSMStovendor(mPojo.getVendorId(), mOtp);


                    }
            }

            @Override
            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
            }
        });
    }

    private void sendSMStovendor(String vendorId, String mOtp) {
        try {
            Call<UserRegistrationPojo> call = apiService.GetUserbyuserid(vendorId);
            call.enqueue(new Callback<UserRegistrationPojo>() {
                @Override
                public void onResponse(Call<UserRegistrationPojo> call, Response<UserRegistrationPojo> response) {
                    UserRegistrationPojo mUserData = response.body();
                    if (mUserData.getStatus().equalsIgnoreCase("200")) {
                        String mMsg = "Dear "+mUserData.getData().get(0).getUsername()+" you got order from " + tinyDB.getString(USER_NAME) + "-" + tinyDB.getString(USER_MOBILE) + " ordertype-COD with OTP : " + mOtp;
                        sendSmsDatagen("91" + mUserData.getData().get(0).getUsermobileNumber(), mMsg);

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

    private void PostOrderItems(List<StoreOrderItemsPojo> mStoreItemsPojoList, String mOtp) {
        Call<PostResponsePojo> call = apiService.PostOrderItems(mStoreItemsPojoList);
        call.enqueue(new Callback<PostResponsePojo>() {
            @Override
            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                PostResponsePojo pojo = response.body();
                if (pojo != null)
                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                        //clearData();


                    }
            }

            @Override
            public void onFailure(Call<PostResponsePojo> call, Throwable t) {

                // hud.dismiss();
            }
        });
    }

    private boolean validInputs() {
        boolean flag = true;
        int selectedId = mRadioGroup.getCheckedRadioButtonId();

        if (TextUtils.isEmpty(mTxtAddressType.getText().toString()) && TextUtils.isEmpty(mTxtAddress.getText().toString())) {
            flag = false;

            Toasty.error(getActivity(), "Please Select Address", Toast.LENGTH_SHORT).show();
        } else if (selectedId == -1) {
            flag = false;

            Toasty.error(getActivity(), "Please Select Payment mode", Toast.LENGTH_SHORT).show();
        }

        return flag;
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

    class RetrieveFeedDatagenTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpRequestRestAPI httpRequestRestAPI = new HttpRequestRestAPI();


                JSONObject jsonObject = httpRequestRestAPI.commonJsonObject(url);
                if (jsonObject != null)
                    getData = new Gson().fromJson(jsonObject.toString(), SMSResponse.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getData.getStatus().toLowerCase().equalsIgnoreCase("success")) {
                            Toasty.success(getActivity(), getData.getStatus(), Toasty.LENGTH_LONG).show();
                        } else {
                            Toasty.error(getActivity(), getData.getStatus(), Toasty.LENGTH_LONG).show();

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

    private String generateOTP() {
        Random random = new Random();
        String id = String.format("%06d", random.nextInt(1000000));
        return id;
    }

    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     *
     * @param name
     * @param number
     * @param description
     */
    private void launchPayUMoneyFlow(String name, String number, String description, String amt) {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Done");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("DeliveryHUB PAYMENTS");

        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(amt);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = "SH" + System.currentTimeMillis();
        //String txnId = "TXNID720431525261327973";
        String phone = number;
        String productName = description;
        String firstName = name;
        String email = "nasuruddinshaik@gmail.com";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((BaseApplication) getActivity().getApplication()).getAppEnvironment();
        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /*
             * Hash should always be generated from your server side.
             * */
            //    generateHashFromServer(mPaymentParams);

            /*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);


            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, getActivity(), R.style.AppTheme_pink, true);


        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            // payNowButton.setEnabled(true);
        }
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

        AppEnvironment appEnvironment = ((BaseApplication) getActivity().getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }
}
