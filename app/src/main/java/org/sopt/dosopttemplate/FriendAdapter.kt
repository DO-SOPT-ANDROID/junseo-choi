package org.sopt.dosopttemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.dosopttemplate.databinding.ItemFriendBinding
import org.sopt.dosopttemplate.databinding.ItemMineBinding

class FriendAdapter(
    context: Context,
    private val userData: UserInfoBundle,
) : ListAdapter<Friend, RecyclerView.ViewHolder>(DiffCallback()) {
    private val inflater by lazy { LayoutInflater.from(context) }

    companion object {
        const val VIEW_TYPE_MINE = 0
        const val VIEW_TYPE_FRIEND = 1
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
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_MINE
            else -> VIEW_TYPE_FRIEND
        }
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