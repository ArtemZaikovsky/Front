package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.components.authentication.LoginRequest;
import com.trade_accounting.components.authentication.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationApi {

    @POST("login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

}
