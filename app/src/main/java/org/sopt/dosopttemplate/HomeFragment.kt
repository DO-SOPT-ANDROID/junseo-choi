package org.sopt.dosopttemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.sopt.dosopttemplate.databinding.FragmentHomeBinding

class HomeFragment(private val userInfo: UserInfo) : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel by viewModels<HomeViewModel>()

    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding) { "바인딩 객체가 생성되지 않았다. 생성하고 불러라 임마!" }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val friendAdapter = FriendAdapter(requireContext(), userInfo, viewModel)
        binding.rvFriends.adapter = friendAdapter

        val birthdayFriends = viewModel.getBirthdayFriends()
        val otherFriends = viewModel.getOtherFriends()

        friendAdapter.setFriendsLists(birthdayFriends, otherFriends)

        binding.fabHomeEdit.setOnClickListener {
            binding.root.showSnackbar("3주차 과제 완료 ^0^")
        }
    }
}