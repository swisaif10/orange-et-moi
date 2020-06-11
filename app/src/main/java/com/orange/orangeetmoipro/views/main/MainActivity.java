package com.orange.orangeetmoipro.views.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.models.tabmenu.TabMenuData;
import com.orange.orangeetmoipro.models.tabmenu.TabMenuItem;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.MainVM;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private MainVM mainVM;
    private Connectivity connectivity;

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
            //tabTextView.setTextColor(isSelected ? getColor(R.color.pink) : getColor(R.color.dark_grey));
            tabTextView.setText(item.getTitle());
            tab.setText(item.getTitle());
            ImageView tabImageView = view.findViewById(R.id.tabImageView);
            int icon = getResources().getIdentifier(item.getIconDisabled(), "drawable", getPackageName());
            tabImageView.setImageDrawable(getDrawable(icon));
            //tab.setIcon(icon);
            tabLayout.addTab(tab);
            LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(i));
            layout.setBackground(null);
            i++;
        }
    }

    private void getTabMenu() {
        if (connectivity.isConnected()) {
            mainVM.getTabMenu();
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
