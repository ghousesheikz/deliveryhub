package com.shaikhomes.deliveryhub.ui.stores;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shaikhomes.deliveryhub.MainActivity;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.pojo.AddressPojo;
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo;
import com.shaikhomes.deliveryhub.ui.address.AddressAdapter;
import com.shaikhomes.deliveryhub.utility.MyScrollController;
import com.shaikhomes.deliveryhub.utility.TinyDB;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.deliveryhub.utility.AppConstants.ADDRESS_LIST;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_ADDRESS;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreItemsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreItemsList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreItemsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreItemsList.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreItemsList newInstance(String param1, String param2) {
        StoreItemsList fragment = new StoreItemsList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mCatList = new ArrayList<>();
        mItemList = new ArrayList<>();
        mTotalItemList = new ArrayList<>();
    }

    View view;
    private ApiInterface apiService;
    private TinyDB tinyDB;
    List<Address> addresses = null;
    RecyclerView mCategoryRecyclerView, mItemsRecyclerview;
    StoreCategoryAdapter mCatAdapter;
    StoreItemsAdapter mItemAdapter;
    List<StoreCategoryPojo.StoreCategoryDetail> mCatList;
    List<StoreItemsPojo.StoreItemsList> mItemList;
    List<StoreItemsPojo.StoreItemsList> mTotalItemList;
    private JSONArray jsonArray = null;
    private JSONObject jsonObject = null;
    private List<AddressPojo> mAddressList = null;
    private String mAddType = "";
    private EditText mEdtFlatNo, mEdtApartmentName, mEdtLandmark, mEdtAreaName, mEdtCityName;
    private TextView mTypeHome, mTypeWork, mTypeOthers;

    private AppCompatButton mBtnSubmit, mBtnAddAddress, mBtnSelectAddress;

    private LinearLayout mDeliveryLL, mAddressLL, mSelectAddressLL, mAddAddressLL;
    private RecyclerView mRecyclerview;
    private AddressAdapter adapter;

    TextView txt_totalprice;
    int minamt = 0;
    double totalAmt = 0, mrpTotalAmt = 0.0;
    boolean mProceedflag = false;
    DecimalFormat df;
    EditText mSearchEdt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store_items_list, container, false);
        tinyDB = new TinyDB(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);


        df = new DecimalFormat("0.00");
        minamt = tinyDB.getInt("MINAMT");
        txt_totalprice = view.findViewById(R.id.txt_totalprice);
        mCategoryRecyclerView = view.findViewById(R.id.store_category_list);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mItemsRecyclerview = view.findViewById(R.id.store_items_list);
        mSearchEdt = view.findViewById(R.id.edt_itemsearch);
        mItemsRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mItemsRecyclerview.addOnScrollListener(new MyScrollController() {
            @SuppressLint("RestrictedApi")
            @Override
            public void show() {
               /* txt_totalprice.setVisibility(View.VISIBLE);
                txt_totalprice.animate().translationY(0).setStartDelay(200).setInterpolator(new DecelerateInterpolator(2)).start();
          */
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void hide() {
               /* txt_totalprice.animate().translationY(txt_totalprice.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                txt_totalprice.setVisibility(View.GONE);
          */
            }
        });
        mCatAdapter = new StoreCategoryAdapter(getActivity(), mCatList, new StoreCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StoreCategoryPojo.StoreCategoryDetail response, int position) {
                getCatItemsDetails("", response.getCategoryId());
            }
        });
        mCategoryRecyclerView.setAdapter(mCatAdapter);
        mItemAdapter = new StoreItemsAdapter(getActivity(), mItemList, new StoreItemsAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(@Nullable List<? extends StoreItemsPojo.StoreItemsList> response, int position) {
                totalAmt = 0.0;
                mrpTotalAmt = 0.0;
                for (int i = 0; i < response.size(); i++) {

                    totalAmt = totalAmt + response.get(i).getTotalAmt();
                    mrpTotalAmt = mrpTotalAmt + response.get(i).getMRPtotalAmt();
                    if (totalAmt >= minamt) {
                        mProceedflag = true;
                        txt_totalprice.setBackgroundResource(R.drawable.button_background);
                        txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                    } else {
                        mProceedflag = false;
                        txt_totalprice.setBackgroundResource(R.drawable.button_background_grey);
                        txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                    }
                        /* for(int j=0;j<mTotalItemList.size();j++){
                            if(mTotalItemList.get(j).getItemId().equalsIgnoreCase(response.get(i).getItemId())){
                                mTotalItemList.remove(j);
                                mTotalItemList.add(j,response.get(i));
                            }
                        }*/

                }
                StoreItemsPojo mPojo = new StoreItemsPojo();
                mPojo.setStoreItemsList((List<StoreItemsPojo.StoreItemsList>) response);
                String mData = new Gson().toJson(mPojo);
                tinyDB.remove("OFFSTORES");
                tinyDB.putString("OFFSTORES", mData);
            }

            @Override
            public void onWeightClick(@Nullable List<? extends StoreItemsPojo.StoreItemsList> mItemChangeList, int position, @NotNull TextView weightText) {
                dialogSelectWeights(mItemChangeList, position,weightText);
            }
        });
        mItemsRecyclerview.setAdapter(mItemAdapter);
        getCategoryDetails(tinyDB.getString("SVENDOR"));
        getItemsDetails(tinyDB.getString("SVENDOR"), "");
        txt_totalprice.setOnClickListener(v -> {
            if (mProceedflag) {
                if (TextUtils.isEmpty(tinyDB.getString(USER_ADDRESS))) {
                    selectAddress();
                } else {
                    proceedOrder();
                }
            } else {
                Toasty.error(getActivity(), "Minimum order amount is : " + df.format(minamt), Toasty.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    if (!TextUtils.isEmpty(mSearchEdt.getText().toString().trim())) {
                        /* if (mAdapter.getList() != null) {
                            mNewOrders = mAdapter.getList();
                        }*/
                        String text = charSequence.toString();
                        List<StoreItemsPojo.StoreItemsList> temp = new ArrayList<>();
                        if (mTotalItemList.size() > 0) {
                            for (StoreItemsPojo.StoreItemsList d : mTotalItemList) {
                                if (d.getItemName().toLowerCase().contains(text.toLowerCase())) {
                                    temp.add(d);
                                }
                            }
                            //update recyclerview
                            mItemAdapter.updateAdapter(temp);
                        } else {
                            mItemAdapter.updateAdapter(mTotalItemList);
                        }
                    }
                } else {
                    mItemAdapter.updateAdapter(mTotalItemList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void dialogSelectWeights(List<? extends StoreItemsPojo.StoreItemsList> mItemChangeList, int position, TextView weightText) {
        try {
            List<WeightPojo> mWeightList = new ArrayList<>();
            WeightPojo mPoso = null;
            mPoso = new WeightPojo();
            mPoso.setmWeight("1Kg");
            mPoso.setmMrpPrice(150);
            mPoso.setmSellingPrice(120);
            mWeightList.add(mPoso);
            mPoso = new WeightPojo();
            mPoso.setmWeight("500gms");
            mPoso.setmMrpPrice(75);
            mPoso.setmSellingPrice(70);
            mWeightList.add(mPoso);
            mPoso = new WeightPojo();
            mPoso.setmWeight("250gms");
            mPoso.setmMrpPrice(40);
            mPoso.setmSellingPrice(38);
            mWeightList.add(mPoso);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int screenWidth = displaymetrics.widthPixels;
            final Dialog dialogcust = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
            dialogcust.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialogcust.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogcust.setContentView(R.layout.dialog_reasons);
            dialogcust.setCancelable(false);
            dialogcust.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogcust.getWindow().setLayout(screenWidth, WindowManager.LayoutParams.MATCH_PARENT);
            dialogcust.getWindow().setGravity(Gravity.CENTER);
            dialogcust.show();
            LinearLayout mReasonLayout = dialogcust.findViewById(R.id.reason_ll);
            TextView dialog_header = dialogcust.findViewById(R.id.item_desc);
            dialog_header.setText(mItemChangeList.get(position).getItemName()+"-"+mItemChangeList.get(position).getItemDescription());
            mReasonLayout.removeAllViews();
            for (int j = 0; j < mWeightList.size(); j++) {
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.reason_text, null);
                final TextView mReasonTxt = view.findViewById(R.id.reason_txt);
                final TextView mMrpPrice = view.findViewById(R.id.mrp_price);
                final TextView mSellingPrice = view.findViewById(R.id.selling_price);
                final LinearLayout mWeightLL = view.findViewById(R.id.weight_ll);
                mReasonTxt.setText(mWeightList.get(j).mWeight);
                mMrpPrice.setText("₹ " +mWeightList.get(j).mMrpPrice);
                mMrpPrice.setPaintFlags(mMrpPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mSellingPrice.setText("₹ " +mWeightList.get(j).mSellingPrice);
                mReasonLayout.addView(view);
                final int finalJ = j;
                mReasonTxt.setOnClickListener(v -> {
                    mWeightLL.setBackgroundColor(Color.parseColor("#35BF34"));
                    mReasonTxt.setTextColor(Color.parseColor("#FFFFFF"));
                    mMrpPrice.setTextColor(Color.parseColor("#FFFFFF"));
                    mSellingPrice.setTextColor(Color.parseColor("#FFFFFF"));
                    mItemChangeList.get(position).setItemPrice(mWeightList.get(finalJ).getmMrpPrice()+"");
                    mItemChangeList.get(position).setSellingPrice(mWeightList.get(finalJ).getmSellingPrice() + "");
                    mItemChangeList.get(position).setItemSize(mWeightList.get(finalJ).getmWeight());
                    mItemChangeList.get(position).setItemCount(0);
                    mItemChangeList.get(position).setMRPtotalAmt(0);
                    mItemChangeList.get(position).setTotalAmt(0);
                    weightText.setText(mWeightList.get(finalJ).getmWeight());
                    dialogcust.dismiss();
                    mTotalItemList = (List<StoreItemsPojo.StoreItemsList>) mItemChangeList;
                    mItemAdapter.updateAdapter(mTotalItemList);
                    totalAmt = 0.0;
                    mrpTotalAmt = 0.0;
                    for (int i = 0; i < mTotalItemList.size(); i++) {

                        totalAmt = totalAmt + mTotalItemList.get(i).getTotalAmt();
                        mrpTotalAmt = mrpTotalAmt + mTotalItemList.get(i).getMRPtotalAmt();
                        if (totalAmt >= minamt) {
                            mProceedflag = true;
                            txt_totalprice.setBackgroundResource(R.drawable.button_background);
                            txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                        } else {
                            mProceedflag = false;
                            txt_totalprice.setBackgroundResource(R.drawable.button_background_grey);
                            txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                        }

                    }
                    StoreItemsPojo mPojo = new StoreItemsPojo();
                    mPojo.setStoreItemsList(mTotalItemList);
                    String mData = new Gson().toJson(mPojo);
                    tinyDB.remove("OFFSTORES");
                    tinyDB.putString("OFFSTORES", mData);

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getCatItemsDetails(String s, String categoryId) {
        List<StoreItemsPojo.StoreItemsList> mItemList = new ArrayList();
        for (int i = 0; i < mTotalItemList.size(); i++) {
            if (mTotalItemList.get(i).getCategoryId().equalsIgnoreCase(categoryId)) {
                mItemList.add(mTotalItemList.get(i));
            }
        }
        mItemAdapter.updateAdapter(mItemList);
    }


    private void selectAddress() {
        if (!TextUtils.isEmpty(tinyDB.getString(ADDRESS_LIST))) {
            try {
                jsonArray = new JSONArray(tinyDB.getString(ADDRESS_LIST));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonArray != null) {
                if (jsonArray.length() > 0) {
                    mAddressList = new ArrayList();
                    AddressPojo mPojo = new AddressPojo();
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
                            try {
                                if (jsonObject.getString("Lat") != null) {
                                    mPojo.setLat(jsonObject.getString("Lat"));
                                } else {
                                    mPojo.setLat("0.0");
                                }
                                if (jsonObject.getString("Lang") != null) {
                                    mPojo.setLang(jsonObject.getString("Lat"));
                                } else {
                                    mPojo.setLang("0.0");
                                }
                            } catch (JSONException e) {
                                mPojo.setLat("0.0");
                                mPojo.setLang("0.0");
                            } catch (NullPointerException e) {
                                mPojo.setLat("0.0");
                                mPojo.setLang("0.0");
                            }
                            mAddressList.add(mPojo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    double latitude = tinyDB.getDouble("LAT", 0.0);
                    double longitude = tinyDB.getDouble("LANG", 0.0);
                    Geocoder geocoder;

                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    captureAddress(addresses, "", mAddressList, "selectaddress");
                } else {
                    Toasty.success(getActivity(), "Your address list is empty, Please Add address to proceed", Toasty.LENGTH_SHORT).show();
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
                }
            }
        } else {
            Toasty.success(getActivity(), "Your address list is empty, Please Add address to proceed", Toast.LENGTH_SHORT, true).show();
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
        }
    }

    private void captureAddress(List<Address> addresses, String argument, List<AddressPojo> mAddressList, String selectaddress) {
        mAddType = "";
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
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
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        if (mAddressList.size() > 0) {
            adapter = new AddressAdapter(getActivity(), this.mAddressList, new AddressAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(AddressPojo response, int position) {
                    tinyDB.remove(USER_ADDRESS);
                    tinyDB.putString(USER_ADDRESS, new Gson().toJson(response));

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
                        if (mJsonArray == null) {
                            mJsonArray = new JSONArray();
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
               /* if (TextUtils.isEmpty(mAddType)) {
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
                   *//* behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Bundle args1 = new Bundle();
                    args1.putString("ARG_PARAM1", argument);
                    args1.putString("ARG_PARAM2", "arguments");
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_insta_services, args1);
                   *//*
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
                        mPojo.setLat(addresses.get(0).getLatitude()+"");
                        mPojo.setLang(addresses.get(0).getLongitude()+"");
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
                }*/
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
                        Toasty.success(getActivity(), "Address updated Successfully", Toasty.LENGTH_SHORT).show();

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

    private void proceedOrder() {
        try {
            List<StoreOrderItemsPojo> mStoreItemsPojoList = new ArrayList();
            StoreOrderItemsPojo mDataPojo = new StoreOrderItemsPojo();
            if (mTotalItemList.size() > 0) {
                for (int i = 0; i < mTotalItemList.size(); i++) {
                    if (mTotalItemList.get(i).getTotalAmt() > 0) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        mDataPojo = new StoreOrderItemsPojo();
                        mDataPojo.setItemId(mTotalItemList.get(i).getItemId());
                        mDataPojo.setItemName(mTotalItemList.get(i).getItemName());
                        mDataPojo.setItemQuantity(mTotalItemList.get(i).getItemQuantity());
                        mDataPojo.setItemCategory(mTotalItemList.get(i).getCategoryId());
                        mDataPojo.setItemprice(mTotalItemList.get(i).getItemPrice());
                        mDataPojo.setTotalamount(df.format(mTotalItemList.get(i).getTotalAmt()));
                        mDataPojo.setOrderType("stores");
                        mDataPojo.setMrptotalamount(df.format(mTotalItemList.get(i).getMRPtotalAmt()));
                        mDataPojo.setItemCount(mTotalItemList.get(i).getItemCount() + "");
                        mDataPojo.setSellingPrice(mTotalItemList.get(i).getSellingPrice());
                        mDataPojo.setItemImage(mTotalItemList.get(i).getItemImage());
                        mStoreItemsPojoList.add(mDataPojo);
                    }
                }
                // mItemList = mItemAdapter.getlist();
                StoreItemsPojo mPojo = new StoreItemsPojo();
                mPojo.setStoreItemsList(mTotalItemList);
                String mData = new Gson().toJson(mPojo);
                tinyDB.remove("OFFSTORES");
                tinyDB.putString("OFFSTORES", mData);
                new StoresOrderCalculation().newInstance(mStoreItemsPojoList, totalAmt, mrpTotalAmt, tinyDB.getString("SVENDOR"));
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_stores_items_ordercal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTotalItemList.size() > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    totalAmt = 0.0;
                    mrpTotalAmt = 0.0;
                    for (int i = 0; i < mTotalItemList.size(); i++) {

                        totalAmt = totalAmt + mTotalItemList.get(i).getTotalAmt();
                        mrpTotalAmt = mrpTotalAmt + mTotalItemList.get(i).getMRPtotalAmt();
                        if (totalAmt >= minamt) {
                            mProceedflag = true;
                            txt_totalprice.setBackgroundResource(R.drawable.button_background);
                            txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                        } else {
                            mProceedflag = false;
                            txt_totalprice.setBackgroundResource(R.drawable.button_background_grey);
                            txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                        }
                    }

                }
            }, 1000);

        }

    }


    private void getItemsDetails(String svendor, String category) {
        try {
            Call<StoreItemsPojo> call = apiService.getStoresItems(category, svendor, "");
            call.enqueue(new Callback<StoreItemsPojo>() {
                @Override
                public void onResponse(Call<StoreItemsPojo> call, Response<StoreItemsPojo> response) {
                    StoreItemsPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getStoreItemsList() != null) {
                            //mTotalItemList = new ArrayList<>();
                            if (mTotalItemList.size() == 0) {
                                if (!TextUtils.isEmpty(tinyDB.getString("OFFSTORES"))) {
                                    StoreItemsPojo mDataPojo = new StoreItemsPojo();
                                    mDataPojo = new Gson().fromJson(tinyDB.getString("OFFSTORES"), StoreItemsPojo.class);
                                    if (mDataPojo.getStoreItemsList().get(0).getVendorId().equalsIgnoreCase(svendor)) {
                                        mTotalItemList = mDataPojo.getStoreItemsList();
                                        mItemAdapter.updateAdapter(mTotalItemList);
                                        if (mTotalItemList.size() > 0) {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    totalAmt = 0.0;
                                                    mrpTotalAmt = 0.0;
                                                    for (int i = 0; i < mTotalItemList.size(); i++) {

                                                        totalAmt = totalAmt + mTotalItemList.get(i).getTotalAmt();
                                                        mrpTotalAmt = mrpTotalAmt + mTotalItemList.get(i).getMRPtotalAmt();
                                                        if (totalAmt >= minamt) {
                                                            mProceedflag = true;
                                                            txt_totalprice.setBackgroundResource(R.drawable.button_background);
                                                            txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                                                        } else {
                                                            mProceedflag = false;
                                                            txt_totalprice.setBackgroundResource(R.drawable.button_background_grey);
                                                            txt_totalprice.setText("Total Amount : ₹" + df.format(totalAmt));
                                                        }
                                                    }
                                                }

                                            }, 500);

                                        }
                                    } else {
                                        mTotalItemList = mItemData.getStoreItemsList();
                                        mItemAdapter.updateAdapter(mTotalItemList);
                                    }
                                } else {
                                    mTotalItemList = mItemData.getStoreItemsList();
                                    mItemAdapter.updateAdapter(mTotalItemList);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<StoreItemsPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void getCategoryDetails(String svendor) {
        try {
            Call<StoreCategoryPojo> call = apiService.getStoresCategory(svendor);
            call.enqueue(new Callback<StoreCategoryPojo>() {
                @Override
                public void onResponse(Call<StoreCategoryPojo> call, Response<StoreCategoryPojo> response) {
                    StoreCategoryPojo mItemData = response.body();
                    if (mItemData.getStatus().equalsIgnoreCase("200")) {
                        if (mItemData.getStoreCategoryDetails() != null) {
                            if (mItemData.getStoreCategoryDetails().size() > 0) {

                                mCatAdapter.updateAdapter(mItemData.getStoreCategoryDetails());
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<StoreCategoryPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }
}