package org.sopt.dosopttemplate

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.sopt.dosopttemplate.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
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
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE) // Add this line

        userInfo = "user_info.json".getUserInfoFromJson(this) ?: defaultUserInfo
        getUserInfoFromServer(id)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fcv_home)
        if (currentFragment == null) {
            replaceFragment(HomeFragment())
            binding.bnvHome.selectedItemId = R.id.menu_home
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
            // setOnNavigationItemReselectedListener 안쓴다네용 o0o
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

    private fun createUserInfoBundle(
        userInfo: UserInfo,
        userName: String,
        nickName: String,
    ): Bundle {
        val bundle = Bundle()
        bundle.putString("profileImage", userInfo.profileImage)
        bundle.putString("userName", userName)
        bundle.putString("nickName", nickName)
        bundle.putString("mbti", userInfo.mbti)
        bundle.putString("birthday", userInfo.birthday)
        bundle.putString("self_description", userInfo.self_description)
        return bundle
    }

    private fun kickToSignIn() {
        showToast(getString(R.string.server_error))
        saveAutoLogin(false)

        val intent = Intent(this@HomeActivity, SigninActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveAutoLogin(autoLogin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AutoLogin", autoLogin)
        editor.apply()
    }

}