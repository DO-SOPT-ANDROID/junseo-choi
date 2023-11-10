package org.sopt.dosopttemplate

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import org.sopt.dosopttemplate.databinding.ItemMineBinding


class MineViewHolder(private val binding: ItemMineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(userInfo: UserInfo) {
        binding.ivProfilePicture.load(userInfo.profileImage) {
            crossfade(true)
            error(R.drawable.ic_mypet)
            transformations(RoundedCornersTransformation())
        }
        binding.tvName.text = userInfo.nickName
        binding.tvSelfDescription.text = userInfo.self_description
    }
}