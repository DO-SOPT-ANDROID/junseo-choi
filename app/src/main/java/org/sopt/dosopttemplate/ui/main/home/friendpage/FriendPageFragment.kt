package org.sopt.dosopttemplate.ui.main.home.friendpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.FragmentFriendPageBinding
import org.sopt.dosopttemplate.network.dto.res.FriendListResponse
import org.sopt.dosopttemplate.ui.main.home.HomeViewModel

class FriendPageFragment : Fragment() {
    private lateinit var binding: FragmentFriendPageBinding
    private val viewModel by viewModels<HomeViewModel>()

    companion object {
        private const val ARG_FRIEND_ID = "arg_friend_id"

        fun newInstance(friendId: Int): FriendPageFragment {
            val args = Bundle()
            args.putInt(ARG_FRIEND_ID, friendId)
            val fragment = FriendPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFriendPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }

        val friendId = arguments?.getInt(ARG_FRIEND_ID)
        friendId?.let {
            viewModel.getFriendInfo(1)
            viewModel.friendList.observe(viewLifecycleOwner) { friendList ->
                friendList.find { it.id == friendId }?.let { friend ->
                    displayFriendInfo(friend)
                }
            }
        }
    }

    private fun displayFriendInfo(friend: FriendListResponse.Data) {
        binding.tvName.text = friend.firstName
        binding.tvId.text = friend.email
        binding.ivProfilePicture.load(friend.avatar) {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }
    }
}
