package org.sopt.dosopttemplate


sealed class Person {
    abstract val profileImage: String
    abstract val userId: String
    abstract val name: String
    abstract val description: String
}

data class Friend(
    override val profileImage: String,
    override val userId: String,
    override val name: String,
    override val description: String,
) : Person()