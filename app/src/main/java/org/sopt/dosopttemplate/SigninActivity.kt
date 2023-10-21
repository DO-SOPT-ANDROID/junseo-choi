package org.sopt.dosopttemplate

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import org.sopt.dosopttemplate.databinding.ActivitySigninBinding

object SharedPreferencesKeys {
    const val USER_INFO = "UserInfo"
    const val USER_ID = "UserId"
    const val PASSWORD = "Password"
    const val NICK_NAME = "NickName"
    const val MBTI = "MBTI"
}

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SharedPreferencesKeys.USER_INFO, MODE_PRIVATE)

        val isAutoLogin = sharedPreferences.getBoolean("AutoLogin", false)
        binding.chkSignInAutologin.isChecked = isAutoLogin

        if (isAutoLogin) {
            val userInfo = getUserInfo()
            performAutoLogin(userInfo)
        }

        binding.root.setOnClickListener {
            hideKeyboard(this, binding.root)
        }

        binding.btnSignInInbutton.setOnClickListener {
            performSignin()
        }

        binding.etSignInInputpw.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSignin()
                true
            } else {
                false
            }
        }

        binding.tvSignUpButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun performAutoLogin(userInfo: UserInfo) {
        val autoLogin = binding.chkSignInAutologin.isChecked

        if (autoLogin) {
            val savedUserId = sharedPreferences.getString(SharedPreferencesKeys.USER_ID, "")
            val savedPassword = sharedPreferences.getString(SharedPreferencesKeys.PASSWORD, "")

            if (!savedUserId.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                binding.etSignInInputid.setText(savedUserId)
                binding.etSignInInputpw.setText(savedPassword)
                performSignin()
            }
        }
    }

    private fun performSignin() {
        val userInfo = getUserInfo()

        val inputId = binding.etSignInInputid.text.toString()
        val inputPw = binding.etSignInInputpw.text.toString()

        val idValid = inputId.length >= 6
        val pwValid = inputPw.length in 6..10

        when {
            !idValid -> {
                binding.root.showSnackbar(getString(R.string.invalid_id))
            }

            !pwValid -> {
                binding.root.showSnackbar(getString(R.string.invalid_password))
            }

            inputId == userInfo.userId && inputPw == userInfo.password -> {
                val autoLogin = binding.chkSignInAutologin.isChecked

                if (autoLogin) {
                    saveAutoLoginInfo(userInfo)
                }

                binding.root.showSnackbar(getString(R.string.login_success))
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            else -> {
                binding.root.showSnackbar(getString(R.string.login_failed))
            }
        }

        hideKeyboard(this, binding.root)
    }

    private fun saveAutoLoginInfo(userInfo: UserInfo) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AutoLogin", true)
        editor.putString(SharedPreferencesKeys.USER_ID, userInfo.userId)
        editor.putString(SharedPreferencesKeys.PASSWORD, userInfo.password)
        editor.putString(SharedPreferencesKeys.NICK_NAME, userInfo.nickName)
        editor.putString(SharedPreferencesKeys.MBTI, userInfo.MBTI)
        editor.apply()
    }

    private fun getUserInfo(): UserInfo {
        val userId = sharedPreferences.getString(SharedPreferencesKeys.USER_ID, "") ?: ""
        val password = sharedPreferences.getString(SharedPreferencesKeys.PASSWORD, "") ?: ""
        val nickName = sharedPreferences.getString(SharedPreferencesKeys.NICK_NAME, "") ?: ""
        val MBTI = sharedPreferences.getString(SharedPreferencesKeys.MBTI, "") ?: ""

        return UserInfo(userId, password, nickName, MBTI)
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



