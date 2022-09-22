package com.gaushala.fragments


import AnimationType
import addFragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gaushala.R
import com.gaushala.fragments.commonfrag.MainBaseFragment
import com.gaushala.fragments.loginSignup.LoginFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SplashFragment : MainBaseFragment() {

    //private var prefREM: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            /*val isLogin = appPref.isLogin()
            if (isLogin) {
                mActivity.startActivity(Intent(mActivity, HomeActivity::class.java))
                mActivity.finish()
            } else {
                mActivity.addFragment(
                    LoginFragment(),
                    false,
                    true,
                    animationType = AnimationType.fadeInfadeOut
                )
            }*/
            mActivity.addFragment(
                LoginFragment(),
                false,
                true,
                animationType = AnimationType.fadeInfadeOut
            )

            //val appDatabase: OldMe911Database = OldMe911Database.getDatabase(mActivity)
            /*if (appDatabase.loginDao().getAll() != null) {
                mActivity.finish()
                mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                val intent = Intent(mActivity, HomeActivity::class.java)
                mActivity.startActivity(intent)
            } else {
                mActivity.addFragment(LoginFragment(), false, true, animationType = AnimationType.fadeInfadeOut)
//                mActivity.addFragment(SubscriptionFragment(), false, true, animationType = AnimationType.fadeInfadeOut)
            }*/
        }, 2000)
    }
}