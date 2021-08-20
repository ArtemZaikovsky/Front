package com.trade_accounting.services.impl;

import com.trade_accounting.components.authentication.LoginRequest;
import com.trade_accounting.components.authentication.LoginResponse;
import com.trade_accounting.services.interfaces.AuthenticationService;
import com.trade_accounting.services.interfaces.api.AuthenticationApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationApi authenticationApi;
    private final String authenticationUrl;

    public AuthenticationServiceImpl(@Value("${authentication_url}") String authenticationUrl, Retrofit retrofit) {
        this.authenticationUrl = authenticationUrl;
        authenticationApi = retrofit.create(AuthenticationApi.class);
    }

    @Override
    public LoginResponse userLogin(LoginRequest loginRequest) {

        Call<LoginResponse> loginResponseCall = authenticationApi.userLogin(loginRequest);

        try {
            Response<LoginResponse> loginResponseResponse = loginResponseCall.execute();
            return loginResponseResponse.body();
        } catch (IOException e) {
            System.out.println("IOException on loginResponseCall.execute()");
        }

        return null;

    }

}
