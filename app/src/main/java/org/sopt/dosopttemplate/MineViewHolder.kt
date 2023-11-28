package org.sopt.dosopttemplate

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import org.sopt.dosopttemplate.databinding.ItemMineBinding
import org.sopt.dosopttemplate.domain.model.UserInfoBundle


class MineViewHolder(private val binding: ItemMineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(userData: UserInfoBundle) {
        binding.ivProfilePicture.load(userData.profileImage) {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }
        binding.tvName.text = userData.nickName
        binding.tvSelfDescription.text = userData.self_description
    }
}