package com.orange.orangeetmoipro.views.authentication.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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

public class SignInFragment extends Fragment {

    private static final int REQUEST_CODE = 100;

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
    CheckBox cguCheck;
    @BindView(R.id.container)
    ScrollView container;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.error_description)
    TextView errorDescription;
    @BindView(R.id.error_layout)
    RelativeLayout errorLayout;
    @BindView(R.id.security_level_layout)
    ConstraintLayout securityLevelLayout;
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.security_description)
    TextView securityDescription;
    @BindView(R.id.show_password_btn)
    AppCompatCheckBox showPasswordBtn;

    private Boolean checked = false;
    private PreferenceManager preferenceManager;
    private Connectivity connectivity;
    private AuthenticationVM authenticationVM;
    private Boolean from_cgu = false;

    public SignInFragment() {
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

        authenticationVM.getSignInMutableLiveData().observe(this, this::handleSignInResponse);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            checked = data.getBooleanExtra("cgu", false);
            from_cgu = true;
        }
    }

    @OnClick({R.id.login_btn, R.id.cgu_btn, R.id.valid_btn, R.id.constraintLayout, R.id.show_password_btn, R.id.close_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                from_cgu = false;
                ((AuthenticationActivity) getActivity()).replaceFragment(new LoginFragment());
                break;
            case R.id.cgu_btn:
                Utilities.hideSoftKeyboard(getContext(), getView());
                CGUFragment cguFragment = new CGUFragment();
                cguFragment.setTargetFragment(this, REQUEST_CODE);
                ((AuthenticationActivity) getActivity()).replaceFragment(cguFragment);
                break;
            case R.id.valid_btn:
                SignIn();
                break;
            case R.id.constraintLayout:
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
            default:
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        if (preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr").equalsIgnoreCase("ar"))
            background.setScaleX(-1);

        ViewTreeObserver observer = container.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(() -> {
            int viewHeight = container.getMeasuredHeight();
            int contentHeight = container.getChildAt(0).getHeight();
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            if (viewHeight - contentHeight < 0) {
                constraintSet.clear(R.id.footer, ConstraintSet.BOTTOM);
                constraintSet.connect(R.id.footer, ConstraintSet.TOP, R.id.credentials_layout, ConstraintSet.BOTTOM, 0);
            } else {
                constraintSet.clear(R.id.footer, ConstraintSet.TOP);
                constraintSet.connect(R.id.footer, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
            }
            constraintSet.applyTo(constraintLayout);
        });

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
                validateForm();
            }
        };

        id.addTextChangedListener(textWatcher);
        cin.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);

        if (!from_cgu) {
            id.getText().clear();
            cin.getText().clear();
            password.getText().clear();
            email.getText().clear();
        }

        email.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                checkEmail();
        });
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                container.smoothScrollTo(securityLevelLayout.getLeft(), securityLevelLayout.getTop());
        });
        cguCheck.setOnCheckedChangeListener((buttonView, isChecked) -> validateForm());
        cguCheck.setChecked(checked);

        if (showPasswordBtn.isChecked())
            password.setTransformationMethod(null);
    }

    private void validateForm() {
        if (password.getText().length() >= 8)
            calculatePasswordStrength(password.getText().toString());
        else if (password.getText().length() < 8) {
            securityLevel.setBackgroundColor(getResources().getColor(R.color.grey_light));
            securityDescription.setText(getString(R.string.security_level_description));
        }

        validBtn.setEnabled(id.getText().length() > 0 && cin.getText().length() > 0 && password.getText().length() >= 8 && cguCheck.isChecked() && checkEmail());
    }

    private boolean checkEmail() {

        if (email.getText().length() > 0) {
            if (Constants.EMAIL_ADDRESS_PATTERN.matcher(email.getText().toString()).matches()) {
                errorLayout.setVisibility(View.GONE);
                return true;
            } else {
                errorDescription.setText(getString(R.string.email_error));
                errorLayout.setVisibility(View.VISIBLE);
                return false;
            }
        } else {
            errorLayout.setVisibility(View.GONE);
            return true;
        }
    }

    private void calculatePasswordStrength(String str) {
        int passwordStrength = Utilities.calculate(str, "");
        switch (passwordStrength) {
            case 1:
                securityLevel.setBackgroundColor(getResources().getColor(R.color.red));
                securityDescription.setText(getString(R.string.weak_password_security));
                break;
            case 2:
                securityLevel.setBackgroundColor(getResources().getColor(R.color.medium_password));
                securityDescription.setText(getString(R.string.medium_password_security));
                break;
            default:
                securityLevel.setBackgroundColor(getResources().getColor(R.color.strong_password));
                securityDescription.setText(getString(R.string.strong_password_security));
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

        if (cin.getText().length() > 0) {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticaneueregular);
            cin.setTypeface(face);
        } else {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticalight);
            cin.setTypeface(face);
        }

        if (email.getText().length() > 0) {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticaneueregular);
            email.setTypeface(face);
            if (Constants.EMAIL_ADDRESS_PATTERN.matcher(email.getText().toString()).matches())
                errorLayout.setVisibility(View.GONE);
        } else {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticalight);
            email.setTypeface(face);
            errorLayout.setVisibility(View.GONE);
        }

        if (password.getText().length() > 0) {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticaneueregular);
            password.setTypeface(face);
        } else {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticalight);
            password.setTypeface(face);
        }
    }

    private void SignIn() {
        if (connectivity.isConnected())
            authenticationVM.signIn(id.getText().toString(), cin.getText().toString(), email.getText().toString(), password.getText().toString(), preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        else {
            errorLayout.setVisibility(View.VISIBLE);
            errorDescription.setText(getString(R.string.no_internet));
        }
    }

    private void handleSignInResponse(LoginData loginData) {
        if (loginData == null) {
            errorLayout.setVisibility(View.VISIBLE);
            errorDescription.setText(getString(R.string.generic_error));
        } else {
            int code = loginData.getHeader().getCode();
            if (code == 200) {
                preferenceManager.putValue(Constants.IS_LOGGED_IN, true);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("show_popup", true);
                startActivity(intent);
                getActivity().finish();
            } else {
                errorLayout.setVisibility(View.VISIBLE);
                errorDescription.setText(loginData.getHeader().getMessage());
            }
        }
    }

}
