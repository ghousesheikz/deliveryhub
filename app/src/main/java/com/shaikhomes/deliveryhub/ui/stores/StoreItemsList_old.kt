/*
package com.shaikhomes.deliveryhub.ui.stores

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.shaikhomes.deliveryhub.R
import com.shaikhomes.deliveryhub.api_services.ApiClient
import com.shaikhomes.deliveryhub.api_services.ApiInterface
import com.shaikhomes.deliveryhub.pojo.AddressPojo
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo
import com.shaikhomes.deliveryhub.ui.address.AddressAdapter
import com.shaikhomes.deliveryhub.ui.stores.StoreCategoryPojo.StoreCategoryDetail
import com.shaikhomes.deliveryhub.utility.AppConstants
import com.shaikhomes.deliveryhub.utility.MyScrollController
import com.shaikhomes.deliveryhub.utility.TinyDB
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_store_items_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

*
 * A simple [Fragment] subclass.
 * Use the [StoreItemsList_old.newInstance] factory method to
 * create an instance of this fragment.


class StoreItemsList_old() : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
        mCatList = ArrayList()
        mItemList = ArrayList()
        mTotalItemList = ArrayList()
    }

    var view: View? = null
    private var apiService: ApiInterface? = null
    private var tinyDB: TinyDB? = null
    var mCategoryRecyclerView: RecyclerView? = null
    var mItemsRecyclerview: RecyclerView? = null
    var mCatAdapter: StoreCategoryAdapter? = null
    var mItemAdapter: StoreItemsAdapter? = null
    var mCatList: List<StoreCategoryDetail>? = null
    var mItemList: List<StoreItemsPojo.StoreItemsList>? = null
    var mTotalItemList: List<StoreItemsPojo.StoreItemsList>? = null
    var txt_totalprice: TextView? = null
    var minamt = 0
    var totalAmt = 0.0
    var mrpTotalAmt = 0.0
    var mProceedflag = false
    var df: DecimalFormat? = null
    private var jsonArray: JSONArray? = null
    private var jsonObject: JSONObject? = null
    private var mAddressList: MutableList<AddressPojo>? = null
    private var mAddType = ""
    private var mEdtFlatNo: EditText? = null
    private var mEdtApartmentName: EditText? = null
    private var mEdtLandmark: EditText? = null
    private var mEdtAreaName: EditText? = null
    private var mEdtCityName: EditText? = null
    private var mTypeHome: TextView? = null
    private var mTypeWork: TextView? = null
    private var mTypeOthers: TextView? = null
    private var mBtnSubmit: AppCompatButton? = null
    private val mBtnAddAddress: AppCompatButton? = null
    private val mBtnSelectAddress: AppCompatButton? = null
    private val mDeliveryLL: LinearLayout? = null
    private val mAddressLL: LinearLayout? = null
    private var mSelectAddressLL: LinearLayout? = null
    private var mAddAddressLL: LinearLayout? = null
    private var mRecyclerview: RecyclerView? = null
    private var adapter: AddressAdapter? = null
    var addresses: List<Address>? = null
    var mSearchEdt: EditText? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_store_items_list, container, false)
        tinyDB = TinyDB(activity)
        apiService = ApiClient.getClient(activity).create(ApiInterface::class.java)
        df = DecimalFormat("0.00")
        minamt = tinyDB!!.getInt("MINAMT")
        txt_totalprice = view.findViewById(R.id.txt_totalprice)
 mCategoryRecyclerView = view.findViewById(R.id.store_category_list)

        store_category_list.setLayoutManager(LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false))
 mItemsRecyclerview = view.findViewById(R.id.store_items_list)

        mSearchEdt = view.findViewById(R.id.edt_itemsearch)
        store_items_list.setLayoutManager(LinearLayoutManager(activity))
        store_items_list.addOnScrollListener(object : MyScrollController() {
            @SuppressLint("RestrictedApi")
            override fun show() {
 txt_totalprice.setVisibility(View.VISIBLE);
                txt_totalprice.animate().translationY(0).setStartDelay(200).setInterpolator(new DecelerateInterpolator(2)).start();


            }

            @SuppressLint("RestrictedApi")
            override fun hide() {
 txt_totalprice.animate().translationY(txt_totalprice.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                txt_totalprice.setVisibility(View.GONE);


            }
        })
        mCatAdapter =  StoreCategoryAdapter((activity)!!, mCatList!!,object :StoreCategoryAdapter.OnItemClickListener{
            override fun onItemClick(response: StoreCategoryDetail?, position: Int) {
                getCatItemsDetails("", response!!.categoryId)
            }
        })

             //
        store_category_list.setAdapter(mCatAdapter)
        mItemAdapter = StoreItemsAdapter((activity)!!, mTotalItemList, object : StoreItemsAdapter.OnItemClickListener {
            override fun onItemClick(response: List<StoreItemsPojo.StoreItemsList>?, position: Int) {
                totalAmt = 0.0
                mrpTotalAmt = 0.0
                for (i in response!!.indices) {
                    if (response[i].totalAmt >= 0) {
                        totalAmt = totalAmt + response[i].totalAmt
                        mrpTotalAmt = mrpTotalAmt + response[i].mrPtotalAmt
                        if (totalAmt >= minamt) {
                            mProceedflag = true
                            txt_totalprice!!.setBackgroundResource(R.drawable.button_background)
                            txt_totalprice!!.setText("Total Amount : ₹" + df!!.format(totalAmt))
                        } else {
                            mProceedflag = false
                            txt_totalprice!!.setBackgroundResource(R.drawable.button_background_grey)
                            txt_totalprice!!.setText("Total Amount : ₹" + df!!.format(totalAmt))
                        }
 */
