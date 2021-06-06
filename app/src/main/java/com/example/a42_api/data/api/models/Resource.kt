package com.example.a42_api.data.api.models

import com.example.a42_api.data.api.models.Status.SUCCESS
import com.example.a42_api.data.api.models.Status.ERROR
import com.example.a42_api.domain.models.ErrorMessage

class Resource<out T>(val status: Status, val data: T?, val message: ErrorMessage?) {

    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(SUCCESS, data, null)

        fun <T> error(message: String, errorCode: Int?, data: T? = null): Resource<T> =
            Resource(ERROR, data, ErrorMessage(message, errorCode))
    }
}