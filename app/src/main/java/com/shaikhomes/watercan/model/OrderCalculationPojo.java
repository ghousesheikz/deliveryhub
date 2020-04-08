package com.shaikhomes.watercan.model;

public class OrderCalculationPojo {
    public String imageURL;
    public String Name;
    public String Price;
    public String Liters;
    public int itemcount;
    public int NoOfCans;
    public double unitAmount;
    public double TotalAmount;

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

    public double getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(double unitAmount) {
        this.unitAmount = unitAmount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getItemcount() {
        return itemcount;
    }

    public void setItemcount(int itemcount) {
        this.itemcount = itemcount;
    }
}
