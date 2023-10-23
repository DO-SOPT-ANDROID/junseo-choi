package org.sopt.dosopttemplate

import androidx.recyclerview.widget.RecyclerView
import org.sopt.dosopttemplate.databinding.ItemBirthdayBinding

class BirthdayViewHolder(private val binding: ItemBirthdayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(friendData: Friend) {
        binding.ivProfilePicture.setImageResource(friendData.profileImage)
        binding.tvName.text = friendData.name
        binding.tvSelfDescription.text = friendData.self_description
    }
}
