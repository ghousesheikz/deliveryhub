package com.shaikhomes.watercan.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaikhomes.watercan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCan extends Fragment {
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearLayoutmanager;
    private OrderCanAdapter mAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderCan() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrderCan newInstance(String param1, String param2) {
        OrderCan fragment = new OrderCan();
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

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_can, container, false);
        mRecyclerview = view.findViewById(R.id.order_can_list);
        mLinearLayoutmanager = new LinearLayoutManager(getActivity());
        mLinearLayoutmanager.setReverseLayout(true);
        mLinearLayoutmanager.setStackFromEnd(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        List<String> mList = new ArrayList<>();
        mAdapter = new OrderCanAdapter(getActivity(), mList, new OrderCanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String response, int position) {

            }
        });
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        // Inflate the layout for this fragment
        return view;
    }
}
