package com.shaikhomes.watercan.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ItemList")
    @Expose
    private List<Item> itemList = null;

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

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public static class Item {
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
        @SerializedName("Update")
        @Expose
        private String Update;
        @SerializedName("CategoryId")
        @Expose
        private String CategoryId;
        @SerializedName("ItemDescription")
        @Expose
        private String ItemDescription;
        @SerializedName("Image1")
        @Expose
        private String Image1;
        @SerializedName("Image2")
        @Expose
        private String Image2;
        @SerializedName("Image3")
        @Expose
        private String Image3;
        @SerializedName("itemLat")
        @Expose
        private double itemLat;
        @SerializedName("itemLong")
        @Expose
        private double itemLong;
        @SerializedName("rangeKms")
        @Expose
        private double rangeKms;


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

        public String getUpdate() {
            return Update;
        }

        public void setUpdate(String update) {
            Update = update;
        }

        public String getCategoryId() {
            return CategoryId;
        }

        public void setCategoryId(String categoryId) {
            CategoryId = categoryId;
        }

        public String getItemDescription() {
            return ItemDescription;
        }

        public void setItemDescription(String itemDescription) {
            ItemDescription = itemDescription;
        }

        public String getImage1() {
            return Image1;
        }

        public void setImage1(String image1) {
            Image1 = image1;
        }

        public String getImage2() {
            return Image2;
        }

        public void setImage2(String image2) {
            Image2 = image2;
        }

        public String getImage3() {
            return Image3;
        }

        public void setImage3(String image3) {
            Image3 = image3;
        }

        public double getItemLat() {
            return itemLat;
        }

        public void setItemLat(double itemLat) {
            this.itemLat = itemLat;
        }

        public double getItemLong() {
            return itemLong;
        }

        public void setItemLong(double itemLong) {
            this.itemLong = itemLong;
        }

        public double getRangeKms() {
            return rangeKms;
        }

        public void setRangeKms(double rangeKms) {
            this.rangeKms = rangeKms;
        }
    }
}
