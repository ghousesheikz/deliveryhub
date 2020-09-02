package com.shaikhomes.deliveryhub.ui;

import com.google.android.gms.maps.model.LatLng;

public interface BottomSheetView {
    void BottomSheetDesignView(String collapse);

    void BottomSheetDesignLocation(LatLng latLng);

    void DialogYes(String yes);

    void DialogNo(String no);

    void CustomerCare(String hide);
}
