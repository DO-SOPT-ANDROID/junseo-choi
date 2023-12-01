package org.sopt.dosopttemplate.ui.main.mypage

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.FragmentMyPageBinding
import org.sopt.dosopttemplate.ui.main.MainActivity
import org.sopt.dosopttemplate.ui.main.home.HomeViewModel
import org.sopt.dosopttemplate.ui.signin.SharedPreferencesKeys
import org.sopt.dosopttemplate.ui.signin.SignInActivity
import org.sopt.dosopttemplate.util.showToast

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding
        get() = requireNotNull(_binding) { "바인딩 객체가 생성되지 않았다. 생성하고 불러라 임마!" }

    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel by viewModels<HomeViewModel>()
    private val mainActivity by lazy { activity as MainActivity }

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

        sharedPreferences = requireActivity().getSharedPreferences(
            SharedPreferencesKeys.USERNAME,
            AppCompatActivity.MODE_PRIVATE
        )

        viewModel.getUserInfo(mainActivity.getUserId())

        displayUserInfo()

        setupSignOutButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayUserInfo() {
        binding.ivProfilePicture.load("") {
            crossfade(true)
            error(R.drawable.ic_default_image)
            transformations(RoundedCornersTransformation())
        }
        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            binding.tvId.text = userInfo.username
            binding.tvName.text = userInfo.nickname
            binding.tvSelfDescription.text = getString(R.string.if_desc_empty)
        }
        viewModel.isServerError.observe(viewLifecycleOwner) {
            (activity as AppCompatActivity).showToast(getString(R.string.server_error))
        }
    }

    private fun setupSignOutButton() {
        binding.tvSignout.setOnClickListener {
            handleSignOut()
        }
    }

    private fun handleSignOut() {
        saveAutoLogin(false)

        val intent = Intent(requireContext(), SignInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


    private fun saveAutoLogin(autoLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AutoLogin", autoLogin)
        editor.apply()
    }

}