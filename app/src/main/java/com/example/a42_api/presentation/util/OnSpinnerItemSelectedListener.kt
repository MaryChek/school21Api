package com.example.a42_api.presentation.util

import android.view.View
import android.widget.AdapterView

open class OnSpinnerItemSelectedListener : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}