package com.orange.orangeetmoipro.views.authentication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.listeners.VersionControlChoiceDialogClickListener;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.PasswordStrength;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.views.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SigninFragment extends Fragment {

    @BindView(R.id.id)
    EditText id;
    @BindView(R.id.cin)
    EditText cin;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.security_level)
    View securityLevel;
    @BindView(R.id.valid_btn)
    Button validBtn;
    @BindView(R.id.cgu_check)
    AppCompatCheckBox cguCheck;
    @BindView(R.id.container)
    ScrollView container;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.error_description)
    TextView errorDescription;
    @BindView(R.id.error_layout)
    RelativeLayout errorLayout;

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
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, new LoginFragment()).
                        addToBackStack(null).
                        commit();
                break;
            case R.id.cgu_btn:
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, new CGUFragment()).
                        addToBackStack(null).
                        commit();
                break;
            case R.id.valid_btn:
                saveInformation();
                break;
            case R.id.credentials_layout:
                Utilities.hideSoftKeyboard(getContext(), getView());
                break;
            default:
        }
    }

    private void init() {
        ViewTreeObserver observer = container.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(() -> {
            int viewHeight = container.getMeasuredHeight();
            int contentHeight = container.getChildAt(0).getHeight();
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            if (viewHeight - contentHeight < 0) {
                constraintSet.clear(R.id.valid_btn, ConstraintSet.BOTTOM);
                constraintSet.connect(R.id.valid_btn, ConstraintSet.TOP, R.id.credentials_layout, ConstraintSet.BOTTOM, 0);
            } else {
                constraintSet.clear(R.id.valid_btn, ConstraintSet.TOP);
                constraintSet.connect(R.id.valid_btn, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
            }
            constraintSet.applyTo(constraintLayout);
        });

        TextWatcher textWatcher = new TextWatcher() {
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
        };

        id.addTextChangedListener(textWatcher);
        cin.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);

        cguCheck.setOnCheckedChangeListener((buttonView, isChecked) -> validateForm());
    }

    private void validateForm() {
        if (password.getText().length() >= 8)
            calculatePasswordStrength(password.getText().toString());
        else if (password.getText().length() < 8)
            securityLevel.setBackgroundColor(getResources().getColor(R.color.grey_light));

        validBtn.setEnabled(id.getText().length() > 0 && cin.getText().length() > 0 && password.getText().length() >= 8 && cguCheck.isChecked() && checkEmail());
    }

    private boolean checkEmail() {
        if (email.getText().length() > 0) {
            if (Constants.EMAIL_ADDRESS_PATTERN.matcher(email.getText().toString()).matches()) {
                errorLayout.setVisibility(View.INVISIBLE);
                return true;
            } else {
                errorDescription.setText(getString(R.string.email_error));
                errorLayout.setVisibility(View.VISIBLE);
                return false;
            }
        } else {
            errorLayout.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private void saveInformation() {
        Utilities.showCompleteProfileDialog(getContext(), "", "", new VersionControlChoiceDialogClickListener() {
            @Override
            public void onAccept() {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }

            @Override
            public void onRefuse() {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
    }

    private void calculatePasswordStrength(String str) {
        int passwordStrength = PasswordStrength.calculate(str);
        switch (passwordStrength) {
            case 1:
                securityLevel.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case 2:
                securityLevel.setBackgroundColor(getResources().getColor(R.color.medium_password));
                break;
            default:
                securityLevel.setBackgroundColor(getResources().getColor(R.color.strong_password));
        }
    }

}
