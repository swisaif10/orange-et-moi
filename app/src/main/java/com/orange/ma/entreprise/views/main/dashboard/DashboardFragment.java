package com.orange.ma.entreprise.views.main.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.orange.ma.entreprise.OrangeEtMoiPro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.OnTemplateItemSelectedListener;
import com.orange.ma.entreprise.models.dashboard.CompoundElement;
import com.orange.ma.entreprise.models.dashboard.DashboardData;
import com.orange.ma.entreprise.models.dashboard.DashboardResponseData;
import com.orange.ma.entreprise.models.dashboard.Template;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.DashboardVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.MainActivity;
import com.orange.ma.entreprise.views.main.adapters.DashboardAdapter;
import com.orange.ma.entreprise.views.main.webview.WebViewFragment;

import java.util.ArrayList;

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
    private boolean initRun = true;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dashboardVM = ViewModelProviders.of(this).get(DashboardVM.class);
        connectivity = new Connectivity(getContext(), this);

        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        dashboardVM.getDashboardMutableLiveData().observe(this, this::handleDashboardData);

        OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(), "page_dash_activated", LocaleManager.getLanguagePref(getContext()));


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

        StartSnapHelper snapHelper;
            /*@Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;


                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));


                int activePosition = layoutManager.findFirstCompletelyVisibleItemPosition();

                return targetPosition;
            }
        };*/
        //snapHelper.attachToRecyclerView(dashboardRecycler);

        snapHelper  = new StartSnapHelper();
        snapHelper.attachToRecyclerView(dashboardRecycler);
        swipeRefresh.setOnRefreshListener(() -> {
            getDashboardList();
            swipeRefresh.setRefreshing(false);
            bundle = new Bundle();
            bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
            OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("page_dash_updated", bundle);

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
    public void onTemplateItemSelected(CompoundElement compoundElement) {

        if (!compoundElement.getActionType().equalsIgnoreCase("none")) {
            Fragment fragment;
            if (compoundElement.getActionType().equalsIgnoreCase("internal")) {
                switch (compoundElement.getAction()) {
                    case "home":
                        getDashboardList();
                        break;
                    case "setting":
                        ((MainActivity) getActivity()).moveToSettingsFragment();
                        break;
                }
            } else if (compoundElement.getInApp()) {
//                fragment = WebViewFragment.newInstance(compoundElement.getAction(), compoundElement.getElements().get(1).getValue());
//                if (fragmentNavigation != null)
//                    fragmentNavigation.pushFragment(fragment);

                String urlVebView = compoundElement.getAction();
                if (!Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, null))&&urlVebView.contains(Constants.EX_SSO_TOKEN)) {
                    String token = preferenceManager.getValue(Constants.TOKEN_KEY, "").replace("Bearer ","");
                    urlVebView = urlVebView.replace(Constants.EX_SSO_TOKEN,token);
                }
                Utilities.openCustomTab(getContext(),urlVebView);

            } else {
                String urlVebView = compoundElement.getAction();
                if (!Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, null))&&urlVebView.contains(Constants.EX_SSO_TOKEN)) {
                    String token = preferenceManager.getValue(Constants.TOKEN_KEY, "").replace("Bearer ","");
                    urlVebView = urlVebView.replace(Constants.EX_SSO_TOKEN,token);
                }
                Utilities.openCustomTab(getContext(),urlVebView);
            }
        }
    }

    private void init(DashboardResponseData dashboardResponseData) {
        ((MainActivity) getActivity()).fragmentHistory.emptyStack();
        if (preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr").equalsIgnoreCase("ar")) {
            background1.setScaleX(-1);
            background2.setScaleX(-1);
        }

        businessName.setText(dashboardResponseData.getUserInfos().getBusinessName());
        fidelity.setText(Html.fromHtml(dashboardResponseData.getUserInfos().getStringFidelity()));

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (dashboardResponseData.getTemplates().get(position).getSize().equalsIgnoreCase("small")) {
                    return 1;
                } else
                    return 2;
            }
        });

        dashboardRecycler.setLayoutManager(layoutManager);
        DashboardAdapter dashboardAdapter = new DashboardAdapter(getContext(), dashboardResponseData.getTemplates(), this::onTemplateItemSelected);
        dashboardRecycler.setAdapter(dashboardAdapter);
        userInfoLayout.setVisibility(View.VISIBLE);

    }

    private void getDashboardList() {
        if (connectivity.isConnected())
            dashboardVM.getDashboardList(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), preferenceManager.getValue(Constants.TOKEN_KEY, ""));
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
                    String hash = preferenceManager.getValue(DASH_TEMPLATE_HASH,"");
                    if((Utilities.isNullOrEmpty(hash)||!hash.equals(dashboardData.getResponse().getHashTemplates()))|initRun){
                        init(dashboardData.getResponse().getData());
                        preferenceManager.putValue(DASH_TEMPLATE_HASH,dashboardData.getResponse().getHashTemplates());
                        initRun = false;
                    }
                    break;
                case 403:
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