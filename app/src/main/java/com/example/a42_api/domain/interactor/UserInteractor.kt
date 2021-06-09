package com.example.a42_api.domain.interactor

import com.example.a42_api.data.repository.LoginRepository
import com.example.a42_api.domain.mapper.UserMapper
import com.example.a42_api.domain.models.*
import java.util.*
import kotlin.concurrent.timerTask

class UserInteractor(
    private val repository: LoginRepository,
    private val mapper: UserMapper,
    private val tokenKeys: TokenKeys
) {
    private var searchedLogin: String? = null
    private var userLogin: String? = null
    private var token: Token? = null

    private var orderedCoalitionIdIds: List<CoalitionId>? = null
    private var coalitions: List<Coalition>? = null
    private var courses: List<Course>? = null

    private var onServerError: ((Int?) -> Unit)? = null
    private var onGetUserCoursesAndCoalitions: ((List<Course>, List<Coalition>) -> Unit)? = null
    private var onGetUser: ((User) -> Unit)? = null

    fun fetchUserByLogin(
        login: String,
        onGetUser: (User) -> Unit,
        onServerError: (Int?) -> Unit
    ) {
        searchedLogin = login
        this.onGetUser = onGetUser
        this.onServerError = onServerError
        fetchUser()
    }

    private fun fetchUser() {
        getAccessToken()?.let { accessToken ->
            searchedLogin?.let { login ->
                onGetUser?.let {
                    repository.fetchUserByLogin(
                        login, accessToken, it, this::onServerError
                    )
                }
            }
        }
    }

    fun fetchUserCoursesAndCoalitions(
        login: String,
        onGetUserCoursesAndCoalitions: (List<Course>, List<Coalition>) -> Unit,
        onServerError: (Int?) -> Unit
    ) {
        userLogin = login
        this.onGetUserCoursesAndCoalitions = onGetUserCoursesAndCoalitions
        this.onServerError = onServerError
        fetchCoalitionIdsInOrderOfCreation()
    }

    private fun fetchCoalitionIdsInOrderOfCreation() {
        getAccessToken()?.let { accessToken ->
            userLogin?.let {
                repository.fetchCoalitionIdsInOrderOfCreationByLogin(
                    it, accessToken, this::onGetTheCoalitionIds, this::onServerError
                )
            }
        }
    }

    private fun onGetTheCoalitionIds(coalitionIds: List<CoalitionId>) {
        orderedCoalitionIdIds = coalitionIds
        fetchCoalitions()
    }

    private fun fetchCoalitions() {
        getAccessToken()?.let { accessToken ->
            userLogin?.let {
                repository.fetchCoalitionsByLogin(
                    it, accessToken, this::onGetTheCoalitions, this::onServerError
                )
            }
        }
    }

    private fun onGetTheCoalitions(coalitions: List<Coalition>) {
        orderedCoalitionIdIds?.let {
            this.coalitions = mapper.mapCoalitions(it, coalitions)
        }
        fetchCoursesListInOrderOfCreationByLogin()
    }

    private fun fetchCoursesListInOrderOfCreationByLogin() {
        getAccessToken()?.let { accessToken ->
            userLogin?.let {
                repository.fetchCoursesListInOrderOfCreationByLogin(
                    it, accessToken, this::onGetTheCourses, this::onServerError
                )
            }
        }
    }

    private fun onGetTheCourses(courses: List<Course>) {
        coalitions?.let { coalitions ->
            onGetUserCoursesAndCoalitions?.invoke(courses, coalitions)
            removeDate()
        }
    }

    private fun fetchToken() {
        tokenKeys.let {
            repository.fetchToken(
                it.grantType,
                it.uId,
                it.secret,
                this::onAuthorizationSuccessful,
                this::onServerError
            )
        }
    }

    private fun onAuthorizationSuccessful(token: Token) {
        this.token = mapper.mapToken(token)
        when {
            userLogin == null ->
                delay(this::fetchUser)
            orderedCoalitionIdIds == null ->
                delay(this::fetchCoalitionIdsInOrderOfCreation)
            coalitions == null ->
                delay(this::fetchCoalitions)
            else ->
                delay(this::fetchCoursesListInOrderOfCreationByLogin)
        }
    }

    private fun getAccessToken(): String? =
        if (token == null) {
            fetchToken()
            null
        } else {
            token?.tokenType + token?.accessToken
        }

    private fun onServerError(errorCode: Int?) =
        when (errorCode) {
            UNAUTHORIZED -> fetchToken()
            else -> onServerError?.invoke(errorCode)
        }

    fun removeDate() {
        userLogin = null
        searchedLogin = null
        orderedCoalitionIdIds = null
        coalitions = null
    }

    companion object {
        private const val UNAUTHORIZED = 401

        private fun delay(function: () -> Unit) {
            Timer().schedule(timerTask {
                function()
            }, 300)
        }
    }
}