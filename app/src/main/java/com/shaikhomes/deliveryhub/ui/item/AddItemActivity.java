package com.shaikhomes.deliveryhub.ui.item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.shaikhomes.deliveryhub.BaseActivity;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.CategoryPojo;
import com.shaikhomes.deliveryhub.pojo.ItemPojo;
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo;
import com.shaikhomes.deliveryhub.pojo.Spinner_global_model;
import com.shaikhomes.deliveryhub.utility.RoundedTransformation;
import com.shaikhomes.deliveryhub.utility.TinyDB;
import com.shaikhomes.deliveryhub.utility.ZoomableImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_ID;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_NAME;


public class AddItemActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 10;
    public static final int SELECT_FILE2 = 20;
    public static final int SELECT_FILE3 = 30;
    public static final int SELECT_FILE4 = 40;
    public static final int REQUEST_IMAGE_CAPTURE_1 = 100;
    private String mCurrentPhotoPath_1;
    private Uri contentUri;
    private String mEncodedImage, mEncodedImage2, mEncodedImage3, mEncodedImage4, mItemCatId = "", mActive = "";
    private ImageView mUploadImage, mViewImage, mUploadImage2, mUploadImage3, mUploadImage4;
    private EditText mItemName, mItemPrice, mItemUnits, mMinQty, mItemDesc;
    private AppCompatButton mRegisterItem;
    private String mEditItem = "";
    private ItemPojo.Item mEditPojo;
    private List<CategoryPojo.CategoryDetail> mList;
    private Spinner mCatSpinner, mUnitsSpinner;
    private ArrayList<Spinner_global_model> spinner_array_category;
    private ArrayList<Spinner_global_model> spinner_array_units;
    private ArrayAdapter<Spinner_global_model> adapter_spinner_category;
    private ArrayAdapter<Spinner_global_model> adapter_spinner_units;
    private AppCompatButton mBtnActive, mBtnDeactive;
    private String mVendorId = "", mVendorName = "", mVendorAddress = "";
    private EditText mEdtRange, mEdtLat, mEdtLang;
    TinyDB tinyDB;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    KProgressHUD hud=null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        tinyDB = new TinyDB(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mList = new ArrayList<>();
        spinner_array_category = new ArrayList<>();
        spinner_array_units = new ArrayList<>();
        spinner_array_units.add(new Spinner_global_model("-1", "Select Units"));
        spinner_array_units.add(new Spinner_global_model("0", "Kgs"));
        spinner_array_units.add(new Spinner_global_model("1", "Ltrs"));
        spinner_array_units.add(new Spinner_global_model("2", "Pcs"));
        spinner_array_units.add(new Spinner_global_model("3", "gms"));
        spinner_array_units.add(new Spinner_global_model("4", "ml"));
        spinner_array_units.add(new Spinner_global_model("3", "crates"));

        spinner_array_category.add(new Spinner_global_model("-1", "Select Category"));
        mUploadImage = findViewById(R.id.upload_image);
        mUploadImage.setOnClickListener(this);
        mUploadImage2 = findViewById(R.id.upload_image2);
        mUploadImage2.setOnClickListener(this);
        mUploadImage3 = findViewById(R.id.upload_image3);
        mUploadImage3.setOnClickListener(this);
        mUploadImage4 = findViewById(R.id.upload_image4);
        mUploadImage4.setOnClickListener(this);
        mViewImage = findViewById(R.id.pickup_image);
        mViewImage.setOnClickListener(this);
        mItemName = findViewById(R.id.edt_itemname);
        mItemPrice = findViewById(R.id.edt_itemprice);
        mItemUnits = findViewById(R.id.edt_itemunits);
        mMinQty = findViewById(R.id.edt_minqty);
        mRegisterItem = findViewById(R.id.btn_submit);
        mBtnActive = findViewById(R.id.btn_active);
        mBtnDeactive = findViewById(R.id.btn_deactive);
        mItemDesc = findViewById(R.id.edt_itemdesc);
        mBtnActive.setOnClickListener(this);
        mBtnDeactive.setOnClickListener(this);
        mRegisterItem.setOnClickListener(this);
        mCatSpinner = findViewById(R.id.spn_category);
        adapter_spinner_category = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spinner_array_category);
        adapter_spinner_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCatSpinner.setAdapter(adapter_spinner_category);

        mEdtLang = findViewById(R.id.edt_lang);
        mEdtRange = findViewById(R.id.edt_range);
        mEdtLat = findViewById(R.id.edt_lat);

       /* mEdtLat.setText(tinyDB.getDouble("LAT", 0.0) + "");
        mEdtLang.setText(tinyDB.getDouble("LANG", 0.0) + "")*/

        mUnitsSpinner = findViewById(R.id.spn_itemunits);
        adapter_spinner_units = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spinner_array_units);
        adapter_spinner_units.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUnitsSpinner.setAdapter(adapter_spinner_units);

        if (getIntent().getStringExtra("edititem") != null) {
            mEditItem = getIntent().getStringExtra("edititem");
            mVendorId = getIntent().getStringExtra("vendorid");
            mVendorName = getIntent().getStringExtra("vendorname");
            mVendorAddress = getIntent().getStringExtra("vendoraddress");
            mEditPojo = new Gson().fromJson(mEditItem, ItemPojo.Item.class);
            mItemName.setText(mEditPojo.getItemName());
            mItemPrice.setText(mEditPojo.getItemPrice());
            mItemUnits.setText(mEditPojo.getItemSize());
            mEdtLat.setText(mEditPojo.getItemLat() + "");
            mEdtLang.setText(mEditPojo.getItemLong() + "");
            int mKms = (int) mEditPojo.getRangeKms();
            mEdtRange.setText(mKms + "");
            for (int i = 0; i < spinner_array_units.size(); i++) {
                if (spinner_array_units.get(i).getName().equalsIgnoreCase(mEditPojo.getItemSize())) {
                    mUnitsSpinner.setSelection(i);
                }
            }
            mMinQty.setText(mEditPojo.getMinqty());
            mItemDesc.setText(mEditPojo.getItemDescription());
            mItemCatId = mEditPojo.getCategoryId();
            mActive = mEditPojo.getItemActive();
            String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mEditPojo.getItemImage();
            String imgUrl2 = "http://delapi.shaikhomes.com/ImageStorage/" + mEditPojo.getImage1();
            String imgUrl3 = "http://delapi.shaikhomes.com/ImageStorage/" + mEditPojo.getImage2();
            String imgUrl4 = "http://delapi.shaikhomes.com/ImageStorage/" + mEditPojo.getImage3();

            Picasso.get().load(imgUrl).resize(500, 500)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .centerCrop().transform(new RoundedTransformation()).into(mUploadImage);
            Picasso.get().load(imgUrl2).resize(500, 500)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .centerCrop().transform(new RoundedTransformation()).into(mUploadImage2);
            Picasso.get().load(imgUrl3).resize(500, 500)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .centerCrop().transform(new RoundedTransformation()).into(mUploadImage3);
            Picasso.get().load(imgUrl4).resize(500, 500)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .centerCrop().transform(new RoundedTransformation()).into(mUploadImage4);
            if (mActive.toLowerCase().equalsIgnoreCase("true")) {
                mBtnActive.setBackgroundResource(R.drawable.accept_border);
                mBtnDeactive.setBackgroundResource(R.drawable.button_border);
                mBtnActive.setTextColor(this.getColor(R.color.white));
                mBtnDeactive.setTextColor(this.getColor(R.color.red));
            } else if (mActive.toLowerCase().equalsIgnoreCase("false")) {
                mBtnDeactive.setBackgroundResource(R.drawable.decline_button);
                mBtnActive.setBackgroundResource(R.drawable.button_border);
                mBtnActive.setTextColor(this.getColor(R.color.green));
                mBtnDeactive.setTextColor(this.getColor(R.color.white));
            }

        }
        getCategoryDetails("");
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void grant_permission() {
        if (ContextCompat.checkSelfPermission(AddItemActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddItemActivity.this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(AddItemActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddItemActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                // mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            // mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(AddItemActivity.this)
                .addConnectionCallbacks(AddItemActivity.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddItemActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(AddItemActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AddItemActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(AddItemActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(AddItemActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        // mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(AddItemActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private File createImageFile_1() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath_1 = image.getAbsolutePath();
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_image:
                dialogforChoice();

                break;
            case R.id.upload_image2:
                new AlertDialog.Builder(this).setTitle("Upload Image").setMessage("Select uploading options").setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE2);
                    }
                }).create().show();

                break;
            case R.id.upload_image3:
                new AlertDialog.Builder(this).setTitle("Upload Image").setMessage("Select uploading options").setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE3);
                    }
                }).create().show();

                break;
            case R.id.upload_image4:
                new AlertDialog.Builder(this).setTitle("Upload Image").setMessage("Select uploading options").setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE4);
                    }
                }).create().show();

                break;
            case R.id.btn_active:
                mActive = "True";
                mBtnActive.setBackgroundResource(R.drawable.accept_border);
                mBtnDeactive.setBackgroundResource(R.drawable.button_border);
                mBtnActive.setTextColor(this.getColor(R.color.white));
                mBtnDeactive.setTextColor(this.getColor(R.color.red));
                break;
            case R.id.btn_deactive:
                mActive = "False";
                mBtnDeactive.setBackgroundResource(R.drawable.decline_button);
                mBtnActive.setBackgroundResource(R.drawable.button_border);
                mBtnActive.setTextColor(this.getColor(R.color.green));
                mBtnDeactive.setTextColor(this.getColor(R.color.white));
                break;
            case R.id.btn_submit:
                int range = 0;
                if (!TextUtils.isEmpty(mEdtRange.getText().toString().trim())) {
                    range = Integer.parseInt(mEdtRange.getText().toString().trim());
                }
                if (TextUtils.isEmpty(mEncodedImage) && TextUtils.isEmpty(mEditItem)) {
                    Toasty.error(AddItemActivity.this, "Please click Image", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEncodedImage2) && TextUtils.isEmpty(mEditItem)) {
                    Toasty.error(AddItemActivity.this, "Please click Image", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEncodedImage3) && TextUtils.isEmpty(mEditItem)) {
                    Toasty.error(AddItemActivity.this, "Please click Image", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEncodedImage4) && TextUtils.isEmpty(mEditItem)) {
                    Toasty.error(AddItemActivity.this, "Please click Image", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mItemName.getText().toString())) {
                    Toasty.error(AddItemActivity.this, "Please enter item name", Toasty.LENGTH_SHORT).show();
                } else if (range == 0) {
                    Toasty.error(AddItemActivity.this, "Please enter range", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mItemPrice.getText().toString())) {
                    Toasty.error(AddItemActivity.this, "Please enter item price", Toasty.LENGTH_SHORT).show();
                } else if (spinner_array_units.get(mUnitsSpinner.getSelectedItemPosition()).getId().equalsIgnoreCase("-1")) {
                    Toasty.error(AddItemActivity.this, "Please enter item units", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mMinQty.getText().toString())) {
                    Toasty.error(AddItemActivity.this, "Please enter item min quantity", Toasty.LENGTH_SHORT).show();
                } else if (spinner_array_category.get(mCatSpinner.getSelectedItemPosition()).getId().equalsIgnoreCase("-1")) {
                    Toasty.error(AddItemActivity.this, "Please select category", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mActive)) {
                    Toasty.error(AddItemActivity.this, "Please select item active/deactive", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mItemDesc.getText().toString())) {
                    Toasty.error(AddItemActivity.this, "Please select item description", Toasty.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(mEditItem)) {

                        ItemPojo.Item mPostItem = new ItemPojo.Item();
                        mPostItem.setItemActive(mActive);
                        mPostItem.setItemCompany("");
                        mPostItem.setItemId("");
                        mPostItem.setItemImage(mEncodedImage);
                        mPostItem.setImage1(mEncodedImage2);
                        mPostItem.setImage2(mEncodedImage3);
                        mPostItem.setImage3(mEncodedImage4);
                        mPostItem.setItemName(mItemName.getText().toString().trim());
                        mPostItem.setItemPrice(mItemPrice.getText().toString().trim());
                        mPostItem.setItemQuantity("1");
                        mPostItem.setItemSize(spinner_array_units.get(mUnitsSpinner.getSelectedItemPosition()).getName());
                        mPostItem.setMinqty(mMinQty.getText().toString().trim());
                        mPostItem.setVendorAddress("");
                        mPostItem.setItemDescription(mItemDesc.getText().toString().trim());
                        mPostItem.setVendorId(tinyDB.getString(USER_ID));
                        mPostItem.setVendorName(tinyDB.getString(USER_NAME));
                        mPostItem.setCategoryId(spinner_array_category.get(mCatSpinner.getSelectedItemPosition()).getId());

                        mPostItem.setItemLat(Double.parseDouble(mEdtLat.getText().toString().trim()));
                        mPostItem.setItemLong(Double.parseDouble(mEdtLang.getText().toString().trim()));
                        mPostItem.setRangeKms(Double.parseDouble(mEdtRange.getText().toString().trim()));
                        hud = KProgressHUD.create(AddItemActivity.this)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("Please wait")
                                .setBackgroundColor(ContextCompat.getColor(AddItemActivity.this, R.color.opaque_black))
                                .setCancellable(true)
                                .setAnimationSpeed(2);
                        if (hud != null)
                            if (!hud.isShowing()) {
                                hud.show();
                            }
                        Call<PostResponsePojo> call = apiService.PostItem(mPostItem);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                                if (hud != null)
                                    if (hud.isShowing()) {
                                        hud.dismiss();
                                    }
                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        clearData();
                                        Toasty.success(AddItemActivity.this, "Item Added Successfully", Toast.LENGTH_SHORT, true).show();
                                    }else{
                                        Toasty.error(AddItemActivity.this, pojo.getMessage(), Toast.LENGTH_SHORT, true).show();
                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                                if (hud != null)
                                    if (hud.isShowing()) {
                                        hud.dismiss();
                                    }
                                Toasty.error(AddItemActivity.this, t.getMessage(), Toast.LENGTH_SHORT, true).show();

                            }
                        });
                    } else {

                        ItemPojo.Item mPostItem = new ItemPojo.Item();
                        mPostItem.setItemActive("True");
                        mPostItem.setItemCompany("");
                        mPostItem.setItemId(mEditPojo.getItemId());
                        if (TextUtils.isEmpty(mEncodedImage)) {
                            mPostItem.setItemImage(mEditPojo.getItemImage());
                            mPostItem.setImage1(mEditPojo.getImage1());
                            mPostItem.setImage2(mEditPojo.getImage2());
                            mPostItem.setImage3(mEditPojo.getImage3());
                            mPostItem.setUpdate("update");
                        } else {
                            mPostItem.setItemImage(mEncodedImage);
                            mPostItem.setImage1(mEncodedImage2);
                            mPostItem.setImage2(mEncodedImage3);
                            mPostItem.setImage3(mEncodedImage4);
                            mPostItem.setUpdate("updateimage");
                        }
                        mPostItem.setItemName(mItemName.getText().toString().trim());
                        mPostItem.setItemPrice(mItemPrice.getText().toString().trim());
                        mPostItem.setItemQuantity("1");
                        mPostItem.setItemSize(spinner_array_units.get(mUnitsSpinner.getSelectedItemPosition()).getName());
                        mPostItem.setMinqty(mMinQty.getText().toString().trim());
                        if (TextUtils.isEmpty(mVendorId)) {
                            mPostItem.setVendorAddress("");
                            mPostItem.setVendorId(tinyDB.getString(USER_ID));
                            mPostItem.setVendorName(tinyDB.getString(USER_NAME));
                        } else {
                            mPostItem.setVendorAddress(mVendorAddress);
                            mPostItem.setVendorId(mVendorId);
                            mPostItem.setVendorName(mVendorName);
                        }
                        mPostItem.setItemDescription(mItemDesc.getText().toString().trim());
                        mPostItem.setItemLat(Double.parseDouble(mEdtLat.getText().toString().trim()));
                        mPostItem.setItemLong(Double.parseDouble(mEdtLang.getText().toString().trim()));
                        mPostItem.setRangeKms(Double.parseDouble(mEdtRange.getText().toString().trim()));
                        mPostItem.setCategoryId(spinner_array_category.get(mCatSpinner.getSelectedItemPosition()).getId());
                        hud = KProgressHUD.create(AddItemActivity.this)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("Please wait")
                                .setBackgroundColor(ContextCompat.getColor(AddItemActivity.this, R.color.opaque_black))
                                .setCancellable(true)
                                .setAnimationSpeed(2);
                        if (hud != null)
                            if (!hud.isShowing()) {
                                hud.show();
                            }
                        Call<PostResponsePojo> call = apiService.UpdateItemDetails(mPostItem);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                                if (hud != null)
                                    if (hud.isShowing()) {
                                        hud.dismiss();
                                    }
                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        clearData();
                                        Toasty.success(AddItemActivity.this, "Item Updated Successfully", Toast.LENGTH_SHORT, true).show();
                                        onBackPressed();
                                        finish();
                                    }else{
                                        Toasty.error(AddItemActivity.this, pojo.getMessage(), Toast.LENGTH_SHORT, true).show();
                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {

                                if (hud != null)
                                    if (hud.isShowing()) {
                                        hud.dismiss();
                                    }
                                Toasty.error(AddItemActivity.this, t.getMessage(), Toast.LENGTH_SHORT, true).show();

                            }
                        });
                    }
                }

                break;
            case R.id.pickup_image:
                final Dialog dialog = new Dialog(AddItemActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setCancelable(false);
                ZoomableImageView imageView = dialog.findViewById(R.id.image_display);
                //imageView.setImageBitmap(bitmap);

                Picasso.get().load(contentUri)
                        .placeholder(R.drawable.ic_loading_img).error(R.drawable.warning)
                        //.resize(500, 500)
                        //.centerCrop()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        //.transform(new RoundedTransformation())
                        .into(imageView);

                Button button = dialog.findViewById(R.id.button_back);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation_2;

                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            try {
                                dialog.dismiss();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    }
                });

                dialog.show();
                break;
        }
    }

    private void clearData() {
        mEncodedImage = "";
        mItemPrice.setText("");
        mItemName.setText("");
        mItemUnits.setText("");
        mMinQty.setText("");
        mUploadImage.setImageResource(R.drawable.ic_photo_camera);
        mUploadImage2.setImageResource(R.drawable.ic_photo_camera);
        mUploadImage3.setImageResource(R.drawable.ic_photo_camera);
        mUploadImage4.setImageResource(R.drawable.ic_photo_camera);
    }

    private void dialogforChoice() {
      /*  new AlertDialog.Builder(this).setTitle("Upload Image").setMessage("Select uploading options").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (ContextCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    grant_permission();

                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile_1();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(AddItemActivity.this,
                                    "com.shaikhomes.watercan.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_1);
                        }
                    }
                }
            }
        }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
            }
        }).create().show();*/

        new AlertDialog.Builder(this).setTitle("Upload Image").setMessage("Select uploading options").setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
            }
        }).create().show();
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == REQUEST_IMAGE_CAPTURE_1 && resultCode == RESULT_OK) {
            //  if (data != null) {
            File f = new File(mCurrentPhotoPath_1);
            contentUri = Uri.fromFile(f);
            Bitmap encoded_new = null;
            try {
                encoded_new = Compressor.getDefault(this).compressToBitmap(f);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Picasso.get().load(contentUri).resize(500, 500)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .centerCrop().transform(new RoundedTransformation()).into(mUploadImage);

            String encoded = null;
            try {
                mEncodedImage = "";
                encoded = encodeImage(Objects.requireNonNull(encoded_new));
                mEncodedImage = encoded;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (encoded != null && !encoded.isEmpty()) {
                if (f.exists()) {
                    if (f.exists()) {
                        getApplicationContext().deleteFile(f.getName());
                    }
                }
            }
            // }
        } else */
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                contentUri = null;
                Uri selectedImageUri = data.getData();
                File f = new File(getImagePath(selectedImageUri));
                contentUri = Uri.fromFile(f);
                Bitmap encoded_new = null;
                try {
                    encoded_new = Compressor.getDefault(this).compressToBitmap(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Picasso.get().load(contentUri).resize(500, 500)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .centerCrop().transform(new RoundedTransformation()).into(mUploadImage);

                String encoded = null;
                try {
                    mEncodedImage = "";
                    encoded = encodeImage(Objects.requireNonNull(encoded_new));
                    mEncodedImage = encoded;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == SELECT_FILE2 && resultCode == RESULT_OK) {
            if (data != null) {
                contentUri = null;
                File f = new File(getImagePath(data.getData()));
                contentUri = Uri.fromFile(f);
                Bitmap encoded_new = null;
                try {
                    encoded_new = Compressor.getDefault(this).compressToBitmap(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Picasso.get().load(contentUri).resize(500, 500)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .centerCrop().transform(new RoundedTransformation()).into(mUploadImage2);

                String encoded = null;
                try {
                    mEncodedImage2 = "";
                    encoded = encodeImage(Objects.requireNonNull(encoded_new));
                    mEncodedImage2 = encoded;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == SELECT_FILE3 && resultCode == RESULT_OK) {
            if (data != null) {
                contentUri = null;
                File f = new File(getImagePath(data.getData()));
                contentUri = Uri.fromFile(f);
                Bitmap encoded_new = null;
                try {
                    encoded_new = Compressor.getDefault(this).compressToBitmap(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Picasso.get().load(contentUri).resize(500, 500)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .centerCrop().transform(new RoundedTransformation()).into(mUploadImage3);

                String encoded = null;
                try {
                    mEncodedImage3 = "";
                    encoded = encodeImage(Objects.requireNonNull(encoded_new));
                    mEncodedImage3 = encoded;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == SELECT_FILE4 && resultCode == RESULT_OK) {
            if (data != null) {
                contentUri = null;
                File f = new File(getImagePath(data.getData()));
                contentUri = Uri.fromFile(f);
                Bitmap encoded_new = null;
                try {
                    encoded_new = Compressor.getDefault(this).compressToBitmap(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Picasso.get().load(contentUri).resize(500, 500)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .centerCrop().transform(new RoundedTransformation()).into(mUploadImage4);

                String encoded = null;
                try {
                    mEncodedImage4 = "";
                    encoded = encodeImage(Objects.requireNonNull(encoded_new));
                    mEncodedImage4 = encoded;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public String getImagePath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }


    private void getCategoryDetails(String number) {
        try {
            Call<CategoryPojo> call = apiService.GetCategoryDetails("");
            call.enqueue(new Callback<CategoryPojo>() {
                @Override
                public void onResponse(Call<CategoryPojo> call, Response<CategoryPojo> response) {
                    CategoryPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getCategoryDetails() != null) {
                            if (mItemData.getCategoryDetails().size() > 0) {
                                if (mList.size() > 0) {
                                    mList.clear();
                                }
                                mList = mItemData.getCategoryDetails();
                                int mSpinpos = 0;
                                if (mList.size() > 0) {
                                    for (int i = 0; i < mList.size(); i++) {
                                        if (mItemCatId.equalsIgnoreCase(mList.get(i).getId())) {
                                            mSpinpos = i;
                                        }
                                        spinner_array_category.add(new Spinner_global_model(mList.get(i).getId(), mList.get(i).getCategoryName()));
                                    }
                                    adapter_spinner_category.notifyDataSetChanged();
                                    if (!TextUtils.isEmpty(mItemCatId)) {
                                        mCatSpinner.setSelection(mSpinpos + 1);
                                    }
                                }
                                // mAdapter.updateAdapter(mItemData.getOrderList());
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<CategoryPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(AddItemActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        mEdtLat.setText(latitude + "");
        mEdtLang.setText(longitude + "");
        //Toast.makeText(AddItemActivity.this,latitude+"",Toast.LENGTH_SHORT).show();
    }
}
