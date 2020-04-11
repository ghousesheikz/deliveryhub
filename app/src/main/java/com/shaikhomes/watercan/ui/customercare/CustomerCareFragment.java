package com.shaikhomes.watercan.ui.customercare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.SplashActivity;
import com.shaikhomes.watercan.ui.BottomSheetView;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerCareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerCareFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    BottomSheetView bottomSheetView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerCareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerCareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerCareFragment newInstance(String param1, String param2) {
        CustomerCareFragment fragment = new CustomerCareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    CardView mCardview;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_care, container, false);
        bottomSheetView = (BottomSheetView) view.getContext();
        bottomSheetView.CustomerCare("hide");
        mCardview = view.findViewById(R.id.call_btn);
        mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    callNumber("9966009289");
                } else {
                    Toasty.info(getActivity(), "Please give permission for calling", Toasty.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void callNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        number = "+91" + number;
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getActivity().startActivity(intent);

    }
}
