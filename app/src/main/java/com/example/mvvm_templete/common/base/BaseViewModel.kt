package com.example.mvvm_templete.common.base

import android.util.Log
import android.util.MalformedJsonException
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_templete.common.Code
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeoutException

// 공통 ViewModel 클래스
open class BaseViewModel() : ViewModel(), LifecycleObserver {

    val TAG: String = javaClass.simpleName

    private val _mErrorMsg = MutableLiveData<String>()
    val mErrorMsg: LiveData<String> = _mErrorMsg

    interface ErrCallback {
        fun onError(code: Code)
    }

    enum class HTTP_ERROR {
        HTTP, PARSE, CONN, SOCKET, HOST, TIMEOUT, ETC
    }

    companion object {
        const val ERROR_TIME = "일시적인 장애가 발생하였습니다\n잠시후에 다시 시도해 주세요."
        const val ERROR_HTTP = "해당 페이지를 찾을수 없습니다\n잠시후에 다시 시도해 주세요."
        const val ERROR_MSG = "정보를 수신을 할 수 없습니다.\n잠시 후 다시 사용하여 주시기 바랍니다."
        const val ERROR_NETWORK = "네트워크 연결상태가 불안정합니다\n네트워크 상태를 확인해주세요."
    }


    suspend fun <T> runDataLoading(
        block: suspend () -> T,
        errCallback: ErrCallback? = null
    ) {
        try {
            block()
        } catch (e: Exception) {
            if (errCallback != null) {
                errCallback.onError(apiErrorCode(e))
            } else {
                apiErrorCheck(e)
            }
        }
    }

    suspend fun <T> runDataLoading(block: suspend () -> T) {
        try {
            block()
            return
        } catch (e: Exception) {
            apiErrorCheck(e)
        }
    }

    private fun apiErrorCode(e: Exception): Code {
        val code = Code()

        if (e is HttpException) {
            code.code = HTTP_ERROR.HTTP.toString()
            code.value = e.message.toString()
        } else if (e is JsonParseException || e is JSONException || e is ParseException || e is MalformedJsonException) {
            code.code = HTTP_ERROR.PARSE.toString()
            code.value = e.message.toString()
        } else if (e is ConnectException) {
            code.code = HTTP_ERROR.CONN.toString()
            code.value = e.message.toString()
        } else if (e is SocketTimeoutException) {
            code.code = HTTP_ERROR.SOCKET.toString()
            code.value = e.message.toString()
        } else if (e is UnknownHostException) {
            code.code = HTTP_ERROR.HOST.toString()
            code.value = e.message.toString()
        } else if (e is TimeoutException) {
            code.code = HTTP_ERROR.TIMEOUT.toString()
            code.value = e.message.toString()
        } else {
            code.code = HTTP_ERROR.ETC.toString()
            code.value = e.message.toString()
        }

        return code
    }

    private fun apiErrorCheck(e: Exception) {
        val msg: String
        when (e) {
            is HttpException -> {
                msg = ERROR_HTTP
                Log.e(TAG, "HttpException = ${e.message}")
            }

            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                msg = ERROR_TIME
                Log.e(TAG, "JsonParseException = ${e.message}")
            }

            is ConnectException -> {
                msg = ERROR_TIME
                Log.e(TAG, "ConnectException = ${e.message}")
            }

            is SocketTimeoutException -> {
                msg = ERROR_TIME
                Log.e(TAG, "SocketTimeoutException = ${e.message}")
            }

            is UnknownHostException -> {
                msg = ERROR_HTTP
                Log.e(TAG, "UnknownHostException = ${e.message}")
            }

            is TimeoutException -> {
                msg = ERROR_TIME
                Log.e(TAG, "TimeoutException = ${e.message}")
            }

            else -> {
                msg = ERROR_MSG
            }
        }

        _mErrorMsg.postValue(msg)

    }
}
