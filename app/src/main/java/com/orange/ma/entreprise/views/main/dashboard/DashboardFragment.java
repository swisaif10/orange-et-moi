package com.orange.ma.entreprise.views.main.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.orange.ma.entreprise.OrangePro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.EncryptedSharedPreferences;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.OnTemplateItemSelectedListener;
import com.orange.ma.entreprise.models.dashboard.CompoundElement;
import com.orange.ma.entreprise.models.dashboard.DashboardData;
import com.orange.ma.entreprise.models.dashboard.DashboardResponseData;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.StartSnapHelper;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.DashboardVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.enterNumber.EnterNumberFragment;
import com.orange.ma.entreprise.views.main.MainActivity;
import com.orange.ma.entreprise.views.main.adapters.DashboardAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

import static com.orange.ma.entreprise.utilities.Constants.DASH_TEMPLATE_HASH;

public class DashboardFragment extends BaseFragment implements OnTemplateItemSelectedListener {

    @BindView(R.id.dashboard_recycler)
    RecyclerView dashboardRecycler;
    @BindView(R.id.background1)
    ImageView background1;
    @BindView(R.id.background2)
    ImageView background2;
    @BindView(R.id.swipeRefresh)
    PullRefreshLayout swipeRefresh;
    @BindView(R.id.loader)
    GifImageView loader;
    @BindView(R.id.name)
    TextView businessName;
    @BindView(R.id.subtitle)
    TextView fidelity;
    @BindView(R.id.user_info_layout)
    RelativeLayout userInfoLayout;
    private Connectivity connectivity;
    private DashboardVM dashboardVM;
    private PreferenceManager preferenceManager;
    private Bundle bundle;
    private GridLayoutManager layoutManager;
    private StartSnapHelper snapHelper;
    private long lastClickTime = 0;
    private EncryptedSharedPreferences encryptedSharedPreferences;


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).showTab();

        dashboardVM = ViewModelProviders.of(this).get(DashboardVM.class);
        connectivity = new Connectivity(getContext(), this);

        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        encryptedSharedPreferences = new EncryptedSharedPreferences();

        encryptedSharedPreferences.getEncryptedSharedPreferences(getContext());

        dashboardVM.getDashboardMutableLiveData().observe(this, this::handleDashboardData);

        OrangePro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(), "page_dash_activated", LocaleManager.getLanguagePref(getContext()));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loader.setVisibility(View.VISIBLE);

        getDashboardList();

        layoutManager = new GridLayoutManager(getActivity(), 2);
        snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(dashboardRecycler);
        swipeRefresh.setOnRefreshListener(() -> {
            getDashboardList();
            swipeRefresh.setRefreshing(false);
            bundle = new Bundle();
            bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
            OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("page_dash_updated", bundle);

        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loader.setVisibility(View.VISIBLE);
            getDashboardList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showTab();

    }

    @Override
    public void onTemplateItemSelected(CompoundElement compoundElement) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        if (!compoundElement.getActionType().equalsIgnoreCase("none")) {
            if (compoundElement.getActionType().equalsIgnoreCase("internal")) {
                switch (compoundElement.getAction()) {
                    case "home":
                        getDashboardList();
                        break;
                    case "setting":
                        ((MainActivity) getActivity()).moveToSettingsFragment();
                        break;
                    case "consult_ligne":
                        if ( ((MainActivity) getActivity()).getFragmentIndex(new EnterNumberFragment()) != -1)
                        {
                            ((MainActivity) getActivity()).selectTab(((MainActivity) getActivity()).getFragmentIndex(new EnterNumberFragment()));
                        }else {
                            Fragment fragment = new EnterNumberFragment();
                            Bundle bundle= new Bundle();
                            bundle.putString("index",((MainActivity) getActivity()).getFragmentIndex(new EnterNumberFragment())+"");
                            fragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container,fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        break;
                }
            } else {
                String urlVebView = compoundElement.getAction();
                if (!Utilities.isNullOrEmpty(encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, null)) && urlVebView.contains(Constants.EX_SSO_TOKEN)) {
                    String token = encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, "").replace("Bearer ", "");
                    urlVebView = urlVebView.replace(Constants.EX_SSO_TOKEN, token);
                }
                if (compoundElement.getInApp())
                    Utilities.openCustomTab(getContext(), urlVebView);
                else
                    Utilities.openInBrowser(getContext(), urlVebView);
            }
        }
    }

    private void init(DashboardResponseData dashboardResponseData) {
        //((MainActivity) getActivity()).fragmentHistory.emptyStack();
        if (preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr").equalsIgnoreCase("ar")) {
            background1.setScaleX(-1);
            background2.setScaleX(-1);
        }

        businessName.setText(dashboardResponseData.getUserInfos().getBusinessName());
        fidelity.setText(Html.fromHtml(dashboardResponseData.getUserInfos().getStringFidelity()));


        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (dashboardResponseData.getTemplates().get(position).getSize() != null && dashboardResponseData.getTemplates().get(position).getSize().equalsIgnoreCase("small")) {
                    return 1;
                } else
                    return 2;
            }
        });

        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(getContext());
        dashboardRecycler.setLayoutManager(layoutManager);
        DashboardAdapter dashboardAdapter = new DashboardAdapter(getContext(), dashboardResponseData.getTemplates(), this::onTemplateItemSelected);
        dashboardAdapter.setListener((holder, position) -> {
            int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
            int first = layoutManager.findFirstVisibleItemPosition();
            int itemCount = layoutManager.getItemCount();
            View lastView = layoutManager.findViewByPosition(lastPosition);
            if (lastView != null && lastView.getTag().equals("blanc") && lastPosition == itemCount - 1) {
                linearSmoothScroller.setTargetPosition(lastPosition);
                layoutManager.startSmoothScroll(linearSmoothScroller);
            }
        });
        dashboardRecycler.setAdapter(dashboardAdapter);
        userInfoLayout.setVisibility(View.VISIBLE);

    }

    private void getDashboardList() {
        if (connectivity.isConnected())
            dashboardVM.getDashboardList(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, ""));
        else
            Utilities.showErrorPopup(getContext(), getString(R.string.no_internet));
    }

    private void handleDashboardData(DashboardData dashboardData) {
        loader.setVisibility(View.GONE);
        if (dashboardData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error));
        } else {
            int code = dashboardData.getHeader().getCode();
            switch (code) {
                case 200:
                    String hash = encryptedSharedPreferences.getValue(DASH_TEMPLATE_HASH, "");
                    //if ((Utilities.isNullOrEmpty(hash) || !hash.equals(dashboardData.getResponse().getHashTemplates()))) {
                    init(dashboardData.getResponse().getData());
                    encryptedSharedPreferences.putValue(DASH_TEMPLATE_HASH, dashboardData.getResponse().getHashTemplates());
                    //initRun = false;
                    //}
                    break;
                case 403:
                    encryptedSharedPreferences.putValue(Constants.IS_LOGGED_IN, false);
//                    preferenceManager.putValue(Constants.IS_LOGGED_IN, false);
                    encryptedSharedPreferences.clearValue(Constants.TOKEN_KEY);
//                    preferenceManager.clearValue(Constants.TOKEN_KEY);
                    encryptedSharedPreferences.putValue(Constants.IS_AUTHENTICATED, false);
//                    preferenceManager.putValue(Constants.IS_AUTHENTICATED, false);
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    intent.putExtra(Constants.ERROR_CODE, dashboardData.getHeader().getCode());
                    intent.putExtra(Constants.ERROR_MESSAGE, dashboardData.getHeader().getMessage());
                    startActivity(intent);
                    getActivity().finish();
                    break;
                default:
                    Utilities.showErrorPopup(getContext(), dashboardData.getHeader().getMessage());
            }
            ;
        }
    }

}