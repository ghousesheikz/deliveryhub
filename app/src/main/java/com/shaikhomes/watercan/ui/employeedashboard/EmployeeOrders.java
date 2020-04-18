package com.shaikhomes.watercan.ui.employeedashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.chaos.view.PinView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shaikhomes.watercan.BaseActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.pojo.EmployeeDetailsPojo;
import com.shaikhomes.watercan.pojo.OrderDelivery;
import com.shaikhomes.watercan.ui.venodrorderdetails.UpdateOrderAdapter;
import com.shaikhomes.watercan.utility.TinyDB;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;
import static com.shaikhomes.watercan.utility.AppConstants.VENDOR_ID;

public class EmployeeOrders extends BaseActivity {
    RecyclerView recyclerView;
    private TinyDB tinyDB;
    private List<OrderDelivery.OrderList> mList;
    EmployeeOrderAdapter mAdapter;
    JSONObject jsonObj = null;
    OrderDelivery getData = null;
    private String mPin = "", mChkoutPin = "";
    boolean mChk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_orders);
        tinyDB = new TinyDB(this);
        mList = new ArrayList<>();
        recyclerView = findViewById(R.id.my_order_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new EmployeeOrderAdapter(this, mList, new EmployeeOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderDelivery.OrderList response, int position) {
                dialogshowbtn(response.getOTP());
            }

            @Override
            public void onAssignEmp(String status, int position, TextView assignEmp) {

            }
        });
        recyclerView.setAdapter(mAdapter);

        String mEmpId = tinyDB.getString(USER_ID) + "_" + tinyDB.getString(USER_NAME);
        getOrderDetails("27_Ishaq Syed", "1");
    }

    private void getOrderDetails(String id, String vendorId) {

        try {
            Call<ResponseBody> call = apiService.GetEmployeeDetails(id, vendorId, "");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = null;
                        result = response.body().string();
                        JsonParser parser = new JsonParser();
                        JsonElement mJson = parser.parse(result);
                        Gson gson = new Gson();
                        getData = gson.fromJson(mJson, OrderDelivery.class);
                        mAdapter.updateAdapter(getData.getOrderList());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }


    public void dialogshowbtn(String mOtp) {
        mChk = false;
        mPin = "";
        final Dialog dialog = new Dialog(EmployeeOrders.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.checkin_verify_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        final PinView pinView = dialog.findViewById(R.id.firstPinView);
        pinView.setTextColor(
                ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()));
        pinView.setTextColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.colorPrimaryDark, getTheme()));
        pinView.setLineColor(
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()));
        pinView.setLineColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.colorAccent, getTheme()));
        pinView.setItemCount(4);
        pinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        pinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        pinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        pinView.setAnimationEnable(true);// start animation when adding text
        pinView.setCursorVisible(false);
        pinView.setCursorColor(
                ResourcesCompat.getColor(getResources(), R.color.sky_blue, getTheme()));
        pinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPin = editable.toString();
            }
        });
        pinView.setInputType(InputType.TYPE_CLASS_NUMBER);
        pinView.setItemBackgroundColor(Color.WHITE);
      /*  pinView.setItemBackground(getResources().getDrawable(R.drawable.button_border));
        pinView.setItemBackgroundResources(R.drawable.button_border);*/
        pinView.setHideLineWhenFilled(false);
        Button btnDialogYes = (Button) dialog.findViewById(R.id.btn_ok);

        btnDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //
                    if (!TextUtils.isEmpty(mPin)) {
                        if (mPin.equalsIgnoreCase(mOtp)) {
                            dialog.dismiss();
                            Toasty.success(EmployeeOrders.this, "OTP Success", Toasty.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }
}
