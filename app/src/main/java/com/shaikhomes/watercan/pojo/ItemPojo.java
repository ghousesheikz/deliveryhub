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
    }
}
