package com.example.a42_api.presentation.adapters.diffutilcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.a42_api.presentation.models.Skill

class SkillItemDiff : DiffUtil.ItemCallback<Skill>() {
    override fun areItemsTheSame(oldItem: Skill, newItem: Skill): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Skill, newItem: Skill): Boolean =
        oldItem.level == newItem.level && oldItem.name == newItem.name
}