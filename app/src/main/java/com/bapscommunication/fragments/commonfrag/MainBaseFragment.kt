package com.bapscommunication.fragments.commonfrag


import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bapscommunication.MainActivity
import com.bapscommunication.utils.AppPreference
import android.app.Activity
import android.content.Intent

open class MainBaseFragment : Fragment() {

    lateinit var mActivity: MainActivity
    lateinit var appPref: AppPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity
        appPref = AppPreference(mActivity)
    }

    /*open fun isSuccess(response: Response<TokenResponse>): Boolean {
        return when {
            response.code() === 200 -> {
                true
            }
            response.code() === 401 -> {
                logout()
                false
            }
            else -> {
                false
            }
        }
    }*/

    open fun logout() {
        appPref.clearPreferences()
        gotoActivity(MainActivity::class.java, null, true)
        (context as Activity).finish()
    }

    fun gotoActivity(className: Class<*>?, bundle: Bundle?, isClearStack: Boolean) {
        val intent = Intent(context, className)
        if (bundle != null) intent.putExtras(bundle)
        if (isClearStack) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        mActivity.startActivity(intent)
    }
}
