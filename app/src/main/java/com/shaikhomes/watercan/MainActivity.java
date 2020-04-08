package com.shaikhomes.watercan;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.shaikhomes.watercan.pojo.AddressPojo;
import com.shaikhomes.watercan.ui.BottomSheetView;
import com.shaikhomes.watercan.ui.address.AddressAdapter;
import com.shaikhomes.watercan.utility.TinyDB;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

import static com.shaikhomes.watercan.utility.AppConstants.ADDRESS_LIST;
import static com.shaikhomes.watercan.utility.AppConstants.DELIVERY_TYPE;
import static com.shaikhomes.watercan.utility.AppConstants.LOGIN_ENABLED;
import static com.shaikhomes.watercan.utility.AppConstants.USER_ADDRESS;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;

public class MainActivity extends AppCompatActivity implements BottomSheetView, View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private BottomSheetBehavior behavior;
    private View persistentbottomSheet;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout mExpandableLayout;
    private TextView mInstantServices, mScheduledServices;
    private TinyDB tinyDB;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private View navView;
    private TextView mUserName, mUserNumber;
    private EditText mEdtFlatNo, mEdtApartmentName, mEdtLandmark, mEdtAreaName, mEdtCityName;
    List<Address> addresses = null;
    private TextView mTypeHome, mTypeWork, mTypeOthers;
    private String mAddType = "";
    private AppCompatButton mBtnSubmit, mBtnAddAddress, mBtnSelectAddress;
    private LinearLayout mDeliveryLL, mAddressLL, mSelectAddressLL, mAddAddressLL;
    private RecyclerView mRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private AddressAdapter adapter;
    private List<AddressPojo> mAddressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tinyDB = new TinyDB(this);
        FloatingActionButton fab = findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_search_places);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mAddressList = new ArrayList<>();
        fab.setVisibility(View.GONE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navView = navigationView.getHeaderView(0);
        mUserName = navView.findViewById(R.id.txt_name);
        mUserNumber = navView.findViewById(R.id.txt_number);
        mUserName.setText("Hello ! " + tinyDB.getString(USER_NAME));
        mUserNumber.setText("+91" + tinyDB.getString(USER_MOBILE));

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
        mBtnAddAddress = coordinatorLayout.findViewById(R.id.btn_addaddress);
        mBtnSelectAddress = coordinatorLayout.findViewById(R.id.btn_selectaddress);
        mDeliveryLL = coordinatorLayout.findViewById(R.id.delivery_ll);
        mAddressLL = coordinatorLayout.findViewById(R.id.address_ll);
        mDeliveryLL.setVisibility(View.GONE);
        mAddressLL.setVisibility(View.VISIBLE);
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
        mScheduledServices.setOnClickListener(this);
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
        mBtnSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(tinyDB.getString(ADDRESS_LIST))) {
                    try {
                        jsonArray = new JSONArray(tinyDB.getString(ADDRESS_LIST));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonArray != null) {
                        if (jsonArray.length() > 0) {
                            mAddressList = new ArrayList<>();
                            AddressPojo mPojo;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mPojo = new AddressPojo();
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                    mPojo.setAddressType(jsonObject.getString("AddressType"));
                                    mPojo.setFlatNumber(jsonObject.getString("FlatNumber"));
                                    mPojo.setApartmentName(jsonObject.getString("ApartmentName"));
                                    mPojo.setLandmark(jsonObject.getString("Landmark"));
                                    mPojo.setAreaName(jsonObject.getString("AreaName"));
                                    mPojo.setCityName(jsonObject.getString("CityName"));
                                    mAddressList.add(mPojo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            captureAddress(addresses, "", mAddressList, "selectaddress");
                        }
                    }
                }

            }
        });
        mBtnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressList.size() > 0) {
                    mAddressList.clear();
                }
                captureAddress(addresses, "", mAddressList, "addaddress");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            tinyDB.remove(LOGIN_ENABLED);
            Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
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

        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.get(0).getLocality() != null)
            if (addresses.get(0).getLocality().toLowerCase().equalsIgnoreCase("kadapa") || addresses.get(0).getLocality().toLowerCase().equalsIgnoreCase("hyderabad")) {
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
                if (tinyDB.getString(USER_ADDRESS) != null) {
                    if (!TextUtils.isEmpty(tinyDB.getString(USER_ADDRESS))) {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Bundle args = new Bundle();
                        args.putString("ARG_PARAM1", "instant");
                        args.putString("ARG_PARAM2", "arguments");
                        tinyDB.remove(DELIVERY_TYPE);
                        tinyDB.putString(DELIVERY_TYPE, "instant");
                        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_insta_services, args);
                    } else {
                        tinyDB.remove(DELIVERY_TYPE);
                        tinyDB.putString(DELIVERY_TYPE, "instant");
                        // captureAddress(addresses, "instant", mAddressList, "selectaddress");
                    }
                } else {
                    tinyDB.remove(DELIVERY_TYPE);
                    tinyDB.putString(DELIVERY_TYPE, "instant");
                    // captureAddress(addresses, "instant", mAddressList, "selectaddress");
                }
                break;
            case R.id.scheduled_services:
                if (tinyDB.getString(USER_ADDRESS) != null) {
                    if (!TextUtils.isEmpty(tinyDB.getString(USER_ADDRESS))) {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Bundle args1 = new Bundle();
                        args1.putString("ARG_PARAM1", "schedule");
                        args1.putString("ARG_PARAM2", "arguments");
                        tinyDB.remove(DELIVERY_TYPE);
                        tinyDB.putString(DELIVERY_TYPE, "schedule");
                        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_insta_services, args1);
                    } else {
                        tinyDB.remove(DELIVERY_TYPE);
                        tinyDB.putString(DELIVERY_TYPE, "schedule");
                        // captureAddress(addresses, "schedule", mAddressList, "selectaddress");
                    }
                } else {
                    tinyDB.remove(DELIVERY_TYPE);
                    tinyDB.putString(DELIVERY_TYPE, "schedule");
                    // captureAddress(addresses, "schedule", mAddressList, "selectaddress");
                }
                break;
        }
    }

    private void captureAddress(List<Address> addresses, String argument, List<AddressPojo> mAddressList, String selectaddress) {
        mAddType = "";
        final Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.address_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        mEdtAreaName = dialog.findViewById(R.id.edt_areaname);
        mEdtFlatNo = dialog.findViewById(R.id.edt_flatno);
        mEdtApartmentName = dialog.findViewById(R.id.edt_apartmentname);
        mEdtLandmark = dialog.findViewById(R.id.edt_landmark);
        mEdtCityName = dialog.findViewById(R.id.edt_cityname);
        mTypeHome = dialog.findViewById(R.id.type_home);
        mTypeWork = dialog.findViewById(R.id.type_work);
        mTypeOthers = dialog.findViewById(R.id.type_others);
        mBtnSubmit = dialog.findViewById(R.id.btn_addr_submit);
        mRecyclerview = dialog.findViewById(R.id.address_list);
        mSelectAddressLL = dialog.findViewById(R.id.select_address_ll);
        mAddAddressLL = dialog.findViewById(R.id.add_address_ll);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        if (mAddressList.size() > 0) {
            adapter = new AddressAdapter(MainActivity.this, this.mAddressList, new AddressAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(AddressPojo response, int position) {
                    tinyDB.remove(USER_ADDRESS);
                    tinyDB.putString(USER_ADDRESS, new Gson().toJson(response));
                    mDeliveryLL.setVisibility(View.VISIBLE);
                    mAddressLL.setVisibility(View.GONE);
                    dialog.dismiss();

                }
            });
            mRecyclerview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mSelectAddressLL.setVisibility(View.VISIBLE);
            mAddAddressLL.setVisibility(View.GONE);
        } else {
            mSelectAddressLL.setVisibility(View.GONE);
            mAddAddressLL.setVisibility(View.VISIBLE);
        }
        mTypeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddType = "Home";
                mTypeHome.setBackgroundResource(R.drawable.button_background);
                mTypeWork.setBackgroundResource(R.drawable.edit_text_background);
                mTypeOthers.setBackgroundResource(R.drawable.edit_text_background);
                mTypeHome.setTextColor(Color.WHITE);
                mTypeWork.setTextColor(Color.BLACK);
                mTypeOthers.setTextColor(Color.BLACK);
            }
        });
        mTypeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddType = "Work";
                mTypeHome.setBackgroundResource(R.drawable.edit_text_background);
                mTypeWork.setBackgroundResource(R.drawable.button_background);
                mTypeOthers.setBackgroundResource(R.drawable.edit_text_background);
                mTypeHome.setTextColor(Color.BLACK);
                mTypeWork.setTextColor(Color.WHITE);
                mTypeOthers.setTextColor(Color.BLACK);
            }
        });
        mTypeOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddType = "Others";
                mTypeHome.setBackgroundResource(R.drawable.edit_text_background);
                mTypeWork.setBackgroundResource(R.drawable.edit_text_background);
                mTypeOthers.setBackgroundResource(R.drawable.button_background);
                mTypeHome.setTextColor(Color.BLACK);
                mTypeWork.setTextColor(Color.BLACK);
                mTypeOthers.setTextColor(Color.WHITE);
            }
        });

        mEdtAreaName.setText(addresses.get(0).getSubLocality());
        mEdtCityName.setText(addresses.get(0).getLocality() + " (" + addresses.get(0).getPostalCode() + ")");
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mAddType)) {
                    Toasty.error(MainActivity.this, "Please select address type", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEdtFlatNo.getText().toString())) {
                    Toasty.error(MainActivity.this, "Please enter flat/building number", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEdtApartmentName.getText().toString())) {
                    Toasty.error(MainActivity.this, "Please enter apartment/street name", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEdtAreaName.getText().toString())) {
                    Toasty.error(MainActivity.this, "Please enter area name", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEdtCityName.getText().toString())) {
                    Toasty.error(MainActivity.this, "Please enter city name", Toasty.LENGTH_SHORT).show();
                } else {
                    String mAddress = "";
                    if (!TextUtils.isEmpty(mEdtLandmark.getText().toString())) {
                        mAddress = mAddType + "__" + mEdtFlatNo.getText().toString().trim() + "__" + mEdtApartmentName.getText().toString().trim() + "__" + mEdtLandmark.getText().toString().trim() + "__" + mEdtAreaName.getText().toString().trim() + "__" + mEdtCityName.getText().toString().trim();
                    } else {
                        mAddress = mAddType + "__" + mEdtFlatNo.getText().toString().trim() + "__" + mEdtApartmentName.getText().toString().trim() + "__" + "NO" + "__" + mEdtAreaName.getText().toString().trim() + "__" + mEdtCityName.getText().toString().trim();
                    }
                   /* behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Bundle args1 = new Bundle();
                    args1.putString("ARG_PARAM1", argument);
                    args1.putString("ARG_PARAM2", "arguments");
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_insta_services, args1);
                   */
                    try {
                        AddressPojo mPojo = new AddressPojo();
                        mPojo.setAddressType(mAddType);
                        mPojo.setFlatNumber(mEdtFlatNo.getText().toString());
                        mPojo.setApartmentName(mEdtApartmentName.getText().toString());
                        if (!TextUtils.isEmpty(mEdtLandmark.getText().toString())) {
                            mPojo.setLandmark(mEdtLandmark.getText().toString());
                        } else {
                            mPojo.setLandmark("NO");
                        }
                        mPojo.setAreaName(mEdtAreaName.getText().toString());
                        mPojo.setCityName(mEdtCityName.getText().toString());

                        jsonObject = new JSONObject(new Gson().toJson(mPojo));
                        if (jsonArray == null) {
                            jsonArray = new JSONArray();
                        }
                        jsonArray.put(jsonObject);
                        tinyDB.remove(ADDRESS_LIST);
                        tinyDB.putString(ADDRESS_LIST, jsonArray.toString());
                        mDeliveryLL.setVisibility(View.VISIBLE);
                        mAddressLL.setVisibility(View.GONE);
                        tinyDB.putString(USER_ADDRESS, new Gson().toJson(mPojo));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                }
            }
        });


    }
}
