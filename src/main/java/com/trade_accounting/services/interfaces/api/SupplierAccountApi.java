package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface SupplierAccountApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<SupplierAccountDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<SupplierAccountDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<SupplierAccountDto> create(@Path(value = "url", encoded = true) String url, @Body SupplierAccountDto supp);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body SupplierAccountDto supp);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/search/{nameFilter}")
    Call<List<SupplierAccountDto>> searchByString(@Path(value = "url", encoded = true) String url,
                                                  @Path(value = "nameFilter", encoded = true) String nameFilter);

    @Headers("Accept: application/json")
    @GET("{url}/querySupplier")
    Call<List<SupplierAccountDto>> searchByFilter(@Path(value = "url", encoded = true) String url,
                                             @QueryMap Map<String, String> querySupplier);


}
