package com.shaikhomes.watercan.ui.vendordashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.shaikhomes.watercan.LoginActivity;
import com.shaikhomes.watercan.MainActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.SignUpActivity;
import com.shaikhomes.watercan.ui.item.AddItemActivity;
import com.shaikhomes.watercan.ui.item.ViewItemsActivity;
import com.shaikhomes.watercan.ui.vendoruserprofile.UserProfile;
import com.shaikhomes.watercan.ui.venodrorderdetails.UpdateOrderDetailsActivity;
import com.shaikhomes.watercan.utility.TinyDB;

import es.dmoral.toasty.Toasty;

import static com.shaikhomes.watercan.utility.AppConstants.LOGIN_ENABLED;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;

public class VendorDashboard extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    TinyDB tinyDB;
    RelativeLayout mAddItem, mAddEmployee, mViewItems, mMyOrders;
    private View navView;
    private TextView mUserName, mUserNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_navigation_menu);
        Toolbar toolbar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //payment environment
       /* ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
        launchPayUMoneyFlow("ghouse", "9966009289", "test_data", "2");*/
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navView = navigationView.getHeaderView(0);
        tinyDB = new TinyDB(this);
        mUserName = navView.findViewById(R.id.txt_name);
        mUserNumber = navView.findViewById(R.id.txt_number);
        mUserName.setText("Hello ! " + tinyDB.getString(USER_NAME));
        mUserNumber.setText("+91" + tinyDB.getString(USER_MOBILE));

        mAddEmployee = findViewById(R.id.add_employee_ll);
        mViewItems = findViewById(R.id.view_items_ll);
        mViewItems.setOnClickListener(this);
        mAddItem = findViewById(R.id.add_tems_ll);
        mMyOrders = findViewById(R.id.my_orders_ll);
        mMyOrders.setOnClickListener(this);
        mAddItem.setOnClickListener(this);
        mAddEmployee.setOnClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            new AlertDialog.Builder(VendorDashboard.this).setTitle("LOGOUT").setMessage("Do you want to logout?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {

                        tinyDB.remove(LOGIN_ENABLED);
                        Intent intent = new Intent(VendorDashboard.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toasty.success(VendorDashboard.this, "Successfully logout", Toasty.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_tems_ll) {
            startActivity(new Intent(VendorDashboard.this, AddItemActivity.class));
        } else if (v.getId() == R.id.add_employee_ll) {
            Intent intent = new Intent(VendorDashboard.this, SignUpActivity.class);
            intent.putExtra("isadmin", "3");
            startActivity(intent);

        } else if (v.getId() == R.id.view_items_ll) {
            Intent intent = new Intent(VendorDashboard.this, ViewItemsActivity.class);
            startActivity(intent);


        } else if (v.getId() == R.id.my_orders_ll) {
            Intent intent = new Intent(VendorDashboard.this, UpdateOrderDetailsActivity.class);
            startActivity(intent);


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_additem) {
            startActivity(new Intent(VendorDashboard.this, AddItemActivity.class));
        } else if (id == R.id.nav_addemp) {
            Intent intent = new Intent(VendorDashboard.this, SignUpActivity.class);
            intent.putExtra("isadmin", "3");
            startActivity(intent);
        }else if (id == R.id.nav_viewitem) {
            Intent intent = new Intent(VendorDashboard.this, ViewItemsActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_myorders) {
            Intent intent = new Intent(VendorDashboard.this, UpdateOrderDetailsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_userprofile) {
            Intent intent = new Intent(VendorDashboard.this, UserProfile.class);
            startActivity(intent);
        }else if (id == R.id.nav_customercare) {
            Intent intent = new Intent(VendorDashboard.this, UpdateOrderDetailsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {
            Intent intent = new Intent(VendorDashboard.this, UpdateOrderDetailsActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
