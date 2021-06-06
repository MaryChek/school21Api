package com.example.a42_api.data.api

import com.example.a42_api.data.api.models.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseApiTalker {
    fun <T> getResult(
        call: Call<T>,
        onServiceCallAnswer: (Resource<T>) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val loginResource: T? = response.body()
                onServiceCallAnswer(
                    if (response.isSuccessful && loginResource != null) {
                        Resource.success(loginResource)
                    } else {
                        error(" ${response.code()} ${response.message()}", response.code())
                    }
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onServiceCallAnswer(error(t.message, null))
            }
        })
    }

    private fun <T> error(message: String?, errorCode: Int?): Resource<T> =
        Resource.error(NETWORK_FAIL + message, errorCode)

    companion object {
        private const val NETWORK_FAIL = "Network call has failed for a following reason: "
    }
}