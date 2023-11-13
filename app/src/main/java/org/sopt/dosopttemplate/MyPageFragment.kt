package org.sopt.dosopttemplate

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
import org.sopt.dosopttemplate.databinding.ActivityMyPageBinding

class MyPageFragment : Fragment() {
    private var _binding: ActivityMyPageBinding? = null
    private val binding: ActivityMyPageBinding
        get() = requireNotNull(_binding) { "바인딩 객체가 생성되지 않았다. 생성하고 불러라 임마!" }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ActivityMyPageBinding.inflate(inflater, container, false)
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
            error(R.drawable.ic_default_image) // 에러 시 보여줄 이미지 설정
            transformations(RoundedCornersTransformation())
        }
        binding.tvId.text = userInfoBundle.userName
        binding.tvName.text = userInfoBundle.nickName
        binding.tvMBTI.text = userInfoBundle.mbti
        binding.tvSelfDescription.text = userInfoBundle.self_description
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


    fun saveAutoLogin(autoLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AutoLogin", autoLogin)
        editor.apply()
    }

}