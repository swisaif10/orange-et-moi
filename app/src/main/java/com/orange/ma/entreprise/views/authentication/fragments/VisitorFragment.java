package com.orange.ma.entreprise.views.authentication.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.orange.ma.entreprise.OrangeEtMoiPro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.OnTemplateItemSelectedListener;
import com.orange.ma.entreprise.models.dashboard.CompoundElement;
import com.orange.ma.entreprise.models.dashboard.Template;
import com.orange.ma.entreprise.models.guest.GuestLoginData;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.AuthenticationVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.main.MainActivity;
import com.orange.ma.entreprise.views.main.adapters.DashboardAdapter;
import com.orange.ma.entreprise.views.main.webview.WebViewFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

import static com.orange.ma.entreprise.utilities.Constants.DASH_TEMPLATE_HASH;
import static com.orange.ma.entreprise.utilities.Constants.VISITOR_TEMPLATE_HASH;

public class VisitorFragment extends Fragment implements OnTemplateItemSelectedListener {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.loader)
    GifImageView loader;
    @BindView(R.id.swipeRefresh)
    PullRefreshLayout swipeRefresh;
    private PreferenceManager preferenceManager;
    private Connectivity connectivity;
    private AuthenticationVM authenticationVM;

    public VisitorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authenticationVM = ViewModelProviders.of(this).get(AuthenticationVM.class);
        connectivity = new Connectivity(getContext(), this);

        authenticationVM.getGuestLoginMutableLiveData().observe(this, this::handleGuestLoginResponse);

        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(), "page_guest", LocaleManager.getLanguagePref(getContext()));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest, container, false);
        ButterKnife.bind(this, view);
        swipeRefresh.setOnRefreshListener(()->{
            if(connectivity.isConnected()){
                swipeRefresh.setRefreshing(true);
                authenticationVM.guestLogin(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        guestLogin();
    }

    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void onTemplateItemSelected(CompoundElement compoundElement) {
        if (!compoundElement.getActionType().equalsIgnoreCase("none")) {
            Fragment fragment;
            if (compoundElement.getActionType().equalsIgnoreCase("internal")) {
                ((AuthenticationActivity)getActivity()).onBackPressed();
            } else if (compoundElement.getInApp()) {
                fragment = WebViewFragment.newInstance(compoundElement.getAction(), compoundElement.getElements().get(1).getValue());
                ((AuthenticationActivity)getActivity()).addFragment(fragment,"inapp");
            } else {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.black));
                CustomTabsIntent customTabsIntent = builder.build();
                String urlVebView = compoundElement.getAction();
                if (!Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, null))&&urlVebView.contains(Constants.EX_SSO_TOKEN)) {
                    String token = preferenceManager.getValue(Constants.TOKEN_KEY, "").replace("Bearer ","");
                    urlVebView = urlVebView.replace(Constants.EX_SSO_TOKEN,token);
                }
                customTabsIntent.launchUrl(getContext(), Uri.parse(urlVebView));
            }
        }
    }

    private void init(List<Template> templates) {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (templates.get(position).getSize().equalsIgnoreCase("small")) {
                    return 1;
                } else
                    return 2;
            }
        });

        recycler.setLayoutManager(layoutManager);
        DashboardAdapter dashboardAdapter = new DashboardAdapter(getContext(), templates, this::onTemplateItemSelected);
        recycler.setAdapter(dashboardAdapter);
    }

    private void guestLogin() {
        if (connectivity.isConnected()) {
            loader.setVisibility(View.VISIBLE);
            authenticationVM.guestLogin(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        }
    }

    private void handleGuestLoginResponse(GuestLoginData guestLoginData) {
        swipeRefresh.setRefreshing(false);
        loader.setVisibility(View.GONE);
        if (guestLoginData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error));
        } else {
            int code = guestLoginData.getHeader().getCode();
            if (code == 200) {
//                String hash = preferenceManager.getValue(VISITOR_TEMPLATE_HASH,"");
//                if(Utilities.isNullOrEmpty(hash)||!hash.equals(guestLoginData.getResponse().getHashTemplates())){
                    init(guestLoginData.getResponse().getData().getTemplates());
//                    preferenceManager.putValue(VISITOR_TEMPLATE_HASH,guestLoginData.getResponse().getHashTemplates());
//                }
            } else
                Utilities.showErrorPopup(getContext(), guestLoginData.getHeader().getMessage());
        }
    }

}