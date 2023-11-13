package org.sopt.dosopttemplate

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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


// 유저 추가정보 저장 및 불러오기
fun UserInfo.toJson(): String {
    return Json.encodeToString(this)
}

fun UserInfo.saveAsJsonFile(fileName: String, context: Context) {
    val jsonString = this.toJson()
    context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
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
)

// Bundle 데이터 받아오기

data class UserInfoBundle(
    @SerializedName("profileImage") val profileImage: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("mbti") val mbti: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("self_description") val self_description: String
)
fun Bundle?.extractUserData(): UserInfoBundle? {
    if (this == null) {
        return null
    }

    val profileImage = getString("profileImage") ?: ""
    val userName = getString("userName") ?: ""
    val nickName = getString("nickName") ?: ""
    val mbti = getString("mbti") ?: ""
    val birthday = getString("birthday") ?: ""
    val selfDescription = getString("self_description") ?: ""

    return UserInfoBundle(profileImage, userName, nickName, mbti, birthday, selfDescription)
}