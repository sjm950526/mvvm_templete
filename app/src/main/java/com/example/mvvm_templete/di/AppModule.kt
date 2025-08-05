package com.example.mvvm_templete.di

import android.content.Context
import com.example.mvvm_templete.common.Constants
import com.example.mvvm_templete.remote.ApiInterface
import com.example.mvvm_templete.remote.JsonAndXmlConverters
import com.example.mvvm_templete.remote.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.mvvm_templete.remote.interceptor.AddHeaderInterceptor
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// Singleton 객체로 REST API 연결 모듈 생성 후 의존성 주입
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesAuthorizationInterceptor(@ApplicationContext context: Context): AddHeaderInterceptor {
        return AddHeaderInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(sessionID: AddHeaderInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(sessionID)
        .writeTimeout(Constants.HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(Constants.HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
        .connectTimeout(Constants.HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(
            JsonAndXmlConverters(
                GsonConverterFactory.create(),
                TikXmlConverterFactory.create()
            )
        )
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)

    @Singleton
    @Provides
    fun provideRepository(apiInterface: ApiInterface) = Repository(apiInterface)
}