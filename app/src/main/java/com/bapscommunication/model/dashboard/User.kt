package com.bapscommunication.model.dashboard

import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable {
    var userId:String? = ""
    var name: String? = ""
    var authToken: String? = ""
    var userMasterId: String? = ""
    var mobileNumber: String? = ""

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        name = parcel.readString()
        authToken = parcel.readString()
        mobileNumber = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(authToken)
        parcel.writeString(mobileNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}