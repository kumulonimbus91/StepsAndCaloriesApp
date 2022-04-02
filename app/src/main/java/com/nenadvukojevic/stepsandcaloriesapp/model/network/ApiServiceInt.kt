package com.nenadvukojevic.stepsandcaloriesapp.model.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.nenadvukojevic.stepsandcaloriesapp.model.entities.ResponseJson
import com.nenadvukojevic.stepsandcaloriesapp.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL).addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory()).baseUrl(Constants.BASE_URL)
    .build()

interface ApiServiceInt {

        @GET(Constants.API_END_POINT)
        fun getSpecificFoodAsync(
            @Query("ingr") food: String,
            @Query("app_id") appId: String = Constants.APP_ID,
            @Query("app_key") appKey: String = Constants.APP_KEY
        ):
                Deferred<ResponseJson>




}

object ApiService {
    val retrofitService: ApiServiceInt by lazy { retrofit.create(ApiServiceInt::class.java) }
}