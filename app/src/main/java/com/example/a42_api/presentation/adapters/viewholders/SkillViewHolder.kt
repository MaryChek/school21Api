package com.example.a42_api.presentation.adapters.viewholders

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a42_api.R
import com.example.a42_api.databinding.ItemSkillBinding
import com.example.a42_api.presentation.models.Skill

class SkillViewHolder(private val binding: ItemSkillBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(skill: Skill, drawable: Drawable?, setColorToTextByRes: (TextView, Int) -> Unit) {
        binding.tvNameSkill.text = skill.name
        binding.progressSkill.progressLevel.progress = skill.levelProgress
        binding.progressSkill.tvProgressLevel.text =
            binding.root.resources.getString(R.string.skill_value, skill.level)

        binding.progressSkill.cardProgressLevel.background = drawable

        setColorToTextByRes(binding.progressSkill.tvProgressLevel, skill.progressTextColorResId)
    }
}