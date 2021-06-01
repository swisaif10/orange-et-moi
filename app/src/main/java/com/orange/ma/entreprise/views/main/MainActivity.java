package com.orange.ma.entreprise.views.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.orange.ma.entreprise.OrangePro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.EncryptedSharedPreferences;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.OnDialogButtonsClickListener;
import com.orange.ma.entreprise.models.login.SettingCompleteAccount;
import com.orange.ma.entreprise.models.tabmenu.TabMenuData;
import com.orange.ma.entreprise.models.tabmenu.TabMenuItem;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.MainVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseActivity;
import com.orange.ma.entreprise.views.enterNumber.EnterNumberFragment;
import com.orange.ma.entreprise.views.main.browser.BrowserFragment;
import com.orange.ma.entreprise.views.main.browser.ExternalBrowserFragment;
import com.orange.ma.entreprise.views.main.dashboard.DashboardFragment;
import com.orange.ma.entreprise.views.main.settings.SettingsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.orange.ma.entreprise.utilities.Constants.ENDPOINT;
import static com.orange.ma.entreprise.utilities.Constants.ENDPOINT_TITLE;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;

    private MainVM mainVM;
    private Connectivity connectivity;
    private ArrayList<Fragment> fragments;
    private PreferenceManager preferenceManager;
    private Fragment fragment;
    private SettingCompleteAccount settingCompleteAccount;
    private EncryptedSharedPreferences encryptedSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainVM = ViewModelProviders.of(this).get(MainVM.class);
        connectivity = new Connectivity(this, this);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        encryptedSharedPreferences = new EncryptedSharedPreferences();

        encryptedSharedPreferences.getEncryptedSharedPreferences(this);


        mainVM.getTabMenuMutableLiveData().observe(this, this::handleTabMenuResponse);

        getTabMenu();

        settingCompleteAccount = (SettingCompleteAccount) getIntent().getSerializableExtra("settingCompleteAccount");
    }

    @Override
    public void onBackPressed() {

        if (getCurrentFragment() instanceof DashboardFragment)
            finish();
        else {
            super.onBackPressed();
            tabLayout.selectTab(tabLayout.getTabAt(getFragmentIndex(getCurrentFragment())));
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
            try {
                int iconDisabled = getResources().getIdentifier(item.getIconDisabled(), "drawable", getPackageName());
                int iconEnabled = getResources().getIdentifier(item.getIconEnabled(), "drawable", getPackageName());
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(new int[]{android.R.attr.state_selected}, getDrawable(iconEnabled));
                stateListDrawable.addState(StateSet.WILD_CARD, getDrawable(iconDisabled));
                tabImageView.setImageDrawable(stateListDrawable);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
                    case "consult_ligne":
                        fragment = new EnterNumberFragment();
                        break;
                    default:
                        fragment = new BrowserFragment();
                        break;
                }
            } else if (item.getInApp())
                fragment = BrowserFragment.newInstance(item.getAction());
            else
                fragment = ExternalBrowserFragment.newInstance(item.getAction());

            fragments.add(fragment);
        }

        int index = getDashboardIndex();
        if (index != -1) {
            switchFragment(fragments.get(index), "");
            tabLayout.selectTab(tabLayout.getTabAt(index));
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if (tab.getTag() == null || !tab.getTag().equals("bp")) {
                    switchFragment(fragments.get(position), "");
                } else
                    tab.setTag(null);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switchFragment(fragments.get(position), "");

            }
        });

    }

    private void showCompleteProfileDialog() {
        Utilities.showCompleteProfileDialog(this, new OnDialogButtonsClickListener() {
            @Override
            public void firstChoice() {
                //firebaseAnalyticsEvent
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getApplicationContext()));
                bundle.putString(Constants.FIREBASE_RC_KEY, encryptedSharedPreferences.getValue(Constants.LOGIN_KEY, ""));
                OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("Clic_completer_mon_profil", bundle);


                String urlVebView = settingCompleteAccount.getAction();
                if (!Utilities.isNullOrEmpty(encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, null)) && urlVebView.contains(Constants.EX_SSO_TOKEN)) {
                    String token = encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, "").replace("Bearer ", "");
                    urlVebView = urlVebView.replace(Constants.EX_SSO_TOKEN, token);
                }
                if (settingCompleteAccount.getInApp())
                    Utilities.openCustomTab(MainActivity.this, urlVebView);
                else
                    Utilities.openInBrowser(MainActivity.this, urlVebView);
            }

            @Override
            public void secondChoice() {

                Bundle bundle = new Bundle();
                bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getApplicationContext()));
                bundle.putString(Constants.FIREBASE_RC_KEY, encryptedSharedPreferences.getValue(Constants.LOGIN_KEY, ""));
                OrangePro.getInstance().getFireBaseAnalyticsInstance().logEvent("Clic_ignorer_completer_mon_profil", bundle);
            }
        });

        if (getIntent().getStringExtra("link") != null && getIntent().getStringExtra("link").equalsIgnoreCase("setting"))
            moveToSettingsFragment();
    }

    private void getTabMenu() {
        if (connectivity.isConnected()) {
            mainVM.getTabMenu(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, ""));
        } else
            Utilities.showErrorPopup(this, getString(R.string.no_internet));

    }

    private void handleTabMenuResponse(TabMenuData tabMenuData) {
        if (tabMenuData == null) {
            Utilities.showErrorPopup(this, getString(R.string.generic_error));
        } else {
            int code = tabMenuData.getHeader().getCode();
            switch (code) {
                case 200:
                    String hash = encryptedSharedPreferences.getValue(Constants.TAB_MENU_HASH, "");
                    if (Utilities.isNullOrEmpty(hash) || !hash.equals(tabMenuData.getTabMenuResponse().getHash())) {
                        init(tabMenuData);
                        encryptedSharedPreferences.putValue(Constants.TAB_MENU_HASH, tabMenuData.getTabMenuResponse().getHash());
                    }
                    handleIntent();
                    break;
                case 403:
                    encryptedSharedPreferences.putValue(Constants.IS_LOGGED_IN, false);
                    encryptedSharedPreferences.clearValue(Constants.TOKEN_KEY);
                    encryptedSharedPreferences.putValue(Constants.IS_AUTHENTICATED, false);
                    Intent intent = new Intent(this, AuthenticationActivity.class);
                    intent.putExtra(Constants.ERROR_CODE, tabMenuData.getHeader().getCode());
                    intent.putExtra(Constants.ERROR_MESSAGE, tabMenuData.getHeader().getMessage());
                    startActivity(intent);
                    finish();
                    break;
                default:
                    Utilities.showErrorPopup(this, tabMenuData.getHeader().getMessage());
            }
            ;

        }
    }

    private void handleIntent() {
        if (getIntent().getExtras() != null) {
            if (getIntent().getStringExtra("link") != null) {
                if ("setting".equals(getIntent().getStringExtra("link").toLowerCase())) {
                    moveToSettingsFragment();
                }else if ("consult_ligne".equals(getIntent().getStringExtra("link").toLowerCase()))
                {
                    switchFragment(new EnterNumberFragment(),"");
                }
            } else if (getIntent().getStringExtra(ENDPOINT) != null) {
                if (getIntent().getStringExtra(ENDPOINT_TITLE) != null) {
                    handleInApp(getIntent().getStringExtra(ENDPOINT), getIntent().getStringExtra(ENDPOINT_TITLE));
                } else
                    handleAppView(getIntent().getStringExtra(ENDPOINT));
            }
            if (!getIntent().getBooleanExtra("isCompleted", true)) {
                OrangePro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(this, "Pop-up_completer_mon_profil", LocaleManager.getLanguagePref(this));
                showCompleteProfileDialog();
                getIntent().removeExtra("isCompleted");
            }
        }
    }

    private void handleInApp(String endpoint, String endpointdata) {
        fragment = BrowserFragment.newInstance(endpoint);
        switchFragment(fragment, "");
    }

    private void handleAppView(String endpoint) {
        if (endpoint != null) {
            switch (endpoint) {
                case "home":
                    fragment = new DashboardFragment();
                    break;
                case "setting":
                    fragment = new SettingsFragment();
                    moveToSettingsFragment();
                    break;
                case "consult_ligne":
                    switchFragment(new EnterNumberFragment(),"");
                    break;
                default:
                    fragment = new DashboardFragment();
                    break;
            }
            switchFragment(fragment, "");
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

    public void moveToDashboardFragment() {
        int index = -1;
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof DashboardFragment) {
                index = i;
                break;
            }
        }
        if (index != -1)
            tabLayout.getTabAt(index).select();
    }

    private int getDashboardIndex() {
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof DashboardFragment) {
                return i;
            }
        }
        return -1;
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    public int getFragmentIndex(Fragment fragment) {
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i).getClass().equals(fragment.getClass()))
                return i;
        }
        return -1;
    }

    public void hideTab (){
        tabLayout.setVisibility(View.GONE);
    }
    public void showTab (){
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void selectTab(int index){
        tabLayout.selectTab(tabLayout.getTabAt(index));
    }
}