package com.hb.jetpack_compose.network


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitApi {

    const val BaseUrl = "https://www.wanandroid.com/"
    private val ContentType = "application/json".toMediaType()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().apply {
            baseUrl(BaseUrl)
                .addConverterFactory(Json.asConverterFactory(contentType = ContentType))
        }.build()
    }

    fun getInstance() = retrofit.create(Api::class.java)

}