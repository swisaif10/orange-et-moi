
package com.orange.ma.entreprise.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orange.ma.entreprise.models.tabmenu.TabMenuItem;

import java.util.List;

public class LoginResponseData {

    @Expose
    private List<TabMenuItem> tabMenu;
    @Expose
    private UserInfos userInfos;

    public List<TabMenuItem> getTabMenu() {
        return tabMenu;
    }

    public void setTabMenu(List<TabMenuItem> tabMenu) {
        this.tabMenu = tabMenu;
    }

    public UserInfos getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(UserInfos userInfos) {
        this.userInfos = userInfos;
    }

}
