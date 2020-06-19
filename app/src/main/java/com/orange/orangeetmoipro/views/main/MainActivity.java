package com.orange.orangeetmoipro.views.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.listeners.VersionControlChoiceDialogClickListener;
import com.orange.orangeetmoipro.models.dashboard.ItemButton;
import com.orange.orangeetmoipro.models.dashboard.ItemString;
import com.orange.orangeetmoipro.models.dashboard.Template;
import com.orange.orangeetmoipro.models.tabmenu.TabMenuData;
import com.orange.orangeetmoipro.models.tabmenu.TabMenuItem;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.MainVM;
import com.orange.orangeetmoipro.views.base.BaseActivity;
import com.orange.orangeetmoipro.views.main.adapters.DashboardAdapter;
import com.orange.orangeetmoipro.views.main.dashboard.DashboardFragment;
import com.orange.orangeetmoipro.views.main.dashboard.SettingsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private MainVM mainVM;
    private Connectivity connectivity;
    private ArrayList<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainVM = ViewModelProviders.of(this).get(MainVM.class);
        connectivity = new Connectivity(this, this);

        mainVM.getTabMenuMutableLiveData().observe(this, this::handleTabMenuResponse);

        getTabMenu();
    }

    private void init(TabMenuData tabMenuData) {

        int i = 0;
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
            LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(i));
            layout.setBackground(null);


            Fragment fragment = new Fragment();
            switch (item.getTitle()) {
                case "Accueil":
                case "Paiement":
                case "Parrainage":
                    fragment = new DashboardFragment();
                    break;
                case "Paramètres":
                    fragment = new SettingsFragment();
                    break;
            }
            //fragments.add(fragment);
            i++;
        }

        getSupportFragmentManager().beginTransaction().
                add(R.id.container, new DashboardFragment()).
                commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        getSupportFragmentManager().beginTransaction().
                                add(R.id.container, new DashboardFragment()).
                                commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().
                                add(R.id.container, new SettingsFragment()).
                                commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        if (getIntent().getBooleanExtra("show_popup", false))
            Utilities.showCompleteProfileDialog(this, "", "", new VersionControlChoiceDialogClickListener() {
                @Override
                public void onAccept() {

                }

                @Override
                public void onRefuse() {

                }
            });
    }

    private void getTabMenu() {
        if (connectivity.isConnected()) {
            mainVM.getTabMenu("fr");
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
