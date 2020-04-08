package com.shaikhomes.watercan.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRegistrationPojo {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<UserData> data = null;

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

    public List<UserData> getData() {
        return data;
    }

    public void setData(List<UserData> data) {
        this.data = data;
    }

    public static class UserData {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("usermobileNumber")
        @Expose
        private String usermobileNumber;
        @SerializedName("isadmin")
        @Expose
        private String isadmin;
        @SerializedName("active")
        @Expose
        private String active;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("paidstatus")
        @Expose
        private String paidstatus;
        @SerializedName("agreementstatus")
        @Expose
        private String agreementstatus;

        public UserData() {

        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsermobileNumber() {
            return usermobileNumber;
        }

        public void setUsermobileNumber(String usermobileNumber) {
            this.usermobileNumber = usermobileNumber;
        }

        public String getIsadmin() {
            return isadmin;
        }

        public void setIsadmin(String isadmin) {
            this.isadmin = isadmin;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPaidstatus() {
            return paidstatus;
        }

        public void setPaidstatus(String paidstatus) {
            this.paidstatus = paidstatus;
        }

        public String getAgreementstatus() {
            return agreementstatus;
        }

        public void setAgreementstatus(String agreementstatus) {
            this.agreementstatus = agreementstatus;
        }

        public UserData(String username, String usermobileNumber, String isadmin, String active, String address, String otp, String userid, String paidstatus, String agreementstatus) {
            this.username = username;
            this.usermobileNumber = usermobileNumber;
            this.isadmin = isadmin;
            this.active = active;
            this.address = address;
            this.otp = otp;
            this.userid = userid;
            this.paidstatus = paidstatus;
            this.agreementstatus = agreementstatus;
        }
    }
}
