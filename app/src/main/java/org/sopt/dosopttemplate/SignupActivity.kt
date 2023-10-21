package org.sopt.dosopttemplate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.sopt.dosopttemplate.databinding.ActivitySignupBinding

data class UserInfo(
    val userId: String,
    val password: String,
    val nickName: String,
    val MBTI: String
)

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        binding.tvSignUpInbutton.setOnClickListener {
            val userId = binding.tvSignUpInputid.text.toString()
            val password = binding.tvSignUpInputpw.text.toString()
            val nickName = binding.tvSignUpInputNick.text.toString()
            val MBTI = binding.tvSignUpInputMBTI.text.toString().uppercase()

            if (userId.isEmpty() || password.isEmpty() || nickName.isEmpty() || MBTI.isEmpty()) {
                showEmptyFieldDialog()
                return@setOnClickListener
            }

            val errorMessage = when {
                !isUserIdValid(userId) -> "아이디는 6~10글자이며, 공백 및 특수문자가 포함되면 안됩니다."
                !isPasswordValid(password) -> "비밀번호는 8~12글자이며, 공백이 포함되면 안됩니다."
                !isNickNameValid(nickName) -> "닉네임은 1~12글자이며, 공백으로만 이루어질 수 없습니다."
                MBTI == "SEXY" -> "당신은 SEXY하시군요! 하지만 저는 넘어가지 않는답니다 :)"
                !isMBTIValid(MBTI) -> "유효한 MBTI를 적어주세요. (예시: ENTJ)"
                else -> null
            }

            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
            } else {
                saveUserInfo(userId, password, nickName, MBTI)
                Snackbar.make(
                    binding.root,
                    "성공적으로 회원가입이 완료되었습니다!",
                    Snackbar.LENGTH_SHORT
                ).show()
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
        val validMBTIList = listOf(
            "INFP",
            "ENFP",
            "ESFJ",
            "ISFJ",
            "ISFJ",
            "ISFP",
            "ESFP",
            "INTP",
            "INFJ",
            "ENFJ",
            "ENTP",
            "ESTJ",
            "ISTJ",
            "INTJ",
            "ISTP",
            "ESTP",
            "ENTJ"
        )
        return validMBTIList.contains(MBTI)
    }

    private fun showEmptyFieldDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("아직 입력되지 않은 칸이 있습니다.")
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

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@SignupActivity, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}