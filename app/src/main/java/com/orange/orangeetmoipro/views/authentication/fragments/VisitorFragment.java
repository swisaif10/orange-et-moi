package com.orange.orangeetmoipro.views.authentication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.orange.orangeetmoipro.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VisitorFragment extends Fragment {


    public VisitorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visitor, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.back_btn, R.id.billing_btn, R.id.account_manger_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                getActivity().onBackPressed();
                break;
            case R.id.billing_btn:
                break;
            case R.id.account_manger_btn:
                break;
            default:
                break;
        }
    }
}