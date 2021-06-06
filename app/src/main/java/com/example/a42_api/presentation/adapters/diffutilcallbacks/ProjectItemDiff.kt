package com.example.a42_api.presentation.adapters.diffutilcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.a42_api.presentation.models.Project

class ProjectItemDiff : DiffUtil.ItemCallback<Project>() {
    override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean =
        oldItem.finalMark == newItem.finalMark && oldItem.name == newItem.name

}