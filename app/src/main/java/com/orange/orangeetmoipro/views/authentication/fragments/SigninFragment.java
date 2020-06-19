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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.PasswordStrength;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.views.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SigninFragment extends Fragment {

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
    private Boolean checked = false;
    private PreferenceManager preferenceManager;

    public SigninFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
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
        }
    }

    @OnClick({R.id.sign_up_btn, R.id.cgu_btn, R.id.valid_btn, R.id.credentials_layout, R.id.show_password_btn, R.id.close_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up_btn:
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, new LoginFragment()).
                        addToBackStack(null).
                        commit();
                break;
            case R.id.cgu_btn:
                Utilities.hideSoftKeyboard(getContext(), getView());
                CGUFragment cguFragment = new CGUFragment();
                cguFragment.setTargetFragment(this, REQUEST_CODE);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, cguFragment).
                        addToBackStack(null).
                        commit();
                break;
            case R.id.valid_btn:
                saveInformation();
                break;
            case R.id.credentials_layout:
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
                errorLayout.setVisibility(View.INVISIBLE);
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
                constraintSet.clear(R.id.valid_btn, ConstraintSet.BOTTOM);
                constraintSet.connect(R.id.valid_btn, ConstraintSet.TOP, R.id.credentials_layout, ConstraintSet.BOTTOM, 0);
            } else {
                constraintSet.clear(R.id.valid_btn, ConstraintSet.TOP);
                constraintSet.connect(R.id.valid_btn, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
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
        preferenceManager.putValue(Constants.IS_LOGGED_IN, true);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("show_popup", true);
        startActivity(intent);
        getActivity().finish();
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
                errorLayout.setVisibility(View.INVISIBLE);
        } else {
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.helveticalight);
            email.setTypeface(face);
            errorLayout.setVisibility(View.INVISIBLE);
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
