package com.hb.jetpack_compose.network


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitApi {

    const val BaseUrl = "https://www.wanandroid.com/"
    private val retrofit: Retrofit by lazy {
        val contentType = "application/json".toMediaType()
        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
        Retrofit.Builder().apply {
            baseUrl(BaseUrl)
//                .addConverterFactory(Json.asConverterFactory(contentType = ContentType))
                .addConverterFactory(json.asConverterFactory(contentType = contentType))
        }.build()
    }

    fun getInstance() = retrofit.create(Api::class.java)

}