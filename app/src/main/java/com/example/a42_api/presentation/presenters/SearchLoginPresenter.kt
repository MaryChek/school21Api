package com.example.a42_api.presentation.presenters

import com.example.a42_api.domain.models.User as DomainUser
import com.example.a42_api.domain.interactor.UserInteractor
import com.example.a42_api.presentation.contract.SearchLoginContract
import com.example.a42_api.presentation.mapper.SearchLoginMapper

class SearchLoginPresenter(
    private val view: SearchLoginContract.View,
    private val interactor: UserInteractor,
    private val mapper: SearchLoginMapper,
) : SearchLoginContract.Presenter {
    private lateinit var login: String

    override fun onQueryTextSubmit(text: String) =
        if (text.isNotBlank()) {
            this.login = text
            fetchUserByLogin()
            view.showProgressBar(true)
        } else {
            view.showLoginError(EMPTY_LOGIN)
        }

    private fun fetchUserByLogin() =
        interactor.fetchUserByLogin(
            login,
            this::onGetTheUser,
            this::onServerError
        )

    private fun onGetTheUser(user: DomainUser) {
        view.showProgressBar(false)
        view.showDetailByLogin(mapper.mapUser(user))
    }

    private fun onServerError(errorCode: Int?) =
        when (errorCode) {
            LOGIN_ERROR_CODE -> {
                view.showProgressBar(false)
                view.showLoginError(LOGIN_ERROR)
            }
            else ->
                view.showConnectionErrorLayout(true)
        }

    override fun onRetryButtonClick() =
        view.showConnectionErrorLayout(false)

    companion object {
        private const val LOGIN_ERROR = "Such login does not exist"
        private const val EMPTY_LOGIN = "Login is empty"
        private const val LOGIN_ERROR_CODE = 404
    }
}