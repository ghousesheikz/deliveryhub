package com.shaikhomes.deliveryhub.ui.stores

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.shaikhomes.deliveryhub.R
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class StoreItemsAdapter(context: Activity, mJoblist: List<StoreItemsPojo.StoreItemsList>?, mItemClickListener: OnItemClickListener) : RecyclerView.Adapter<StoreItemsAdapter.MyViewHolder>() {
    var context: Context
    var itemClickListener: OnItemClickListener
    var mJoblist: List<StoreItemsPojo.StoreItemsList>?
    var rupee: String
    var mCount = 0
    var df: DecimalFormat
    var mCategory = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.store_itemslist_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //  animator.animateAdd(holder);
        if (mCategory.equals("", ignoreCase = true)) {
            holder.mOrderCanLL.visibility = View.VISIBLE
        } else if (!TextUtils.isEmpty(mCategory)) {
            if (mCategory.equals(mJoblist!![position].categoryId, ignoreCase = true)) {
                holder.mOrderCanLL.visibility = View.VISIBLE
            } else {
                holder.mOrderCanLL.visibility = View.GONE
            }
        }
        if (!TextUtils.isEmpty(mJoblist!![position].itemImage)) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            val COLORS = intArrayOf(
                    R.color.colorPrimary
            )
            circularProgressDrawable.setColorSchemeColors(*COLORS)
            circularProgressDrawable.start()
            val imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mJoblist!![position].itemImage
            Picasso.get()
                    .load(imgUrl)
                    .placeholder(circularProgressDrawable)
                    .fit()
                    .into(holder.mCanImage)
            /*  Picasso.get().load(imgUrl).placeholder(circularProgressDrawable)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).fit()
                    .into(holder.mCanImage);*/
        }
        holder.mCompName.text = mJoblist!!.get(position).itemName.trim { it <= ' ' }
        //holder.mQty.setText("Qty : "+mJoblist.get(position).getItemCount());
        // holder.mPrice.setText(rupee + " " + mJoblist.get(position).getItemPrice() + " ");
        holder.mItemDsc.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (!TextUtils.isEmpty(mJoblist!![position].itemDescription)) {
                    AlertDialog.Builder(context).setTitle("Item Info").setMessage(mJoblist!![position].itemDescription).setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        try {
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }).create().show()
                } else {
                    Toasty.error(context, "Item description not mentioned", Toasty.LENGTH_SHORT).show()
                }
            }
        })
        if (!TextUtils.isEmpty(mJoblist!![position].weight1)) {
            if (mJoblist!![position].mSelect == 0||mJoblist!![position].mSelect==1) {
                holder.weight_text.text = mJoblist!![position].weight1
                mJoblist!![position].itemSize = mJoblist!![position].weight1
                mJoblist!![position].itemPrice = mJoblist!![position].mrpPrice1
                mJoblist!![position].sellingPrice = mJoblist!![position].sellingPrice1
            }else if(mJoblist!![position].mSelect == 2){
                holder.weight_text.text = mJoblist!![position].weight2
                mJoblist!![position].itemSize = mJoblist!![position].weight2
                mJoblist!![position].itemPrice = mJoblist!![position].mrpPrice2
                mJoblist!![position].sellingPrice = mJoblist!![position].sellingPrice2
            }else if(mJoblist!![position].mSelect == 3){
                holder.weight_text.text = mJoblist!![position].weight3
                mJoblist!![position].itemSize = mJoblist!![position].weight3
                mJoblist!![position].itemPrice = mJoblist!![position].mrpPrice3
                mJoblist!![position].sellingPrice = mJoblist!![position].sellingPrice3
            }
        } else {
            holder.weight_text.text = mJoblist!![position].itemSize

        }
        val amt = mJoblist!![position].itemPrice.toInt()
        holder.mPrice.text = "₹ " + df.format(amt.toLong())
        holder.mPrice.paintFlags = holder.mPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        val saleamt = mJoblist!![position].sellingPrice.toInt()
        holder.mSellingPrice.text = "₹ " + df.format(saleamt.toLong())
        mCount = mJoblist!![position].itemCount
        holder.mPlusCnt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                mCount = mJoblist!![position].itemCount
                mCount = mCount + 1
                var amt = mJoblist!![position].sellingPrice.toDouble()
                var mrpamt = mJoblist!![position].itemPrice.toDouble()
                amt = mCount * amt
                mrpamt = mCount * mrpamt
                mJoblist!!.get(position).mrPtotalAmt = mrpamt
                mJoblist!!.get(position).totalAmt = amt
                mJoblist!!.get(position).itemCount = mCount

                holder.mCanCount.text = mCount.toString()
                itemClickListener.onItemClick(mJoblist, position)
            } // }
        })

        holder.mMinusCnt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                mCount = mJoblist!![position].itemCount
                mCount = mCount - 1
                if (mCount < 0) {
                    mCount = 0
                    var amt = mJoblist!![position].sellingPrice.toDouble()
                    var mrpamt = mJoblist!![position].itemPrice.toDouble()
                    amt = mCount * amt
                    mrpamt = mCount * mrpamt
                    mJoblist!!.get(position).mrPtotalAmt = mrpamt
                    mJoblist!!.get(position).totalAmt = amt
                    mJoblist!!.get(position).itemCount = mCount
                    holder.mCanCount.text = mCount.toString()
                    itemClickListener.onItemClick(mJoblist, position)
                } else {
                    var amt = mJoblist!![position].sellingPrice.toDouble()
                    var mrpamt = mJoblist!![position].itemPrice.toDouble()
                    amt = mCount * amt
                    mrpamt = mCount * mrpamt
                    mJoblist!!.get(position).mrPtotalAmt = mrpamt
                    mJoblist!!.get(position).totalAmt = amt
                    mJoblist!!.get(position).itemCount = mCount
                    // holder.mPrice.setText("₹ " + df.format(amt) + " ");
                    holder.mCanCount.text = mCount.toString()
                    itemClickListener.onItemClick(mJoblist, position)
                }
                // }
            }
        })
        holder.mCanCount.text = mJoblist!!.get(position).itemCount.toString()


        holder.weight_text.setOnClickListener({
            itemClickListener.onWeightClick(mJoblist, position, holder.weight_text)
        })
    }

    interface OnItemClickListener {
        fun onItemClick(response: List<StoreItemsPojo.StoreItemsList>?, position: Int)

        fun onWeightClick(mItemPojo: List<StoreItemsPojo.StoreItemsList>?, position: Int, weightText: TextView)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        // return 12;
        return if (mJoblist == null) 0 else mJoblist!!.size
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mCompName: TextView
        var mPrice: TextView
        var mCanCount: TextView
        var mSellingPrice: TextView
        var mPlusCnt: ImageView
        var mMinusCnt: ImageView
        var mCanImage: ImageView
        var mItemDsc: ImageView
        var mOrderCanLL: LinearLayout
        var weight_text: TextView

        init {
            mCompName = v.findViewById(R.id.name_)
            mPrice = v.findViewById(R.id.price_)
            mSellingPrice = v.findViewById(R.id.sellingprice_)
            mCanImage = v.findViewById(R.id.image_)
            mOrderCanLL = v.findViewById(R.id.order_can_ll)
            mItemDsc = v.findViewById(R.id.item_desc)
            mCanCount = v.findViewById(R.id.can_count)
            mPlusCnt = v.findViewById(R.id.plus_count)
            mMinusCnt = v.findViewById(R.id.minus_count)
            weight_text = v.findViewById(R.id.weight_txt);
        }
    }

    fun updateAdapter(updatelist: List<StoreItemsPojo.StoreItemsList>?) {
        mJoblist = updatelist
        notifyDataSetChanged()
    }

    fun updateItemAdapter(updatelist: String) {
        mCategory = updatelist
        notifyDataSetChanged()
    }

    fun getlist(): List<StoreItemsPojo.StoreItemsList>? {
        return mJoblist
    }

    companion object {
        fun round(value: Double, places: Int): String {
            if (places < 0) throw IllegalArgumentException()
            var bd = BigDecimal(value)
            bd = bd.setScale(places, RoundingMode.HALF_UP)
            return bd.toPlainString()
        }
    }

    init {
        this.context = context
        this.mJoblist = mJoblist
        itemClickListener = mItemClickListener
        rupee = context.resources.getString(R.string.Rs)
        df = DecimalFormat("0.00")
    }
}