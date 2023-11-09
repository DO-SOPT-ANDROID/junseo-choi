package org.sopt.dosopttemplate

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.sopt.dosopttemplate.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userInfo: UserInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userInfo = "user_info.json".getUserInfoFromJson(this) ?: defaultUserInfo

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fcv_home)
        if (currentFragment == null) {
            replaceFragment(HomeFragment())
        }

        clickBottomNavigation()
        setupOnBackPressedCallback()
    }

    private fun clickBottomNavigation() {
        binding.bnvHome.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.menu_do_android -> {
                    replaceFragment(DoAndroidFragment())
                    true
                }

                R.id.menu_mypage -> {
                    replaceFragment(MyPageFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val bundle = createUserInfoBundle(userInfo)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_home, fragment)
            .commit()
    }

    private fun setupOnBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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

    private fun createUserInfoBundle(userInfo: UserInfo): Bundle {
        val bundle = Bundle()
        bundle.putString("userId", userInfo.userId)
        bundle.putString("password", userInfo.password)
        bundle.putString("nickName", userInfo.nickName)
        bundle.putString("MBTI", userInfo.MBTI)
        bundle.putString("birthday", userInfo.birthday.toString()) // LocalDate를 String으로 변환
        bundle.putString("self_description", userInfo.self_description) // self_description 추가

        return bundle
    }
}

