package com.shaikhomes.deliveryhub.ui.stores;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderItemsListPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("OrderItemsList")
    @Expose
    private List<StoreOrderItemsPojo> orderItemsList = null;

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

    public List<StoreOrderItemsPojo> getOrderItemsList() {
        return orderItemsList;
    }

    public void setOrderItemsList(List<StoreOrderItemsPojo> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }
    public class OrderItemsList {

        @SerializedName("OrderItemId")
        @Expose
        private String orderItemId;
        @SerializedName("OrderId")
        @Expose
        private String orderId;
        @SerializedName("ItemName")
        @Expose
        private String itemName;
        @SerializedName("ItemQuantity")
        @Expose
        private String itemQuantity;
        @SerializedName("OTP")
        @Expose
        private String oTP;
        @SerializedName("UserMobileNo")
        @Expose
        private String userMobileNo;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("VendorName")
        @Expose
        private String vendorName;
        @SerializedName("VendorAddress")
        @Expose
        private String vendorAddress;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("Itemprice")
        @Expose
        private String itemprice;
        @SerializedName("totalamount")
        @Expose
        private String totalamount;
        @SerializedName("paid_status")
        @Expose
        private String paidStatus;
        @SerializedName("typeoforder")
        @Expose
        private String typeoforder;
        @SerializedName("OrderDate")
        @Expose
        private String orderDate;
        @SerializedName("OrderStatus")
        @Expose
        private String orderStatus;
        @SerializedName("OrderType")
        @Expose
        private String orderType;
        @SerializedName("PaymentType")
        @Expose
        private String paymentType;
        @SerializedName("DeliveredBy")
        @Expose
        private String deliveredBy;
        @SerializedName("DeliveredDate")
        @Expose
        private String deliveredDate;
        @SerializedName("ItemCategory")
        @Expose
        private String itemCategory;
        @SerializedName("PaymentTxnId")
        @Expose
        private String paymentTxnId;
        @SerializedName("ItemId")
        @Expose
        private String itemId;

        public String getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(String orderItemId) {
            this.orderItemId = orderItemId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
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

        public String getOTP() {
            return oTP;
        }

        public void setOTP(String oTP) {
            this.oTP = oTP;
        }

        public String getUserMobileNo() {
            return userMobileNo;
        }

        public void setUserMobileNo(String userMobileNo) {
            this.userMobileNo = userMobileNo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getItemprice() {
            return itemprice;
        }

        public void setItemprice(String itemprice) {
            this.itemprice = itemprice;
        }

        public String getTotalamount() {
            return totalamount;
        }

        public void setTotalamount(String totalamount) {
            this.totalamount = totalamount;
        }

        public String getPaidStatus() {
            return paidStatus;
        }

        public void setPaidStatus(String paidStatus) {
            this.paidStatus = paidStatus;
        }

        public String getTypeoforder() {
            return typeoforder;
        }

        public void setTypeoforder(String typeoforder) {
            this.typeoforder = typeoforder;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getDeliveredBy() {
            return deliveredBy;
        }

        public void setDeliveredBy(String deliveredBy) {
            this.deliveredBy = deliveredBy;
        }

        public String getDeliveredDate() {
            return deliveredDate;
        }

        public void setDeliveredDate(String deliveredDate) {
            this.deliveredDate = deliveredDate;
        }

        public String getItemCategory() {
            return itemCategory;
        }

        public void setItemCategory(String itemCategory) {
            this.itemCategory = itemCategory;
        }

        public String getPaymentTxnId() {
            return paymentTxnId;
        }

        public void setPaymentTxnId(String paymentTxnId) {
            this.paymentTxnId = paymentTxnId;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

    }
}
