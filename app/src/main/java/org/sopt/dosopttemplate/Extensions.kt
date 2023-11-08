package org.sopt.dosopttemplate

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONObject
import java.time.LocalDate

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

fun String.getUserInfoFromJson(context: Context): UserInfo? {
    try {
        val jsonString = context.openFileInput(this).bufferedReader().use { it.readText() }
        val gson = Gson()
        return gson.fromJson(jsonString, UserInfo::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

val defaultUserInfo = UserInfo(
    "",
    "",
    "",
    "",
    LocalDate.of(1000, 5, 11),
    ""
)