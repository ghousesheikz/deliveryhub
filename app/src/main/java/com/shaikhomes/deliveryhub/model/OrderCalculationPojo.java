package com.shaikhomes.deliveryhub.model;

public class OrderCalculationPojo {
    public String itemid;
    public String imageURL;
    public String imageURL2;
    public String imageURL3;
    public String imageURL4;
    public String Name;
    public String Description;
    public String Price;
    public String Liters;
    public String VendorId;
    public String VendorName;
    public String MinQty;
    public String CategoryId;
    public int itemcount;
    public int NoOfCans;
    public int unitAmount;
    public int TotalAmount;

    public String getImageURL() {
        return imageURL;
    }

    public String getImageURL2() {
        return imageURL2;
    }

    public void setImageURL2(String imageURL2) {
        this.imageURL2 = imageURL2;
    }

    public String getImageURL3() {
        return imageURL3;
    }

    public void setImageURL3(String imageURL3) {
        this.imageURL3 = imageURL3;
    }

    public String getImageURL4() {
        return imageURL4;
    }

    public void setImageURL4(String imageURL4) {
        this.imageURL4 = imageURL4;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getLiters() {
        return Liters;
    }

    public void setLiters(String liters) {
        Liters = liters;
    }

    public int getNoOfCans() {
        return NoOfCans;
    }

    public void setNoOfCans(int noOfCans) {
        NoOfCans = noOfCans;
    }

    public int getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(int unitAmount) {
        this.unitAmount = unitAmount;
    }

    public int getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getItemcount() {
        return itemcount;
    }

    public void setItemcount(int itemcount) {
        this.itemcount = itemcount;
    }

    public String getVendorId() {
        return VendorId;
    }

    public void setVendorId(String vendorId) {
        VendorId = vendorId;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    public String getMinQty() {
        return MinQty;
    }

    public void setMinQty(String minQty) {
        MinQty = minQty;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }
}
