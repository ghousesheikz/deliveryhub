package com.shaikhomes.watercan.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateWalletPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("WalletDetails")
    @Expose
    private List<WalletDetail> walletDetails = null;

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

    public List<WalletDetail> getWalletDetails() {
        return walletDetails;
    }

    public void setWalletDetails(List<WalletDetail> walletDetails) {
        this.walletDetails = walletDetails;
    }

    public static class WalletDetail {

        @SerializedName("WalletId")
        @Expose
        private String walletId;
        @SerializedName("Active")
        @Expose
        private String active;
        @SerializedName("VendorId")
        @Expose
        private String vendorId;
        @SerializedName("VendorName")
        @Expose
        private String vendorName;
        @SerializedName("VendorMobileNo")
        @Expose
        private String vendorMobileNo;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("oline_orders")
        @Expose
        private String olineOrders;
        @SerializedName("COD_orders")
        @Expose
        private String cODOrders;
        @SerializedName("total_orders")
        @Expose
        private String totalOrders;
        @SerializedName("totalamount")
        @Expose
        private String totalamount;
        @SerializedName("online_amount")
        @Expose
        private String onlineAmount;
        @SerializedName("cod_amount")
        @Expose
        private String codAmount;

        public String getWalletId() {
            return walletId;
        }

        public void setWalletId(String walletId) {
            this.walletId = walletId;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
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

        public String getVendorMobileNo() {
            return vendorMobileNo;
        }

        public void setVendorMobileNo(String vendorMobileNo) {
            this.vendorMobileNo = vendorMobileNo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getOlineOrders() {
            return olineOrders;
        }

        public void setOlineOrders(String olineOrders) {
            this.olineOrders = olineOrders;
        }

        public String getCODOrders() {
            return cODOrders;
        }

        public void setCODOrders(String cODOrders) {
            this.cODOrders = cODOrders;
        }

        public String getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(String totalOrders) {
            this.totalOrders = totalOrders;
        }

        public String getTotalamount() {
            return totalamount;
        }

        public void setTotalamount(String totalamount) {
            this.totalamount = totalamount;
        }

        public String getOnlineAmount() {
            return onlineAmount;
        }

        public void setOnlineAmount(String onlineAmount) {
            this.onlineAmount = onlineAmount;
        }

        public String getCodAmount() {
            return codAmount;
        }

        public void setCodAmount(String codAmount) {
            this.codAmount = codAmount;
        }

    }
}
