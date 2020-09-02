package com.shaikhomes.deliveryhub.ui.stores;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreCategoryPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("StoreCategoryDetails")
    @Expose
    private List<StoreCategoryDetail> storeCategoryDetails = null;

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

    public List<StoreCategoryDetail> getStoreCategoryDetails() {
        return storeCategoryDetails;
    }

    public void setStoreCategoryDetails(List<StoreCategoryDetail> storeCategoryDetails) {
        this.storeCategoryDetails = storeCategoryDetails;
    }
    public class StoreCategoryDetail {

        @SerializedName("CategoryId")
        @Expose
        private String categoryId;
        @SerializedName("VendorId")
        @Expose
        private String vendorId;
        @SerializedName("CategoryName")
        @Expose
        private String categoryName;
        @SerializedName("CategoryImage")
        @Expose
        private String categoryImage;
        @SerializedName("update")
        @Expose
        private Object update;
        @SerializedName("updateImage")
        @Expose
        private Object updateImage;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryImage() {
            return categoryImage;
        }

        public void setCategoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
        }

        public Object getUpdate() {
            return update;
        }

        public void setUpdate(Object update) {
            this.update = update;
        }

        public Object getUpdateImage() {
            return updateImage;
        }

        public void setUpdateImage(Object updateImage) {
            this.updateImage = updateImage;
        }

    }
}
