package com.shaikhomes.deliveryhub.ui.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shaikhomes.deliveryhub.BaseActivity;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.pojo.DeliveryhubOffersPojo;
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo;
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

public class ModifyOffers extends BaseActivity implements View.OnClickListener {
    ImageView mAdvImg1, mAdvImg2, mAdvImg3, mAdvImg4, mAdvImg5, mAdvImg6, mOfrImg1, mOfrImg2, mOfrImg3, mOfrImg4, mImage;
    List<DeliveryhubOffersPojo.OffersList> mOfferList;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 10;

    public static final int REQUEST_IMAGE_CAPTURE_1 = 100;
    private String mCurrentPhotoPath_1;
    private Uri contentUri;
    private String mEncodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_offers);
        mOfferList = new ArrayList<>();
        mAdvImg1 = findViewById(R.id.adv_img1);
        mAdvImg2 = findViewById(R.id.adv_img2);
        mAdvImg3 = findViewById(R.id.adv_img3);
        mAdvImg4 = findViewById(R.id.adv_img4);
        mAdvImg5 = findViewById(R.id.adv_img5);
        mAdvImg6 = findViewById(R.id.adv_img6);
        mOfrImg1 = findViewById(R.id.offer_img1);
        mOfrImg2 = findViewById(R.id.offer_img2);
        mOfrImg3 = findViewById(R.id.offer_img3);
        mOfrImg4 = findViewById(R.id.offer_img4);
        mAdvImg1.setOnClickListener(this);
        mAdvImg2.setOnClickListener(this);
        mAdvImg3.setOnClickListener(this);
        mAdvImg4.setOnClickListener(this);
        mAdvImg5.setOnClickListener(this);
        mAdvImg6.setOnClickListener(this);

        mOfrImg1.setOnClickListener(this);
        mOfrImg2.setOnClickListener(this);
        mOfrImg3.setOnClickListener(this);
        mOfrImg4.setOnClickListener(this);

        getAdvList();
    }

    private void getAdvList() {
        try {
            Call<DeliveryhubOffersPojo> call = apiService.GetAppOffers("adv");
            call.enqueue(new Callback<DeliveryhubOffersPojo>() {
                @Override
                public void onResponse(Call<DeliveryhubOffersPojo> call, Response<DeliveryhubOffersPojo> response) {
                    DeliveryhubOffersPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        for (int i = 0; i < mPojo.getOffersList().size(); i++) {
                            mOfferList.add(mPojo.getOffersList().get(i));
                        }
                        getOffersData();
                    } else {
                        Toasty.error(ModifyOffers.this, mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeliveryhubOffersPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void getOffersData() {
        try {
            Call<DeliveryhubOffersPojo> call = apiService.GetAppOffers("offer");
            call.enqueue(new Callback<DeliveryhubOffersPojo>() {
                @Override
                public void onResponse(Call<DeliveryhubOffersPojo> call, Response<DeliveryhubOffersPojo> response) {
                    DeliveryhubOffersPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        for (int i = 0; i < mPojo.getOffersList().size(); i++) {
                            mOfferList.add(mPojo.getOffersList().get(i));
                        }
                        if (mOfferList.size() == 10) {
                            fillImageData();
                        }
                    } else {
                        Toasty.error(ModifyOffers.this, mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeliveryhubOffersPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void fillImageData() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(ModifyOffers.this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        int[] COLORS = new int[]{
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.red)
        };
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(0).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdvImg1);
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(1).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdvImg2);
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(2).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdvImg3);
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(3).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdvImg4);
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(4).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdvImg5);
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(5).getImage())
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mAdvImg6);

        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(6).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mOfrImg1);
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(7).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mOfrImg2);
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(8).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mOfrImg3);
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mOfferList.get(9).getImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mOfrImg4);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.adv_img1) {
            updateImage(mOfferList.get(0), "adv");
        } else if (v.getId() == R.id.adv_img2) {
            updateImage(mOfferList.get(1), "adv");
        } else if (v.getId() == R.id.adv_img3) {
            updateImage(mOfferList.get(2), "adv");
        } else if (v.getId() == R.id.adv_img4) {
            updateImage(mOfferList.get(3), "adv");
        } else if (v.getId() == R.id.adv_img5) {
            updateImage(mOfferList.get(4), "adv");
        } else if (v.getId() == R.id.adv_img6) {
            updateImage(mOfferList.get(5), "adv");
        } else if (v.getId() == R.id.offer_img1) {
            updateImage(mOfferList.get(6), "offer");
        } else if (v.getId() == R.id.offer_img2) {
            updateImage(mOfferList.get(7), "offer");
        } else if (v.getId() == R.id.offer_img3) {
            updateImage(mOfferList.get(8), "offer");
        } else if (v.getId() == R.id.offer_img4) {
            updateImage(mOfferList.get(9), "offer");
        }
    }

    private void updateImage(DeliveryhubOffersPojo.OffersList mData, String type) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(ModifyOffers.this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        int[] COLORS = new int[]{
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.red)
        };
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();
        final Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_offers_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        mImage = dialog.findViewById(R.id.upload_image);
        AppCompatButton mSubmit = dialog.findViewById(R.id.btn_submit);
        Glide.with(ModifyOffers.this).load("http://delapi.shaikhomes.com/Offers/" + mData.getImage())
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_no_image)
                .fitCenter().into(mImage);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ModifyOffers.this).setTitle("Upload Image").setMessage("Select uploading options").setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
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
        });
        mSubmit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mEncodedImage)) {
                Toasty.error(ModifyOffers.this, "Please click image", Toasty.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                mData.setType(type);
                mData.setImage(mEncodedImage);
                UpdateOfferStatus(mData);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                        .centerCrop().transform(new RoundedTransformation()).into(mImage);

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

    private void UpdateOfferStatus(DeliveryhubOffersPojo.OffersList mPojo) {
        Call<PostResponsePojo> call = apiService.UpdateOffers(mPojo);
        call.enqueue(new Callback<PostResponsePojo>() {
            @Override
            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                PostResponsePojo pojo = response.body();
                if (pojo != null)
                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                        getAdvList();
                        Toasty.success(ModifyOffers.this, "Image Updated Successfully", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(ModifyOffers.this, pojo.getMessage(), Toast.LENGTH_SHORT, true).show();

                    }
            }

            @Override
            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
            }
        });
    }

}