/*for(int j=0;j<mTotalItemList.size();j++){
                            if(mTotalItemList.get(j).getItemId().equalsIgnoreCase(response.get(i).getItemId())){
                                mTotalItemList.remove(j);
                                mTotalItemList.add(j,response.get(i));
                            }*//*



                    }
                }
                val mPojo = StoreItemsPojo()
                mPojo.storeItemsList = response
                val mData = Gson().toJson(mPojo)
                tinyDB!!.remove("OFFSTORES")
                tinyDB!!.putString("OFFSTORES", mData)
            }
        })
        store_items_list.setAdapter(mItemAdapter)
        getCategoryDetails(tinyDB!!.getString("SVENDOR"))
        getItemsDetails(tinyDB!!.getString("SVENDOR"), "")
        txt_totalprice!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (mProceedflag) {
                if (TextUtils.isEmpty(tinyDB!!.getString(AppConstants.USER_ADDRESS))) {
                    selectAddress()
                } else {
                    proceedOrder()
                }
            } else {
                Toasty.error((getActivity())!!, "Minimum order amount is : " + df!!.format(minamt.toLong()), Toasty.LENGTH_SHORT).show()
            }
        })
        mSearchEdt!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //  textToSpeech.speak(charSequence.toString(), TextToSpeech.QUEUE_FLUSH, null);
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    if (!TextUtils.isEmpty(mSearchEdt!!.getText().toString().trim { it <= ' ' })) {
 if (mAdapter.getList() != null) {
                            mNewOrders = mAdapter.getList();
                        }

                        val text = charSequence.toString()
                        val temp: ArrayList<StoreItemsPojo.StoreItemsList?> = ArrayList<StoreItemsPojo.StoreItemsList?>()
                        if (mTotalItemList!!.size > 0) {
                            for (d: StoreItemsPojo.StoreItemsList in mTotalItemList!!) {
                                if (d.itemName.toLowerCase().contains(text.toLowerCase())) {
                                    temp.add(d)
                                }
                            }
                            //update recyclerview
                            mItemAdapter!!.updateAdapter(temp)
                        } else {
                            mItemAdapter!!.updateAdapter(mTotalItemList)
                        }
                    }
                } else {
                    mItemAdapter!!.updateAdapter(mTotalItemList)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        // Inflate the layout for this fragment
        return view
    }

    private fun getCatItemsDetails(s: String, categoryId: String) {
        val mItemList: MutableList<StoreItemsPojo.StoreItemsList> = ArrayList()
        for (i in mTotalItemList!!.indices) {
            if (mTotalItemList!![i].categoryId.equals(categoryId, ignoreCase = true)) {
                mItemList.add(mTotalItemList!![i])
            }
        }
        mItemAdapter!!.updateAdapter(mItemList)
    }

    override fun onResume() {
        super.onResume()
        if (mTotalItemList!!.size > 0) {
            Handler().postDelayed(object : Runnable {
                override fun run() {
                    // mItemAdapter.updateAdapter(mItemList);
                    totalAmt = 0.0
                    mrpTotalAmt = 0.0
                    for (i in mTotalItemList!!.indices) {
                        if (mTotalItemList!![i].totalAmt >= 0) {
                            totalAmt = totalAmt + mTotalItemList!![i].totalAmt
                            mrpTotalAmt = mrpTotalAmt + mTotalItemList!![i].mrPtotalAmt
                            if (totalAmt >= minamt) {
                                mProceedflag = true
                                txt_totalprice!!.setBackgroundResource(R.drawable.button_background)
                                txt_totalprice!!.text = "Total Amount : ₹" + df!!.format(totalAmt)
                            } else {
                                mProceedflag = false
                                txt_totalprice!!.setBackgroundResource(R.drawable.button_background_grey)
                                txt_totalprice!!.text = "Total Amount : ₹" + df!!.format(totalAmt)
                            }
                        }
                    }
                }
            }, 1000)
        }
    }

    private fun proceedOrder() {
        try {
            val mStoreItemsPojoList: MutableList<StoreOrderItemsPojo> = ArrayList()
            var mDataPojo: StoreOrderItemsPojo
            if (mTotalItemList!!.size > 0) {
                for (i in mTotalItemList!!.indices) {
                    if (mTotalItemList!![i].totalAmt > 0) {
                        val df = DecimalFormat("0.00")
                        mDataPojo = StoreOrderItemsPojo()
                        mDataPojo.itemId = mTotalItemList!!.get(i).itemId
                        mDataPojo.itemName = mTotalItemList!!.get(i).itemName
                        mDataPojo.itemQuantity = mTotalItemList!!.get(i).itemQuantity
                        mDataPojo.itemCategory = mTotalItemList!!.get(i).categoryId
                        mDataPojo.itemprice = mTotalItemList!!.get(i).itemPrice
                        mDataPojo.totalamount = df.format(mTotalItemList!!.get(i).totalAmt)
                        mDataPojo.orderType = "stores"
                        mDataPojo.mrptotalamount = df.format(mTotalItemList!!.get(i).mrPtotalAmt)
                        mDataPojo.itemCount = mTotalItemList!!.get(i).itemCount.toString() + ""
                        mDataPojo.sellingPrice = mTotalItemList!!.get(i).sellingPrice
                        mDataPojo.itemImage = mTotalItemList!!.get(i).itemImage
                        mStoreItemsPojoList.add(mDataPojo)
                    }
                }
                // mItemList = mItemAdapter.getlist();
                val mPojo = StoreItemsPojo()
                mPojo.storeItemsList = mTotalItemList
                val mData = Gson().toJson(mPojo)
                tinyDB!!.remove("OFFSTORES")
                tinyDB!!.putString("OFFSTORES", mData)
                StoresOrderCalculation().newInstance(mStoreItemsPojoList, totalAmt, mrpTotalAmt, tinyDB!!.getString("SVENDOR"))
                Navigation.findNavController((activity)!!, R.id.nav_host_fragment).navigate(R.id.nav_stores_items_ordercal)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getItemsDetails(svendor: String, category: String) {
        try {
            val call = apiService!!.getStoresItems(category, svendor, "")
            call.enqueue(object : Callback<StoreItemsPojo?> {
                override fun onResponse(call: Call<StoreItemsPojo?>, response: Response<StoreItemsPojo?>) {
                    val mItemData = response.body()
                    if (mItemData!!.status.equals("200", ignoreCase = true)) {
                        if (mItemData.storeItemsList != null) {
                            //mTotalItemList = new ArrayList<>();
                            if (mTotalItemList!!.size == 0) {
                                if (!TextUtils.isEmpty(tinyDB!!.getString("OFFSTORES"))) {
                                    var mDataPojo = StoreItemsPojo()
                                    mDataPojo = Gson().fromJson(tinyDB!!.getString("OFFSTORES"), StoreItemsPojo::class.java)
                                    if (mDataPojo.storeItemsList[0].vendorId.equals(svendor, ignoreCase = true)) {
                                        mTotalItemList = mDataPojo.storeItemsList
                                        mItemAdapter!!.updateAdapter(mTotalItemList)
                                        if (mTotalItemList.size > 0) {
                                            Handler().postDelayed(object : Runnable {
                                                override fun run() {
                                                    // mItemAdapter.updateAdapter(mItemList);
                                                    totalAmt = 0.0
                                                    mrpTotalAmt = 0.0
                                                    for (i in mTotalItemList.indices) {
                                                        if (mTotalItemList.get(i).totalAmt > 0) {
                                                            totalAmt = totalAmt + mTotalItemList.get(i).totalAmt
                                                            mrpTotalAmt = mrpTotalAmt + mTotalItemList.get(i).mrPtotalAmt
                                                            if (totalAmt >= minamt) {
                                                                mProceedflag = true
                                                                txt_totalprice!!.setBackgroundResource(R.drawable.button_background)
                                                                txt_totalprice!!.text = "Total Amount : ₹" + df!!.format(totalAmt)
                                                            } else {
                                                                mProceedflag = false
                                                                txt_totalprice!!.setBackgroundResource(R.drawable.button_background_grey)
                                                                txt_totalprice!!.text = "Total Amount : ₹" + df!!.format(totalAmt)
                                                            }
                                                        }
                                                    }
                                                }
                                            }, 500)
                                        }
                                    } else {
                                        mTotalItemList = mItemData.storeItemsList
                                        mItemAdapter!!.updateAdapter(mTotalItemList)
                                    }
                                } else {
                                    mTotalItemList = mItemData.storeItemsList
                                    mItemAdapter!!.updateAdapter(mTotalItemList)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<StoreItemsPojo?>, t: Throwable) {
                    Log.i("ERROR", t.message)
                }
            })
        } catch (e: Exception) {
            Log.i("ERROR", e.message)
            e.printStackTrace()
        }
    }

    private fun getCategoryDetails(svendor: String) {
        try {
            val call = apiService!!.getStoresCategory(svendor)
            call.enqueue(object : Callback<StoreCategoryPojo?> {
                override fun onResponse(call: Call<StoreCategoryPojo?>, response: Response<StoreCategoryPojo?>) {
                    val mItemData = response.body()
                    if (mItemData!!.status.equals("200", ignoreCase = true)) {
                        if (mItemData.storeCategoryDetails != null) {
                            if (mItemData.storeCategoryDetails.size > 0) {
                                mCatAdapter!!.updateAdapter(mItemData.storeCategoryDetails)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<StoreCategoryPojo?>, t: Throwable) {
                    Log.i("ERROR", t.message)
                }
            })
        } catch (e: Exception) {
            Log.i("ERROR", e.message)
            e.printStackTrace()
        }
    }

    private fun selectAddress() {
        if (!TextUtils.isEmpty(tinyDB!!.getString(AppConstants.ADDRESS_LIST))) {
            try {
                jsonArray = JSONArray(tinyDB!!.getString(AppConstants.ADDRESS_LIST))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (jsonArray != null) {
                if (jsonArray!!.length() > 0) {
                    mAddressList = ArrayList()
                    var mPojo: AddressPojo
                    for (i in 0 until jsonArray!!.length()) {
                        mPojo = AddressPojo()
                        try {
                            val jsonObject = JSONObject(jsonArray!![i].toString())
                            mPojo.addressType = jsonObject.getString("AddressType")
                            mPojo.flatNumber = jsonObject.getString("FlatNumber")
                            mPojo.apartmentName = jsonObject.getString("ApartmentName")
                            mPojo.landmark = jsonObject.getString("Landmark")
                            mPojo.areaName = jsonObject.getString("AreaName")
                            mPojo.cityName = jsonObject.getString("CityName")
                            try {
                                if (jsonObject.getString("Lat") != null) {
                                    mPojo.lat = jsonObject.getString("Lat")
                                } else {
                                    mPojo.lat = "0.0"
                                }
                                if (jsonObject.getString("Lang") != null) {
                                    mPojo.lang = jsonObject.getString("Lat")
                                } else {
                                    mPojo.lang = "0.0"
                                }
                            } catch (e: JSONException) {
                                mPojo.lat = "0.0"
                                mPojo.lang = "0.0"
                            } catch (e: NullPointerException) {
                                mPojo.lat = "0.0"
                                mPojo.lang = "0.0"
                            }
                            mAddressList.add(mPojo)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    val latitude = tinyDB!!.getDouble("LAT", 0.0)
                    val longitude = tinyDB!!.getDouble("LANG", 0.0)
                    val geocoder: Geocoder
                    geocoder = Geocoder(activity, Locale.getDefault())
                    addresses = null
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    captureAddress(addresses, "", mAddressList, "selectaddress")
                } else {
                    Toasty.success((activity)!!, "Your address list is empty, Please Add address to proceed", Toast.LENGTH_SHORT, true).show()
                    Navigation.findNavController((activity)!!, R.id.nav_host_fragment).navigate(R.id.nav_home)
                }
            }
        } else {
            Toasty.success((activity)!!, "Your address list is empty, Please Add address to proceed", Toast.LENGTH_SHORT, true).show()
            Navigation.findNavController((activity)!!, R.id.nav_host_fragment).navigate(R.id.nav_home)
        }
    }

    private fun captureAddress(addresses: List<Address>?, argument: String, mAddressList: List<AddressPojo>, selectaddress: String) {
        mAddType = ""
        val dialog = Dialog((activity)!!, R.style.Theme_AppCompat_DayNight_Dialog)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.address_dialog)
        val window = dialog.window
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        mEdtAreaName = dialog.findViewById(R.id.edt_areaname)
        mEdtFlatNo = dialog.findViewById(R.id.edt_flatno)
        mEdtApartmentName = dialog.findViewById(R.id.edt_apartmentname)
        mEdtLandmark = dialog.findViewById(R.id.edt_landmark)
        mEdtCityName = dialog.findViewById(R.id.edt_cityname)
        mTypeHome = dialog.findViewById(R.id.type_home)
        mTypeWork = dialog.findViewById(R.id.type_work)
        mTypeOthers = dialog.findViewById(R.id.type_others)
        mBtnSubmit = dialog.findViewById(R.id.btn_addr_submit)
        mRecyclerview = dialog.findViewById(R.id.address_list)
        mSelectAddressLL = dialog.findViewById(R.id.select_address_ll)
        mAddAddressLL = dialog.findViewById(R.id.add_address_ll)
        mRecyclerview.setLayoutManager(LinearLayoutManager(activity))
        mRecyclerview.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        if (mAddressList.size > 0) {
            adapter = AddressAdapter(activity, this.mAddressList, object : AddressAdapter.OnItemClickListener {
                override fun onItemClick(response: AddressPojo, position: Int) {
                    tinyDB!!.remove(AppConstants.USER_ADDRESS)
                    tinyDB!!.putString(AppConstants.USER_ADDRESS, Gson().toJson(response))
                    proceedOrder()
                    dialog.dismiss()
                }

                override fun onItemDelete(response: AddressPojo, position: Int) {
                    var mJsonObject: JSONObject? = null
                    var mJsonArray: JSONArray? = null
                    mAddressList.removeAt(position)
                    adapter!!.updateAdapter(mAddressList)
                    try {
                        for (i in adapter!!.getlist().indices) {
                            mJsonObject = JSONObject(Gson().toJson(adapter!!.getlist()[i]))
                            if (mJsonArray == null) {
                                mJsonArray = JSONArray()
                            }
                            mJsonArray.put(mJsonObject)
                        }
                        tinyDB!!.remove(AppConstants.ADDRESS_LIST)
                        tinyDB!!.putString(AppConstants.ADDRESS_LIST, mJsonArray.toString())
                        saveAddress(mJsonArray.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
            mRecyclerview.setAdapter(adapter)
            adapter!!.notifyDataSetChanged()
            mSelectAddressLL.setVisibility(View.VISIBLE)
            mAddAddressLL.setVisibility(View.GONE)
        } else {
            mSelectAddressLL.setVisibility(View.GONE)
            mAddAddressLL.setVisibility(View.VISIBLE)
        }
        mTypeHome.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                mAddType = "Home"
                mTypeHome.setBackgroundResource(R.drawable.button_background)
                mTypeWork.setBackgroundResource(R.drawable.edit_text_background)
                mTypeOthers.setBackgroundResource(R.drawable.edit_text_background)
                mTypeHome.setTextColor(Color.WHITE)
                mTypeWork.setTextColor(Color.BLACK)
                mTypeOthers.setTextColor(Color.BLACK)
            }
        })
        mTypeWork.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                mAddType = "Work"
                mTypeHome.setBackgroundResource(R.drawable.edit_text_background)
                mTypeWork.setBackgroundResource(R.drawable.button_background)
                mTypeOthers.setBackgroundResource(R.drawable.edit_text_background)
                mTypeHome.setTextColor(Color.BLACK)
                mTypeWork.setTextColor(Color.WHITE)
                mTypeOthers.setTextColor(Color.BLACK)
            }
        })
        mTypeOthers.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                mAddType = "Others"
                mTypeHome.setBackgroundResource(R.drawable.edit_text_background)
                mTypeWork.setBackgroundResource(R.drawable.edit_text_background)
                mTypeOthers.setBackgroundResource(R.drawable.button_background)
                mTypeHome.setTextColor(Color.BLACK)
                mTypeWork.setTextColor(Color.BLACK)
                mTypeOthers.setTextColor(Color.WHITE)
            }
        })
        mEdtAreaName.setText(addresses!![0].subLocality)
        mEdtCityName.setText(addresses[0].locality + " (" + addresses[0].postalCode + ")")
        mBtnSubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (TextUtils.isEmpty(mAddType)) {
                    Toasty.error((activity)!!, "Please select address type", Toasty.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(mEdtFlatNo.getText().toString())) {
                    Toasty.error((activity)!!, "Please enter flat/building number", Toasty.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(mEdtApartmentName.getText().toString())) {
                    Toasty.error((activity)!!, "Please enter apartment/street name", Toasty.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(mEdtAreaName.getText().toString())) {
                    Toasty.error((activity)!!, "Please enter area name", Toasty.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(mEdtCityName.getText().toString())) {
                    Toasty.error((activity)!!, "Please enter city name", Toasty.LENGTH_SHORT).show()
                } else {
                    var mAddress: String = ""
                    if (!TextUtils.isEmpty(mEdtLandmark.getText().toString())) {
                        mAddress = mAddType + "__" + mEdtFlatNo.getText().toString().trim { it <= ' ' } + "__" + mEdtApartmentName.getText().toString().trim { it <= ' ' } + "__" + mEdtLandmark.getText().toString().trim { it <= ' ' } + "__" + mEdtAreaName.getText().toString().trim { it <= ' ' } + "__" + mEdtCityName.getText().toString().trim { it <= ' ' }
                    } else {
                        mAddress = mAddType + "__" + mEdtFlatNo.getText().toString().trim { it <= ' ' } + "__" + mEdtApartmentName.getText().toString().trim { it <= ' ' } + "__" + "NO" + "__" + mEdtAreaName.getText().toString().trim { it <= ' ' } + "__" + mEdtCityName.getText().toString().trim { it <= ' ' }
                    }
 behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Bundle args1 = new Bundle();
                    args1.putString("ARG_PARAM1", argument);
                    args1.putString("ARG_PARAM2", "arguments");
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_insta_services, args1);

try {
                        val mPojo = AddressPojo()
                        mPojo.addressType = mAddType
                        mPojo.flatNumber = mEdtFlatNo.getText().toString()
                        mPojo.apartmentName = mEdtApartmentName.getText().toString()
                        if (!TextUtils.isEmpty(mEdtLandmark.getText().toString())) {
                            mPojo.landmark = mEdtLandmark.getText().toString()
                        } else {
                            mPojo.landmark = "NO"
                        }
                        mPojo.areaName = mEdtAreaName.getText().toString()
                        mPojo.cityName = mEdtCityName.getText().toString()
                        jsonObject = JSONObject(Gson().toJson(mPojo))
                        if (jsonArray == null) {
                            jsonArray = JSONArray()
                        }
                        jsonArray!!.put(jsonObject)
                        tinyDB!!.remove(AppConstants.ADDRESS_LIST)
                        tinyDB!!.putString(AppConstants.ADDRESS_LIST, jsonArray.toString())
                        mDeliveryLL!!.visibility = View.VISIBLE
                        mAddressLL!!.visibility = View.GONE
                        saveAddress(jsonArray.toString())
                        tinyDB!!.putString(AppConstants.USER_ADDRESS, Gson().toJson(mPojo))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    dialog.dismiss()
                }
            }
        })
    }

    private fun saveAddress(address: String) {
        try {
            val call = apiService!!.UpdateAddress(address, tinyDB!!.getString(AppConstants.USER_ID))
            call.enqueue(object : Callback<PostResponsePojo?> {
                override fun onResponse(call: Call<PostResponsePojo?>, response: Response<PostResponsePojo?>) {
                    val mItemData = response.body()
                    if (mItemData!!.status.equals("200", ignoreCase = true)) {
                        Toasty.success((activity)!!, "Address updated Successfully", Toasty.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PostResponsePojo?>, t: Throwable) {
                    Log.i("ERROR", t.message)
                }
            })
        } catch (e: Exception) {
            Log.i("ERROR", e.message)
            e.printStackTrace()
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

*
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StoreItemsList.


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): StoreItemsList_old {
            val fragment = StoreItemsList_old()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
*/
