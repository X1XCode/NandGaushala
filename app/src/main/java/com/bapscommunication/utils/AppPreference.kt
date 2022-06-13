package com.bapscommunication.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class AppPreference {



    private var _sharedPrefs: SharedPreferences? = null
    private var _prefsEditor: SharedPreferences.Editor? = null

    constructor(context: Context){
        _sharedPrefs = context.getSharedPreferences(
            APP_SHARED_PREFS,
            Activity.MODE_PRIVATE
        )
        _prefsEditor = _sharedPrefs?.edit()
    }

    fun getAuthToken(): String? {
        return _sharedPrefs!!.getString(
            KEY_AUTH_TOKEN,
            ""
        )
    }

    fun setAuthToken(accessToken: String) {
        _prefsEditor!!.putString(KEY_AUTH_TOKEN, accessToken)
        _prefsEditor!!.commit()
    }

    fun setFCMUserID(id: String){
        _prefsEditor!!.putString(KEY_USER_ID, id)
        _prefsEditor!!.commit()
    }

    fun getFCMUserId(): String? {
        return _sharedPrefs!!.getString(
            KEY_USER_ID,
            ""
        )
    }

    fun setUserMasterId(id: String){
        _prefsEditor!!.putString(KEY_USER_MASTER_ID, id)
        _prefsEditor!!.commit()
    }

    fun getUserMasterId(): String? {
        return _sharedPrefs!!.getString(
            KEY_USER_MASTER_ID,
            ""
        )
    }

    fun setLoggedInUserName(name: String){
        _prefsEditor!!.putString(KEY_LOGGED_IN_USER_NAME, name)
        _prefsEditor!!.commit()
    }

    fun getLoggedUserName(): String? {
        return _sharedPrefs!!.getString(
            KEY_LOGGED_IN_USER_NAME,
            ""
        )
    }

    fun setLogin(isLogin: Boolean){
        _prefsEditor!!.putBoolean(IS_LOGIN, isLogin)
        _prefsEditor!!.commit()
    }

    fun isLogin() : Boolean{
        return _sharedPrefs!!.getBoolean(
            IS_LOGIN, false
        )
    }

    fun clearPreferences(){
        _prefsEditor?.clear()
        _prefsEditor?.commit()
    }

}