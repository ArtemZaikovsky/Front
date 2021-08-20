package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.ContractorStatusDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface ContractorStatusApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<ContractorStatusDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call <ContractorStatusDto> getById(@Path(value = "url", encoded = true) String url, @Path(value="id") Long id);
}
