package com.shaikhomes.watercan.model;

public class OrderCalculationPojo {
    public String imageURL;
    public String Name;
    public String Price;
    public String Liters;
    public String VendorId;
    public String VendorName;
    public String MinQty;
    public int itemcount;
    public int NoOfCans;
    public int unitAmount;
    public int TotalAmount;

    public String getImageURL() {
        return imageURL;
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
}
