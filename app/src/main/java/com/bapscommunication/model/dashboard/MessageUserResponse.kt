package com.bapscommunication.model.dashboard

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageUserResponse {

    @SerializedName("code")
    @Expose
    var code: String? = ""

    @SerializedName("data")
    @Expose
    var data: ArrayList<UserSearchData>? = ArrayList()

    class UserSearchData() : Parcelable {
        @SerializedName("toUserMasterId")
        @Expose
        var toUserMasterId: Int? = 0

        @SerializedName("toUserMasterFireBaseId")
        @Expose
        var toUserMasterFireBaseId: String? = ""

        @SerializedName("userMasterId")
        @Expose
        var userMasterId: Int? = 0

        @SerializedName("groupMasterId")
        @Expose
        var groupMasterId: Int? = 0

        @SerializedName("groupFirebaseId")
        @Expose
        var groupFirebaseId: String? = ""

        @SerializedName("name")
        @Expose
        var name: String? = ""

        @SerializedName("photo")
        @Expose
        var photo: String? = ""

        constructor(parcel: Parcel) : this() {
            toUserMasterId = parcel.readValue(Int::class.java.classLoader) as? Int
            userMasterId = parcel.readValue(Int::class.java.classLoader) as? Int
            toUserMasterFireBaseId = parcel.readString()
            groupMasterId = parcel.readValue(Int::class.java.classLoader) as? Int
            groupFirebaseId = parcel.readString()
            name = parcel.readString()
            photo = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(toUserMasterId)
            parcel.writeValue(userMasterId)
            parcel.writeString(toUserMasterFireBaseId)
            parcel.writeValue(groupMasterId)
            parcel.writeString(groupFirebaseId)
            parcel.writeString(name)
            parcel.writeString(photo)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<UserSearchData> {
            override fun createFromParcel(parcel: Parcel): UserSearchData {
                return UserSearchData(parcel)
            }

            override fun newArray(size: Int): Array<UserSearchData?> {
                return arrayOfNulls(size)
            }
        }
    }

    @SerializedName("message")
    @Expose
    var message: String? = ""

    @SerializedName("error")
    @Expose
    var errorData: ErrorData? = null

    class ErrorData {

        @SerializedName("code")
        @Expose
        var code: String? = ""

        @SerializedName("message")
        @Expose
        var message: String? = ""

        @SerializedName("origin")
        @Expose
        var originData: String? = ""
    }

}