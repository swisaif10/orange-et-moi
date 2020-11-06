package com.orange.ma.entreprise.views.authentication.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
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

import com.orange.ma.entreprise.OrangePro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.EncryptedSharedPreferences;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.models.login.LoginData;
import com.orange.ma.entreprise.models.login.SettingTagData;
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
    private Intent intent;
    private long lastClickTime = 0;
    private EncryptedSharedPreferences encryptedSharedPreferences;


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

        encryptedSharedPreferences = new EncryptedSharedPreferences();

        encryptedSharedPreferences.getEncryptedSharedPreferences(getContext());

        authenticationVM.getLoginMutableLiveData().observe(this, this::handleLoginResponse);
        authenticationVM.getSettingTagMutableLiveData().observe(this, this::handleSettingTagAction);

        //firebaseAnalyticsEvent
        OrangePro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(), "page_login", LocaleManager.getLanguagePref(getContext()));


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

    @OnClick({R.id.signin_btn, R.id.valid_btn, R.id.container, R.id.close_btn, R.id.visitor_mode, R.id.forgotten_pwd_btn, R.id.gest_account_btn})
    public void onViewClicked(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        switch (view.getId()) {
            case R.id.signin_btn:
                ((AuthenticationActivity) getActivity()).replaceFragment(new SignInFragment());
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("btn_creer_compte", bundle);
                break;
            case R.id.valid_btn:

                bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                bundle.putString(Constants.FIREBASE_RC_KEY, encryptedSharedPreferences.getValue(Constants.LOGIN_KEY, ""));
                OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("btn_login", bundle);

                login();
                break;
            case R.id.container:
                Utilities.hideSoftKeyboard(getContext(), getView());
                break;
            case R.id.show_password_btn:
//                if (password.getTransformationMethod() instanceof PasswordTransformationMethod) {
//                    password.setTransformationMethod(null);
//                } else {
//                    password.setTransformationMethod(new PasswordTransformationMethod());
//                }
//                password.setSelection(password.getText().length());
                break;
            case R.id.close_btn:
                errorLayout.setVisibility(View.GONE);
                break;
            case R.id.visitor_mode:
                ((AuthenticationActivity) getActivity()).replaceFragment(new VisitorFragment());
                break;
            case R.id.forgotten_pwd_btn:
                bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("btn_forgotten_password", bundle);
                performTagAction(Constants.FORGOT_PASSWORD_TAG);
                break;
            case R.id.gest_account_btn:
                bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("btn_changement_gestionnaire", bundle);
                performTagAction(Constants.CHANGE_ACCOUNT_MANAGER_TAG);
                break;
            default:
                break;
        }
    }

    private void init() {
        if (preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr").equalsIgnoreCase("ar"))
            background.setScaleX(-1);

        id.setText(encryptedSharedPreferences.getValue(Constants.LOGIN_KEY, ""));
        password.setText(encryptedSharedPreferences.getValue(Constants.PASS_KEY, ""));
        saveBtn.setChecked(encryptedSharedPreferences.getValue(Constants.SAVE_CREDENTIALS_KEY, false));
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

        showPasswordBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                password.setTransformationMethod(!isChecked?new PasswordTransformationMethod():null);
                password.setSelection(password.getText().length());
            }
        });
    }

    private void login() {

        if (connectivity.isConnected())
            authenticationVM.login(String.valueOf(id.getText()).trim(), String.valueOf(password.getText()).trim(), saveBtn.isChecked(), preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), preferenceManager, encryptedSharedPreferences);
        else {
            errorLayout.setVisibility(View.VISIBLE);
            errorDescription.setText(getString(R.string.no_internet));
        }
    }

    private void performTagAction(String tag) {
        if (connectivity.isConnected())
            authenticationVM.performTagAction(tag, preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
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
                    encryptedSharedPreferences.putValue(Constants.IS_LOGGED_IN, true);
//                    preferenceManager.putValue(Constants.IS_LOGGED_IN, true);
                    encryptedSharedPreferences.putValue(Constants.LOGIN_KEY, id.getText().toString());
//                    preferenceManager.putValue(Constants.LOGIN_KEY, id.getText().toString());
                    encryptedSharedPreferences.putValue(Constants.PASS_KEY, password.getText().toString());
//                    preferenceManager.putValue(Constants.PASS_KEY, password.getText().toString());
                    encryptedSharedPreferences.putValue(Constants.SAVE_CREDENTIALS_KEY, true);
//                    preferenceManager.putValue(Constants.SAVE_CREDENTIALS_KEY, true);
                } else {
                    encryptedSharedPreferences.clearValue(Constants.LOGIN_KEY);
//                    preferenceManager.clearValue(Constants.LOGIN_KEY);
                    encryptedSharedPreferences.clearValue(Constants.PASS_KEY);
//                    preferenceManager.clearValue(Constants.PASS_KEY);
                    encryptedSharedPreferences.clearValue(Constants.SAVE_CREDENTIALS_KEY);
//                    preferenceManager.clearValue(Constants.SAVE_CREDENTIALS_KEY);
                }

                intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("isCompleted", loginData.getResponse().getData().getUserInfos().isCompleted());
                intent.putExtra("settingCompleteAccount", loginData.getResponse().getData().getSettingCompleteAccount());
                startActivity(intent);
                getActivity().finish();
            } else {
                errorLayout.setVisibility(View.VISIBLE);
                errorDescription.setText(loginData.getHeader().getMessage());
            }
        }
    }

    private void handleSettingTagAction(SettingTagData settingTagData) {
        if (settingTagData == null) {
            errorLayout.setVisibility(View.VISIBLE);
            errorDescription.setText(getString(R.string.generic_error));
        } else {
            int code = settingTagData.getHeader().getCode();
            switch (code) {
                case 200:
                    if (settingTagData.getResponse().getData().getActionType().equals("external")) {
                        String url = settingTagData.getResponse().getData().getAction();
                        if (!Utilities.isNullOrEmpty(encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, null)) && url.contains(Constants.EX_SSO_TOKEN)) {
                            String token = encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, "").replace("Bearer ", "");
                            url = url.replace(Constants.EX_SSO_TOKEN, token);
                        }
                        if (settingTagData.getResponse().getData().isInApp())
                            Utilities.openCustomTab(getContext(), url);
                        else
                            Utilities.openInBrowser(getContext(), url);
                    }
                    break;
                default:
                    errorLayout.setVisibility(View.VISIBLE);
                    errorDescription.setText(settingTagData.getHeader().getMessage());
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
