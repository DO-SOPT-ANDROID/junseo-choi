package org.sopt.dosopttemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.dosopttemplate.databinding.ItemBirthdayBinding
import org.sopt.dosopttemplate.databinding.ItemFriendBinding
import org.sopt.dosopttemplate.databinding.ItemMineBinding

class FriendAdapter(
    context: Context,
    private val userInfo: UserInfo,
    private val viewModel: HomeViewModel,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater by lazy { LayoutInflater.from(context) }

    private val viewTypeMine = 0
    private val viewTypeFriend = 1
    private val viewTypeBirthday = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            viewTypeMine -> {
                val binding = ItemMineBinding.inflate(inflater, parent, false)
                MineViewHolder(binding)
            }

            viewTypeFriend -> {
                val binding = ItemFriendBinding.inflate(inflater, parent, false)
                FriendViewHolder(binding)
            }

            viewTypeBirthday -> {
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
                    // Do nothing for the header item (userInfo)
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

    override fun getItemCount(): Int {
        val birthdayFriends = viewModel.getBirthdayFriends()
        val otherFriends = viewModel.getOtherFriends()
        return birthdayFriends.size + otherFriends.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> viewTypeMine
            in 1..viewModel.getBirthdayFriends().size -> viewTypeBirthday
            else -> viewTypeFriend
        }
    }

    fun setFriendsLists(birthdayFriends: List<Friend>, otherFriends: List<Friend>) {
        notifyDataSetChanged()
    }
}
