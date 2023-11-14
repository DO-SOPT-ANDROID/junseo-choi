package org.sopt.dosopttemplate

import java.time.LocalDate


sealed class Person {
    abstract val profileImage: String
    abstract val userId: String
    abstract val name: String
    abstract val self_description: String
    abstract val birthday: LocalDate
}

data class Friend(
    override val profileImage: String,
    override val userId: String,
    override val name: String,
    override val self_description: String,
    override val birthday: LocalDate,
) : Person()