package com.shaikhomes.deliveryhub.ui.stores

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.shaikhomes.deliveryhub.R
import com.shaikhomes.deliveryhub.ui.stores.StoreCategoryPojo.StoreCategoryDetail
import com.shaikhomes.deliveryhub.utility.RoundedTransformation
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class StoreCategoryAdapter(context: Activity, mCanList: List<StoreCategoryDetail>, mItemClickListener: OnItemClickListener) : RecyclerView.Adapter<StoreCategoryAdapter.MyViewHolder>() {
    var context: Context
    var itemClickListener: OnItemClickListener
    var mCanList: List<StoreCategoryDetail>
    var rupee: String
    var mCount = 0
    var decimalFormat: DecimalFormat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_list_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //  animator.animateAdd(holder);
        if (!TextUtils.isEmpty(mCanList[position].categoryImage)) {
            if (mCanList[position].categoryImage.toLowerCase().equals("all", ignoreCase = true)) {
                Picasso.get().load(R.drawable.all_categories).resize(300, 300)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .centerCrop().transform(RoundedTransformation())
                        .into(holder.mImgCategory)
            } else {
                val circularProgressDrawable = CircularProgressDrawable(context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                val COLORS = intArrayOf(
                        R.color.colorPrimary
                )
                circularProgressDrawable.setColorSchemeColors(*COLORS)
                circularProgressDrawable.start()
                val imgUrl = "http://delapi.shaikhomes.com/ImageStorage/" + mCanList[position].categoryImage
                Picasso.get().load(imgUrl).resize(300, 300).placeholder(circularProgressDrawable)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .centerCrop().transform(RoundedTransformation())
                        .into(holder.mImgCategory)
            }
        }
        holder.mTxtCategory.text = mCanList[position].categoryName
        holder.mCatLL.setOnClickListener { itemClickListener.onItemClick(mCanList[position], position) }
    }

    interface OnItemClickListener {
        fun onItemClick(response: StoreCategoryDetail?, position: Int)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mCanList.size
        //  return mCanList == null ? 0 : mCanList.size();
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTxtCategory: TextView
        var mImgCategory: ImageView
        var mCatLL: RelativeLayout

        init {
            mTxtCategory = v.findViewById(R.id.category_txt)
            mImgCategory = v.findViewById(R.id.category_img)
            mCatLL = v.findViewById(R.id.category_)
        }
    }

    fun updateAdapter(updatelist: List<StoreCategoryDetail>) {
        mCanList = updatelist
        notifyDataSetChanged()
    }

    fun getlist(): List<StoreCategoryDetail> {
        return mCanList
    }

    init {
        this.context = context
        this.mCanList = mCanList
        itemClickListener = mItemClickListener
        rupee = context.resources.getString(R.string.Rs)
        decimalFormat = DecimalFormat("0.00")
    }
}