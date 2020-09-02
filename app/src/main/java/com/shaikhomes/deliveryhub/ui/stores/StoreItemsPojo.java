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

        private int itemCount;

        private double totalAmt;

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
