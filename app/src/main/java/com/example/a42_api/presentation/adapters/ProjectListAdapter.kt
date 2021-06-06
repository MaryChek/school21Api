package com.example.a42_api.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import com.example.a42_api.databinding.ItemProjectBinding
import com.example.a42_api.presentation.adapters.diffutilcallbacks.ProjectItemDiff
import com.example.a42_api.presentation.adapters.viewholders.ProjectViewHolder
import com.example.a42_api.presentation.models.Project

class ProjectListAdapter(
    private val updateVisibility: (View, Boolean) -> Unit,
    private val setColorToTextByRes: (TextView, Int) -> Unit
) :
    ListAdapter<Project, ProjectViewHolder>(ProjectItemDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProjectViewHolder(
            ItemProjectBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(getItem(position), updateVisibility, setColorToTextByRes)
    }
}