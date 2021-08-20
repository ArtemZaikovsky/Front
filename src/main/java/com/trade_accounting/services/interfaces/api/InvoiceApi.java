package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.InvoiceDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface InvoiceApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<InvoiceDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<InvoiceDto>> getAll(@Path(value = "url", encoded = true) String url, @Query("typeOfInvoice") String typeOfInvoice);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<InvoiceDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/search")
    Call<List<InvoiceDto>> search(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> query);

    @Headers("Accept: application/json")
    @GET("{url}/searchByString")
    Call<List<InvoiceDto>> search(@Path(value = "url", encoded = true) String url,
                                          @Query("search") String search,
                                          @Query("typeOfInvoice") String typeOfInvoice);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<InvoiceDto> create(@Path(value = "url", encoded = true) String url, @Body InvoiceDto invoiceDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body InvoiceDto invoiceDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);
}
