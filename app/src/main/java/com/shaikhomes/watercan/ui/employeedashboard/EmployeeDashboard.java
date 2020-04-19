package com.shaikhomes.watercan.ui.employeedashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.shaikhomes.watercan.LoginActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.ui.customercare.CustomerCareActivity;
import com.shaikhomes.watercan.ui.vendordashboard.VendorDashboard;
import com.shaikhomes.watercan.ui.vendoruserprofile.UserProfile;
import com.shaikhomes.watercan.ui.venodrorderdetails.UpdateOrderDetailsActivity;
import com.shaikhomes.watercan.utility.TinyDB;

import es.dmoral.toasty.Toasty;

import static com.shaikhomes.watercan.utility.AppConstants.LOGIN_ENABLED;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;

public class EmployeeDashboard extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    RelativeLayout my_orders_ll;
    TinyDB tinyDB;
    private View navView;
    private TextView mUserName, mUserNumber;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        Toolbar toolbar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navView = navigationView.getHeaderView(0);
        tinyDB = new TinyDB(this);
        mUserName = navView.findViewById(R.id.txt_name);
        mUserNumber = navView.findViewById(R.id.txt_number);
        mUserName.setText("Hello ! " + tinyDB.getString(USER_NAME));
        mUserNumber.setText("+91" + tinyDB.getString(USER_MOBILE));


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        my_orders_ll = findViewById(R.id.my_orders_ll);
        my_orders_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.my_orders_ll) {
            startActivity(new Intent(EmployeeDashboard.this, EmployeeOrders.class));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
            }

            this.doubleBackToExitPressedOnce = true;
            Toasty.info(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
            // dialog("Do you want to Logout?");
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_myorders) {
            Intent intent = new Intent(EmployeeDashboard.this, EmployeeOrders.class);
            startActivity(intent);
        } else if (id == R.id.nav_userprofile) {
            Intent intent = new Intent(EmployeeDashboard.this, EmployeeProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_customercare) {
            Intent intent = new Intent(EmployeeDashboard.this, CustomerCareActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            LogoutDialog();
        }
        return true;
    }


    public void LogoutDialog() {
        AppCompatButton mBtnYes, mBtnNo;
        final Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        mBtnYes = dialog.findViewById(R.id.btn_yes);
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinyDB.remove(LOGIN_ENABLED);
                dialog.dismiss();
                Intent intent = new Intent(EmployeeDashboard.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toasty.success(EmployeeDashboard.this, "Successfully logout", Toasty.LENGTH_SHORT).show();

            }
        });
        mBtnNo = dialog.findViewById(R.id.btn_no);
        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
