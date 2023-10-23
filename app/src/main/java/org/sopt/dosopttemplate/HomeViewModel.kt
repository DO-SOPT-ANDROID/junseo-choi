package org.sopt.dosopttemplate

import androidx.lifecycle.ViewModel
import java.time.LocalDate

class HomeViewModel : ViewModel() {
    val mockFriendList = listOf<Friend>(
        Friend(
            profileImage = R.drawable.default_profile,
            name = "전성기 시절 파트장",
            self_description = "꼼짝마 손들어",
            birthday = LocalDate.of(1999, 11, 11),
        ),
        Friend(
            profileImage = R.drawable.default_profile,
            name = "손흥민",
            self_description = "나보다 잘생긴 사람 있으면 나와",
            birthday = LocalDate.of(1999, 11, 11),
        ),
        Friend(
            profileImage = R.drawable.default_profile,
            name = "파트장",
            self_description = "표정 풀자",
            birthday = LocalDate.of(1999, 11, 11),
        ),
    )
}