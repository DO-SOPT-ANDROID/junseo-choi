package org.sopt.dosopttemplate.ui.main.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.FragmentHomeBinding
import org.sopt.dosopttemplate.network.dto.res.FriendListResponse
import org.sopt.dosopttemplate.network.dto.res.UserInfoResponse
import org.sopt.dosopttemplate.ui.main.MainActivity
import org.sopt.dosopttemplate.ui.main.home.friendpage.FriendAdapter
import org.sopt.dosopttemplate.ui.main.home.friendpage.FriendPageFragment
import org.sopt.dosopttemplate.util.showSnackbar

interface ScrollableFragment {
    fun scrollToTop()
}

class HomeFragment : Fragment(), ScrollableFragment {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel by viewModels<HomeViewModel>()
    private val mainActivity by lazy { activity as MainActivity }

    private val friendAdapter: FriendAdapter by lazy {
        FriendAdapter(
            requireContext(),
        )
    }

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFriendInfo()
        viewModel.getUserInfo(mainActivity.getUserId())
        binding.rvFriends.adapter = friendAdapter

        binding.rvFriends.layoutManager = LinearLayoutManager(
            requireContext(),
            if (isScreenInPortraitMode()) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
            false
        )

        viewModel.friendList.observe(viewLifecycleOwner) { friendList ->
            friendAdapter.setFriendsList(friendList)
        }

        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            friendAdapter.setUserInfo(userInfo ?: UserInfoResponse())
        }

        binding.fabHomeEdit.setOnClickListener {
            binding.root.showSnackbar("3주차 과제 완료 ^0^")
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun scrollToTop() {
        binding.rvFriends.smoothScrollToPosition(0)
    }

    private fun updateRecyclerViewLayout() {
        val layoutManager = LinearLayoutManager(
            requireContext(),
            if (isScreenInPortraitMode()) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvFriends.layoutManager = layoutManager
    }

    private fun isScreenInPortraitMode(): Boolean {
        val orientation = resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_PORTRAIT
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        updateRecyclerViewLayout()
    }
}