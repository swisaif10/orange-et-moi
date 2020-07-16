package com.orange.ma.entreprise.views.main.dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

import com.orange.ma.entreprise.OrangeEtMoiPro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.OnDashboardItemSelectedListener;
import com.orange.ma.entreprise.models.dashboard.CompoundElement;
import com.orange.ma.entreprise.models.dashboard.DashboardData;
import com.orange.ma.entreprise.models.dashboard.Template;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.DashboardVM;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.MainActivity;
import com.orange.ma.entreprise.views.main.adapters.DashboardAdapter;
import com.orange.ma.entreprise.views.main.webview.WebViewFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

public class DashboardFragment extends BaseFragment implements OnDashboardItemSelectedListener {

    @BindView(R.id.dashboard_recycler)
    RecyclerView dashboardRecycler;
    @BindView(R.id.background1)
    ImageView background1;
    @BindView(R.id.background2)
    ImageView background2;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.loader)
    GifImageView loader;
    private Connectivity connectivity;
    private DashboardVM dashboardVM;
    private PreferenceManager preferenceManager;
    private Bundle bundle;

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

        LinearSnapHelper snapHelper = new LinearSnapHelper();
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

        swipeRefresh.setOnRefreshListener(() -> {
            getDashboardList();
            swipeRefresh.setRefreshing(false);
            bundle = new Bundle();
            bundle.putString("Langue", LocaleManager.getLanguagePref(getContext()));
            OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("page_dash_updated", bundle);

        });
    }

    @Override
    public void onDashboardItemSelected(CompoundElement compoundElement) {

        if (!compoundElement.getActionType().equalsIgnoreCase("none")) {
            Fragment fragment;
            if (compoundElement.getActionType().equalsIgnoreCase("internal")) {
                switch (compoundElement.getAction()) {
                    case "home":
                        getDashboardList();
                        break;
                    case "parametres":
                        ((MainActivity) getActivity()).moveToSettingsFragment();
                        break;
                }
            } else if (compoundElement.getInApp()) {
                fragment = WebViewFragment.newInstance(compoundElement.getAction(), compoundElement.getElements().get(1).getValue());
                handleInApp(fragment);
            } else {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.black));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getContext(), Uri.parse(compoundElement.getAction()));
            }
        }
    }

    private void init(List<Template> arrayList) {
        if (preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr").equalsIgnoreCase("ar")) {
            background1.setScaleX(-1);
            background2.setScaleX(-1);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (arrayList.get(position).getSize().equalsIgnoreCase("small")) {
                    return 1;
                } else
                    return 2;
            }
        });

        dashboardRecycler.setLayoutManager(layoutManager);
        DashboardAdapter dashboardAdapter = new DashboardAdapter(getContext(), arrayList, this::onDashboardItemSelected);
        dashboardRecycler.setAdapter(dashboardAdapter);

    }

    private void getDashboardList() {
        if (connectivity.isConnected())
            dashboardVM.getDashboardList(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        else
            Utilities.showErrorPopup(getContext(), getString(R.string.no_internet));
    }

    private void handleDashboardData(DashboardData dashboardData) {
        loader.setVisibility(View.GONE);
        if (dashboardData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error));
        } else {
            int code = dashboardData.getHeader().getCode();
            if (code == 200) {
                init(dashboardData.getResponse().getData().getTemplates());
            } else
                Utilities.showErrorPopup(getContext(), dashboardData.getHeader().getMessage());
        }
    }

    void handleInApp(Fragment fragment){
        if (fragmentNavigation != null)
            fragmentNavigation.pushFragment(fragment);
    }
}