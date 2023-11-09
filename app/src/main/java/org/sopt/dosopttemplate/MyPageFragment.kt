package org.sopt.dosopttemplate

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.sopt.dosopttemplate.databinding.ActivityMyPageBinding

class MyPageFragment : Fragment() {
    private var _binding: ActivityMyPageBinding? = null
    private val binding: ActivityMyPageBinding
        get() = requireNotNull(_binding) { "바인딩 객체가 생성되지 않았다. 생성하고 불러라 임마!" }

    private lateinit var userInfo: UserInfo
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

        val userInfo = arguments.extractUserInfo()

        displayUserInfo(userInfo)

        setupSignOutButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayUserInfo(userInfo: UserInfo) {
        binding.tvId.text = userInfo.userId
        binding.tvName.text = userInfo.nickName
        binding.tvMBTI.text = userInfo.MBTI
        binding.tvSelfDescription.text = userInfo.self_description
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