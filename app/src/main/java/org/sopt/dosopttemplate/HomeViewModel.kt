package org.sopt.dosopttemplate

import androidx.lifecycle.ViewModel
import java.time.LocalDate

class HomeViewModel : ViewModel() {
    val mockFriendList = listOf<Friend>(
        Friend(
            profileImage = R.drawable.ic_ex1,
            name = "이태희",
            self_description = "부장님.",
            birthday = LocalDate.of(1999, 8, 6),
        ),
        Friend(
            profileImage = R.drawable.ic_ex1,
            name = "곽의진",
            self_description = "차기 파트장입니다",
            birthday = LocalDate.of(2001, 7, 28),
        ),
        Friend(
            profileImage = R.drawable.ic_ex2,
            name = "박소현",
            self_description = "네? 제가 카리나라구요?!",
            birthday = LocalDate.of(2001, 12, 7),
        ),
        Friend(
            profileImage = R.drawable.ic_ex2,
            name = "엄현지",
            self_description = "파이브가이즈 먹으러 갈사람~~",
            birthday = LocalDate.of(1999, 8, 7),
        ),
        Friend(
            profileImage = R.drawable.ic_ex2,
            name = "허민회",
            self_description = "이래보여도 I랍니다",
            birthday = LocalDate.of(2000, 10, 5),
        ),
        Friend(
            profileImage = R.drawable.ic_ex1,
            name = "우상욱",
            self_description = "표정 풀자",
            birthday = LocalDate.of(1998, 6, 10),
        ),
        Friend(
            profileImage = R.drawable.ic_ex1,
            name = "서재원",
            self_description = "마 내가 솝맛짱이야",
            birthday = LocalDate.of(1998, 6, 22),
        ),
        Friend(
            profileImage = R.drawable.ic_ex2,
            name = "박강희",
            self_description = "너도 곧 생일이니까 생일하자",
            birthday = LocalDate.of(1999, 10, 25),
        ),
        Friend(
            profileImage = R.drawable.ic_ex1,
            name = "김민우",
            self_description = "?.?",
            birthday = LocalDate.of(1998, 9, 29),
        ),
        Friend(
            profileImage = R.drawable.ic_ex2,
            name = "손명지",
            self_description = "오버워치 프로연습생",
            birthday = LocalDate.of(2002, 8, 13),
        ),
        Friend(
            profileImage = R.drawable.ic_ex1,
            name = "이준희",
            self_description = "Music Is My Life",
            birthday = LocalDate.of(2001, 9, 16),
        ),
        Friend(
            profileImage = R.drawable.ic_ex3,
            name = "최준서",
            self_description = "나는 매일매일 생일할거야",
            birthday = LocalDate.of(1999, 11, 8),
        ),
    )

    private val birthdayFriends = mutableListOf<Friend>()
    private val otherFriends = mutableListOf<Friend>()

    init {
        separateFriendsByBirthday()
    }

    private fun separateFriendsByBirthday() {
        val sortedFriends = mockFriendList.sortedBy { it.name } // 여기서도 정렬은 빼놓을 수는 없지 히히
        val today = LocalDate.now()

        for (friend in sortedFriends) {
            if (friend.birthday.monthValue == today.monthValue && friend.birthday.dayOfMonth == today.dayOfMonth) {
                birthdayFriends.add(friend)
            } else {
                otherFriends.add(friend)
            }
        }
    }

    fun getBirthdayFriends(): List<Friend> {
        return birthdayFriends
    }

    fun getOtherFriends(): List<Friend> {
        return otherFriends
    }
}