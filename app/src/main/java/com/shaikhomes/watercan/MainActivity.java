package com.shaikhomes.watercan;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.shaikhomes.watercan.ui.BottomSheetView;
import com.shaikhomes.watercan.ui.orders.OrderCan;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomSheetView, View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private BottomSheetBehavior behavior;
    private View persistentbottomSheet;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout mExpandableLayout;
    private TextView mInstantServices, mScheduledServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_search_places);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_myorders, R.id.nav_amount_due,
                R.id.nav_canreturn, R.id.nav_moreservices, R.id.nav_callcustomercare, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        persistentbottomSheet = coordinatorLayout.findViewById(R.id.bottomsheet);
        behavior = BottomSheetBehavior.from(persistentbottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        mExpandableLayout = findViewById(R.id.expand_layout);
        mInstantServices = findViewById(R.id.instant_services);
        mScheduledServices = findViewById(R.id.scheduled_services);
        mInstantServices.setOnClickListener(this);
       /* mExpandableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void BottomSheetDesignView(View view) {

    }

    @Override
    public void BottomSheetDesignLocation(LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(17.4401, 78.3489, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.get(0).getLocality() != null)
            if (addresses.get(0).getLocality().toLowerCase().equalsIgnoreCase("hosur") || addresses.get(0).getLocality().toLowerCase().equalsIgnoreCase("hyderabad")) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.instant_services:
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Bundle args = new Bundle();
                args.putString("ARG_PARAM1", "ghouse");
                args.putString("ARG_PARAM2", "arguments");
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_insta_services, args);
                break;
        }
    }
}
