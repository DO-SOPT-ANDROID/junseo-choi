package org.sopt.dosopttemplate.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.ActivityHomeBinding
import org.sopt.dosopttemplate.domain.model.UserInfo
import org.sopt.dosopttemplate.ui.main.doandroid.DoAndroidFragment
import org.sopt.dosopttemplate.ui.main.home.HomeFragment
import org.sopt.dosopttemplate.ui.main.mypage.MyPageFragment
import org.sopt.dosopttemplate.ui.signin.SigninActivity
import org.sopt.dosopttemplate.util.defaultUserInfo
import org.sopt.dosopttemplate.util.getUserInfoFromJson
import org.sopt.dosopttemplate.util.showToast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fcv_home)

        if (currentFragment == null) {
            replaceFragment(HomeFragment())
            binding.bnvHome.selectedItemId = R.id.menu_home
        }

        clickBottomNavigation()
        doubleClickBottomNavigation()
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

    private fun doubleClickBottomNavigation() {
        binding.bnvHome.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    val homeFragment =
                        supportFragmentManager.findFragmentById(R.id.fcv_home) as? HomeFragment
                    homeFragment?.scrollToTop()
                }

                R.id.menu_do_android -> {
                }

                R.id.menu_mypage -> {
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
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

    private fun kickToSignIn() {
        showToast(getString(R.string.server_error))
        saveAutoLogin(false)

        val intent = Intent(this@MainActivity, SigninActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveAutoLogin(autoLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AutoLogin", autoLogin)
        editor.apply()
    }

}