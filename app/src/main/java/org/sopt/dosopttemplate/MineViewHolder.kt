package org.sopt.dosopttemplate

import androidx.recyclerview.widget.RecyclerView
import org.sopt.dosopttemplate.databinding.ItemMineBinding


class MineViewHolder(private val binding: ItemMineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(userInfo: UserInfo) {
        binding.ivProfilePicture.setImageResource(R.drawable.default_profile)
        binding.tvName.text = userInfo.nickName
        binding.tvSelfDescription.text = userInfo.self_description
    }
}