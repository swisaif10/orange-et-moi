package com.orange.ma.entreprise.views.consult_line;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.ma.entreprise.OrangePro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.EncryptedSharedPreferences;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.models.consult.ConsultData;
import com.orange.ma.entreprise.models.listmsisdn.ListMsisdnData;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.ConsultLigneVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.consult_line.adapters.SoldeAdapter;
import com.orange.ma.entreprise.views.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

public class ConsultLineFragment extends Fragment {

    @BindView(R.id.loader)
    GifImageView loader;

    @BindView(R.id.num_ligne_p)
    TextView num_ligne_p;

    @BindView(R.id.num_ligne_)
    TextView num_ligne_;

    @BindView(R.id.forfait)
    TextView forfait;

    @BindView(R.id.status_p)
    TextView status_p;

    @BindView(R.id.status)
    TextView status;

    @BindView(R.id.code_p)
    TextView code_p;

    @BindView(R.id.code)
    TextView code;

    @BindView(R.id.share)
    ImageView share;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.back_btn)
    ImageView back_btn;

    @BindView(R.id.container)
    RelativeLayout container;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.status_img)
    ImageView status_img;
    private Connectivity connectivity;
    private PreferenceManager preferenceManager;
    private long lastClickTime = 0;
    private EncryptedSharedPreferences encryptedSharedPreferences;
    private ConsultLigneVM consultlineVM;
    private String num;
    private SoldeAdapter amountAdapter;
    private String csrf;


    public ConsultLineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).hideTab();

        if (getArguments() != null) {
            num = getArguments().getString("num");
            csrf = getArguments().getString("csrf");
        }
        consultlineVM = ViewModelProviders.of(this).get(ConsultLigneVM.class);
        connectivity = new Connectivity(getContext(), this);

        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        encryptedSharedPreferences = new EncryptedSharedPreferences();

        encryptedSharedPreferences.getEncryptedSharedPreferences(getContext());

        consultlineVM.getConsultMutableLiveData().observe(this, this::handleConsultData);

        //  OrangePro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(), "page_dash_activated", LocaleManager.getLanguagePref(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consult_line, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loader.setVisibility(View.VISIBLE);



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        getConsultDeatil();
    }

    @OnClick({ R.id.container})
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
    public void onPause() {
        super.onPause();
    }

    private void init(ConsultData consultData) {

        code.setText(consultData.getResponse().getData().getCode_puk());
        code_p.setText(consultData.getResponse().getStrings().getLabel_code_puk());

        status.setText(consultData.getResponse().getData().getStatus());
        status_p.setText(consultData.getResponse().getStrings().getLabel_status_line());

        date.setText(consultData.getResponse().getStrings().getLast_update_balance());
        num_ligne_.setText(num);
        num_ligne_p.setText(consultData.getResponse().getStrings().getNum_line());
        forfait.setText(consultData.getResponse().getData().getProfile_name());

        status_img.setColorFilter(Color.parseColor(consultData.getResponse().getData().getLabel_color_status()), android.graphics.PorterDuff.Mode.MULTIPLY);


        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        amountAdapter = new SoldeAdapter(getActivity(), consultData.getResponse().getData().getBalance().getLine_balance());
        rv.setAdapter(amountAdapter);


        container.setVisibility(View.VISIBLE);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundles = new Bundle();
                bundles.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                bundles.putString("Msisdn", num);
                bundles.putString("PUK", consultData.getResponse().getData().getCode_puk());
                OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("btn_share_puk", bundles);
                shareContent(getActivity(),getString(R.string.recuperer_code_puk_share, consultData.getResponse().getData().getCode_puk()))   ;
            }
        });

    }


    private void getConsultDeatil() {
        if (connectivity.isConnected())
            consultlineVM.getConsultDetai(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, ""),num,csrf);
        else
            Utilities.showErrorPopupWithClickListener(getContext(), getString(R.string.no_internet),new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
    }

    private void handleConsultData(ConsultData listMsisdnData) {
        loader.setVisibility(View.GONE);

        Bundle bundles = new Bundle();
        bundles.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
        bundles.putString("Msisdn", num);
        bundles.putString("Plantarrifaire", listMsisdnData.getResponse().getData().getProfile_name());
        bundles.putString("Status", listMsisdnData.getResponse().getData().getStatus());
        OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("detail_ligne", bundles);
        OrangePro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(), "detail_ligne", LocaleManager.getLanguagePref(getContext()));

        if (listMsisdnData == null) {
            Utilities.showErrorPopupWithClickListener(getContext(), getString(R.string.generic_error),new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

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
                    Utilities.showErrorPopupWithClickListener(getContext(), listMsisdnData.getHeader().getMessage(), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
            }

        }
    }

    public static void shareContent(Context mContext, String txt) {
        Log.d("Utils", String.format("Sharing content: %s", txt));
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, txt);
        mContext.startActivity(Intent.createChooser(shareIntent, mContext.getResources().getString(R.string.text_share)));
    }


}
