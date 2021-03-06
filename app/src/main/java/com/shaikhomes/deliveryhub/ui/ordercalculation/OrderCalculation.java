package com.shaikhomes.deliveryhub.ui.ordercalculation;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.shaikhomes.deliveryhub.MainActivity;
import com.shaikhomes.deliveryhub.OTPAuthentication;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.model.OrderCalculationPojo;
import com.shaikhomes.deliveryhub.pojo.AddressPojo;
import com.shaikhomes.deliveryhub.pojo.OrderDelivery;
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo;
import com.shaikhomes.deliveryhub.pojo.SMSResponse;
import com.shaikhomes.deliveryhub.pojo.UserRegistrationPojo;
import com.shaikhomes.deliveryhub.ui.orders.OrderListAdapter;
import com.shaikhomes.deliveryhub.utility.AppEnvironment;
import com.shaikhomes.deliveryhub.utility.BaseApplication;
import com.shaikhomes.deliveryhub.utility.HttpRequestRestAPI;
import com.shaikhomes.deliveryhub.utility.TinyDB;
import com.shaikhomes.deliveryhub.utility.ZoomableImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.ezdialoglib.EZDialog;
import spencerstudios.com.ezdialoglib.EZDialogListener;

import static com.shaikhomes.deliveryhub.BaseActivity.hashCal;
import static com.shaikhomes.deliveryhub.utility.AppConstants.DELIVERY_TYPE;
import static com.shaikhomes.deliveryhub.utility.AppConstants.ORDER_DATA;
import static com.shaikhomes.deliveryhub.utility.AppConstants.ORDER_TYPE;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_ADDRESS;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_ID;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_NAME;
import static com.shaikhomes.deliveryhub.utility.UtilityConstants.ORDER_CAN_LIST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCalculation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCalculation extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SMSResponse getData;
    @Nullable
    DatePickerDialog dialouge;
    int year, month, day;
    private boolean isDisableExitConfirmation = false;
    Calendar c;
    private static final String ARG_IMAGE = "image";
    private static final String ARG_LITERS = "liters";
    private static final String ARG_NAME = "name";
    private static final String ARG_NOOFCANS = "noofcans";
    private static final String ARG_TOTAMT = "totamt";
    private static final String ARG_UNITAMT = "unitamt";
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<OrderCalculationPojo> mOrdersList = new ArrayList<>();
    private TinyDB tinyDB;
    private JSONArray jsonArray;
    private OrderCalculationPojo mOrderPojo;
    private OrderListAdapter mAdapter;
    private View view;
    private RecyclerView mRecyclerView;
    private TextView mtxtTotalAmt, mTxtCanCount, mTxtEmptyCanAmount, mTxtWaterPrice, mMasterTotal, mTxtAddressType, mTxtAddress;
    private CircularProgressButton mOrderProceedFab;
    private RelativeLayout mEmptycanLL;
    private Switch mEmptyCanSwitch;
    private int mEmptyCanCount = 0;
    private int mEmptyCanPrice;
    private ImageView mCanCountPlus, mCanCountMinus;
    private int totAmt = 0, mTotalPrice = 0;
    private String mPaymentChk = "";
    private RadioButton mOnlinePay, mCashPay;
    private TextView mScheduledDate;
    private RadioGroup mRadioGroup;
    private RadioButton radioButton;
    private LinearLayout mScheduleLL;
    //private DecimalFormat decimalFormat;
    public ApiInterface apiService;

    public OrderCalculation() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrderCalculation newInstance(String imageURL, String liters, String name, int noOfCans, double totalAmount, double unitAmount) {
        OrderCalculation fragment = new OrderCalculation();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE, imageURL);
        args.putString(ARG_LITERS, liters);
        args.putString(ARG_NAME, name);
        args.putInt(ARG_NOOFCANS, noOfCans);
        args.putDouble(ARG_TOTAMT, totalAmount);
        args.putDouble(ARG_UNITAMT, unitAmount);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          /*  mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
        tinyDB = new TinyDB(getActivity());
        //   decimalFormat = new DecimalFormat("0.00");
        try {
            jsonArray = new JSONArray(tinyDB.getString(ORDER_CAN_LIST));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonArray != null) {
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    mOrderPojo = new OrderCalculationPojo();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        mOrderPojo.setImageURL(jsonObject.getString("imageURL"));
                        mOrderPojo.setName(jsonObject.getString("Name"));
                        mOrderPojo.setPrice(jsonObject.getString("Price"));
                        mOrderPojo.setLiters(jsonObject.getString("Liters"));
                        mOrderPojo.setNoOfCans(jsonObject.getInt("NoOfCans"));
                        mOrderPojo.setUnitAmount(jsonObject.getInt("unitAmount"));
                        mOrderPojo.setTotalAmount(jsonObject.getInt("TotalAmount"));
                        mOrderPojo.setVendorName(jsonObject.getString("VendorName"));
                        mOrderPojo.setVendorId(jsonObject.getString("VendorId"));
                        mOrderPojo.setMinQty(jsonObject.getString("MinQty"));
                        mOrderPojo.setCategoryId(jsonObject.getString("CategoryId"));
                        mOrderPojo.setDescription(jsonObject.getString("Description"));
                        mOrderPojo.setImageURL2(jsonObject.getString("imageURL2"));
                        mOrderPojo.setImageURL3(jsonObject.getString("imageURL3"));
                        mOrderPojo.setImageURL4(jsonObject.getString("imageURL4"));
                        mOrderPojo.setItemid(jsonObject.getString("itemid"));
                        mOrdersList.add(mOrderPojo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        //payment environment
        ((BaseApplication) getActivity().getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_calculation, container, false);
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        mRadioGroup = view.findViewById(R.id.radiogroup);
        mRecyclerView = view.findViewById(R.id.order_cal_list);
        mtxtTotalAmt = view.findViewById(R.id.total_amount);
        mOrderProceedFab = view.findViewById(R.id.proceed_order);
        mEmptycanLL = view.findViewById(R.id.empty_can_ll);
        mEmptyCanSwitch = view.findViewById(R.id.swtch_add_can);
        mCanCountPlus = view.findViewById(R.id.plus_count);
        mCanCountMinus = view.findViewById(R.id.minus_count);
        mTxtCanCount = view.findViewById(R.id.empty_can_count);
        mTxtEmptyCanAmount = view.findViewById(R.id.empty_can_amount);
        mTxtWaterPrice = view.findViewById(R.id.water_amount);
        mMasterTotal = view.findViewById(R.id.master_total_amount);
        mOnlinePay = view.findViewById(R.id.radio_online_pay);
        mCashPay = view.findViewById(R.id.radio_cash_on_delivery);
        mTxtAddressType = view.findViewById(R.id.txt_address_type);
        mTxtAddress = view.findViewById(R.id.txt_address);
        mScheduledDate = view.findViewById(R.id.txt_scheduledate);
        mScheduleLL = view.findViewById(R.id.schedule_ll);
        mParam1 = tinyDB.getString(DELIVERY_TYPE);
        if (mParam1.equalsIgnoreCase("instant")) {
            mScheduleLL.setVisibility(View.GONE);
        } else if (mParam1.equalsIgnoreCase("schedule")) {
            mScheduleLL.setVisibility(View.VISIBLE);
        }
        mScheduledDate.setOnClickListener(this);
        mCanCountPlus.setOnClickListener(this);
        mCanCountMinus.setOnClickListener(this);
        mEmptyCanSwitch.setOnClickListener(this);
        mOrderProceedFab.setOnClickListener(this);

        if (mOrdersList.size() > 0) {
            totAmt = 0;
            for (int i = 0; i < mOrdersList.size(); i++) {
                totAmt += mOrdersList.get(i).getUnitAmount();
            }
            mTxtWaterPrice.setText("₹ " + totAmt + " ");
            getTotalAmt();

        }

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
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter = new OrderListAdapter(getActivity(), mOrdersList, new OrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<OrderCalculationPojo> response, int position) {
                tinyDB.remove(ORDER_CAN_LIST);
                tinyDB.putString(ORDER_CAN_LIST, new Gson().toJson(response));
                mAdapter.updateAdapter(response);
                mOrdersList = response;
                totAmt = 0;
                for (int i = 0; i < response.size(); i++) {
                    totAmt += response.get(i).getUnitAmount();
                }
                mTxtWaterPrice.setText("₹ " + totAmt + " ");
                getTotalAmt();
            }

            @Override
            public void onFabClick(OrderCalculationPojo response, int position) {
                getQueryData(response, position);
            }

            @Override
            public void onImageClick(OrderCalculationPojo response, int position, String imageno, ImageView mImageView) {
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setCancelable(false);
                ZoomableImageView imageView = dialog.findViewById(R.id.image_display);
                ImageView mImg1 = dialog.findViewById(R.id.img1);
                ImageView mImg2 = dialog.findViewById(R.id.img2);
                ImageView mImg3 = dialog.findViewById(R.id.img3);
                ImageView mImg4 = dialog.findViewById(R.id.img4);
                //imageView.setImageBitmap(bitmap);
                String imgUrl3 = "";
                if (imageno.equalsIgnoreCase("1")) {
                    imgUrl3 = "http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL();
                } else if (imageno.equalsIgnoreCase("2")) {
                    imgUrl3 = "http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL2();
                } else if (imageno.equalsIgnoreCase("3")) {
                    imgUrl3 = "http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL3();
                } else if (imageno.equalsIgnoreCase("4")) {
                    imgUrl3 = "http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL4();
                }
                Picasso.get().load(imgUrl3)
                        .placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_no_image)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(imageView);
                Picasso.get().load("http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL())
                        .placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_no_image)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(mImg1);
                Picasso.get().load("http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL2())
                        .placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_no_image)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(mImg2);
                Picasso.get().load("http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL3())
                        .placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_no_image)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(mImg3);
                Picasso.get().load("http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL4())
                        .placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_no_image)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(mImg4);
                mImg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mImgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL();
                        Picasso.get().load(mImgUrl)
                                .placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_no_image)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(imageView);
                    }
                });
                mImg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mImgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL2();
                        Picasso.get().load(mImgUrl)
                                .placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_no_image)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(imageView);
                    }
                });
                mImg3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mImgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL3();
                        Picasso.get().load(mImgUrl)
                                .placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_no_image)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(imageView);
                    }
                });
                mImg4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mImgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + response.getImageURL4();
                        Picasso.get().load(mImgUrl)
                                .placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_no_image)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(imageView);
                    }
                });
                Button button = dialog.findViewById(R.id.button_back);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation_2;

                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            try {
                                dialog.dismiss();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    }
                });

                dialog.show();
            }

        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        return view;
    }


    private void getTotalAmt() {
        mTotalPrice = 0;
        mTotalPrice = totAmt + mEmptyCanPrice;
        mtxtTotalAmt.setText("₹ " + mTotalPrice + " ");
        mMasterTotal.setText("₹ " + mTotalPrice + " ");
    }


    @Override
    public void onClick(View v) {

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
                            mPostData.setItemName(mOrdersList.get(0).getName());
                            mPostData.setItemprice(mOrdersList.get(0).getPrice());

                            mPostData.setItemQuantity(String.valueOf(mAdapter.getlist().get(0).getItemcount()));
                            mPostData.setOTP(mOtp);
                            mPostData.setUserMobileNo(tinyDB.getString(USER_MOBILE));
                            mPostData.setUserName(tinyDB.getString(USER_NAME));
                            mPostData.setVendorName(mOrdersList.get(0).getVendorId());
                            mPostData.setVendorAddress(mOrdersList.get(0).getVendorName());
                            mPostData.setAddress(mTxtAddressType.getText().toString() + "_" + mTxtAddress.getText().toString());
                            mPostData.setTotalamount(String.valueOf(mTotalPrice));
                            mPostData.setPaidStatus("");
                            mPostData.setTypeoforder(mParam1);
                            if (mParam1.equalsIgnoreCase("instant")) {
                                mPostData.setOrderDate(df.format(c));
                            } else {
                                mPostData.setOrderDate(mScheduledDate.getText().toString().trim());
                            }
                            mPostData.setOrderStatus("Pending");
                            mPostData.setOrderType("Online");
                            mPostData.setPaymentType("Online");
                            mPostData.setItemCategory(mOrdersList.get(0).getCategoryId());
                            mPostData.setDeliveredDate(null);
                            mPostData.setDeliveredBy("");
                            tinyDB.putString(ORDER_DATA, new Gson().toJson(mPostData));
                            tinyDB.putString(ORDER_TYPE, "items");
                            mOrderProceedFab.stopAnimation();
                            launchPayUMoneyFlow(tinyDB.getString(USER_NAME), tinyDB.getString(USER_MOBILE), mOrdersList.get(0).getName() + "DeliveryHUB", String.valueOf(mTotalPrice));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       /*

                        Call<PostResponsePojo> call = apiService.PostOrder(mPostData);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        //clearData();

                                        Toasty.success(getActivity(), "Item Ordered Successfully", Toast.LENGTH_SHORT, true).show();

                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                                // hud.dismiss();
                            }
                        });

*/
                    } else if (radioButton.getText().toString().toLowerCase().equalsIgnoreCase("cash on delivery")) {
                        mPaymentChk = "cash";

                        Toasty.success(getActivity(), "Your Total Order AMount is " + mtxtTotalAmt.getText().toString().trim() + mPaymentChk, Toast.LENGTH_SHORT).show();
                        try {
                            String mOtp = generateOTP();

                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            OrderDelivery.OrderList mPostData = new OrderDelivery.OrderList();
                            mPostData.setItemName(mOrdersList.get(0).getName());
                            mPostData.setItemprice(mOrdersList.get(0).getPrice());

                            mPostData.setItemQuantity(String.valueOf(mAdapter.getlist().get(0).getItemcount()));
                            mPostData.setOTP(mOtp);
                            mPostData.setUserMobileNo(tinyDB.getString(USER_MOBILE));
                            mPostData.setUserName(tinyDB.getString(USER_NAME));
                            mPostData.setVendorName(mOrdersList.get(0).getVendorId());
                            mPostData.setVendorAddress(mOrdersList.get(0).getVendorName());
                            mPostData.setAddress(mTxtAddressType.getText().toString() + "_" + mTxtAddress.getText().toString());
                            mPostData.setTotalamount(String.valueOf(mTotalPrice));
                            mPostData.setPaidStatus("");
                            mPostData.setTypeoforder(mParam1);
                            if (mParam1.equalsIgnoreCase("instant")) {
                                mPostData.setOrderDate(df.format(c));
                            } else {
                                mPostData.setOrderDate(mScheduledDate.getText().toString().trim());
                            }
                            mPostData.setOrderStatus("Pending");
                            mPostData.setOrderType("COD");
                            mPostData.setPaymentType("COD");
                            mPostData.setItemCategory(mOrdersList.get(0).getCategoryId());
                            mPostData.setDeliveredDate(null);
                            mPostData.setDeliveredBy("");
                            tinyDB.putString(ORDER_DATA, new Gson().toJson(mPostData));
                            tinyDB.putString(ORDER_TYPE, "items");
                            Call<PostResponsePojo> call = apiService.PostOrder(mPostData);
                            call.enqueue(new Callback<PostResponsePojo>() {
                                @Override
                                public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                                    mOrderProceedFab.stopAnimation();
                                    PostResponsePojo pojo = response.body();
                                    if (pojo != null)
                                        if (pojo.getStatus().equalsIgnoreCase("200")) {
                                            //clearData();

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
                                                                tinyDB.remove(ORDER_DATA);
                                                                Toasty.success(getActivity(), "Item Ordered Successfully", Toast.LENGTH_SHORT, true).show();
                                                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);

                                                                //  Toasty.info(OTPAuthentication.this,mOtp,Toasty.LENGTH_SHORT).show();
                                                                sendSmsDatagen("91" + tinyDB.getString(USER_MOBILE), "Thank you for ordering with DELIVERY HUB. Please share OTP : " + mOtp + " for confirmation.");
                                                                sendSMStovendor(mOrdersList.get(0).getVendorId(),mOtp);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    })
                                                    .build();

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




                       /* new AlertDialog.Builder(getActivity()).setTitle("Info").setMessage("Sorry, This option is not available in your area").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                               *//* try {
                                    Toasty.success(getActivity(), "Successfully logout", Toasty.LENGTH_SHORT).show();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }*//*

                            }
                        }).create().show();*/
                    }
                    // tinyDB.remove(USER_ADDRESS);
                }
            } else {
                mOrderProceedFab.stopAnimation();
            }
        } else if (v.getId() == R.id.swtch_add_can) {
            if (mEmptyCanSwitch.isChecked()) {
                mEmptycanLL.setVisibility(View.VISIBLE);
            } else {
                mEmptycanLL.setVisibility(View.GONE);

            }
        } else if (v.getId() == R.id.plus_count) {
            mEmptyCanCount = Integer.parseInt(mTxtCanCount.getText().toString());
            mEmptyCanCount = mEmptyCanCount + 1;
            mEmptyCanPrice = mEmptyCanCount * 150;
            mTxtEmptyCanAmount.setText("₹ " + mEmptyCanPrice + " ");
            mTxtCanCount.setText(String.valueOf(mEmptyCanCount));
            getTotalAmt();

        } else if (v.getId() == R.id.minus_count) {
            mEmptyCanCount = Integer.parseInt(mTxtCanCount.getText().toString());
            mEmptyCanCount = mEmptyCanCount - 1;
            if (mEmptyCanCount < 0) {
                mEmptyCanPrice = 0 * 150;
                mTxtEmptyCanAmount.setText("₹ " + mEmptyCanPrice + " ");
                mTxtCanCount.setText(String.valueOf(0));
                getTotalAmt();
            } else {
                mEmptyCanPrice = mEmptyCanCount * 150;
                mTxtEmptyCanAmount.setText("₹ " + mEmptyCanPrice + " ");
                mTxtCanCount.setText(String.valueOf(mEmptyCanCount));
                getTotalAmt();
            }
        } else if (v.getId() == R.id.txt_scheduledate) {
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
        if (mParam1.equalsIgnoreCase("instant")) {
            flag = true;
        } else if (mParam1.equalsIgnoreCase("schedule")) {
            if (TextUtils.isEmpty(mScheduledDate.getText().toString())) {
                flag = false;

                Toasty.error(getActivity(), "Please Select Schedule delivery date", Toast.LENGTH_SHORT).show();
            }
        }

        return flag;
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

    private String generateOTP() {
        Random random = new Random();
        String id = String.format("%06d", random.nextInt(1000000));
        return id;
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

    /* @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         PayuResponse getData = null;
         // Result Code is -1 send from Payumoney activity
         Log.d("PaymentRequest", "request code " + requestCode + " resultcode " + resultCode);
         if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                 null) {
             TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                     .INTENT_EXTRA_TRANSACTION_RESPONSE);
             String response = transactionResponse.getPayuResponse();
             JsonParser parser = new JsonParser();
             JsonElement mJson = parser.parse(response);
             Gson gson = new Gson();
             getData = gson.fromJson(mJson, PayuResponse.class);
             ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

             // Check which object is non-null
             if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                 if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                     //Success Transaction
                     try {
                        *//* {
                            "OrderId": "1",
                                "ItemName": "BisleriBottle",
                                "ItemQuantity": "3",
                                "OTP": "",
                                "UserMobileNo": "8688589282",
                                "UserName": "Ghouse",
                                "VendorName": "Nasuruddin",
                                "VendorAddress": "Hyderabad",
                                "Address": "Kadapa",
                                "Itemprice": "100",
                                "totalamount": "360",
                                "paid_status": "Pending",
                                "typeoforder": "Instant",
                                "OrderDate": "03-04-2020 00:00:00",
                                "OrderStatus": "Pending",
                                "OrderType": "Online",
                                "PaymentType": "Online"
                        }*//*
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        OrderDelivery.OrderList mPostData = new OrderDelivery.OrderList();
                        mPostData.setItemName(mOrdersList.get(0).getName());
                        mPostData.setItemprice(mOrdersList.get(0).getPrice());

                        mPostData.setItemQuantity(String.valueOf(mAdapter.getlist().get(0).getItemcount()));
                        mPostData.setOTP("9966");
                        mPostData.setUserMobileNo(tinyDB.getString(USER_MOBILE));
                        mPostData.setUserName(tinyDB.getString(USER_NAME));
                        mPostData.setVendorName(mOrdersList.get(0).getName());
                        mPostData.setVendorAddress(mOrdersList.get(0).getPrice());
                        mPostData.setAddress(mTxtAddressType.getText().toString() + "_" + mTxtAddress.getText().toString());
                        mPostData.setTotalamount(mtxtTotalAmt.getText().toString());
                        mPostData.setPaidStatus("Successful");
                        mPostData.setTypeoforder(mParam1);
                        if (mParam1.equalsIgnoreCase("instant")) {
                            mPostData.setOrderDate(df.format(c));
                        } else {
                            mPostData.setOrderDate(mScheduledDate.getText().toString().trim());
                        }
                        mPostData.setOrderStatus("Pending");
                        mPostData.setOrderType("Online");
                        mPostData.setPaymentType("Online");

                        Call<PostResponsePojo> call = apiService.PostOrder(mPostData);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        //clearData();

                                        Toasty.success(getActivity(), "Item Ordered Successfully", Toast.LENGTH_SHORT, true).show();

                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                                // hud.dismiss();
                            }
                        });
                      *//*  String mAddress = "", mDesc = "";
                        mAddress = mApartmentname.getText().toString().trim() + "-" + mFlatno.getText().toString().trim();
                        mDesc = "Security deposit : " + mSecurityAmt.getText().toString().trim() + "\n" + " Rent Amount : " + mRent.getText().toString().trim() + "\n" + " PaymentID : " + getData.getResult().getPayuMoneyId();
                        String mOtp = geek_OTP();
                        String mOrderID = geek_OTP();
                        sendSMS(mNumber.getText().toString().trim(), "Your Agreement is registered successfully" + " OTP : " + mOtp + "  \n Please share this OTP once work is done.");
                        final KProgressHUD hud = KProgressHUD.create(RegisterAgreement.this)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("Please wait")
                                .setCancellable(false)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .show();
                        PostRegistrationServicce mPostData = new PostRegistrationServicce("Rental Agreement", mName.getText().toString().trim(), mNumber.getText().toString().trim(), mAddress, mDesc, "Pending", " Joining Date : " + mJoinDate.getText().toString(), mOtp, String.valueOf("SHRA" + mOrderID));
                        Call<PostResponsePojo> call = apiService.ServiceRegistration(mPostData);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                                hud.dismiss();
                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        clearData();
                                        Toasty.success(RegisterAgreement.this, "Agreement Registered Successfully", Toast.LENGTH_SHORT, true).show();
                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                                hud.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*//*
                        Toasty.success(getActivity(), "Product ordered Successfully", Toast.LENGTH_SHORT, true).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new AlertDialog.Builder(getActivity())
                            .setCancelable(false)
                            .setTitle("Payment Success")
                            .setMessage("Your PaymentID is : " + getData.getResult().getPayuMoneyId())
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();

                                }
                            }).show();
                } else {
                    //Failure Transaction
                    new AlertDialog.Builder(getActivity())
                            .setCancelable(false)
                            .setTitle("Payment Failure")
                            .setMessage("Your PaymentID is : " + getData.getResult().getPayuMoneyId())
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();

                                }
                            }).show();
                }

          *//*  // Response from Payumoney
            String payuResponse = transactionResponse.getPayuResponse();

            // Response from SURl and FURL
            String merchantResponse = transactionResponse.getTransactionDetails();

            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    }).show();*//*

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("RESPONSE", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("RESPONSE", "Both objects are null!");
            }
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOrderProceedFab.dispose();
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

    private void getQueryData(OrderCalculationPojo data, int position) {
        try {
            Call<ItemQueriesPojo> call = apiService.getQueries("", tinyDB.getString(USER_ID), "1", data.getItemid());
            call.enqueue(new Callback<ItemQueriesPojo>() {
                @Override
                public void onResponse(Call<ItemQueriesPojo> call, Response<ItemQueriesPojo> response) {
                    ItemQueriesPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {

                        chatDialog(data, position, response.body().getQueryList());
                    } else {
                        Toasty.error(getActivity(), mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
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

    private void chatDialog(OrderCalculationPojo response, int position, List<ItemQueriesPojo.QueryList> mQueryList) {
        List<String> mChatList = new ArrayList<>();
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.queries_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        EditText mChatMsg = dialog.findViewById(R.id.edt_msg);
        FloatingActionButton mFabSend = dialog.findViewById(R.id.btn_send);
        ImageView mCloseBtn = dialog.findViewById(R.id.close_dialog);
        ImageView mCallBtn = dialog.findViewById(R.id.call_dialog);
        mCloseBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });
        mCallBtn.setOnClickListener(v -> {

        });
        RecyclerView mRecyclerview = dialog.findViewById(R.id.chat_msg_list);
        ChatAdapter mAdapter = new ChatAdapter(getActivity(), mQueryList);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.scrollToPosition(mAdapter.getlist().size() - 1);
        mFabSend.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mChatMsg.getText().toString().trim())) {

                mFabSend.setEnabled(false);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();


                ItemQueriesPojo.QueryList mPojo = new ItemQueriesPojo.QueryList();
                mPojo.setItemName(response.getName());
                mPojo.setItemId(response.getItemid());
                mPojo.setUserId(tinyDB.getString(USER_ID));
                mPojo.setVendorId(response.getVendorId());
                mPojo.setUserName(tinyDB.getString(USER_NAME));
                mPojo.setQueryDate(dateFormat.format(cal.getTime()));
                mPojo.setQueryImage(response.getImageURL());
                mPojo.setDescription(mChatMsg.getText().toString().trim());
                mPojo.setType("Q");
                Call<PostResponsePojo> call = apiService.PostItemQuery(mPojo);
                call.enqueue(new Callback<PostResponsePojo>() {
                    @Override
                    public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                        PostResponsePojo pojo = response.body();
                        if (pojo != null)
                            if (pojo.getStatus().equalsIgnoreCase("200")) {
                                mFabSend.setEnabled(true);
                                mChatMsg.setText("");
                                mQueryList.add(mPojo);
                                mAdapter.updateAdapter(mQueryList);
                                mRecyclerview.scrollToPosition(mAdapter.getlist().size() - 1);
                                Toasty.success(getActivity(), "Query sent Successfully", Toast.LENGTH_SHORT, true).show();
                            } else {
                                Toasty.error(getActivity(), pojo.getMessage(), Toast.LENGTH_SHORT, true).show();

                            }
                    }

                    @Override
                    public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                    }
                });

            } else {
                Toasty.error(getActivity(), "Please enter message", Toasty.LENGTH_SHORT).show();
            }

        });

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
}
