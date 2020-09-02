package com.shaikhomes.deliveryhub.ui.maps;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.Marker;

public class BounceAnimation  implements Runnable{
    private final long mStart, mDuration;
    private final Interpolator mInterpolator;
    private final Marker mMarker;
    private final Handler mHandler;

    public BounceAnimation(long start, long duration, Marker marker, Handler handler) {
        mStart = start;
        mDuration = duration;
        mMarker = marker;
        mHandler = handler;
        mInterpolator = new BounceInterpolator();
    }

    @Override
    public void run() {
        long elapsed = SystemClock.uptimeMillis() - mStart;
        float t = Math.max(1 - mInterpolator.getInterpolation((float) elapsed / mDuration), 0f);
        mMarker.setAnchor(0.5f, 1.0f + 1.2f * t);

        if (t > 0.0) {
            // Post again 16ms later.
            mHandler.postDelayed(this, 16L);
        }
    }

}
