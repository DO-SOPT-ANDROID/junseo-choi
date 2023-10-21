package org.sopt.dosopttemplate

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.sopt.dosopttemplate.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        val isAutoLogin = sharedPreferences.getBoolean("AutoLogin", false)
        binding.tvSignInAutologin.isChecked = isAutoLogin

        if (isAutoLogin) {
            val userInfo = getUserInfo()
            performAutoLogin(userInfo)
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        binding.tvSignInInbutton.setOnClickListener {
            performSignin()
        }

        binding.tvSignInInputpw.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSignin()
                true
            } else {
                false
            }
        }

        binding.tvSignUpButton.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun performAutoLogin(userInfo: UserInfo) {
        val autoLogin = binding.tvSignInAutologin.isChecked

        if (autoLogin) {
            val savedUserId = sharedPreferences.getString("UserId", "")
            val savedPassword = sharedPreferences.getString("Password", "")

            if (!savedUserId.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                binding.tvSignInInputid.setText(savedUserId)
                binding.tvSignInInputpw.setText(savedPassword)
                performSignin()
            }
        }
    }

    private fun performSignin() {
        val userInfo = getUserInfo()

        if (binding.tvSignInInputid.length() < 6) {
            Snackbar.make(
                binding.root,
                "아이디가 잘못되었습니다.",
                Snackbar.LENGTH_SHORT
            ).show()
            hideKeyboard()
        } else if (binding.tvSignInInputpw.length() !in 6..10) {
            Snackbar.make(
                binding.root,
                "비밀번호가 잘못되었습니다.",
                Snackbar.LENGTH_SHORT
            ).show()
            hideKeyboard()
        } else if (binding.tvSignInInputid.text.toString() == userInfo.userId &&
            binding.tvSignInInputpw.text.toString() == userInfo.password) {

            val autoLogin = binding.tvSignInAutologin.isChecked

            if (autoLogin) {
                saveAutoLoginInfo(userInfo)
            }

            Snackbar.make(
                binding.root,
                "성공적으로 로그인되었습니다!",
                Snackbar.LENGTH_SHORT
            ).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Snackbar.make(
                binding.root,
                "회원님의 정보가 일치하지 않습니다.",
                Snackbar.LENGTH_SHORT
            ).show()
            hideKeyboard()
        }
    }

    private fun saveAutoLoginInfo(userInfo: UserInfo) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AutoLogin", true)
        editor.putString("UserId", userInfo.userId)
        editor.putString("Password", userInfo.password)
        editor.putString("NickName", userInfo.nickName)
        editor.putString("MBTI", userInfo.MBTI)
        editor.apply()
    }

    private fun getUserInfo(): UserInfo {
        val sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        val userId = sharedPreferences.getString("UserId", "") ?: ""
        val password = sharedPreferences.getString("Password", "") ?: ""
        val nickName = sharedPreferences.getString("NickName", "") ?: ""
        val MBTI = sharedPreferences.getString("MBTI", "") ?: ""

        return UserInfo(userId, password, nickName, MBTI)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    private var backPressedTime = 0L

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this@SigninActivity, "뒤로가기를 한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}



