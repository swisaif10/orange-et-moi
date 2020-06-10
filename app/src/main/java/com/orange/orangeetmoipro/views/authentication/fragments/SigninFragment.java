package com.orange.orangeetmoipro.views.authentication.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SigninFragment extends Fragment {

    @BindView(R.id.id)
    EditText id;
    @BindView(R.id.cin)
    EditText cin;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.security_level)
    View securityLevel;
    @BindView(R.id.valid_btn)
    Button validBtn;
    @BindView(R.id.cgu_check)
    AppCompatRadioButton cguCheck;

    public SigninFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }


    @OnClick({R.id.sign_up_btn, R.id.cgu_btn, R.id.valid_btn, R.id.credentials_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up_btn:
                getActivity().onBackPressed();
                break;
            case R.id.valid_btn:
                saveInformation();
                getActivity().onBackPressed();
                break;
            case R.id.credentials_layout:
                Utilities.hideSoftKeyboard(getContext(), getView());
                break;
        }
    }

    private void init() {
        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateForm();
            }
        });

        cin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateForm();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateForm();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateForm();
            }
        });

        cguCheck.setOnCheckedChangeListener((buttonView, isChecked) -> validateForm());
    }

    private void validateForm() {
        if (id.getText().length() > 0 && cin.getText().length() > 0 && password.getText().length() >= 8 && cguCheck.isChecked() && checkEmail())
            validBtn.setEnabled(true);
        else
            validBtn.setEnabled(false);
    }

    private boolean checkEmail() {
        if (email.getText().length() > 0)
            return Constants.EMAIL_ADDRESS_PATTERN.matcher(email.getText().toString()).matches();
        else
            return true;
    }

    private void saveInformation() {
    }

    @OnClick(R.id.credentials_layout)
    public void onViewClicked() {
    }
}
