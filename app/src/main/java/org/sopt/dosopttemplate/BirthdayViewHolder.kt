package org.sopt.dosopttemplate

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import org.sopt.dosopttemplate.databinding.ItemBirthdayBinding

class BirthdayViewHolder(private val binding: ItemBirthdayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(friendData: Friend) {
        with(binding) { // 이렇게 binding을 할 수도 있음
            binding.ivProfilePicture.load(friendData.profileImage) {
                crossfade(true)
                error(R.drawable.ic_default_image)
                transformations(RoundedCornersTransformation())
            }
            tvName.text = friendData.name
            tvSelfDescription.text = friendData.self_description
        }
    }

}
