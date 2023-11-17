package org.sopt.dosopttemplate

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.dosopttemplate.databinding.FragmentHomeBinding

interface ScrollableFragment {
    fun scrollToTop()
}

class HomeFragment : Fragment(), ScrollableFragment {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel by viewModels<HomeViewModel>()

    private val friendAdapter: FriendAdapter by lazy {
        FriendAdapter(
            requireContext(),
            arguments?.extractUserData()!!
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

        binding.rvFriends.adapter = friendAdapter

        binding.rvFriends.layoutManager = LinearLayoutManager(
            requireContext(),
            if (isScreenInPortraitMode()) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
            false
        )

        viewModel.allFriends.observe(viewLifecycleOwner) { allFriends ->
            friendAdapter.setFriendsList(allFriends)
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