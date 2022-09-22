package com.gaushala.fragments.loginSignup


import addFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gaushala.R
import com.gaushala.fragments.commonfrag.MainBaseFragment
import kotlinx.android.synthetic.main.fragment_login_type.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginTypeFragment : MainBaseFragment(), View.OnClickListener {

    //private var prefREM: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_type, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flNormalLogin.setOnClickListener(this)
        flAdminLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.flAdminLogin->{
                mActivity.addFragment(LoginFragment.newInstance(true), true, true, animationType = AnimationType.fadeInfadeOut)
            }
            R.id.flNormalLogin ->{
                mActivity.addFragment(LoginFragment.newInstance(false), true, true, animationType = AnimationType.fadeInfadeOut)
            }
        }
    }
}