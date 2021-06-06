package com.example.a42_api.data.api

import com.example.a42_api.data.api.models.*

class School42ApiTalker(
    clientConfig: ClientConfig,
    service: ClientApiCreator,
    private val client: School42Api = service.createApiClient(clientConfig)
) : BaseApiTalker() {

    fun fetchToken(
        grantType: String,
        uId: String,
        secret: String,
        onServiceCallAnswer: (Resource<Token>) -> Unit
    ) {
        getResult(client.getToken(grantType, uId, secret), onServiceCallAnswer)
    }

    fun fetchUser(login: String, token: String, onServiceCallAnswer: (Resource<User>) -> Unit) {
        getResult(client.getUser(token, login), onServiceCallAnswer)
    }

    fun fetchCoalitionsIdsInOrderOfCreationByLogin(
        login: String,
        token: String,
        onServiceCallAnswer: (Resource<List<CoalitionId>>) -> Unit
    ) {
        getResult(
            client.getCoalitionsIdsInOrderOfCreationByLogin(token, login),
            onServiceCallAnswer
        )
    }

    fun fetchCoalitionsByLogin(
        login: String,
        token: String,
        onServiceCallAnswer: (Resource<List<Coalition>>) -> Unit
    ) {
        getResult(client.getCoalitionByLogin(token, login), onServiceCallAnswer)
    }

    fun fetchCoursesInOrderOfCreationByLogin(
        login: String,
        token: String,
        onServiceCallAnswer: (Resource<List<Course>>) -> Unit
    ) {
        getResult(client.getCoursesListInOrderOfCreationByLogin(token, login), onServiceCallAnswer)
    }
}