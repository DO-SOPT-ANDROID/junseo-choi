package org.sopt.dosopttemplate.ui.main.home

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.ItemFriendBinding
import org.sopt.dosopttemplate.databinding.ItemFriendHorizontalBinding
import org.sopt.dosopttemplate.databinding.ItemMineBinding
import org.sopt.dosopttemplate.databinding.ItemMineHorizontalBinding
import org.sopt.dosopttemplate.domain.model.Friend
import org.sopt.dosopttemplate.domain.model.FriendDto
import org.sopt.dosopttemplate.domain.model.UserInfoBundle

class FriendAdapter(
    private val context: Context,
    private val userData: UserInfoBundle,
    private val onProfileClickListener: (Friend) -> Unit,
) : ListAdapter<Friend, RecyclerView.ViewHolder>(DiffCallback()) {
    private val inflater by lazy { LayoutInflater.from(context) }

    companion object {
        const val VIEW_TYPE_MINE = 0
        const val VIEW_TYPE_FRIEND = 1
        const val VIEW_TYPE_MINE_HORIZONTAL = 2
        const val VIEW_TYPE_FRIEND_HORIZONTAL = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MINE -> {
                val binding = ItemMineBinding.inflate(inflater, parent, false)
                MineViewHolder(binding)
            }

            VIEW_TYPE_FRIEND -> {
                val binding = ItemFriendBinding.inflate(inflater, parent, false)
                FriendViewHolder(binding)
            }

            VIEW_TYPE_FRIEND_HORIZONTAL -> {
                val binding = ItemFriendHorizontalBinding.inflate(inflater, parent, false)
                FriendHorizontalViewHolder(binding)
            }

            VIEW_TYPE_MINE_HORIZONTAL -> {
                val binding = ItemMineHorizontalBinding.inflate(inflater, parent, false)
                MineHorizontalViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MineViewHolder -> {
                holder.onBind(userData)
            }

            is FriendViewHolder -> {
                val friend = getItem(position)
                friend?.let { holder.onBind(it) }

                holder.itemView.setOnClickListener {
                    friend?.let { onProfileClickListener.invoke(it) }
                }
            }

            is MineHorizontalViewHolder -> {
                holder.onBind(userData)
            }

            is FriendHorizontalViewHolder -> {
                val friend = getItem(position)
                friend?.let { holder.onBind(it) }

                holder.itemView.setOnClickListener {
                    friend?.let { onProfileClickListener.invoke(it) }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                if (isScreenInPortraitMode()) VIEW_TYPE_MINE
                else VIEW_TYPE_MINE_HORIZONTAL
            }

            else -> {
                if (isScreenInPortraitMode()) VIEW_TYPE_FRIEND
                else VIEW_TYPE_FRIEND_HORIZONTAL
            }
        }
    }


    private fun isScreenInPortraitMode(): Boolean {
        val orientation = context.resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_PORTRAIT
    }


    fun setFriendsList(allFriends: List<FriendDto>) {
        val newList = mutableListOf<Friend>()
        val userInfoFriend = Friend(
            profileImage = userData.profileImage,
            userId = userData.userName,
            name = userData.nickName,
            description = userData.self_description,
        )

        newList.add(userInfoFriend)
        newList.addAll(allFriends.map { it.toFriend() })
        submitList(newList)
    }

    fun FriendDto.toFriend(): Friend {
        return Friend(
            profileImage = this.avatar,
            userId = this.email,
            name = this.firstName,
            description = "",
        )
    }
}

class DiffCallback : DiffUtil.ItemCallback<Friend>() {
    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem == newItem
    }
}

class FriendViewHolder(
    private val binding: ItemFriendBinding,
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

class MineHorizontalViewHolder(
    private val binding: ItemMineHorizontalBinding
) :
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