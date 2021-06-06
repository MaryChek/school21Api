package com.example.a42_api.presentation.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import com.example.a42_api.databinding.ItemSkillBinding
import com.example.a42_api.presentation.adapters.diffutilcallbacks.SkillItemDiff
import com.example.a42_api.presentation.adapters.viewholders.SkillViewHolder
import com.example.a42_api.presentation.models.Skill

class SkillsListAdapter(
    private val progressLevelBackgroundDrawable: Drawable?,
    private val setColorToTextByRes: (TextView, Int) -> Unit
) : ListAdapter<Skill, SkillViewHolder>(SkillItemDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SkillViewHolder(
            ItemSkillBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        holder.bind(getItem(position), progressLevelBackgroundDrawable, setColorToTextByRes)
    }
}