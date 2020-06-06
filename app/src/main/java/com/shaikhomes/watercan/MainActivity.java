package com.shaikhomes.watercan;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.shaikhomes.watercan.pojo.AddressPojo;
import com.shaikhomes.watercan.pojo.ItemPojo;
import com.shaikhomes.watercan.pojo.OrderDelivery;
import com.shaikhomes.watercan.pojo.PayuResponse;
import com.shaikhomes.watercan.pojo.PostResponsePojo;
import com.shaikhomes.watercan.pojo.SMSResponse;
import com.shaikhomes.watercan.pojo.UpdateOrderPojo;
import com.shaikhomes.watercan.pojo.UpdateWalletPojo;
import com.shaikhomes.watercan.ui.BottomSheetView;
import com.shaikhomes.watercan.ui.address.AddressAdapter;
import com.shaikhomes.watercan.ui.logout.LogoutFragment;
import com.shaikhomes.watercan.ui.ordercalculation.OrderCalculation;
import com.shaikhomes.watercan.ui.orders.OrderCan;
import com.shaikhomes.watercan.ui.venodrorderdetails.UpdateOrderDetailsActivity;
import com.shaikhomes.watercan.utility.TinyDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.ADDRESS_LIST;
import static com.shaikhomes.watercan.utility.AppConstants.CAT_ID;
import static com.shaikhomes.watercan.utility.AppConstants.DELIVERY_TYPE;
import static com.shaikhomes.watercan.utility.AppConstants.LOGIN_ENABLED;
import static com.shaikhomes.watercan.utility.AppConstants.ORDER_DATA;
import static com.shaikhomes.watercan.utility.AppConstants.USER_ADDRESS;
import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;
import static com.shaikhomes.watercan.utility.AppConstants.USER_MOBILE;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;

