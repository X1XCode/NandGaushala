package com.gaushala.fragments.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gaushala.R
import com.gaushala.fragments.commonfrag.HomeBaseFragment
import kotlinx.android.synthetic.main.toolbar_header.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : HomeBaseFragment(), View.OnClickListener{

    //private var prefREM: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        initView()
    }

    private fun initView() {
        iv_back.setOnClickListener(this)
    }

    private fun setHeader() {
        iv_back.visibility = View.VISIBLE
        tvHeader.text = "User Profile"
       // mActivity.checkNavigationItem(1)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back -> {
                mActivity.onBackPressed()
            }
        }
    }
}