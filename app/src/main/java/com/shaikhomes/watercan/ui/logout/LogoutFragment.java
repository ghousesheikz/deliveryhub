package com.shaikhomes.watercan.ui.logout;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.ui.BottomSheetView;
import com.shaikhomes.watercan.utility.TinyDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

import static com.shaikhomes.watercan.utility.AppConstants.LOGIN_ENABLED;


public class LogoutFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private OnDialogClicked mCallback;

    Spinner sales_person;

    Activity activity;
    TinyDB tinyDB;


    AppCompatButton mBtnYes, mBtnNo;


    @Nullable
    Activity context;
    BottomSheetView bottomSheetView;
    public LogoutFragment() {
        setRetainInstance(true);
    }


    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public interface OnDialogClicked {
        void positive(DialogFragment dialog);

        void negative(DialogFragment dialog);

    }


    @NonNull
    public static LogoutFragment newInstance() {
        LogoutFragment frag = new LogoutFragment();
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.logout_dialog, container, false);


        activity = getActivity();


        tinyDB = new TinyDB(activity);

        mBtnYes = v.findViewById(R.id.btn_yes);
        mBtnYes.setOnClickListener(this);
        mBtnNo = v.findViewById(R.id.btn_no);
        mBtnNo.setOnClickListener(this);
        bottomSheetView = (BottomSheetView) v.getContext();
        return v;

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onClick(@NonNull View v) {

        if (v.getId() == R.id.btn_yes) {
            tinyDB.remove(LOGIN_ENABLED);
            bottomSheetView.DialogYes("yes");
        } else if (v.getId() == R.id.btn_no) {

            dismiss();
            bottomSheetView.DialogNo("yes");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


}