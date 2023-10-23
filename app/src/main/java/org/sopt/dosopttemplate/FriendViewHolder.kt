package org.sopt.dosopttemplate

import androidx.recyclerview.widget.RecyclerView
import org.sopt.dosopttemplate.databinding.ItemFriendBinding


class FriendViewHolder(private val binding: ItemFriendBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(friendData: Friend) {
        binding.ivMypageProfilePicture.setImageResource(friendData.profileImage)
        binding.tvName.text = friendData.name
        binding.tvSelfDescription.text = friendData.self_description
    }
}