public class MainActivity extends BaseActivity implements BottomSheetView, View.OnClickListener {

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
    SMSResponse getData;
    boolean doubleBackToExitPressedOnce = false;
    BottomNavigationView mBtmnavigation;

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
        mBtmnavigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        mBtmnavigation.getMenu().getItem(0).setCheckable(true);
        mBtmnavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        SearchView finalSearchView = searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!finalSearchView.isIconified()) {
                    finalSearchView.setIconified(true);
                }
                searchItem.collapseActionView();
                if (!TextUtils.isEmpty(query)) {
                    if (!TextUtils.isEmpty(tinyDB.getString(USER_ADDRESS))) {
                        if (!TextUtils.isEmpty(tinyDB.getString(DELIVERY_TYPE))) {
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            tinyDB.remove(CAT_ID);
                            tinyDB.putString(CAT_ID, "SEA__" + query);
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_insta_services);
                        } else {
                            Toasty.info(MainActivity.this, "Please select address", Toasty.LENGTH_SHORT).show();
                        }
                    } else {
                        Toasty.info(MainActivity.this, "Please select delivery type", Toasty.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public static final String LOGOUT_TAG = "LOGOUT_TAG";
    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
           *//* new AlertDialog.Builder(MainActivity.this).setTitle("LOGOUT").setMessage("Do you want to logout?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {

                        tinyDB.remove(LOGIN_ENABLED);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toasty.success(MainActivity.this, "Successfully logout", Toasty.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }).create().show();*//*
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(LOGOUT_TAG);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            LogoutFragment logoutFragment = LogoutFragment.newInstance();

            logoutFragment.setCancelable(true);
            logoutFragment.show(ft, LOGOUT_TAG);
        }
        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void BottomSheetDesignView(String collapse) {
        if (collapse.equalsIgnoreCase("hide")) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

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
    public void DialogYes(String yes) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        Toasty.success(MainActivity.this, "Successfully logout", Toasty.LENGTH_SHORT).show();

    }

    @Override
    public void DialogNo(String no) {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_home);

    }

    @Override
    public void CustomerCare(String hide) {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
                        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_userdashboard, args);
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
                        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_userdashboard, args1);
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

                @Override
                public void onItemDelete(AddressPojo response, int position) {
                    JSONObject mJsonObject = null;
                    JSONArray mJsonArray = null;
                    mAddressList.remove(position);
                    adapter.updateAdapter(mAddressList);
                    try {
                        for (int i = 0; i < adapter.getlist().size(); i++) {

                            mJsonObject = new JSONObject(new Gson().toJson(adapter.getlist().get(i)));
                            if (mJsonArray == null) {
                                mJsonArray = new JSONArray();
                            }
                            mJsonArray.put(mJsonObject);

                        }
                        tinyDB.remove(ADDRESS_LIST);
                        tinyDB.putString(ADDRESS_LIST, mJsonArray.toString());
                        saveAddress(mJsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                        saveAddress(jsonArray.toString());
                        tinyDB.putString(USER_ADDRESS, new Gson().toJson(mPojo));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                }
            }
        });


    }

    private void saveAddress(String address) {
        try {
            Call<PostResponsePojo> call = apiService.UpdateAddress(address, tinyDB.getString(USER_ID));
            call.enqueue(new Callback<PostResponsePojo>() {
                @Override
                public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                    PostResponsePojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        Toasty.success(MainActivity.this, "Address updated Successfully", Toasty.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PayuResponse getData = null;
        OrderDelivery.OrderList mPostData = null;
        // Result Code is -1 send from Payumoney activity
        Log.d("PaymentRequest", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
            String response = transactionResponse.getPayuResponse();
            JsonParser parser = new JsonParser();
            JsonElement mJson = parser.parse(response);
            Gson gson = new Gson();
            getData = gson.fromJson(mJson, PayuResponse.class);
            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    try {
                        mPostData = new Gson().fromJson(tinyDB.getString(ORDER_DATA), OrderDelivery.OrderList.class);
                        mPostData.setPaidStatus("Success");
                        mPostData.setPaymentTxnId(getData.getResult().getPayuMoneyId());
                        Call<PostResponsePojo> call = apiService.PostOrder(mPostData);
                        OrderDelivery.OrderList finalMPostData = mPostData;
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        //clearData();
                                        UpdateWalletPojo.WalletDetail mPojo = new UpdateWalletPojo.WalletDetail();
                                        mPojo.setVendorId(finalMPostData.getVendorName());
                                        mPojo.setOnlineAmount(finalMPostData.getTotalamount());
                                        UpdateWalletStatus(mPojo, finalMPostData.getOTP());
                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                                // hud.dismiss();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(false)
                            .setTitle("Payment Success")
                            .setMessage("Your PaymentID is : " + getData.getResult().getPayuMoneyId())
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();

                                }
                            }).show();
                } else {
                    try {
                        mPostData = new Gson().fromJson(tinyDB.getString(ORDER_DATA), OrderDelivery.OrderList.class);
                        mPostData.setPaidStatus("Failure");
                        mPostData.setPaymentTxnId("0");
                        Call<PostResponsePojo> call = apiService.PostOrder(mPostData);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        //clearData();
                                        tinyDB.remove(ORDER_DATA);
                                        Toasty.error(MainActivity.this, "Payment Failed", Toast.LENGTH_SHORT, true).show();

                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                                // hud.dismiss();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Failure Transaction
                    new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(false)
                            .setTitle("Failed")
                            .setMessage("Payment Failure")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();

                                }
                            }).show();
                }

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("RESPONSE", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("RESPONSE", "Both objects are null!");
            }
        } else {
            /*try {
                mPostData = new Gson().fromJson(tinyDB.getString(ORDER_DATA), OrderDelivery.OrderList.class);
                mPostData.setPaidStatus("Success");
                mPostData.setPaymentTxnId("10020");
                Call<PostResponsePojo> call = apiService.PostOrder(mPostData);
                OrderDelivery.OrderList finalMPostData = mPostData;
                call.enqueue(new Callback<PostResponsePojo>() {
                    @Override
                    public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                        PostResponsePojo pojo = response.body();
                        if (pojo != null)
                            if (pojo.getStatus().equalsIgnoreCase("200")) {
                                //clearData();
                                UpdateWalletPojo.WalletDetail mPojo = new UpdateWalletPojo.WalletDetail();
                                mPojo.setVendorId(finalMPostData.getVendorName());
                                mPojo.setOnlineAmount(finalMPostData.getTotalamount());
                                UpdateWalletStatus(mPojo);
                            }
                    }

                    @Override
                    public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                        // hud.dismiss();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }*/
            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setTitle("Failed")
                    .setMessage("Payment Failure")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();

                        }
                    }).show();
        }
    }

    private void UpdateWalletStatus(UpdateWalletPojo.WalletDetail mPojo, String mOtp) {
        Call<PostResponsePojo> call = apiService.UpdateWalletDetails(mPojo);
        call.enqueue(new Callback<PostResponsePojo>() {
            @Override
            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                PostResponsePojo pojo = response.body();
                if (pojo != null)
                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                        tinyDB.remove(ORDER_DATA);
                        Toasty.success(MainActivity.this, "Item Ordered Successfully", Toast.LENGTH_SHORT, true).show();
                        sendSms("91" + tinyDB.getString(USER_MOBILE), "Thank you for ordering with DeliveryHUB OTP : " + mOtp + ". Please share OTP with the delivery person for verification.");
                    }
            }

            @Override
            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
            }
        });
    }

    public void sendSms(String number, String msg) {
        try {
            // Construct data
            String apiKey = "apikey=" + "rHjrP/vVGC0-6QwewVQbHjh1xnWwlnoMWXiC4ofDcK";
            String message = "&message=" + msg;
            String sender = "&sender=" + "SHAIKH";
            String numbers = "&numbers=" + number;

            // Send data
            String url = apiKey + numbers + message + sender;
            new RetrieveFeedTask().execute(url);
        } catch (Exception e) {


        }
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
                String data = urls[0];
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                conn.getOutputStream().write(data.getBytes("UTF-8"));
                final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    stringBuffer.append(line);
                }
                rd.close();
                JsonParser parser = new JsonParser();
                JsonElement mJson = parser.parse(stringBuffer.toString());
                Gson gson = new Gson();
                getData = gson.fromJson(mJson, SMSResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getData.getStatus().toLowerCase().equalsIgnoreCase("success")) {
                            Toasty.success(MainActivity.this, getData.getStatus(), Toasty.LENGTH_LONG).show();
                        } else {
                            Toasty.error(MainActivity.this, getData.getStatus(), Toasty.LENGTH_LONG).show();

                        }
                    }
                });
                Log.v("response", stringBuffer.toString());
                return stringBuffer.toString();
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item0:
                    item.setCheckable(true);
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_home);
                    return true;
                case R.id.item3:
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_order_cal);
                    return true;
                case R.id.item4:
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_queries);

                    return true;
            }
            return false;
        }
    };

   /* @Override
    public void onBackPressed() {
      *//*  DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        }*//*
    }*/
}
