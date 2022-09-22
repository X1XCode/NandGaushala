package com.gaushala.model.dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserSearchResponse {

    @SerializedName("code")
    @Expose
    var code: String? = ""

    @SerializedName("data")
    @Expose
    var data: ArrayList<UserSearchData>? = ArrayList()

   class UserSearchData{
       @SerializedName("userMasterId")
       @Expose
       var userMasterId: Int? = 0

       @SerializedName("registrationId")
       @Expose
       var registrationId: Int? = 0

       @SerializedName("userName")
       @Expose
       var userName: String? = ""

       @SerializedName("password")
       @Expose
       var password: String? = ""

       @SerializedName("email")
       @Expose
       var email: String? = ""

       @SerializedName("mobile")
       @Expose
       var mobile: String? = ""

       @SerializedName("address")
       @Expose
       var address: String? = ""

       @SerializedName("reportingToUserMasterId")
       @Expose
       var reportingToUserMasterId: Int? = 0

       @SerializedName("profilePhoto")
       @Expose
       var profilePhoto: String? = ""

       @SerializedName("attachmentFileName")
       @Expose
       var attachmentFileName: String? = ""

       @SerializedName("attachmentData")
       @Expose
       var attachmentData: String? = ""

       @SerializedName("accessRight")
       @Expose
       var accessRight: String? = ""

       @SerializedName("departmentName")
       @Expose
       var departmentName: String? = ""

       @SerializedName("departmentMasterId")
       @Expose
       var departmentMasterId: Int? = 0

       @SerializedName("designationName")
       @Expose
       var designationName: String? = ""


       @SerializedName("designationMasterId")
       @Expose
       var designationMasterId: Int? = 0

       @SerializedName("deActive")
       @Expose
       var deActive: Boolean? = false

       @SerializedName("entryDateTime")
       @Expose
       var entryDateTime: String? = ""

       @SerializedName("entryUserMasterId")
       @Expose
       var entryUserMasterId: Int? = 0

       @SerializedName("entryVersion")
       @Expose
       var entryVersion: String? = ""

       @SerializedName("editDateTime")
       @Expose
       var editDateTime: String? = ""

       @SerializedName("editUserMasterId")
       @Expose
       var editUserMasterId: Int? = 0

       @SerializedName("editVersion")
       @Expose
       var editVersion: String? = ""

   }
}