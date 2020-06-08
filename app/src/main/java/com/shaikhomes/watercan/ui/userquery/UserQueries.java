package com.shaikhomes.watercan.ui.userquery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaikhomes.watercan.BaseActivity;
import com.shaikhomes.watercan.R;
import com.shaikhomes.watercan.api_services.ApiClient;
import com.shaikhomes.watercan.api_services.ApiInterface;
import com.shaikhomes.watercan.model.OrderCalculationPojo;
import com.shaikhomes.watercan.pojo.DeliveryhubOffersPojo;
import com.shaikhomes.watercan.pojo.PostResponsePojo;
import com.shaikhomes.watercan.ui.ordercalculation.ChatAdapter;
import com.shaikhomes.watercan.ui.ordercalculation.ItemQueriesPojo;
import com.shaikhomes.watercan.utility.SliderAdapter;
import com.shaikhomes.watercan.utility.TinyDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shaikhomes.watercan.utility.AppConstants.USER_ID;
import static com.shaikhomes.watercan.utility.AppConstants.USER_NAME;

public class UserQueries extends Fragment {
    private RecyclerView mRecyclerview;
    QueriesAdapter mAdapter;
    TinyDB tinyDB;
    View view;
    private ApiInterface apiService;
    List<ItemQueriesPojo.QueryList> mQueryList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_user_queries, container, false);
        tinyDB = new TinyDB(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        mRecyclerview = view.findViewById(R.id.queries_list);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mQueryList = new ArrayList<>();
        mAdapter = new QueriesAdapter(getActivity(), mQueryList, new QueriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemQueriesPojo.QueryList response, int position) {
                chatDialog(response, position);
            }
        });
        mRecyclerview.setAdapter(mAdapter);
        getQueryData();
        return view;
    }

    private void getQueryData() {
        try {
            Call<ItemQueriesPojo> call = apiService.getQueries("", tinyDB.getString(USER_ID), "","");
            call.enqueue(new Callback<ItemQueriesPojo>() {
                @Override
                public void onResponse(Call<ItemQueriesPojo> call, Response<ItemQueriesPojo> response) {
                    ItemQueriesPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        mAdapter.updateAdapter(response.body().getQueryList());
                    } else {
                        Toasty.error(getActivity(), mPojo.getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ItemQueriesPojo> call, Throwable t) {
                    Log.i("ERROR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }


    private void chatDialog(ItemQueriesPojo.QueryList response, int position) {
        List<String> mChatList = new ArrayList<>();
        if (!TextUtils.isEmpty(response.getQuestion1())) {
            mChatList.add(response.getQuestion1());
        }
        if (!TextUtils.isEmpty(response.getAnswer1())) {
            mChatList.add(response.getAnswer1());
        }
        if (!TextUtils.isEmpty(response.getQuestion2())) {
            mChatList.add(response.getQuestion2());
        }
        if (!TextUtils.isEmpty(response.getAnswer2())) {
            mChatList.add(response.getAnswer2());
        }
        if (!TextUtils.isEmpty(response.getQuestion3())) {
            mChatList.add(response.getQuestion3());
        }
        if (!TextUtils.isEmpty(response.getAnswer3())) {
            mChatList.add(response.getAnswer3());
        }
        if (!TextUtils.isEmpty(response.getQuestion4())) {
            mChatList.add(response.getQuestion4());
        }
        if (!TextUtils.isEmpty(response.getAnswer4())) {
            mChatList.add(response.getAnswer4());
        }
        final int chatsize = mChatList.size();
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.queries_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        EditText mChatMsg = dialog.findViewById(R.id.edt_msg);
        FloatingActionButton mFabSend = dialog.findViewById(R.id.btn_send);
        ImageView mCloseBtn = dialog.findViewById(R.id.close_dialog);
        ImageView mCallBtn = dialog.findViewById(R.id.call_dialog);
        mCloseBtn.setOnClickListener(v -> {
            dialog.dismiss();
            getQueryData();
        });
        mCallBtn.setOnClickListener(v->{

        });
        RecyclerView mRecyclerview = dialog.findViewById(R.id.chat_msg_list);
        ChatAdapter mAdapter = new ChatAdapter(getActivity(), mChatList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        if (!TextUtils.isEmpty(response.getQuestion4())) {
            mFabSend.setEnabled(false);
        }
        if (!TextUtils.isEmpty(response.getAnswer4())) {
            mFabSend.setEnabled(false);
        }
        mFabSend.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mChatMsg.getText().toString().trim())) {
                if (mChatList.size() == chatsize) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();

                    ItemQueriesPojo.QueryList mPojo = new ItemQueriesPojo.QueryList();
                    mPojo.setItemId(response.getItemId());
                    mPojo.setItemName(response.getItemName());
                    mPojo.setUserId(tinyDB.getString(USER_ID));
                    mPojo.setVendorId(response.getVendorId());
                    mPojo.setUserName(tinyDB.getString(USER_NAME));
                    mPojo.setQueryDate(dateFormat.format(cal.getTime()));
                    mPojo.setQueryImage(response.getQueryImage());
                    mPojo.setQueryId(response.getQueryId());
                    boolean mSendUpdate = false;
                    if (TextUtils.isEmpty(response.getQuestion2()) && !TextUtils.isEmpty(response.getAnswer1())) {
                        mSendUpdate = true;
                        mFabSend.setEnabled(false);
                        mChatList.add(mChatMsg.getText().toString().trim());
                        mAdapter.updateAdapter(mChatList);
                        mPojo.setQuestion1(response.getQuestion1());
                        mPojo.setAnswer1(response.getAnswer1());
                        mPojo.setQuestion2(mChatMsg.getText().toString().trim());
                    } else if (TextUtils.isEmpty(response.getQuestion3()) && !TextUtils.isEmpty(response.getAnswer2())) {
                        mSendUpdate = true;
                        mFabSend.setEnabled(false);
                        mChatList.add(mChatMsg.getText().toString().trim());
                        mAdapter.updateAdapter(mChatList);
                        mPojo.setQuestion1(response.getQuestion1());
                        mPojo.setAnswer1(response.getAnswer1());
                        mPojo.setQuestion2(response.getQuestion2());
                        mPojo.setAnswer2(response.getAnswer2());
                        mPojo.setQuestion3(mChatMsg.getText().toString().trim());
                    } else if (TextUtils.isEmpty(response.getQuestion4()) && !TextUtils.isEmpty(response.getAnswer3())) {
                        mSendUpdate = true;
                        mFabSend.setEnabled(false);
                        mChatList.add(mChatMsg.getText().toString().trim());
                        mAdapter.updateAdapter(mChatList);
                        mPojo.setQuestion1(response.getQuestion1());
                        mPojo.setAnswer1(response.getAnswer1());
                        mPojo.setQuestion2(response.getQuestion2());
                        mPojo.setAnswer2(response.getAnswer2());
                        mPojo.setQuestion3(response.getQuestion3());
                        mPojo.setAnswer3(response.getAnswer3());
                        mPojo.setQuestion4(mChatMsg.getText().toString().trim());
                    }
                    mPojo.setUpdate("update");
                    if (mSendUpdate) {
                        Call<PostResponsePojo> call = apiService.PostItemQuery(mPojo);
                        call.enqueue(new Callback<PostResponsePojo>() {
                            @Override
                            public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                                PostResponsePojo pojo = response.body();
                                if (pojo != null)
                                    if (pojo.getStatus().equalsIgnoreCase("200")) {
                                        mChatMsg.setText("");
                                        Toasty.success(getActivity(), "Query sent Successfully", Toast.LENGTH_SHORT, true).show();
                                    } else {
                                        Toasty.error(getActivity(), pojo.getMessage(), Toast.LENGTH_SHORT, true).show();

                                    }
                            }

                            @Override
                            public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                            }
                        });
                    } else {
                        Toasty.error(getActivity(), "Please wait for the reply from vendor", Toasty.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(getActivity(), "Please enter message", Toasty.LENGTH_SHORT).show();
                }
            }
        });

    }
}
