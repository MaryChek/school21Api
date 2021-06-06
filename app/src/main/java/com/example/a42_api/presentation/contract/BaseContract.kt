package com.example.a42_api.presentation.contract

interface BaseContract {
    interface View {
        fun showConnectionErrorLayout(show: Boolean)

        fun showProgressBar(show: Boolean)
    }

    interface Presenter {
        fun onRetryButtonClick()
    }
}