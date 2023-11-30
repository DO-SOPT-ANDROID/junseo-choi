package org.sopt.dosopttemplate.ui.main.home.friendpage

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
import org.sopt.dosopttemplate.network.dto.res.FriendListResponse

class FriendPageFragment : Fragment() {

    private lateinit var binding: FragmentFriendPageBinding

    companion object {
        private const val ARG_FRIEND = "arg_friend"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
    }

    private fun displayFriendInfo(friend: FriendListResponse.Data) {
        binding.tvName.text = friend.firstName
        binding.tvId.text = friend.id.toString()
        binding.ivProfilePicture.load(friend.avatar) {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }
    }
}