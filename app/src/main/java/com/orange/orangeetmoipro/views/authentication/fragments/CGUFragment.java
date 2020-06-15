package com.orange.orangeetmoipro.views.authentication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.orange.orangeetmoipro.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CGUFragment extends Fragment {


    public CGUFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cgu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.accept_btn, R.id.refuse_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.accept_btn:
            case R.id.refuse_btn:
                getActivity().onBackPressed();
                break;
        }
    }
}