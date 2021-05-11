package com.orange.ma.entreprise.views.enterNumber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.EncryptedSharedPreferences;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.models.listmsisdn.ListMsisdnData;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.ConsultLigneVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.MainActivity;

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


    public EnterNumberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).hideTab();

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

        getMsisdnList();
    }

    @OnClick({R.id.valid_btn, R.id.container})
    public void onViewClicked(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        switch (view.getId()) {
            case R.id.valid_btn:

                break;
            case R.id.container:
                Utilities.hideSoftKeyboard(getContext(), getView());
                break;
            default:
                break;
        }
    }

    private void init(ListMsisdnData listMsisdnData) {

        desc.setText(listMsisdnData.getResponse().getStrings().getSelect_my_lines_description());
        validBtn.setText(listMsisdnData.getResponse().getStrings().getSelect_my_lines_label_button());
        numtel.setHint(listMsisdnData.getResponse().getStrings().getSelect_my_lines_placeholder());

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
