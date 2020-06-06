package com.shaikhomes.watercan.ui.ordercalculation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemQueriesPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("QueryList")
    @Expose
    private List<QueryList> queryList = null;

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

    public List<QueryList> getQueryList() {
        return queryList;
    }

    public void setQueryList(List<QueryList> queryList) {
        this.queryList = queryList;
    }

    public static class QueryList {

        @SerializedName("QueryId")
        @Expose
        private String queryId;
        @SerializedName("ItemName")
        @Expose
        private String itemName;
        @SerializedName("VendorId")
        @Expose
        private String vendorId;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("QueryDate")
        @Expose
        private String queryDate;
        @SerializedName("question1")
        @Expose
        private String question1;
        @SerializedName("question2")
        @Expose
        private String question2;
        @SerializedName("question3")
        @Expose
        private String question3;
        @SerializedName("question4")
        @Expose
        private String question4;
        @SerializedName("answer1")
        @Expose
        private String answer1;
        @SerializedName("answer2")
        @Expose
        private String answer2;
        @SerializedName("answer3")
        @Expose
        private String answer3;
        @SerializedName("answer4")
        @Expose
        private String answer4;
        @SerializedName("QueryImage")
        @Expose
        private String queryImage;
        @SerializedName("ItemId")
        @Expose
        private String itemId;
        @SerializedName("update")
        @Expose
        private Object update;

        public String getQueryId() {
            return queryId;
        }

        public void setQueryId(String queryId) {
            this.queryId = queryId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getQueryDate() {
            return queryDate;
        }

        public void setQueryDate(String queryDate) {
            this.queryDate = queryDate;
        }

        public String getQuestion1() {
            return question1;
        }

        public void setQuestion1(String question1) {
            this.question1 = question1;
        }

        public String getQuestion2() {
            return question2;
        }

        public void setQuestion2(String question2) {
            this.question2 = question2;
        }

        public String getQuestion3() {
            return question3;
        }

        public void setQuestion3(String question3) {
            this.question3 = question3;
        }

        public String getQuestion4() {
            return question4;
        }

        public void setQuestion4(String question4) {
            this.question4 = question4;
        }

        public String getAnswer1() {
            return answer1;
        }

        public void setAnswer1(String answer1) {
            this.answer1 = answer1;
        }

        public String getAnswer2() {
            return answer2;
        }

        public void setAnswer2(String answer2) {
            this.answer2 = answer2;
        }

        public String getAnswer3() {
            return answer3;
        }

        public void setAnswer3(String answer3) {
            this.answer3 = answer3;
        }

        public String getAnswer4() {
            return answer4;
        }

        public void setAnswer4(String answer4) {
            this.answer4 = answer4;
        }

        public String getQueryImage() {
            return queryImage;
        }

        public void setQueryImage(String queryImage) {
            this.queryImage = queryImage;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public Object getUpdate() {
            return update;
        }

        public void setUpdate(Object update) {
            this.update = update;
        }

    }
}
