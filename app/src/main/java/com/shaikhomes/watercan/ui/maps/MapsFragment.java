package com.shaikhomes.watercan.ui.maps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.pojo.AddressPojo;
import com.shaikhomes.watercan.ui.BottomSheetView;
import com.shaikhomes.watercan.utility.TinyDB;

import static com.shaikhomes.watercan.utility.AppConstants.USER_ADDRESS;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private View view;

    double latitude;
    double longitude;
    SupportMapFragment mapFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleMap mGoogleMap;
    BottomSheetView bottomSheetView;
    TinyDB tinyDB;
    TextView mAddresstxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.maps_layout, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);
        bottomSheetView = (BottomSheetView) view.getContext();
        tinyDB = new TinyDB(getActivity());
        mAddresstxt = view.findViewById(R.id.address_selected);
        String mAddress = tinyDB.getString(USER_ADDRESS);
        AddressPojo mAddPojo = new Gson().fromJson(mAddress, AddressPojo.class);
        if (mAddPojo != null) {
            mAddresstxt.setVisibility(View.VISIBLE);
            mAddresstxt.setPadding(5, 5, 5, 5);
            if (!mAddPojo.getLandmark().equalsIgnoreCase("NO")) {
                mAddresstxt.setText(mAddPojo.getFlatNumber() + ", " + mAddPojo.getApartmentName() + ", " + mAddPojo.getLandmark() + ", " + mAddPojo.getAreaName() + ", " + mAddPojo.getCityName());
            } else {
                mAddresstxt.setText(mAddPojo.getFlatNumber() + ", " + mAddPojo.getApartmentName() + ", " + mAddPojo.getAreaName() + ", " + mAddPojo.getCityName());

            }
        } else {
            mAddresstxt.setVisibility(View.GONE);
        }
        return view;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(final GoogleMap mMap) {
        mGoogleMap = mMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.clear(); //clear old markers

      /*  CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(17.4401, 78.3489))
                .zoom(40)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(17.4401, 78.3489))
                .title("Gachibowli")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker)));*/

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                // mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            // mMap.setMyLocationEnabled(true);
        }

     /*   mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.4629101, -122.2449094))
                .title("Iron Man")
                .snippet("His Talent : Plenty of money"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.3092293, -122.1136845))
                .title("Captain America"));*/
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                bottomSheetView.BottomSheetDesignLocation(latLng);

                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("New Location")
                        .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker)));
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            //stop location updates when Activity is no longer active
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition googlePlex = CameraPosition.builder()
                .target(latLng)
                .zoom(30)
                .bearing(1f)
                .tilt(45)
                .build();


        //Place current location marker

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker));
        // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        //  mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        bottomSheetView.BottomSheetDesignLocation(latLng);


    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        // mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
