package com.example.mvvm_templete.remote

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class JsonAndXmlConverters(
    private val jsonFactory: Converter.Factory,
    private val xmlFactory: Converter.Factory
) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>,retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        for (annotation in annotations) {
            if (annotation is ApiInterface.Json)
                return jsonFactory.responseBodyConverter(type, annotations, retrofit)
            if (annotation is ApiInterface.Xml) {
                return xmlFactory.responseBodyConverter(type, annotations, retrofit)
            }
        }
        return null
    }

    override fun requestBodyConverter(
        type: Type, parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        for (annotation in parameterAnnotations) {
            if (annotation is ApiInterface.Json) {
                return jsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,retrofit)
            }
            if (annotation is ApiInterface.Xml) {
                return xmlFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,retrofit)
            }
        }
        return null
    }
}