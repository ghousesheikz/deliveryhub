package com.shaikhomes.deliveryhub.ui.stores;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreItemsPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("StoreItemsList")
    @Expose
    private List<StoreItemsList> storeItemsList = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StoreItemsList> getStoreItemsList() {
        return storeItemsList;
    }

    public void setStoreItemsList(List<StoreItemsList> storeItemsList) {
        this.storeItemsList = storeItemsList;
    }

    public class StoreItemsList {

        @SerializedName("ItemId")
        @Expose
        private String itemId;
        @SerializedName("ItemName")
        @Expose
        private String itemName;
        @SerializedName("ItemQuantity")
        @Expose
        private String itemQuantity;
        @SerializedName("ItemSize")
        @Expose
        private String itemSize;
        @SerializedName("ItemPrice")
        @Expose
        private String itemPrice;
        @SerializedName("VendorId")
        @Expose
        private String vendorId;
        @SerializedName("VendorName")
        @Expose
        private String vendorName;
        @SerializedName("VendorAddress")
        @Expose
        private String vendorAddress;
        @SerializedName("ItemCompany")
        @Expose
        private String itemCompany;
        @SerializedName("ItemActive")
        @Expose
        private String itemActive;
        @SerializedName("minqty")
        @Expose
        private String minqty;
        @SerializedName("ItemImage")
        @Expose
        private String itemImage;
        @SerializedName("CategoryId")
        @Expose
        private String categoryId;
        @SerializedName("ItemDescription")
        @Expose
        private String itemDescription;
        @SerializedName("Image1")
        @Expose
        private String image1;
        @SerializedName("Image2")
        @Expose
        private String image2;
        @SerializedName("Image3")
        @Expose
        private String image3;
        @SerializedName("itemLat")
        @Expose
        private Double itemLat;
        @SerializedName("itemLong")
        @Expose
        private Double itemLong;
        @SerializedName("rangeKms")
        @Expose
        private Double rangeKms;
        @SerializedName("SellingPrice")
        @Expose
        private String SellingPrice;
        @SerializedName("Weight1")
        @Expose
        private String Weight1;
        @SerializedName("MrpPrice1")
        @Expose
        private String MrpPrice1;
        @SerializedName("SellingPrice1")
        @Expose
        private String SellingPrice1;
        @SerializedName("Weight2")
        @Expose
        private String Weight2;
        @SerializedName("MrpPrice2")
        @Expose
        private String MrpPrice2;
        @SerializedName("SellingPrice2")
        @Expose
        private String SellingPrice2;
        @SerializedName("Weight3")
        @Expose
        private String Weight3;
        @SerializedName("MrpPrice3")
        @Expose
        private String MrpPrice3;
        @SerializedName("SellingPrice3")
        @Expose
        private String SellingPrice3;
        @SerializedName("mSelect")
        @Expose
        public int mSelect=0;

        public int getmSelect() {
            return mSelect;
        }

        public void setmSelect(int mSelect) {
            this.mSelect = mSelect;
        }

        public String getWeight1() {
            return Weight1;
        }

        public void setWeight1(String weight1) {
            Weight1 = weight1;
        }

        public String getMrpPrice1() {
            return MrpPrice1;
        }

        public void setMrpPrice1(String mrpPrice1) {
            MrpPrice1 = mrpPrice1;
        }

        public String getSellingPrice1() {
            return SellingPrice1;
        }

        public void setSellingPrice1(String sellingPrice1) {
            SellingPrice1 = sellingPrice1;
        }

        public String getWeight2() {
            return Weight2;
        }

        public void setWeight2(String weight2) {
            Weight2 = weight2;
        }

        public String getMrpPrice2() {
            return MrpPrice2;
        }

        public void setMrpPrice2(String mrpPrice2) {
            MrpPrice2 = mrpPrice2;
        }

        public String getSellingPrice2() {
            return SellingPrice2;
        }

        public void setSellingPrice2(String sellingPrice2) {
            SellingPrice2 = sellingPrice2;
        }

        public String getWeight3() {
            return Weight3;
        }

        public void setWeight3(String weight3) {
            Weight3 = weight3;
        }

        public String getMrpPrice3() {
            return MrpPrice3;
        }

        public void setMrpPrice3(String mrpPrice3) {
            MrpPrice3 = mrpPrice3;
        }

        public String getSellingPrice3() {
            return SellingPrice3;
        }

        public void setSellingPrice3(String sellingPrice3) {
            SellingPrice3 = sellingPrice3;
        }

        public String getSellingPrice() {
            return SellingPrice;
        }

        public void setSellingPrice(String sellingPrice) {
            SellingPrice = sellingPrice;
        }

        private int itemCount;

        private double totalAmt;

        private double MRPtotalAmt;

        public double getMRPtotalAmt() {
            return MRPtotalAmt;
        }

        public void setMRPtotalAmt(double MRPtotalAmt) {
            this.MRPtotalAmt = MRPtotalAmt;
        }

        public double getTotalAmt() {
            return totalAmt;
        }

        public void setTotalAmt(double totalAmt) {
            this.totalAmt = totalAmt;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemQuantity() {
            return itemQuantity;
        }

        public void setItemQuantity(String itemQuantity) {
            this.itemQuantity = itemQuantity;
        }

        public String getItemSize() {
            return itemSize;
        }

        public void setItemSize(String itemSize) {
            this.itemSize = itemSize;
        }

        public String getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(String itemPrice) {
            this.itemPrice = itemPrice;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getVendorAddress() {
            return vendorAddress;
        }

        public void setVendorAddress(String vendorAddress) {
            this.vendorAddress = vendorAddress;
        }

        public String getItemCompany() {
            return itemCompany;
        }

        public void setItemCompany(String itemCompany) {
            this.itemCompany = itemCompany;
        }

        public String getItemActive() {
            return itemActive;
        }

        public void setItemActive(String itemActive) {
            this.itemActive = itemActive;
        }

        public String getMinqty() {
            return minqty;
        }

        public void setMinqty(String minqty) {
            this.minqty = minqty;
        }

        public String getItemImage() {
            return itemImage;
        }

        public void setItemImage(String itemImage) {
            this.itemImage = itemImage;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public String getImage1() {
            return image1;
        }

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public String getImage3() {
            return image3;
        }

        public void setImage3(String image3) {
            this.image3 = image3;
        }

        public Double getItemLat() {
            return itemLat;
        }

        public void setItemLat(Double itemLat) {
            this.itemLat = itemLat;
        }

        public Double getItemLong() {
            return itemLong;
        }

        public void setItemLong(Double itemLong) {
            this.itemLong = itemLong;
        }

        public Double getRangeKms() {
            return rangeKms;
        }

        public void setRangeKms(Double rangeKms) {
            this.rangeKms = rangeKms;
        }

    }
}
