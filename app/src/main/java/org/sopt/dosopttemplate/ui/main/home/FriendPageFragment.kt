package org.sopt.dosopttemplate.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.RoundedCornersTransformation
import org.sopt.dosopttemplate.domain.model.Friend
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.FragmentFriendPageBinding

class FriendPageFragment : Fragment() {

    private lateinit var binding: FragmentFriendPageBinding

    companion object {
        private const val ARG_FRIEND = "arg_friend"

        fun newInstance(friend: Friend): FriendPageFragment {
            val args = Bundle().apply {
                putParcelable(ARG_FRIEND, friend)
            }
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

        val friend = arguments?.getParcelable<Friend>(ARG_FRIEND)
        friend?.let {
            displayFriendInfo(it)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
    }

    private fun displayFriendInfo(friend: Friend) {
        binding.tvName.text = friend.name
        binding.tvId.text = friend.userId
        binding.ivProfilePicture.load(friend.profileImage) {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }
    }
}
