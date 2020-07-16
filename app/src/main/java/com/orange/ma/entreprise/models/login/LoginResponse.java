
package com.orange.ma.entreprise.models.login;

import com.google.gson.annotations.Expose;

public class LoginResponse {

    @Expose
    private LoginResponseData data;
    @Expose
    private String hashTabMenu;
    @Expose
    private String token;

    public LoginResponseData getData() {
        return data;
    }

    public void setData(LoginResponseData data) {
        this.data = data;
    }

    public String getHashTabMenu() {
        return hashTabMenu;
    }

    public void setHashTabMenu(String hashTabMenu) {
        this.hashTabMenu = hashTabMenu;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
