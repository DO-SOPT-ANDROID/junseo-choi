package org.sopt.dosopttemplate.ui.main.mypage

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.RoundedCornersTransformation
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.domain.model.UserInfoBundle
import org.sopt.dosopttemplate.databinding.FragmentMyPageBinding
import org.sopt.dosopttemplate.ui.signin.SharedPreferencesKeys
import org.sopt.dosopttemplate.ui.signin.SigninActivity
import org.sopt.dosopttemplate.util.extractUserData

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding
        get() = requireNotNull(_binding) { "바인딩 객체가 생성되지 않았다. 생성하고 불러라 임마!" }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(
            SharedPreferencesKeys.USER_INFO,
            AppCompatActivity.MODE_PRIVATE
        )

        val userInfoBundle = arguments.extractUserData()

        displayUserInfo(userInfoBundle!!)

        setupSignOutButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayUserInfo(userInfoBundle: UserInfoBundle) {
        binding.ivProfilePicture.load(userInfoBundle.profileImage) {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }
        binding.tvId.text = userInfoBundle.userName
        binding.tvName.text = userInfoBundle.nickName
        binding.tvMBTI.text = userInfoBundle.mbti
        val selfDescription = userInfoBundle.self_description
        binding.tvSelfDescription.text =
            selfDescription.ifEmpty { getString(R.string.if_desc_empty) }
    }


    private fun setupSignOutButton() {
        binding.tvSignout.setOnClickListener {
            handleSignOut()
        }
    }

    private fun handleSignOut() {
        saveAutoLogin(false)

        val intent = Intent(requireContext(), SigninActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


    private fun saveAutoLogin(autoLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AutoLogin", autoLogin)
        editor.apply()
    }

}