package com.trade_accounting.services.interfaces;

import com.trade_accounting.components.authentication.LoginRequest;
import com.trade_accounting.components.authentication.LoginResponse;

public interface AuthenticationService {

    LoginResponse userLogin(LoginRequest loginRequest);

}
