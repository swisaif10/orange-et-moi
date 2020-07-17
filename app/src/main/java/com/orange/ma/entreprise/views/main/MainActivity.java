package com.orange.ma.entreprise.views.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.orange.ma.entreprise.OrangeEtMoiPro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.OnDialogButtonsClickListener;
import com.orange.ma.entreprise.models.tabmenu.TabMenuData;
import com.orange.ma.entreprise.models.tabmenu.TabMenuItem;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.FragNavController;
import com.orange.ma.entreprise.utilities.FragmentHistory;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.MainVM;
import com.orange.ma.entreprise.views.base.BaseActivity;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.browser.BrowserFragment;
import com.orange.ma.entreprise.views.main.dashboard.DashboardFragment;
import com.orange.ma.entreprise.views.main.settings.SettingsFragment;
import com.orange.ma.entreprise.views.main.webview.WebViewFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;

    private MainVM mainVM;
    private Connectivity connectivity;
    private ArrayList<Fragment> fragments;
    private FragNavController fragNavController;
    private FragmentHistory fragmentHistory;
    private Bundle savedInstanceState;
    private PreferenceManager preferenceManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.savedInstanceState = savedInstanceState;

        mainVM = ViewModelProviders.of(this).get(MainVM.class);
        connectivity = new Connectivity(this, this);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        mainVM.getTabMenuMutableLiveData().observe(this, this::handleTabMenuResponse);

        getTabMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragNavController != null) {
            fragNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        if (!fragNavController.isRootFragment()) {
            fragNavController.popFragment();
        } else {
            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {
                if (fragmentHistory.getStackSize() > 1) {
                    int position = fragmentHistory.popPrevious();
                    tabLayout.getTabAt(position).setTag("bp");
                    tabLayout.getTabAt(position).select();
                } else {
                    tabLayout.getTabAt(0).select();
                    fragmentHistory.emptyStack();
                }
            }
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        return fragments.get(index);
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {

    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {

    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (fragNavController != null) {
            fragNavController.pushFragment(fragment);
        }
    }

    private void init(TabMenuData tabMenuData) {
        fragments = new ArrayList<>();
        for (TabMenuItem item : tabMenuData.getTabMenuResponse().getData()) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = tab.getCustomView() == null ? LayoutInflater.from(tabLayout.getContext()).inflate(R.layout.custom_tab, null) : tab.getCustomView();
            if (tab.getCustomView() == null) {
                tab.setCustomView(view);
            }
            TextView tabTextView = view.findViewById(R.id.tabTextView);
            tabTextView.setText(item.getTitle());
            tab.setText(item.getTitle());
            ImageView tabImageView = view.findViewById(R.id.tabImageView);
            int icon = getResources().getIdentifier(item.getIconDisabled(), "drawable", getPackageName());
            tabImageView.setImageDrawable(getDrawable(icon));
            tabLayout.addTab(tab);
            LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tabMenuData.getTabMenuResponse().getData().indexOf(item)));
            layout.setBackground(null);

            if (item.getActionType().equalsIgnoreCase("internal")) {
                switch (item.getAction()) {
                    case "home":
                        fragment = new DashboardFragment();
                        break;
                    case "setting":
                        fragment = new SettingsFragment();
                        break;
                    default:
                        fragment = new BrowserFragment();
                        break;
                }
            } else if (item.getInApp())
                fragment = WebViewFragment.newInstance(item.getAction(), item.getTitle());
            else
                fragment = BrowserFragment.newInstance(item.getAction());

            fragments.add(fragment);
        }

        fragmentHistory = new FragmentHistory();
        fragNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rootFragmentListener(this, fragments.size())
                .build();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (tab.getTag() == null || !tab.getTag().equals("bp"))
                    fragmentHistory.push(position);
                else
                    tab.setTag(null);
                switchTab(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                fragNavController.clearStack();
                switchTab(tab.getPosition());
            }
        });

        tabLayout.getTabAt(0).select();

        if (getIntent().getBooleanExtra("show_popup", false)) {
            OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(this, "Pop-up_completer_mon_profil", LocaleManager.getLanguagePref(this));

            Utilities.showCompleteProfileDialog(this, new OnDialogButtonsClickListener() {
                @Override
                public void firstChoice() {
                    //firebaseAnalyticsEvent
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getApplicationContext()));
                    bundle.putString(Constants.FIREBASE_RC_KEY, preferenceManager.getValue(Constants.LOGIN_KEY, ""));
                    OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("Clic_completer_mon_profil", bundle);
                }

                @Override
                public void secondChoice() {

                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getApplicationContext()));
                    bundle.putString(Constants.FIREBASE_RC_KEY, preferenceManager.getValue(Constants.LOGIN_KEY, ""));
                    OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("Clic_ignorer_completer_mon_profil", bundle);
                }
            });

            if (getIntent().getStringExtra("link") != null && getIntent().getStringExtra("link").equalsIgnoreCase("parametres"))
                moveToSettingsFragment();
            else
                tabLayout.getTabAt(0).select();

        }


    }

    private void switchTab(int position) {
        fragNavController.switchTab(position);
    }

    private void getTabMenu() {
        if (connectivity.isConnected()) {
            mainVM.getTabMenu(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), preferenceManager.getValue(Constants.TOKEN_KEY, ""));
        } else
            Utilities.showErrorPopup(this, getString(R.string.no_internet));

    }

    private void handleTabMenuResponse(TabMenuData tabMenuData) {
        if (tabMenuData == null) {
            Utilities.showErrorPopup(this, getString(R.string.generic_error));
        } else {
            int code = tabMenuData.getHeader().getCode();
            if (code == 200) {
                init(tabMenuData);
                handleIntent();
            } else
                Utilities.showErrorPopup(this, tabMenuData.getHeader().getMessage());
        }
    }

    private void handleIntent() {
        if (getIntent().getExtras() != null && getIntent().getStringExtra("endpoint")!=null) {
            if (getIntent().getStringExtra("endpointdata") != null) {
                handleInApp(getIntent().getStringExtra("endpoint"), getIntent().getStringExtra("endpointdata"));
            } else
                handleAppView(getIntent().getStringExtra("endpoint"));
        }
    }

    private void handleInApp(String endpoint, String endpointdata) {
        fragment = WebViewFragment.newInstance(endpoint, endpointdata);
        fragNavController.pushFragment(fragment);
    }

    private void handleAppView(String endpoint) {
        if (endpoint != null) {
            switch (endpoint) {
                case "home":
                    fragment = new DashboardFragment();
                    break;
                case "setting":
                    fragment = new SettingsFragment();
                    break;
                default:
                    fragment = new BrowserFragment();
                    break;
            }
            fragNavController.pushFragment(fragment);
        }
    }

    public void moveToSettingsFragment() {
        int index = -1;
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof SettingsFragment) {
                index = i;
                break;
            }
        }
        if (index != -1)
            tabLayout.getTabAt(index).select();
    }


}