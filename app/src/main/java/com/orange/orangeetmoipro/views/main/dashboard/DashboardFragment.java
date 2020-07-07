package com.orange.orangeetmoipro.views.main.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.models.dashboard.DashboardData;
import com.orange.orangeetmoipro.models.dashboard.Template;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.DashboardVM;
import com.orange.orangeetmoipro.views.base.BaseFragment;
import com.orange.orangeetmoipro.views.main.adapters.DashboardAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

public class DashboardFragment extends BaseFragment {

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
        snapHelper.attachToRecyclerView(dashboardRecycler);

        swipeRefresh.setOnRefreshListener(() -> {
            getDashboardList();
            swipeRefresh.setRefreshing(false);
        });
    }

    private void init(ArrayList<Template> arrayList) {
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
        DashboardAdapter dashboardAdapter = new DashboardAdapter(getContext(), arrayList);
        dashboardRecycler.setAdapter(dashboardAdapter);

    }

    private void getDashboardList() {
        if (connectivity.isConnected())
            dashboardVM.getDashboardList(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        else
            Utilities.showErrorPopup(getContext(), getString(R.string.no_internet), "");
    }

    private void handleDashboardData(DashboardData dashboardData) {
        loader.setVisibility(View.GONE);
        if (dashboardData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error), "");
        } else {
            int code = dashboardData.getHeader().getCode();
            if (code == 200) {
                init(dashboardData.getResponse().getData().getTemplates());
            } else
                Utilities.showErrorPopup(getContext(), dashboardData.getHeader().getMessage(), "");
        }
    }


}