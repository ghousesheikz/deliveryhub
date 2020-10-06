package com.shaikhomes.deliveryhub.interfaces;

import com.shaikhomes.deliveryhub.pojo.CategoryPojo;

public interface DashboardOnClick {

  /*  void clickMenu(int c);
    void clickVehicle(int c, int subCategoryID);
    void clickOthers(int c, int categoryID);
    void clickProduct(String productCode);*/
    void clickHeader(String c);

    void onItemClick(CategoryPojo.CategoryDetail categoryDetail, int position);
}
