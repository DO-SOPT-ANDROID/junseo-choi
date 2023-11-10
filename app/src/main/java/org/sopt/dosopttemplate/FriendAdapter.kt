package org.sopt.dosopttemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.dosopttemplate.databinding.ItemBirthdayBinding
import org.sopt.dosopttemplate.databinding.ItemFriendBinding
import org.sopt.dosopttemplate.databinding.ItemMineBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class FriendAdapter(
    context: Context,
    private val userInfo: UserInfo,
    private val viewModel: HomeViewModel
) : ListAdapter<Friend, RecyclerView.ViewHolder>(DiffCallback()) {
    private val inflater by lazy { LayoutInflater.from(context) }

    companion object {
        const val VIEW_TYPE_MINE = 0
        const val VIEW_TYPE_FRIEND = 1
        const val VIEW_TYPE_BIRTHDAY = 2
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

            VIEW_TYPE_BIRTHDAY -> {
                val binding = ItemBirthdayBinding.inflate(inflater, parent, false)
                BirthdayViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MineViewHolder -> {
                holder.onBind(userInfo)
            }

            is FriendViewHolder -> {
                if (position == 0) {
                    // 뿜빰
                } else {
                    val birthdayFriends = viewModel.getBirthdayFriends()
                    val otherFriends = viewModel.getOtherFriends()
                    val friend = if (position <= birthdayFriends.size) {
                        birthdayFriends.getOrNull(position - 1)
                    } else {
                        otherFriends.getOrNull(position - birthdayFriends.size - 1)
                    }

                    friend?.let { holder.onBind(it) }
                }
            }

            is BirthdayViewHolder -> {
                if (position <= viewModel.getBirthdayFriends().size) {
                    val birthdayFriend = viewModel.getBirthdayFriends()[position - 1]
                    holder.onBind(birthdayFriend)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_MINE
            in 1..viewModel.getBirthdayFriends().size -> VIEW_TYPE_BIRTHDAY
            else -> VIEW_TYPE_FRIEND
        }
    }

    fun setFriendsLists(birthdayFriends: List<Friend>, otherFriends: List<Friend>) {
        val newList = mutableListOf<Friend>()
        val userInfoFriend = Friend(
            profileImage = R.drawable.ic_ex0,
            userId = userInfo.userId,
            name = userInfo.nickName,
            self_description = userInfo.self_description,
            birthday = userInfo.birthday
        )

        newList.add(userInfoFriend)
        newList.addAll(birthdayFriends)
        newList.addAll(otherFriends)
        submitList(newList)
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
