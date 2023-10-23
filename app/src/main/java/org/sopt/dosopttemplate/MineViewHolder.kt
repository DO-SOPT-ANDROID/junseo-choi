package org.sopt.dosopttemplate

import androidx.recyclerview.widget.RecyclerView
import org.sopt.dosopttemplate.databinding.ItemMineBinding


class MineViewHolder(private val binding: ItemMineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(mineData: Mine, userInfo: UserInfo) {
        binding.ivMypageProfilePicture.setImageResource(R.drawable.default_profile)
        binding.tvMypageViewnickname.text = userInfo.nickName
        binding.tvMypageSelfDescription.text = binding.root.context.getString(R.string.test_text)
    }
}