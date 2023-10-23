package org.sopt.dosopttemplate


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import org.sopt.dosopttemplate.databinding.ActivityMyPageBinding
import java.time.LocalDate

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding
    private lateinit var userInfo: UserInfo
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userInfo = "user_info.json".getUserInfoFromJson(this) ?: UserInfo(
            "",
            "",
            "",
            "",
            LocalDate.of(0, 0, 0),
            ""
        )

        displayUserInfo(userInfo)

        setupSignOutButton()
        setupOnBackPressedCallback()
    }

    private fun displayUserInfo(userInfo: UserInfo) {
        binding.tvMypageViewid.text = userInfo.userId
        binding.tvMypageViewnickname.text = userInfo.nickName
        binding.tvMypageViewMBTI.text = userInfo.MBTI
    }

    private fun setupSignOutButton() {
        binding.tvMypageSignout.setOnClickListener {
            handleSignOut()
        }
    }

    private fun handleSignOut() {
        saveAutoLogin(false)

        val intent = Intent(this, SigninActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupOnBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun saveAutoLogin(autoLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AutoLogin", autoLogin)
        editor.apply()
    }

    private var backPressedTime = 0L

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                showToast(getString(R.string.double_back_to_exit))
            }
        }
    }
}
