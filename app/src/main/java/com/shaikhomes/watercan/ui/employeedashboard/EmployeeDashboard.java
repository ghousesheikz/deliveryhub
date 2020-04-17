package com.shaikhomes.watercan.ui.employeedashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.shaikhomes.watercan.R;

public class EmployeeDashboard extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout my_orders_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        my_orders_ll = findViewById(R.id.my_orders_ll);
        my_orders_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.my_orders_ll){

        }
    }
}
