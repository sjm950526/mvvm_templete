package com.example.mvvm_templete.remote

import com.example.mvvm_templete.remote.data.ResponseTest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @Retention(AnnotationRetention.RUNTIME)
    annotation class Json

    @Retention(AnnotationRetention.RUNTIME)
    annotation class Xml

    @GET("todos")
    @Json
    suspend fun getList(): Response<List<ResponseTest>>

    @GET("todos/{number}")
    @Json
    suspend fun getData(@Path("number") number: Int): Response<ResponseTest>

}