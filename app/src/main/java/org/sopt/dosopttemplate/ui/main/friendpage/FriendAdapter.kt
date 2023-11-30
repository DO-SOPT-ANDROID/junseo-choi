package org.sopt.dosopttemplate.ui.main.friendpage

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
import org.sopt.dosopttemplate.network.dto.res.FriendListResponse
import org.sopt.dosopttemplate.network.dto.res.UserInfoResponse

class FriendAdapter(
    private val context: Context,
    private val itemClickListener: (FriendListResponse.Data) -> Unit,
) : ListAdapter<FriendListResponse.Data, RecyclerView.ViewHolder>(DiffCallback()) {
    private val inflater by lazy { LayoutInflater.from(context) }

    private var _userInfo: UserInfoResponse? = null

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
                FriendViewHolder(binding, itemClickListener)
            }

            VIEW_TYPE_FRIEND_HORIZONTAL -> {
                val binding = ItemFriendHorizontalBinding.inflate(inflater, parent, false)
                FriendHorizontalViewHolder(binding, itemClickListener)
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
                holder.onBind(_userInfo ?: UserInfoResponse())
            }

            is FriendViewHolder -> {
                val friend = getItem(position)
                friend?.let { holder.onBind(it) }
            }

            is MineHorizontalViewHolder -> {
                holder.onBind(_userInfo ?: UserInfoResponse())
            }

            is FriendHorizontalViewHolder -> {
                val friend = getItem(position)
                friend?.let { holder.onBind(it) }
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

    fun setUserInfo(userInfo: UserInfoResponse) {
        _userInfo = userInfo
        submitList(currentList)
    }

    fun setFriendsList(allFriends: List<FriendListResponse.Data>) {
        submitList(allFriends)
    }
}

class DiffCallback : DiffUtil.ItemCallback<FriendListResponse.Data>() {
    override fun areItemsTheSame(
        oldItem: FriendListResponse.Data,
        newItem: FriendListResponse.Data,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FriendListResponse.Data,
        newItem: FriendListResponse.Data,
    ): Boolean {
        return oldItem == newItem
    }
}

class FriendViewHolder(
    private val binding: ItemFriendBinding,
    private val itemClickListener: (FriendListResponse.Data) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(friendData: FriendListResponse.Data) {
        binding.root.setOnClickListener { itemClickListener(friendData) }
        binding.ivProfilePicture.load(friendData.avatar) {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }

        binding.tvName.text = friendData.firstName
        binding.tvSelfDescription.text = ""
    }
}


class FriendHorizontalViewHolder(
    private val binding: ItemFriendHorizontalBinding,
    private val itemClickListener: (FriendListResponse.Data) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(friendData: FriendListResponse.Data) {
        binding.root.setOnClickListener { itemClickListener(friendData) }
        binding.ivProfilePicture.load(friendData.avatar) {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }

        binding.tvName.text = friendData.firstName
        binding.tvSelfDescription.text = ""
    }
}

class MineViewHolder(private val binding: ItemMineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(userInfo: UserInfoResponse) {
        binding.ivProfilePicture.load("") {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }

        binding.tvName.text = userInfo.nickname
        binding.tvSelfDescription.text = ""
    }
}

class MineHorizontalViewHolder(
    private val binding: ItemMineHorizontalBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(userInfo: UserInfoResponse) {
        binding.ivProfilePicture.load("") {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }

        binding.tvName.text = userInfo.nickname
        binding.tvSelfDescription.text = ""
    }
}