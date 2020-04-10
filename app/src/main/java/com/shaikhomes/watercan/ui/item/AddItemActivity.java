package com.shaikhomes.watercan.ui.item;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.google.gson.Gson;
import com.shaikhomes.watercan.BaseActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.SignUpActivity;
import com.shaikhomes.watercan.pojo.CategoryPojo;
import com.shaikhomes.watercan.pojo.ItemPojo;
import com.shaikhomes.watercan.pojo.OrderDelivery;
import com.shaikhomes.watercan.pojo.PostResponsePojo;
import com.shaikhomes.watercan.pojo.Spinner_global_model;
import com.shaikhomes.watercan.utility.RoundedTransformation;
import com.shaikhomes.watercan.utility.ZoomableImageView;
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

import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;


public class AddItemActivity extends BaseActivity implements View.OnClickListener {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 10;
    public static final int REQUEST_IMAGE_CAPTURE_1 = 100;
    private String mCurrentPhotoPath_1;
    private Uri contentUri;
    private String mEncodedImage;
    private ImageView mUploadImage, mViewImage;
    private EditText mItemName, mItemPrice, mItemUnits, mMinQty;
    private AppCompatButton mRegisterItem;
    private String mEditItem = "";
    private ItemPojo.Item mEditPojo;
    private List<CategoryPojo.CategoryDetail> mList;
    private Spinner mCatSpinner;
    private ArrayList<Spinner_global_model> spinner_array_category;
    private ArrayAdapter<Spinner_global_model> adapter_spinner_category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mList = new ArrayList<>();
        spinner_array_category = new ArrayList<>();
        spinner_array_category.add(new Spinner_global_model("-1", "Select Category"));
        mUploadImage = findViewById(R.id.upload_image);
        mUploadImage.setOnClickListener(this);
        mViewImage = findViewById(R.id.pickup_image);
        mViewImage.setOnClickListener(this);
        mItemName = findViewById(R.id.edt_itemname);
        mItemPrice = findViewById(R.id.edt_itemprice);
        mItemUnits = findViewById(R.id.edt_itemunits);
        mMinQty = findViewById(R.id.edt_minqty);
        mRegisterItem = findViewById(R.id.btn_submit);
        mRegisterItem.setOnClickListener(this);
        mCatSpinner = findViewById(R.id.spn_category);
        adapter_spinner_category = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spinner_array_category);
        adapter_spinner_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCatSpinner.setAdapter(adapter_spinner_category);
        getCategoryDetails("");
        if (getIntent().getStringExtra("edititem") != null) {
            mEditItem = getIntent().getStringExtra("edititem");
            mEditPojo = new Gson().fromJson(mEditItem, ItemPojo.Item.class);
            mItemName.setText(mEditPojo.getItemName());
            mItemPrice.setText(mEditPojo.getItemPrice());
            mItemUnits.setText(mEditPojo.getItemSize());
            mMinQty.setText(mEditPojo.getMinqty());
            String imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mEditPojo.getItemImage();

            Picasso.get().load(imgUrl).resize(500, 500)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .centerCrop().transform(new RoundedTransformation()).into(mUploadImage);


        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_image:
                dialogforChoice();

                break;
            case R.id.btn_submit:

                if (TextUtils.isEmpty(mEncodedImage) && TextUtils.isEmpty(mEditItem)) {
                    Toasty.error(AddItemActivity.this, "Please click Image", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mItemName.getText().toString())) {
                    Toasty.error(AddItemActivity.this, "Please enter item name", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mItemPrice.getText().toString())) {
                    Toasty.error(AddItemActivity.this, "Please enter item price", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mItemUnits.getText().toString())) {
                    Toasty.error(AddItemActivity.this, "Please enter item units", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mMinQty.getText().toString())) {
                    Toasty.error(AddItemActivity.this, "Please enter item min quantity", Toasty.LENGTH_SHORT).show();
                } else if (spinner_array_category.get(mCatSpinner.getSelectedItemPosition()).getId().equalsIgnoreCase("-1")) {
                    Toasty.error(AddItemActivity.this, "Please select category", Toasty.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(mEditItem)) {
                        ItemPojo.Item mPostItem = new ItemPojo.Item();
                        mPostItem.setItemActive("True");
                        mPostItem.setItemCompany("");
                        mPostItem.setItemId("");
                        mPostItem.setItemImage(mEncodedImage);
                        mPostItem.setItemName(mItemName.getText().toString());
                        mPostItem.setItemPrice(mItemPrice.getText().toString());
                        mPostItem.setItemQuantity("1");
                        mPostItem.setItemSize(mItemUnits.getText().toString());
                        mPostItem.setMinqty(mMinQty.getText().toString());
                        mPostItem.setVendorAddress("");
                        mPostItem.setVendorId(tinyDB.getString(USER_ID));
                        mPostItem.setVendorName(tinyDB.getString(USER_NAME));
                        mPostItem.setCategoryId(spinner_array_category.get(mCatSpinner.getSelectedItemPosition()).getId());


                        Call<PostResponsePojo> call = apiService.PostItem(mPostItem);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        clearData();
                                        Toasty.success(AddItemActivity.this, "Item Added Successfully", Toast.LENGTH_SHORT, true).show();
                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                            }
                        });
                    } else {

                        ItemPojo.Item mPostItem = new ItemPojo.Item();
                        mPostItem.setItemActive("True");
                        mPostItem.setItemCompany("");
                        mPostItem.setItemId(mEditPojo.getItemId());
                        if (TextUtils.isEmpty(mEncodedImage)) {
                            mPostItem.setItemImage(mEditPojo.getItemImage());
                            mPostItem.setUpdate("update");
                        } else {
                            mPostItem.setItemImage(mEncodedImage);
                            mPostItem.setUpdate("updateimage");
                        }
                        mPostItem.setItemName(mItemName.getText().toString());
                        mPostItem.setItemPrice(mItemPrice.getText().toString());
                        mPostItem.setItemQuantity("1");
                        mPostItem.setItemSize(mItemUnits.getText().toString());
                        mPostItem.setMinqty(mMinQty.getText().toString());
                        mPostItem.setVendorAddress("");
                        mPostItem.setVendorId(tinyDB.getString(USER_ID));
                        mPostItem.setVendorName(tinyDB.getString(USER_NAME));
                        mPostItem.setCategoryId(spinner_array_category.get(mCatSpinner.getSelectedItemPosition()).getId());
                        Call<PostResponsePojo> call = apiService.UpdateItemDetails(mPostItem);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        clearData();
                                        Toasty.success(AddItemActivity.this, "Item Updated Successfully", Toast.LENGTH_SHORT, true).show();
                                        onBackPressed();
                                        finish();
                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
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
                        .placeholder(R.drawable.sand_clock).error(R.drawable.warning)
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
    }

    private void dialogforChoice() {
        new AlertDialog.Builder(this).setTitle("Upload Image").setMessage("Select uploading options").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE_1 && resultCode == RESULT_OK) {
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
        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                contentUri = null;
                File f = new File(getPath(this, data.getData()));
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
        }
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
                                if (mList.size() > 0) {
                                    for (int i = 0; i < mList.size(); i++) {
                                        spinner_array_category.add(new Spinner_global_model(mList.get(i).getId(), mList.get(i).getCategoryName()));
                                    }
                                    adapter_spinner_category.notifyDataSetChanged();
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
}
