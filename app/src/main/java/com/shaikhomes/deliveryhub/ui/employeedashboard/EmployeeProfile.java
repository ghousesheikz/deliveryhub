package com.shaikhomes.deliveryhub.ui.employeedashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_NAME;

public class EmployeeProfile extends AppCompatActivity {
    private TextView mUserName,mAddress,mMobileNumber,mTotalAmount,mOnlineAmount,mCODAmount;
    private TinyDB tinyDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);
        tinyDB = new TinyDB(this);
        mUserName = findViewById(R.id.user_name);
        mAddress = findViewById(R.id.address);
        mMobileNumber = findViewById(R.id.phone);
        mUserName.setText(tinyDB.getString(USER_NAME));
        mMobileNumber.setText("+91 - "+tinyDB.getString(USER_MOBILE));
    }
}
