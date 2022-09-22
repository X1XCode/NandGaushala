package com.gaushala.fragments.loginSignup


import AnimationType
import addFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gaushala.R
import com.gaushala.fragments.commonfrag.MainBaseFragment
import kotlinx.android.synthetic.main.fragment_signup.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SignupFragment : MainBaseFragment() {

    //private var prefREM: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSignup.setOnClickListener{
            mActivity.addFragment(SendOtpFragment(), true, true, AnimationType.fadeInfadeOut)
        }
    }
}