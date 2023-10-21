package org.sopt.dosopttemplate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.sopt.dosopttemplate.databinding.ActivitySignupBinding

data class UserInfo(
    val userId: String,
    val password: String,
    val nickName: String,
    val MBTI: String,
)

enum class MBTIType {
    INFP, ENFP, ESFJ, ISFJ, ISFP, ESFP, INTP, INFJ, ENFJ, ENTP, ESTJ, ISTJ, INTJ, ISTP, ESTP, ENTJ
}

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            hideKeyboard(this, binding.root)
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        binding.btnSignUpInbutton.setOnClickListener {
            val userId = binding.etSignUpInputid.text.toString()
            val password = binding.etSignUpInputpw.text.toString()
            val nickName = binding.etSignUpInputNick.text.toString()
            val MBTI = binding.etSignUpInputMBTI.text.toString().uppercase()

            if (userId.isEmpty() || password.isEmpty() || nickName.isEmpty() || MBTI.isEmpty()) {
                showEmptyFieldDialog()
                return@setOnClickListener
            }

            val errorMessage = when {
                !isUserIdValid(userId) -> getString(R.string.user_id_error)
                !isPasswordValid(password) -> getString(R.string.password_error)
                !isNickNameValid(nickName) -> getString(R.string.nickname_error)
                MBTI == "SEXY" -> getString(R.string.sexy_error)
                !isMBTIValid(MBTI) -> getString(R.string.mbti_error)
                else -> null
            }

            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
            } else {
                saveUserInfo(userId, password, nickName, MBTI)
                binding.root.showSnackbar(getString(R.string.signup_success_message))
                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun isUserIdValid(userId: String): Boolean {
        return userId.length in 6..10 && userId.none { it.isWhitespace() || !it.isLetterOrDigit() }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length in 8..12 && !password.contains(" ")
    }

    private fun isNickNameValid(nickName: String): Boolean {
        return nickName.length in 1..12 && !nickName.all { it.isWhitespace() }
    }

    private fun isMBTIValid(MBTI: String): Boolean {
        return try {
            MBTIType.valueOf(MBTI)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun showEmptyFieldDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(getString(R.string.empty_field_message))
            .setCancelable(false)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("알림")
        alert.show()
    }

    private fun saveUserInfo(userId: String, password: String, nickName: String, MBTI: String) {
        val sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("UserId", userId)
        editor.putString("Password", password)
        editor.putString("NickName", nickName)
        editor.putString("MBTI", MBTI)

        editor.apply()
    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@SignupActivity, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}

