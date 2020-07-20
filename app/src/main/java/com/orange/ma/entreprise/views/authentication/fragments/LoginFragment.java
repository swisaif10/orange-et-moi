package com.orange.ma.entreprise.views.authentication.fragments;

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

import com.orange.ma.entreprise.OrangeEtMoiPro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.models.login.LoginData;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.AuthenticationVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.main.MainActivity;

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
    @BindView(R.id.error_layout)
    RelativeLayout errorLayout;
    @BindView(R.id.error_description)
    TextView errorDescription;
    @BindView(R.id.background)
    ImageView background;
    private AuthenticationVM authenticationVM;
    private Connectivity connectivity;
    private PreferenceManager preferenceManager;
    private Bundle bundle;
    private Intent intent;

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

        //firebaseAnalyticsEvent
        OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(), "page_login", LocaleManager.getLanguagePref(getContext()));


    }

    private void handleIntent() {
        intent = getActivity().getIntent();
        if (intent.getExtras() != null) {
            if (!Utilities.isNullOrEmpty(intent.getStringExtra(Constants.ERROR_MESSAGE)) && intent.getIntExtra(Constants.ERROR_CODE, -1) != -1) {
                int code = intent.getIntExtra(Constants.ERROR_CODE, -1);
                String message = intent.getStringExtra(Constants.ERROR_MESSAGE);
                switch (code) {
                    case 403:
                        errorLayout.setVisibility(View.VISIBLE);
                        errorDescription.setText(message);
                        break;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        handleIntent();
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
                bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("btn_creer_compte", bundle);
                break;
            case R.id.valid_btn:

                bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                bundle.putString(Constants.FIREBASE_RC_KEY, preferenceManager.getValue(Constants.LOGIN_KEY, ""));
                OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("btn_login", bundle);

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
                break;
            case R.id.close_btn:
                errorLayout.setVisibility(View.GONE);
                break;
            case R.id.visitor_mode:
                ((AuthenticationActivity) getActivity()).replaceFragment(new VisitorFragment());

            case R.id.forgotten_pwd_btn:
                bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("bnt_forgotten_password", bundle);
                break;
            case R.id.gest_account_btn:
                bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("btn_changement_gestionnaire", bundle);
                break;
            default:
                break;

        }
    }

    private void init() {
        if (preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr").equalsIgnoreCase("ar"))
            background.setScaleX(-1);

        id.setText(preferenceManager.getValue(Constants.LOGIN_KEY, ""));
        password.setText(preferenceManager.getValue(Constants.PASS_KEY, ""));
        saveBtn.setChecked(preferenceManager.getValue(Constants.SAVE_CREDENTIALS_KEY, false));
        validBtn.setEnabled(id.getText().length() > 0 && password.getText().length() > 0);

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

        if (showPasswordBtn.isChecked())
            password.setTransformationMethod(null);
    }

    private void login() {
        if (connectivity.isConnected())
            authenticationVM.login(String.valueOf(id.getText()).trim(), String.valueOf(password.getText()).trim(), saveBtn.isChecked(), preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
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
                if (saveBtn.isChecked()) {
                    preferenceManager.putValue(Constants.IS_LOGGED_IN, true);
                    preferenceManager.putValue(Constants.LOGIN_KEY, id.getText().toString());
                    preferenceManager.putValue(Constants.PASS_KEY, password.getText().toString());
                    preferenceManager.putValue(Constants.SAVE_CREDENTIALS_KEY, true);
                } else {
                    preferenceManager.clearValue(Constants.LOGIN_KEY);
                    preferenceManager.clearValue(Constants.PASS_KEY);
                    preferenceManager.clearValue(Constants.SAVE_CREDENTIALS_KEY);
                }
                preferenceManager.putValue(Constants.TOKEN_KEY, "Bearer " + loginData.getResponse().getToken());
                preferenceManager.putValue(Constants.IS_AUTHENTICATED, true);

                intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("isCompleted", loginData.getResponse().getData().getUserInfos().isCompleted());
                startActivity(intent);
                getActivity().finish();
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
