package com.example.app_delivery;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    @Headers({"appKey:31a0c8ab-6880-42ba-b6f2-18080fbe6070","contentType: application/json",
            "Accept: */*"})
    @POST("tmap/routes/routeOptimization10?version=1&format=json")
    Call<JSONObject> doList(@Body String body);
}
class HTTPRequestBody {
    String key1 = "value1";
    String key2 = "value2";

}