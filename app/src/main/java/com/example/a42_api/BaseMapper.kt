package com.example.a42_api

open class BaseMapper {

    protected fun firstUpperCase(word: String?): String =
        word?.let {
            it.substring(0, 1).uppercase() + it.substring(1)
        } ?: EMPTY_STRING

    companion object {
        private const val EMPTY_STRING = ""
    }
}