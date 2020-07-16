package com.orange.ma.entreprise.datamanager.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class TokenRenewInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        String newToken = response.header("x-auth-token");
        if (newToken != null) {
            System.out.println(newToken);
        }

        return response;
    }
}
