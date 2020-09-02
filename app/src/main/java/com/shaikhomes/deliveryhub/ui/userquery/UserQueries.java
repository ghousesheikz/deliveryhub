package com.shaikhomes.deliveryhub.ui.userquery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaikhomes.deliveryhub.R;
import com.shaikhomes.deliveryhub.api_services.ApiClient;
import com.shaikhomes.deliveryhub.api_services.ApiInterface;
import com.shaikhomes.deliveryhub.pojo.PostResponsePojo;
import com.shaikhomes.deliveryhub.ui.BottomSheetView;
import com.shaikhomes.deliveryhub.ui.adminqueries.AdminQueries;
import com.shaikhomes.deliveryhub.ui.ordercalculation.ChatAdapter;
import com.shaikhomes.deliveryhub.ui.ordercalculation.ItemQueriesPojo;
import com.shaikhomes.deliveryhub.utility.RoundedTransformation;
import com.shaikhomes.deliveryhub.utility.TinyDB;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Html.fromHtml;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_ID;
import static com.shaikhomes.deliveryhub.utility.AppConstants.USER_NAME;

public class UserQueries extends Fragment {
    private RecyclerView mRecyclerview;
    QueriesAdapter mAdapter;
    TinyDB tinyDB;
    View view;
    private ApiInterface apiService;
    List<ItemQueriesPojo.QueryList> mQueryList;
    ChatAdapter mChatAdapter;
    EditText mChatMsg;
    FloatingActionButton mFabSend;
    BottomSheetView bottomSheetView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_user_queries, container, false);
        bottomSheetView = (BottomSheetView) view.getContext();
        bottomSheetView.BottomSheetDesignView("hide");
        tinyDB = new TinyDB(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        mQueryList = new ArrayList<>();
        mChatMsg = view.findViewById(R.id.edt_msg);
        mFabSend = view.findViewById(R.id.btn_send);
        ImageView mCloseBtn = view.findViewById(R.id.close_dialog);
        ImageView mCallBtn = view.findViewById(R.id.call_dialog);
        mCloseBtn.setOnClickListener(v -> {


        });
        mCallBtn.setOnClickListener(v -> {

        });
        mRecyclerview = view.findViewById(R.id.chat_msg_list);
        mChatAdapter = new ChatAdapter(getActivity(), mQueryList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mChatAdapter);
        mFabSend.setOnClickListener(v -> {
            postQueryData(mQueryList.get(mQueryList.size() - 1));
        });
        //getQueryData();
        mRecyclerview = view.findViewById(R.id.queries_list);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new QueriesAdapter(getActivity(),"0", mQueryList, new QueriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemQueriesPojo.QueryList response, int position) {
                getUserQueryData(response,position);
            }
        });
        mRecyclerview.setAdapter(mAdapter);
      //  getQueryData();
        return view;
    }

    private void postQueryData(ItemQueriesPojo.QueryList response) {
        if (!TextUtils.isEmpty(mChatMsg.getText().toString().trim())) {
            if (response != null) {

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
                mPojo.setDescription(mChatMsg.getText().toString().trim());
                mPojo.setType("Q");
                Call<PostResponsePojo> call = apiService.PostItemQuery(mPojo);
                call.enqueue(new Callback<PostResponsePojo>() {
                    @Override
                    public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> response) {
                        PostResponsePojo pojo = response.body();
                        if (pojo != null)
                            if (pojo.getStatus().equalsIgnoreCase("200")) {
                                mChatMsg.setText("");
                                mQueryList.add(mPojo);
                                mChatAdapter.updateAdapter(mQueryList);
                                mRecyclerview.scrollToPosition(mChatAdapter.getlist().size() - 1);
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
                Toasty.error(getActivity(), "Please enter message", Toasty.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getQueryData();
    }

    private void getQueryData() {
        try {
            Call<ItemQueriesPojo> call = apiService.getQueries("", tinyDB.getString(USER_ID),"user","");
            call.enqueue(new Callback<ItemQueriesPojo>() {
                @Override
                public void onResponse(Call<ItemQueriesPojo> call, Response<ItemQueriesPojo> response) {
                    ItemQueriesPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {
                        mQueryList = response.body().getQueryList();
                        mAdapter.updateAdapter(response.body().getQueryList());
                       // mRecyclerview.scrollToPosition(mChatAdapter.getlist().size() - 1);
                        //   chatDialog(response.body().getQueryList().get(0),1);
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
    private void getUserQueryData(ItemQueriesPojo.QueryList data, int position) {
        try {
            Call<ItemQueriesPojo> call = apiService.getQueries("", tinyDB.getString(USER_ID),"1",data.getItemId());
            call.enqueue(new Callback<ItemQueriesPojo>() {
                @Override
                public void onResponse(Call<ItemQueriesPojo> call, Response<ItemQueriesPojo> response) {
                    ItemQueriesPojo mPojo = response.body();
                    if (mPojo.getStatus().equalsIgnoreCase("200")) {

                        chatDialog(response.body().getQueryList(), position);
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

    private void chatDialog(List<ItemQueriesPojo.QueryList> response, int position) {
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
        ChatAdapter mAdapter = new ChatAdapter(getActivity(), response);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.scrollToPosition(mAdapter.getlist().size() - 1);
        mFabSend.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mChatMsg.getText().toString().trim())) {

                int pos = response.size() - 1;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();

                ItemQueriesPojo.QueryList mPojo = new ItemQueriesPojo.QueryList();
                mPojo.setItemId(response.get(pos).getItemId());
                mPojo.setItemName(response.get(pos).getItemName());
                mPojo.setUserId( tinyDB.getString(USER_ID));
                mPojo.setVendorId(response.get(pos).getVendorId());
                mPojo.setUserName( tinyDB.getString(USER_NAME));
                mPojo.setQueryDate(dateFormat.format(cal.getTime()));
                mPojo.setQueryImage(response.get(pos).getQueryImage());
                mPojo.setDescription(mChatMsg.getText().toString().trim());
                mPojo.setType("Q");


                Call<PostResponsePojo> call = apiService.PostItemQuery(mPojo);
                call.enqueue(new Callback<PostResponsePojo>() {
                    @Override
                    public void onResponse(Call<PostResponsePojo> call, Response<PostResponsePojo> latresponse) {
                        PostResponsePojo pojo = latresponse.body();
                        if (pojo != null)
                            if (pojo.getStatus().equalsIgnoreCase("200")) {
                                mChatMsg.setText("");
                                response.add(mPojo);
                                mAdapter.updateAdapter(response);
                                mRecyclerview.scrollToPosition(mAdapter.getlist().size() - 1);
                                Toasty.success(getActivity(), "Query sent Successfully", Toast.LENGTH_SHORT, true).show();
                            } else {
                                Toasty.error(getActivity(), pojo.getMessage(), Toast.LENGTH_SHORT, true).show();

                            }
                    }

                    @Override
                    public void onFailure(Call<PostResponsePojo> call, Throwable t) {
                    }
                });
            }
        });

    }

  /*  private void chatDialog(ItemQueriesPojo.QueryList response, int position) {
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

    }*/
}
