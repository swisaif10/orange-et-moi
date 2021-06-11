package com.orange.ma.entreprise.views.enterNumber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.orange.ma.entreprise.OrangePro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.EncryptedSharedPreferences;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.models.listmsisdn.ListMsisdnData;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.ProductSearchAdapter;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.ConsultLigneVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.consult_line.ConsultLineFragment;
import com.orange.ma.entreprise.views.main.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

public class EnterNumberFragment extends Fragment {

    @BindView(R.id.numtel)
    AutoCompleteTextView numtel;
    @BindView(R.id.valid_btn)
    Button validBtn;
    @BindView(R.id.background)
    ImageView background;

    @BindView(R.id.back_btn)
    ImageView back_btn;

    @BindView(R.id.loader)
    GifImageView loader;

    @BindView(R.id.desc)
    TextView desc;

    @BindView(R.id.container)
    RelativeLayout container;
    private Connectivity connectivity;
    private PreferenceManager preferenceManager;
    private long lastClickTime = 0;
    private EncryptedSharedPreferences encryptedSharedPreferences;
    private ConsultLigneVM consultlineVM;
    private String index;


    public EnterNumberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        consultlineVM = ViewModelProviders.of(this).get(ConsultLigneVM.class);
        connectivity = new Connectivity(getContext(), this);

        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        encryptedSharedPreferences = new EncryptedSharedPreferences();


        encryptedSharedPreferences.getEncryptedSharedPreferences(getContext());

        consultlineVM.getListMsisdnMutableLiveData().observe(this, this::handleListMsisdndData);

        //  OrangePro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(), "page_dash_activated", LocaleManager.getLanguagePref(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_number, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loader.setVisibility(View.VISIBLE);

        validBtn.setEnabled(numtel.getText().toString().length() == 10);

        getMsisdnList();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).moveToDashboardFragment();

            }
        });
    }

    @OnClick({R.id.valid_btn, R.id.container})
    public void onViewClicked(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        switch (view.getId()) {
            case R.id.container:
                Utilities.hideSoftKeyboard(getContext(), getView());
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        numtel.setText("");
        validBtn.setEnabled(numtel.getText().toString().length() == 10);

            if (((MainActivity) getActivity()).getFragmentIndex(new EnterNumberFragment()) != -1)
            {
                ((MainActivity) getActivity()).showTab();
            }else {
                ((MainActivity) getActivity()).hideTab();

            }




        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    ((MainActivity) getActivity()).moveToDashboardFragment();

                    return true;
                }
                return false;
            }
        });



    }


    private void init(ListMsisdnData listMsisdnData) {

        desc.setText(listMsisdnData.getResponse().getStrings().getSelect_my_lines_description());
        validBtn.setText(listMsisdnData.getResponse().getStrings().getSelect_my_lines_label_button());
        numtel.setHint(listMsisdnData.getResponse().getStrings().getSelect_my_lines_placeholder());

        validBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(getContext(), getView());
                Fragment fragment = new ConsultLineFragment();
                Bundle bundle = new Bundle();
                bundle.putString("num", numtel.getText().toString());
                bundle.putString("csrf", listMsisdnData.getResponse().getCsrf_token());
                fragment.setArguments(bundle);
                ((MainActivity) getActivity()).switchFragment(fragment, "");
                Bundle bundles = new Bundle();
                bundles.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                bundles.putString("Msisdn", numtel.getText().toString());
                OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("btn_consult_ligne", bundles);
            }
        });

        if (listMsisdnData.getResponse().getData().size() == 1)
            numtel.setText(listMsisdnData.getResponse().getData().get(0).getLine());

        ProductSearchAdapter adapter = new ProductSearchAdapter(getActivity(), R.layout.number_item, new ArrayList<>(listMsisdnData.getResponse().getData()));
        numtel.setAdapter(adapter);

        numtel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validBtn.setEnabled(numtel.getText().toString().length() == 10);


            }
        });

        container.setVisibility(View.VISIBLE);
    }


    private void getMsisdnList() {
        if (connectivity.isConnected())
            consultlineVM.getListNum(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, ""));
        else
            Utilities.showErrorPopup(getContext(), getString(R.string.no_internet));
    }

    private void handleListMsisdndData(ListMsisdnData listMsisdnData) {
        loader.setVisibility(View.GONE);
        if (listMsisdnData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error));
        } else {
            int code = listMsisdnData.getHeader().getCode();
            switch (code) {
                case 200:

                    init(listMsisdnData);

                    break;
                case 403:
                    encryptedSharedPreferences.putValue(Constants.IS_LOGGED_IN, false);
                    encryptedSharedPreferences.clearValue(Constants.TOKEN_KEY);
                    encryptedSharedPreferences.putValue(Constants.IS_AUTHENTICATED, false);
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    intent.putExtra(Constants.ERROR_CODE, listMsisdnData.getHeader().getCode());
                    intent.putExtra(Constants.ERROR_MESSAGE, listMsisdnData.getHeader().getMessage());
                    startActivity(intent);
                    getActivity().finish();
                    break;
                default:
                    Utilities.showErrorPopup(getContext(), listMsisdnData.getHeader().getMessage());
            }

        }
    }


}
