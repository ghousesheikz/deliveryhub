package com.shaikhomes.watercan.ui.vendoruserprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.shaikhomes.watercan.BaseActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.pojo.OrderDelivery;
import com.shaikhomes.watercan.pojo.UpdateWalletPojo;
import com.shaikhomes.watercan.utility.TinyDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.ADDRESS_LIST;
import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;

public class UserProfile extends BaseActivity {
    private TextView mUserName,mAddress,mMobileNumber,mTotalAmount,mOnlineAmount,mCODAmount;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        tinyDB = new TinyDB(this);
        mUserName = findViewById(R.id.user_name);
        mAddress = findViewById(R.id.address);
        mMobileNumber = findViewById(R.id.phone);
        mTotalAmount = findViewById(R.id.total_amt);
        mOnlineAmount = findViewById(R.id.online_amt);
        mCODAmount = findViewById(R.id.cod_amt);
        mUserName.setText(tinyDB.getString(USER_NAME));
        mAddress.setText(tinyDB.getString(ADDRESS_LIST));
        mMobileNumber.setText("+91 - "+tinyDB.getString(USER_MOBILE));
        getWalletData(tinyDB.getString(USER_ID));
    }

    private void getWalletData(String userid) {
        try {
            Call<UpdateWalletPojo> call = apiService.GetWalletDetails(userid);
            call.enqueue(new Callback<UpdateWalletPojo>() {
                @Override
                public void onResponse(Call<UpdateWalletPojo> call, Response<UpdateWalletPojo> response) {
                    UpdateWalletPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getWalletDetails() != null) {
                            if (mItemData.getWalletDetails().size() > 0) {
                               mTotalAmount.setText("Total Amount : ₹"+mItemData.getWalletDetails().get(0).getTotalamount());
                                mOnlineAmount.setText("Online Orders Amount : ₹"+mItemData.getWalletDetails().get(0).getOnlineAmount());
                                mCODAmount.setText("COD Orders Amount : ₹"+mItemData.getWalletDetails().get(0).getCodAmount());

                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<UpdateWalletPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }
}
