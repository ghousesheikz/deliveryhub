package com.shaikhomes.watercan.ui;

import android.view.View;

import com.google.android.gms.maps.model.LatLng;

public interface BottomSheetView {
    public void BottomSheetDesignView(String collapse);

    public void BottomSheetDesignLocation(LatLng latLng);

    public void DialogYes(String yes);

    public void DialogNo(String no);

    void CustomerCare(String hide);
}
