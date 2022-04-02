package com.mvts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface apiInterface {
    @GET
    Call<String> obtainRoute (@Url String url);

}
