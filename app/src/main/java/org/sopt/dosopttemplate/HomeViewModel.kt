package org.sopt.dosopttemplate

import androidx.lifecycle.ViewModel
import java.time.LocalDate

class HomeViewModel : ViewModel() {
    val mockFriendList = mutableListOf<Friend>(
        Friend(
            userId = "temp000",
            profileImage = "https://github.com/DO-SOPT-ANDROID/sohyun-park/assets/98076050/d398bf70-3639-476b-bcaf-2d7ffce89549",
            name = "이태희",
            self_description = "부장님.",
            birthday = LocalDate.of(1999, 8, 6),
        ),
        Friend(
            userId = "temp001",
            profileImage = "https://github.com/DO-SOPT-ANDROID/sohyun-park/assets/98076050/103c1c02-934d-4944-bf9c-2045b2794efa",
            name = "곽의진",
            self_description = "차기 파트장입니다",
            birthday = LocalDate.of(2001, 7, 28),
        ),
        Friend(
            userId = "temp002",
            profileImage = "https://github.com/DO-SOPT-ANDROID/sohyun-park/assets/98076050/f4c286d3-bea5-4b4a-845d-cbdac25ff5c4",
            name = "박소현",
            self_description = "네? 제가 카리나라구요?!",
            birthday = LocalDate.of(2001, 12, 7),
        ),
        Friend(
            userId = "temp003",
            profileImage = "https://github.com/DO-SOPT-ANDROID/sohyun-park/assets/98076050/ed6bd602-0ab0-4c37-bcfb-23ebcf5300fe",
            name = "엄현지",
            self_description = "파이브가이즈 먹으러 갈사람~~",
            birthday = LocalDate.of(1999, 8, 7),
        ),
        Friend(
            userId = "temp004",
            profileImage = "https://github.com/DO-SOPT-ANDROID/sohyun-park/assets/98076050/01ec8aaa-6c09-4359-ac69-86f2f4975d6f",
            name = "허민회",
            self_description = "이래보여도 I랍니다",
            birthday = LocalDate.of(2000, 10, 5),
        ),
        Friend(
            userId = "temp005",
            profileImage = "https://s3.ap-northeast-2.amazonaws.com/sopt-makers-internal//prod/image/project/a51f5bfa-c33a-4fe5-9341-512643acaad5-88CF368E-EBA9-4584-8293-EA06A5002080.jpeg",
            name = "우상욱",
            self_description = "표정 풀자",
            birthday = LocalDate.of(1998, 6, 10),
        ),
        Friend(
            userId = "temp006",
            profileImage = "https://github.com/DO-SOPT-ANDROID/sohyun-park/assets/98076050/49c829ad-c995-4950-9027-3bb2c736e2f6",
            name = "서재원",
            self_description = "마 내가 솝맛짱이야",
            birthday = LocalDate.of(1998, 6, 22),
        ),
        Friend(
            userId = "temp007",
            profileImage = "https://s3.ap-northeast-2.amazonaws.com/sopt-makers-internal//prod/image/project/be9b9ddc-b32d-4e7a-93ef-f18a937371f5-6F9C3076-0E93-44AB-A063-62D05C1CBCEC.jpeg",
            name = "박강희",
            self_description = "너도 그냥 같이 생일하자",
            birthday = LocalDate.now(),
        ),
        Friend(
            userId = "temp008",
            profileImage = "",
            name = "김민우",
            self_description = "?.?",
            birthday = LocalDate.of(1998, 9, 29),
        ),
        Friend(
            userId = "temp009",
            profileImage = "https://github.com/DO-SOPT-ANDROID/sohyun-park/assets/98076050/e7c898b5-8e9e-4946-81bb-8728c06a086f",
            name = "손명지",
            self_description = "오버워치 프로연습생",
            birthday = LocalDate.of(2002, 8, 13),
        ),
        Friend(
            userId = "temp010",
            profileImage = "https://github.com/DO-SOPT-ANDROID/sohyun-park/assets/98076050/22820613-2a7e-4712-a720-c7c64ba97275",
            name = "이준희",
            self_description = "Music Is My Life",
            birthday = LocalDate.of(2001, 9, 16),
        ),
        Friend(
            userId = "temp011",
            profileImage = "https://github.com/DO-SOPT-ANDROID/sohyun-park/assets/98076050/071612a3-6fce-4d91-8f8a-ac7450be21bb",
            name = "최준서",
            self_description = "나는 매일매일 생일할거야",
            birthday = LocalDate.now(),
        )
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