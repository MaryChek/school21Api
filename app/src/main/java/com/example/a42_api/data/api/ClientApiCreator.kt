package com.example.a42_api.data.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ClientApiCreator {
    fun createApiClient(config: ClientConfig): School42Api =
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(config.okHttpClientBuilder.build())
            .build()
            .create(School42Api::class.java)
}