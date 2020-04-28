package com.shaikhomes.watercan.ui.vendordashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.shaikhomes.watercan.LoginActivity;
import com.shaikhomes.watercan.MainActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.SignUpActivity;
import com.shaikhomes.watercan.ui.addcategories.AddCategories;
import com.shaikhomes.watercan.ui.customercare.CustomerCareActivity;
import com.shaikhomes.watercan.ui.item.AddItemActivity;
import com.shaikhomes.watercan.ui.item.ViewItemsActivity;
import com.shaikhomes.watercan.ui.logout.LogoutFragment;
import com.shaikhomes.watercan.ui.vendoruserprofile.UserProfile;
import com.shaikhomes.watercan.ui.venodrorderdetails.UpdateOrderDetailsActivity;
import com.shaikhomes.watercan.utility.TinyDB;

import es.dmoral.toasty.Toasty;

import static com.shaikhomes.watercan.MainActivity.LOGOUT_TAG;
import static com.shaikhomes.watercan.utility.AppConstants.LOGIN_ENABLED;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;

public class VendorDashboard extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    TinyDB tinyDB;
    RelativeLayout mAddItem, mAddEmployee, mViewItems, mMyOrders, mAddCat,mVendorsList;
    private View navView;
    private TextView mUserName, mUserNumber;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout mAdminLL;

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
        mAdminLL = findViewById(R.id.admin1_ll);
        mAddCat = findViewById(R.id.add_cat_ll);
        mVendorsList = findViewById(R.id.vendors_list);
        mVendorsList.setOnClickListener(this);
        mMyOrders.setOnClickListener(this);
        mAddItem.setOnClickListener(this);
        mAddEmployee.setOnClickListener(this);
        mAddCat.setOnClickListener(this);
        if (getIntent().getStringExtra("admin").equalsIgnoreCase("1")) {
            mAdminLL.setVisibility(View.VISIBLE);
        } else {
            mAdminLL.setVisibility(View.VISIBLE);
        }
    }

    /*  public boolean onCreateOptionsMenu(Menu menu) {
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
  */
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
        } else if (v.getId() == R.id.add_cat_ll) {
            Intent intent = new Intent(VendorDashboard.this, AddCategories.class);
            startActivity(intent);
        } else if (v.getId() == R.id.vendors_list) {
            Intent intent = new Intent(VendorDashboard.this, VendorsList.class);
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
        } else if (id == R.id.nav_viewitem) {
            Intent intent = new Intent(VendorDashboard.this, ViewItemsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_myorders) {
            Intent intent = new Intent(VendorDashboard.this, UpdateOrderDetailsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_userprofile) {
            Intent intent = new Intent(VendorDashboard.this, UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_customercare) {
            Intent intent = new Intent(VendorDashboard.this, CustomerCareActivity.class);
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
                Intent intent = new Intent(VendorDashboard.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toasty.success(VendorDashboard.this, "Successfully logout", Toasty.LENGTH_SHORT).show();

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
