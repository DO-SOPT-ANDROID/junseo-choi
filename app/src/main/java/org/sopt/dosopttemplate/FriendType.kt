package org.sopt.dosopttemplate

import androidx.annotation.DrawableRes
import java.time.LocalDate


sealed class Person {
    abstract val name: String
    abstract val self_description: String
    abstract val birthday: LocalDate
}

data class Mine(
    @DrawableRes val profileImage: Int,
    override val name: String,
    override val self_description: String,
    override val birthday: LocalDate,
) : Person()

data class Friend(
    @DrawableRes val profileImage: Int,
    override val name: String,
    override val self_description: String,
    override val birthday: LocalDate,
) : Person()


data class Birthday(
    @DrawableRes val profileImage: Int,
    override val name: String,
    override val self_description: String,
    override val birthday: LocalDate,
) : Person()