package org.sopt.dosopttemplate

object ServicePool {
    val authService = AuthApiFactory.create<AuthService>()
    val friendService = FriendApiFactory.create<FriendService>()
}