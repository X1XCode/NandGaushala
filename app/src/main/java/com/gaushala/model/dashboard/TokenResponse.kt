package com.gaushala.model.dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TokenResponse {

    @SerializedName("access_token")
    @Expose
    var accessToken: String? = ""

    @SerializedName("token_type")
    @Expose
    var tokenType: String? = ""

    @SerializedName("expires_in")
    @Expose
    var expiresIn: Int? = 0

    @SerializedName("refresh_token")
    @Expose
    var refreshToken: String? = ""

    @SerializedName("userName")
    @Expose
    var userName: String? = ""

    @SerializedName("mobile")
    @Expose
    var mobile: String? = ""

    @SerializedName("userMasterId")
    @Expose
    var userMasterId: String? = ""

    @SerializedName("FirebaseId")
    @Expose
    var firebaseId: String? = ""

    @SerializedName(".issued")
    @Expose
    var issued: String? = ""

    @SerializedName(".expires")
    @Expose
    var expires: String? = ""
}