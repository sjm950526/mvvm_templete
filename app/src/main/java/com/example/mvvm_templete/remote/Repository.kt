package com.example.mvvm_templete.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getList() = apiInterface.getList()
    suspend fun getData(number: Int) = apiInterface.getData(number)

    // custom Response 사용시 응답 값 내 에러 처리 기능
//    private suspend fun <T> call(apiCall: suspend () -> Response<T>): T {
//        try {
//            val response = apiCall()
//            if (response.body()?.code == "256"){
//                throw Exception("256")
//            }
//            return response.body()!!
//        } catch (e: Exception) {
//            throw e
//        }
//    }

}