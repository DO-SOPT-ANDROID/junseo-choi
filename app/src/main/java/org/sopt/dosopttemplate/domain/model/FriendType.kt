package org.sopt.dosopttemplate.domain.model

import android.os.Parcel
import android.os.Parcelable


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
) : Person(), Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(profileImage)
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Friend> {
        override fun createFromParcel(parcel: Parcel): Friend {
            return Friend(parcel)
        }

        override fun newArray(size: Int): Array<Friend?> {
            return arrayOfNulls(size)
        }
    }
}