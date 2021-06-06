package com.example.a42_api.data.repository

import android.util.Log
import com.example.a42_api.data.api.School42ApiTalker
import com.example.a42_api.data.mapper.School42ResourceMapper
import com.example.a42_api.data.api.models.*
import com.example.a42_api.domain.models.ErrorMessage
import com.example.a42_api.domain.models.User as DomainUser
import com.example.a42_api.domain.models.Token as DomainToken
import com.example.a42_api.domain.models.CoalitionId as DomainCoalitionId
import com.example.a42_api.domain.models.Coalition as DomainCoalition
import com.example.a42_api.domain.models.Course as DomainCourse

class LoginRepository(
    private val apiTalker: School42ApiTalker,
    private val mapper: School42ResourceMapper
) {
    private var onGetTheToken: ((DomainToken) -> Unit)? = null
    private var onGetTheUser: ((DomainUser) -> Unit)? = null
    private var onGetOrderedCoalitionIds: ((List<DomainCoalitionId>) -> Unit)? = null
    private var onGetCoalitions: ((List<DomainCoalition>) -> Unit)? = null
    private var onGetOrderedCourses: ((List<DomainCourse>) -> Unit)? = null
    private var onServerFinishedError: ((Int?) -> Unit)? = null

    fun fetchToken(
        grantType: String,
        uId: String,
        secret: String,
        onGetTheToken: (DomainToken) -> Unit,
        onAuthorizationFail: (Int?) -> Unit
    ) {
        this.onGetTheToken = onGetTheToken
        this.onServerFinishedError = onAuthorizationFail
        apiTalker.fetchToken(grantType, uId, secret, this::onServerSendTokenResource)
    }

    private fun onServerSendTokenResource(resource: Resource<Token>) {
        getResourceData(resource)?.let {
            onGetTheToken?.invoke(mapper.mapToken(it))
        }
    }

    fun fetchUserByLogin(
        login: String,
        accessToken: String,
        onGetTheUser: (DomainUser) -> Unit,
        onLoginError: (Int?) -> Unit
    ) {
        this.onGetTheUser = onGetTheUser
        this.onServerFinishedError = onLoginError
        apiTalker.fetchUser(login, accessToken, this::onServerSendUserResource)
    }

    private fun onServerSendUserResource(resource: Resource<User>) {
        getResourceData(resource)?.let {
            onGetTheUser?.invoke(mapper.mapUser(it))
        }
    }

    fun fetchCoalitionIdsInOrderOfCreationByLogin(
        login: String,
        accessToken: String,
        onGetOrderedCoalitionIds: (List<DomainCoalitionId>) -> Unit,
        onResponseError: (Int?) -> Unit
    ) {
        this.onGetOrderedCoalitionIds = onGetOrderedCoalitionIds
        this.onServerFinishedError = onResponseError
        apiTalker.fetchCoalitionsIdsInOrderOfCreationByLogin(
            login,
            accessToken,
            this::onServerSendCoalitionIdResource
        )
    }

    private fun onServerSendCoalitionIdResource(resource: Resource<List<CoalitionId>>) {
        getResourceData(resource)?.let {
            onGetOrderedCoalitionIds?.invoke(mapper.mapCoalitionIds(it))
        }
    }

    fun fetchCoalitionsByLogin(
        login: String,
        accessToken: String,
        onGetCoalitions: (List<DomainCoalition>) -> Unit,
        onResponseError: (Int?) -> Unit
    ) {
        this.onGetCoalitions = onGetCoalitions
        this.onServerFinishedError = onResponseError
        apiTalker.fetchCoalitionsByLogin(login, accessToken, this::onServerSendCoalitionsResource)
    }

    private fun onServerSendCoalitionsResource(resource: Resource<List<Coalition>>) {
        getResourceData(resource)?.let {
            onGetCoalitions?.invoke(mapper.mapCoalitions(it))
        }
    }

    fun fetchCoursesListInOrderOfCreationByLogin(
        login: String,
        accessToken: String,
        onGetOrderedCourses: (List<DomainCourse>) -> Unit,
        onResponseError: (Int?) -> Unit
    ) {
        this.onGetOrderedCourses = onGetOrderedCourses
        this.onServerFinishedError = onResponseError
        apiTalker.fetchCoursesInOrderOfCreationByLogin(
            login,
            accessToken,
            this::onServerSendCoursesResource
        )
    }

    private fun onServerSendCoursesResource(resource: Resource<List<Course>>) {
        getResourceData(resource)?.let {
            onGetOrderedCourses?.invoke(mapper.mapCourses(it))
        }
    }

    private fun <T> getResourceData(res: Resource<T>): T? =
        if (res.status == Status.SUCCESS) {
            res.data
        } else {
            onResponseError(res.message)
            null
        }

    private fun onResponseError(message: ErrorMessage?) {
        message?.let {
            Log.e(null, it.message)
            onServerFinishedError?.invoke(it.errorCode)
        }
    }
}