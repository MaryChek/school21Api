package com.example.a42_api.presentation.adapters.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a42_api.databinding.ItemProjectBinding
import com.example.a42_api.presentation.models.Project

class ProjectViewHolder(
    private val binding: ItemProjectBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        project: Project,
        updateVisibility: (View, Boolean) -> Unit,
        setColorToTextByRes: (TextView, Int) -> Unit
    ) {
        binding.tvProjectName.text = project.name
        binding.tvFinalMark.text = project.finalMark.toString()
        setColorToTextByRes(binding.tvFinalMark, project.finalMarkColorResId)
        updateVisibility(binding.imgInProgress, project.isInProgressVisible)
        updateVisibility(binding.imgFail, project.isInFailVisible)
        updateVisibility(binding.imgSuccess, project.isInSuccessVisible)
        updateVisibility(binding.tvFinalMark, project.isFinalMarkVisible)
    }
}