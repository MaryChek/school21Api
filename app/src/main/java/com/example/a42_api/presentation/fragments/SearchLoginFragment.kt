package com.example.a42_api.presentation.fragments

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.a42_api.App
import com.example.a42_api.R
import com.example.a42_api.presentation.contract.SearchLoginContract
import com.example.a42_api.databinding.FragmentSearchLoginBinding
import com.example.a42_api.domain.interactor.UserInteractor
import com.example.a42_api.presentation.mapper.SearchLoginMapper
import com.example.a42_api.presentation.models.User
import com.example.a42_api.presentation.util.KeyForLogin
import com.example.a42_api.presentation.presenters.SearchLoginPresenter

class SearchLoginFragment : BaseLoginFragment(), SearchLoginContract.View {
    private var binding: FragmentSearchLoginBinding? = null
    private lateinit var presenter: SearchLoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val app: App = (requireActivity().applicationContext as App)
        val interactor: UserInteractor = app.interactor
        presenter = SearchLoginPresenter(this, interactor, SearchLoginMapper())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigationToolBar()
        initButtonSearchListener()
        initEditorActionListener()
        setOnRetryConnectionClickListener()
    }

    private fun initNavigationToolBar() {
        binding?.toolbar?.setupWithNavController(findNavController())
    }

    private fun initButtonSearchListener() {
        binding?.searchButton?.setOnClickListener {
            presenter.onQueryTextSubmit(binding?.etLogin?.text.toString())
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            activity?.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun initEditorActionListener() {
        binding?.etLogin?.let {
            it.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
                if (event != null &&
                    (actionId == EditorInfo.IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    it.clearFocus()
                    it.isCursorVisible = false
                    hideKeyboard()
                    return@OnEditorActionListener true
                } else {
                    return@OnEditorActionListener false
                }
            })
        }
    }

    private fun setOnRetryConnectionClickListener() {
        binding?.layoutConnectionError?.buttonRetryConnection?.setOnClickListener {
            presenter.onRetryButtonClick()
        }
    }

    override fun showProgressBar(show: Boolean) {
        binding?.apply {
            progressIndicator.updateVisibility(show)
            searchButton.updateVisibility(!show)
            etLogin.isCursorVisible = !show
            etLogin.isClickable = !show
        }
    }

    override fun showDetailByLogin(user: User) =
        findNavController().navigate(
            R.id.action_searchLoginFragment_to_loginDetailsFragment,
            bundleOf(KeyForLogin.key to user)
        )

    override fun showLoginError(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

    override fun showConnectionErrorLayout(show: Boolean) {
        binding?.searchButton?.updateVisibility(!show)
        binding?.progressIndicator?.updateVisibility(!show)
        binding?.layoutConnectionError?.root?.updateVisibility(show)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}