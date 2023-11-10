package org.sopt.dosopttemplate

import androidx.annotation.DrawableRes
import java.time.LocalDate


sealed class Person {
    abstract val userId: String
    abstract val name: String
    abstract val self_description: String
    abstract val birthday: LocalDate
}

data class Friend(
    @DrawableRes val profileImage: Int,
    override val userId: String,
    override val name: String,
    override val self_description: String,
    override val birthday: LocalDate,
) : Person()