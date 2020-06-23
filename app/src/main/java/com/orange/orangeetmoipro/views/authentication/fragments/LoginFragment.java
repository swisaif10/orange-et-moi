package com.orange.orangeetmoipro.views.authentication.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.models.login.LoginData;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.AuthenticationVM;
import com.orange.orangeetmoipro.views.authentication.AuthenticationActivity;
import com.orange.orangeetmoipro.views.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginFragment extends Fragment {

    @BindView(R.id.id)
    EditText id;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.show_password_btn)
    AppCompatCheckBox showPasswordBtn;
    @BindView(R.id.save_btn)
    AppCompatCheckBox saveBtn;
    @BindView(R.id.valid_btn)
    Button validBtn;
    @BindView(R.id.forgot_password_btn)
    TextView forgotPasswordBtn;
    @BindView(R.id.change_btn)
    TextView changeBtn;
    @BindView(R.id.error_layout)
    RelativeLayout errorLayout;
    @BindView(R.id.error_description)
    TextView errorDescription;
    @BindView(R.id.background)
    ImageView background;
    private AuthenticationVM authenticationVM;
    private Connectivity connectivity;
    private PreferenceManager preferenceManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationVM = ViewModelProviders.of(this).get(AuthenticationVM.class);
        connectivity = new Connectivity(getContext(), this);

        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        authenticationVM.getLoginMutableLiveData().observe(this, this::handleLoginResponse);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    @OnClick({R.id.signin_btn, R.id.valid_btn, R.id.container, R.id.show_password_btn, R.id.close_btn, R.id.visitor_mode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.signin_btn:
                ((AuthenticationActivity) getActivity()).replaceFragment(new SignInFragment());
                break;
            case R.id.valid_btn:
                login();
                break;
            case R.id.container:
                Utilities.hideSoftKeyboard(getContext(), getView());
                break;
            case R.id.show_password_btn:
                if (password.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    password.setTransformationMethod(null);
                } else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
                password.setSelection(password.getText().length());
            case R.id.close_btn:
                errorLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.visitor_mode:
                ((AuthenticationActivity) getActivity()).replaceFragment(new VisitorFragment());
            default:
                break;
        }
    }

    private void init() {
        if (preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr").equalsIgnoreCase("ar"))
            background.setScaleX(-1);

        updateFontFamily();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFontFamily();
            }

            @Override
            public void afterTextChanged(Editable s) {
                validBtn.setEnabled(id.getText().length() > 0 && password.getText().length() > 0);
            }
        };

        id.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }

    private void login() {
        if (connectivity.isConnected())
            authenticationVM.login(id.getText().toString().trim(), password.toString().trim(), "fr");
        else {
            errorLayout.setVisibility(View.VISIBLE);
            errorDescription.setText(getString(R.string.no_internet));
        }
    }

    private void handleLoginResponse(LoginData loginData) {
        if (loginData == null) {
            errorLayout.setVisibility(View.VISIBLE);
            errorDescription.setText(getString(R.string.generic_error));
        } else {
            int code = loginData.getHeader().getCode();
            if (code == 200) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                if (saveBtn.isChecked())
                    preferenceManager.putValue(Constants.IS_LOGGED_IN, true);
            } else {
                errorLayout.setVisibility(View.VISIBLE);
                errorDescription.setText(loginData.getHeader().getMessage());
            }
        }
    }

    private void updateFontFamily() {
        if (id.getText().length() > 0) {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticaneueregular);
            id.setTypeface(face);
        } else {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticalight);
            id.setTypeface(face);
        }

        if (password.getText().length() > 0) {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticaneueregular);
            password.setTypeface(face);
        } else {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticalight);
            password.setTypeface(face);
        }
    }

}
