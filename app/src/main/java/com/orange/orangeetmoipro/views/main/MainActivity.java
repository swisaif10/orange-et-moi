package com.orange.orangeetmoipro.views.main;

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
import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.listeners.DialogButtonsClickListener;
import com.orange.orangeetmoipro.models.tabmenu.TabMenuData;
import com.orange.orangeetmoipro.models.tabmenu.TabMenuItem;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.FragNavController;
import com.orange.orangeetmoipro.utilities.FragmentHistory;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.MainVM;
import com.orange.orangeetmoipro.views.base.BaseActivity;
import com.orange.orangeetmoipro.views.base.BaseFragment;
import com.orange.orangeetmoipro.views.main.blank.BlankFragment;
import com.orange.orangeetmoipro.views.main.dashboard.DashboardFragment;
import com.orange.orangeetmoipro.views.main.settings.SettingsFragment;
import com.orange.orangeetmoipro.views.main.webview.WebViewFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private MainVM mainVM;
    private Connectivity connectivity;
    private ArrayList<Fragment> fragments;
    private FragNavController fragNavController;
    private FragmentHistory fragmentHistory;
    private Bundle savedInstanceState;
    private PreferenceManager preferenceManager;


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
                    switchTab(position);
                } else {
                    switchTab(0);
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

            Fragment fragment;

            if (item.getActionType().equalsIgnoreCase("internal")) {
                switch (item.getAction()) {
                    case "home":
                        fragment = new DashboardFragment();
                        break;
                    case "setting":
                        fragment = new SettingsFragment();
                        break;
                    default:
                        fragment = new BlankFragment();
                        break;
                }
            } else if (item.getInApp()) {
                fragment = WebViewFragment.newInstance(item.getAction());
            } else {
                fragment = BlankFragment.newInstance(item.getAction());
            }
            fragments.add(fragment);
        }

        fragmentHistory = new FragmentHistory();
        fragNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rootFragmentListener(this, fragments.size())
                .build();

        switchTab(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                fragmentHistory.push(position);
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


        if (getIntent().getBooleanExtra("show_popup", false))
            Utilities.showCompleteProfileDialog(this, "", "", new DialogButtonsClickListener() {
                @Override
                public void firstChoice() {

                }

                @Override
                public void secondChoice() {

                }
            });
    }

    private void switchTab(int position) {
        fragNavController.switchTab(position);
    }

    private void getTabMenu() {
        if (connectivity.isConnected()) {
            mainVM.getTabMenu(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        } else
            Utilities.showErrorPopup(this, getString(R.string.no_internet), "");

    }

    private void handleTabMenuResponse(TabMenuData tabMenuData) {
        if (tabMenuData == null) {
            Utilities.showErrorPopup(this, getString(R.string.generic_error), "");
        } else {
            int code = tabMenuData.getHeader().getCode();
            if (code == 200) {
                init(tabMenuData);
            } else
                Utilities.showErrorPopup(this, tabMenuData.getHeader().getMessage(), "");
        }
    }

}
