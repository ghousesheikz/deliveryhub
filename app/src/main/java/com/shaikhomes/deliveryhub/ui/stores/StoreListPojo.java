package com.shaikhomes.deliveryhub.ui.stores;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreListPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ShopsList")
    @Expose
    private List<ShopsList> shopsList = null;

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

    public List<ShopsList> getShopsList() {
        return shopsList;
    }

    public void setShopsList(List<ShopsList> shopsList) {
        this.shopsList = shopsList;
    }

    public class ShopsList {

        @SerializedName("VendorId")
        @Expose
        private String vendorId;
        @SerializedName("Active")
        @Expose
        private String active;
        @SerializedName("VendorMobileNo")
        @Expose
        private String vendorMobileNo;
        @SerializedName("VendorName")
        @Expose
        private String vendorName;
        @SerializedName("ShopName")
        @Expose
        private String shopName;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("MinOrderAmt")
        @Expose
        private Double minOrderAmt;
        @SerializedName("GST")
        @Expose
        private Double gST;
        @SerializedName("Tax")
        @Expose
        private Double tax;
        @SerializedName("DiscountPercent")
        @Expose
        private Double discountPercent;
        @SerializedName("WalletAmt")
        @Expose
        private Double walletAmt;
        @SerializedName("ShopImg")
        @Expose
        private String shopImg;
        @SerializedName("mLat")
        @Expose
        private Double mLat;
        @SerializedName("mLong")
        @Expose
        private Double mLong;
        @SerializedName("rangeKms")
        @Expose
        private Double rangeKms;
        @SerializedName("VendorUserId")
        @Expose
        private String VendorUserId;

        public String getVendorUserId() {
            return VendorUserId;
        }

        public void setVendorUserId(String vendorUserId) {
            VendorUserId = vendorUserId;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getVendorMobileNo() {
            return vendorMobileNo;
        }

        public void setVendorMobileNo(String vendorMobileNo) {
            this.vendorMobileNo = vendorMobileNo;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Double getMinOrderAmt() {
            return minOrderAmt;
        }

        public void setMinOrderAmt(Double minOrderAmt) {
            this.minOrderAmt = minOrderAmt;
        }

        public Double getGST() {
            return gST;
        }

        public void setGST(Double gST) {
            this.gST = gST;
        }

        public Double getTax() {
            return tax;
        }

        public void setTax(Double tax) {
            this.tax = tax;
        }

        public Double getDiscountPercent() {
            return discountPercent;
        }

        public void setDiscountPercent(Double discountPercent) {
            this.discountPercent = discountPercent;
        }

        public Double getWalletAmt() {
            return walletAmt;
        }

        public void setWalletAmt(Double walletAmt) {
            this.walletAmt = walletAmt;
        }

        public String getShopImg() {
            return shopImg;
        }

        public void setShopImg(String shopImg) {
            this.shopImg = shopImg;
        }

        public Double getMLat() {
            return mLat;
        }

        public void setMLat(Double mLat) {
            this.mLat = mLat;
        }

        public Double getMLong() {
            return mLong;
        }

        public void setMLong(Double mLong) {
            this.mLong = mLong;
        }

        public Double getRangeKms() {
            return rangeKms;
        }

        public void setRangeKms(Double rangeKms) {
            this.rangeKms = rangeKms;
        }

    }
}
