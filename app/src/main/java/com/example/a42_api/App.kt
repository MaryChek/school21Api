package com.example.a42_api

import android.app.Application
import com.example.a42_api.data.api.ClientApiCreator
import com.example.a42_api.data.api.ClientConfig
import com.example.a42_api.data.api.School42ApiTalker
import com.example.a42_api.data.mapper.School42ResourceMapper
import com.example.a42_api.data.repository.LoginRepository
import com.example.a42_api.domain.interactor.UserInteractor
import com.example.a42_api.domain.mapper.UserMapper
import com.example.a42_api.domain.models.TokenKeys
import com.example.a42_api.presentation.models.UserDetailModel
import okhttp3.OkHttpClient

class App: Application() {

    lateinit var interactor: UserInteractor
    val userDetailModel = UserDetailModel()
    private val tokenKeys = TokenKeys(GRANT_TYPE, UID, SECRET)

    override fun onCreate() {
        super.onCreate()
        val clientConfig = ClientConfig(URL, OkHttpClient.Builder())
        val apiTalker = School42ApiTalker(clientConfig, ClientApiCreator())
        val repository = LoginRepository(apiTalker, School42ResourceMapper())
        interactor = UserInteractor(repository, UserMapper(), tokenKeys)
    }

    companion object {
        private const val URL = "https://api.intra.42.fr"
        private const val GRANT_TYPE: String = "client_credentials"
        private const val UID: String = "782023ea9581a42b46561a555e6c9f4ffcfed0aea36036e790e3b1364155db64"
        private const val SECRET: String =
            "5099b21742bb03cc4e994ece6be693ba2212dac91fedca56bcdea6cf972c56ce"
    }
}