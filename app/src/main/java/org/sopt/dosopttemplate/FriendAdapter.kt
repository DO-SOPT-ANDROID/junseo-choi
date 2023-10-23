package org.sopt.dosopttemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.dosopttemplate.databinding.ItemFriendBinding
import org.sopt.dosopttemplate.databinding.ItemMineBinding

class FriendAdapter(context: Context, private val userInfo: UserInfo) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater by lazy { LayoutInflater.from(context) }
    private var personList: List<Person> = emptyList()


    private val VIEW_TYPE_MINE = 1
    private val VIEW_TYPE_FRIEND = 2

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
                val mine = personList[position] as Mine
                holder.onBind(mine, userInfo)
            }

            is FriendViewHolder -> {
                val friend = personList[position] as Friend
                holder.onBind(friend)
            }
        }
    }

    override fun getItemCount() = personList.size

    override fun getItemViewType(position: Int): Int {
        return when (personList[position]) {
            is Mine -> VIEW_TYPE_MINE
            is Friend -> VIEW_TYPE_FRIEND
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun setPersonList(personList: List<Person>) {
        this.personList = listOf(
            Mine(
                profileImage = R.drawable.default_profile,
                name = userInfo.nickName,  // 사용자 정보에서 가져온 데이터
                self_description = userInfo.self_description,  // 사용자 정보에서 가져온 데이터
                birthday = userInfo.birthday
            )
        ) + personList.toList()
        notifyDataSetChanged()
    }


}
