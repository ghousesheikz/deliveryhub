package com.shaikhomes.deliveryhub.ui.addcategories;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.shaikhomes.deliveryhub.BaseActivity;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.CategoryPojo;
import com.shaikhomes.deliveryhub.pojo.ItemPojo;
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo;
import com.shaikhomes.deliveryhub.pojo.Spinner_global_model;
import com.shaikhomes.deliveryhub.utility.RoundedTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategories extends BaseActivity implements View.OnClickListener {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 10;
    public static final int REQUEST_IMAGE_CAPTURE_1 = 100;
    private String mCurrentPhotoPath_1;
    private Uri contentUri;
    private String mEncodedImage, mItemCatId = "", mActive = "";
    private ImageView mUploadImage, mViewImage;
    private EditText mItemName;
    private AppCompatButton mRegisterItem;

    private ItemPojo.Item mEditPojo;
    private List<CategoryPojo.CategoryDetail> mList;
    private Spinner mCatSpinner;
    private ArrayList<Spinner_global_model> spinner_array_category;
    private ArrayAdapter<Spinner_global_model> adapter_spinner_category;
    private AppCompatButton mBtnActive, mBtnDeactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mList = new ArrayList<>();

        mUploadImage = findViewById(R.id.upload_image);
        mUploadImage.setOnClickListener(this);
        mViewImage = findViewById(R.id.pickup_image);
        mViewImage.setOnClickListener(this);
        mItemName = findViewById(R.id.edt_itemname);
        mRegisterItem = findViewById(R.id.btn_submit);
        mRegisterItem.setOnClickListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_image:
                new AlertDialog.Builder(this).setTitle("Upload Image").setMessage("Select uploading options").setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }
                }).create().show();

                break;
            case R.id.btn_submit:

                if (TextUtils.isEmpty(mEncodedImage)) {
                    Toasty.error(AddCategories.this, "Please click Image", Toasty.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mItemName.getText().toString())) {
                    Toasty.error(AddCategories.this, "Please enter category name", Toasty.LENGTH_SHORT).show();
                } else {

                    CategoryPojo.CategoryDetail mPostItem = new CategoryPojo.CategoryDetail();
                    mPostItem.setCategoryImage(mEncodedImage);
                    mPostItem.setCategoryName(mItemName.getText().toString());

                    Call<PostResponsePojo> call = apiService.PostCategories(mPostItem);
                    call.enqueue(new Callback<PostResponsePojo>() {
                        @Override
                        public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {

                            PostResponsePojo pojo = response.body();
                            if (pojo != null)
                                if (pojo.getStatus().equalsIgnoreCase("200")) {

                                    Toasty.success(AddCategories.this, "Item Added Successfully", Toast.LENGTH_SHORT, true).show();
                                }
                        }

                        @Override
                        public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                        }
                    });
                }


                break;

        }
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

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
