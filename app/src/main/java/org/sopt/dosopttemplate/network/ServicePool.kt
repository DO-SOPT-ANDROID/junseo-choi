package org.sopt.dosopttemplate.network

import org.sopt.dosopttemplate.network.service.AuthApiFactory
import org.sopt.dosopttemplate.network.service.AuthService
import org.sopt.dosopttemplate.network.service.FriendApiFactory
import org.sopt.dosopttemplate.network.service.FriendService

object ServicePool {
    val authService = AuthApiFactory.create<AuthService>()
    val friendService = FriendApiFactory.create<FriendService>()
}