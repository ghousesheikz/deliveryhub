package com.shaikhomes.watercan.ui.vendordashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shaikhomes.watercan.LoginActivity;
import com.shaikhomes.watercan.MainActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.SignUpActivity;
import com.shaikhomes.watercan.ui.item.AddItemActivity;
import com.shaikhomes.watercan.ui.item.ViewItemsActivity;
import com.shaikhomes.watercan.utility.TinyDB;

import es.dmoral.toasty.Toasty;

import static com.shaikhomes.watercan.utility.AppConstants.LOGIN_ENABLED;

public class VendorDashboard extends AppCompatActivity implements View.OnClickListener {
    TinyDB tinyDB;
    RelativeLayout mAddItem, mAddEmployee,mViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);
        tinyDB = new TinyDB(this);
        mAddEmployee = findViewById(R.id.add_employee_ll);
        mViewItems = findViewById(R.id.view_items_ll);
        mViewItems.setOnClickListener(this);
        mAddItem = findViewById(R.id.add_tems_ll);
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
            finish();

        }else if (v.getId() == R.id.view_items_ll) {
            Intent intent = new Intent(VendorDashboard.this, ViewItemsActivity.class);
            startActivity(intent);


        }
    }
}
