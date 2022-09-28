package com.hb.jetpack_compose.network


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitApi {

    const val BaseUrl = "https://www.wanandroid.com/"


    private val okHttpClient: okhttp3.OkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    private val retrofit: Retrofit by lazy {
        val contentType = "application/json".toMediaType()
        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
        Retrofit.Builder().apply {
            baseUrl(BaseUrl)
            client(okHttpClient)
//                .addConverterFactory(Json.asConverterFactory(contentType = ContentType))
                .addConverterFactory(json.asConverterFactory(contentType = contentType))
        }.build()
    }

    fun getInstance() = retrofit.create(Api::class.java)

}