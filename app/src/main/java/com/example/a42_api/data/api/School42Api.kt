package com.example.a42_api.data.api

import com.example.a42_api.data.api.models.*
import retrofit2.Call
import retrofit2.http.*

interface School42Api {
    @FormUrlEncoded
    @POST("/oauth/token")
    fun getToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Call<Token>

    @GET("/v2/users/{login}")
    fun getUser(
        @Header("Authorization") token: String,
        @Path("login") login: String
    ): Call<User>

    @GET("/v2/users/{login}/coalitions_users")
    fun getCoalitionsIdsInOrderOfCreationByLogin(
        @Header("Authorization") token: String,
        @Path("login") login: String
    ): Call<List<CoalitionId>>

    @GET("/v2/users/{login}/coalitions")
    fun getCoalitionByLogin(
        @Header("Authorization") token: String,
        @Path("login") login: String
    ): Call<List<Coalition>>

    @GET("/v2/users/{login}/cursus_users")
    fun getCoursesListInOrderOfCreationByLogin(
        @Header("Authorization") token: String,
        @Path("login") login: String
    ): Call<List<Course>>
}