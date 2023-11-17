package org.sopt.dosopttemplate

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import org.sopt.dosopttemplate.databinding.ItemFriendHorizontalBinding


class FriendHorizontalViewHolder(
    private val binding: ItemFriendHorizontalBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(friendData: Friend) {
        binding.ivProfilePicture.load(friendData.profileImage) {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }

        binding.tvName.text = friendData.name
        binding.tvSelfDescription.text = friendData.description
    }
}
