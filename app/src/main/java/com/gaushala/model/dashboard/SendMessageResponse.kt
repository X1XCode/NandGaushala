package com.gaushala.model.dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendMessageResponse {

    @SerializedName("code")
    @Expose
    var code: String? = ""

    @SerializedName("data")
    @Expose
    var data: ArrayList<UserSearchResponse.UserSearchData>? = ArrayList()

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


