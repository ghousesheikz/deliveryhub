package com.shaikhomes.deliveryhub.ui.customercare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.shaikhomes.deliveryhub.R;

import es.dmoral.toasty.Toasty;

public class CustomerCareActivity extends AppCompatActivity {
    CardView mCardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_customer_care);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mCardview = findViewById(R.id.call_btn);
        mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CustomerCareActivity.this, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    callNumber("9966009289");
                } else {
                    Toasty.info(CustomerCareActivity.this, "Please give permission for calling", Toasty.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(CustomerCareActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void callNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        number = "+91" + number;
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(CustomerCareActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);

    }
}
