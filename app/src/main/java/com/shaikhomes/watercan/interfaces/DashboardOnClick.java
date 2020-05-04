package com.shaikhomes.watercan.interfaces;

import com.shaikhomes.watercan.pojo.CategoryPojo;

public interface DashboardOnClick {

  /*  void clickMenu(int c);
    void clickVehicle(int c, int subCategoryID);
    void clickOthers(int c, int categoryID);
    void clickProduct(String productCode);*/
    void clickHeader(int c, int subCategoryID);

    void onItemClick(CategoryPojo.CategoryDetail categoryDetail, int position);
}
