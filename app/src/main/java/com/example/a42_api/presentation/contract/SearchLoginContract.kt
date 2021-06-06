package com.example.a42_api.presentation.contract

import com.example.a42_api.presentation.models.User

interface SearchLoginContract {
    interface View : BaseContract.View {
        fun showDetailByLogin(user: User)

        fun showLoginError(message: String)
    }

    interface Presenter : BaseContract.Presenter {
        fun onQueryTextSubmit(text: String)
    }
}