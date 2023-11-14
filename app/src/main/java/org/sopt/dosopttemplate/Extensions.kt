package org.sopt.dosopttemplate

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// 키보드 감추기
fun hideKeyboard(context: Context, view: View?) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (view != null) {
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

// 스낵바 출력
fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

fun AppCompatActivity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}


// 유저 정보 저장하기
fun UserInfo.toJson(): String {
    val gson = Gson()
    return gson.toJson(this)
}

fun String.saveAsJsonFile(fileName: String, context: Context) {
    try {
        val jsonObject = JSONObject(this)
        val jsonString = jsonObject.toString()
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { stream ->
            stream.write(jsonString.toByteArray())
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private val gson: Gson = Gson() // Gson 객체를 클래스의 멤버 변수로 선언하여 재사용

fun String.getUserInfoFromJson(context: Context): UserInfo? {
    try {
        val jsonString = context.openFileInput(this).bufferedReader().use { it.readText() }
        return gson.fromJson(jsonString, UserInfo::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

// 유저 정보 Default값
val defaultUserInfo = UserInfo(
    "",
    "",
    "",
    "",
    "",
    LocalDate.of(1000, 5, 11),
    ""
)

// Bundle 데이터 받아오기

fun Bundle?.extractUserInfo(): UserInfo {
    val profileImage = this?.getString("profileImage", "")
    val userId = this?.getString("userId", "")
    val password = this?.getString("password", "")
    val nickName = this?.getString("nickName", "")
    val MBTI = this?.getString("MBTI", "")
    val birthdayString = this?.getString("birthday", "")
    val selfDescription = this?.getString("self_description", "") ?: ""

    val formatter = DateTimeFormatter.ISO_DATE
    val birthday = if (birthdayString != null && birthdayString.isNotBlank()) {
        try {
            LocalDate.parse(birthdayString, formatter)
        } catch (e: DateTimeParseException) {
            defaultUserInfo.birthday
        }
    } else {
        defaultUserInfo.birthday
    }

    return if (profileImage != null && userId != null && password != null && nickName != null && MBTI != null) {
        UserInfo(profileImage, userId, password, nickName, MBTI, birthday, selfDescription)
    } else {
        defaultUserInfo
    }
}