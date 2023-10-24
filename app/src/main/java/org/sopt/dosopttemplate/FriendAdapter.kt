package org.sopt.dosopttemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.dosopttemplate.databinding.ItemBirthdayBinding
import org.sopt.dosopttemplate.databinding.ItemFriendBinding
import org.sopt.dosopttemplate.databinding.ItemMineBinding
import java.time.LocalDate

class FriendAdapter(context: Context, private val userInfo: UserInfo) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater by lazy { LayoutInflater.from(context) }
    private var personList: List<Person> = emptyList()

    private val viewTypeMine = 1
    private val viewTypeFriend = 2
    private val viewTypeBirthday = 3

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
                val friend = personList[position] as Friend
                holder.onBind(friend)
            }
            is BirthdayViewHolder -> {
                val friend = personList[position] as Friend
                holder.onBind(friend)
            }
        }
    }

    override fun getItemCount() = personList.size

    override fun getItemViewType(position: Int): Int {
        return when (personList[position]) {
            is Mine -> viewTypeMine
            is Friend -> {
                if (isBirthdayToday(personList[position] as Friend)) {
                    viewTypeBirthday
                } else {
                    viewTypeFriend
                }
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    private fun isBirthdayToday(friend: Friend): Boolean {
        val today = LocalDate.now()
        return friend.birthday != null && friend.birthday.monthValue == today.monthValue && friend.birthday.dayOfMonth == today.dayOfMonth
    }

    fun setPersonList(personList: List<Person>) {
        val sortedList = sortFriendsByBirthday(personList)

        this.personList = listOf(
            Mine(
                profileImage = R.drawable.default_profile,
                name = userInfo.nickName,
                self_description = userInfo.self_description,
                birthday = userInfo.birthday
            )
        ) + sortedList

        notifyDataSetChanged()
    }

    private fun sortFriendsByBirthday(personList: List<Person>): List<Person> {
        val sortedList = personList.toMutableList()
        sortedList.sortWith(compareByDescending { it is Friend && isBirthdayToday(it) })
        return sortedList.toList()
    }
}






