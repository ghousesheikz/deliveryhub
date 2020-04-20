package com.shaikhomes.watercan.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("CategoryDetails")
    @Expose
    private List<CategoryDetail> categoryDetails = null;

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

    public List<CategoryDetail> getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(List<CategoryDetail> categoryDetails) {
        this.categoryDetails = categoryDetails;
    }

    public static class CategoryDetail {

        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("CategoryImage")
        @Expose
        private String categoryImage;
        @SerializedName("CategoryName")
        @Expose
        private String categoryName;

        public CategoryDetail() {

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryImage() {
            return categoryImage;
        }

        public void setCategoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public CategoryDetail(String id, String categoryImage, String categoryName) {
            this.id = id;
            this.categoryImage = categoryImage;
            this.categoryName = categoryName;
        }
    }

}
