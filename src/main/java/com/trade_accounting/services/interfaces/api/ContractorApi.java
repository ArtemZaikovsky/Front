package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.FiasModelDto;
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

public interface ContractorApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<ContractorDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/search/{searchTerm}")
    Call<List<ContractorDto>> getAll(@Path(value = "url", encoded = true) String url,@Path("searchTerm") String searchTerm);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<ContractorDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/searchContractor")
    Call<List<ContractorDto>> searchContractor(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> queryContractor);

    @Headers("Accept: application/json")
    @GET("{url}/searchAddressByLevel/{level}")
    Call<List<FiasModelDto>> searchAddressByLevel(@Path(value = "url", encoded = true) String url, @Path("level") String level);

    @Headers("Accept: application/json")
    @GET("{url}/searchAddressByAoguid/{guid}")
    Call<List<FiasModelDto>> searchAddressByAoguid(@Path(value = "url", encoded = true) String url, @Path("guid") String guid);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body ContractorDto contractorDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body ContractorDto contractorDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);
}
