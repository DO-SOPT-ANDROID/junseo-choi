package org.sopt.dosopttemplate


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import org.sopt.dosopttemplate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userInfo: UserInfo
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userInfo = getUserInfo()

        binding.tvMainViewid.text = "${userInfo.userId}"
        binding.tvMainViewnickname.text = "${userInfo.nickName}"
        binding.tvMainViewMBTI.text = "${userInfo.MBTI}"

        binding.tvMainSignout.setOnClickListener {
            saveAutoLogin(false)

            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun getUserInfo(): UserInfo {
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("UserId", "") ?: ""
        val password = sharedPreferences.getString("Password", "") ?: ""
        val nickName = sharedPreferences.getString("NickName", "") ?: ""
        val MBTI = sharedPreferences.getString("MBTI", "") ?: ""
        return UserInfo(userId, password, nickName, MBTI)
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
                Toast.makeText(this@MainActivity, "뒤로가기를 한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
