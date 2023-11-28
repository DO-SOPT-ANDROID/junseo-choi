package org.sopt.dosopttemplate.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.sopt.dosopttemplate.network.service.AuthService
import org.sopt.dosopttemplate.network.service.FriendService
import org.sopt.dosopttemplate.ui.main.mypage.MyPageFragment
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.domain.model.ResponseGetUserInfoDto
import org.sopt.dosopttemplate.network.ServicePool
import org.sopt.dosopttemplate.domain.model.UserInfo
import org.sopt.dosopttemplate.databinding.ActivityHomeBinding
import org.sopt.dosopttemplate.ui.auth.SigninActivity
import org.sopt.dosopttemplate.ui.main.doandroid.DoAndroidFragment
import org.sopt.dosopttemplate.ui.main.home.HomeFragment
import org.sopt.dosopttemplate.ui.main.home.HomeViewModel
import org.sopt.dosopttemplate.util.defaultUserInfo
import org.sopt.dosopttemplate.util.getUserInfoFromJson
import org.sopt.dosopttemplate.util.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userInfo: UserInfo
    private lateinit var authService: AuthService
    private lateinit var friendService: FriendService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var receivedUserInfo: ResponseGetUserInfoDto
    private var id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getIntExtra("id", -1)
        authService = ServicePool.authService
        friendService = ServicePool.friendService
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        userInfo = "user_info.json".getUserInfoFromJson(this) ?: defaultUserInfo

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fcv_home)

        if (currentFragment == null) {
            getUserInfoFromServer(id)
            binding.bnvHome.selectedItemId = R.id.menu_home
        }

        viewModel.toastMessage.observe(this) { message ->
            showToast(message)
        }

        clickBottomNavigation()
        doubleClickBottomNavigation()
        setupOnBackPressedCallback()
    }

    private fun getUserInfoFromServer(id: Int) {
        authService.getUserInfo(id)
            .enqueue(object : Callback<ResponseGetUserInfoDto> {
                override fun onResponse(
                    call: Call<ResponseGetUserInfoDto>,
                    response: Response<ResponseGetUserInfoDto>,
                ) {
                    when (response.code()) {
                        200 -> {
                            val data: ResponseGetUserInfoDto? = response.body()
                            if (data != null) {
                                receivedUserInfo = data
                                replaceFragment(HomeFragment())
                            }
                        }

                        400 -> {
                            kickToSignIn()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseGetUserInfoDto>, t: Throwable) {
                    kickToSignIn()
                }
            })
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
        if (::receivedUserInfo.isInitialized) {
            val userInfoBundle =
                createUserInfoBundle(userInfo, receivedUserInfo.username, receivedUserInfo.nickname)
            fragment.arguments = userInfoBundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_home, fragment)
                .commit()
        } else {
            getUserInfoFromServer(id)
        }
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

    object BundleKeys {
        const val PROFILE_IMAGE = "profileImage"
        const val USER_NAME = "userName"
        const val NICK_NAME = "nickName"
        const val MBTI = "mbti"
        const val BIRTHDAY = "birthday"
        const val SELF_DESCRIPTION = "self_description"
    }

    private fun createUserInfoBundle(
        userInfo: UserInfo,
        userName: String,
        nickName: String,
    ): Bundle {
        val bundle = Bundle()
        bundle.putString(BundleKeys.PROFILE_IMAGE, userInfo.profileImage)
        bundle.putString(BundleKeys.USER_NAME, userName)
        bundle.putString(BundleKeys.NICK_NAME, nickName)
        bundle.putString(BundleKeys.MBTI, userInfo.mbti)
        bundle.putString(BundleKeys.BIRTHDAY, userInfo.birthday)
        bundle.putString(BundleKeys.SELF_DESCRIPTION, userInfo.self_description)
        return bundle
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