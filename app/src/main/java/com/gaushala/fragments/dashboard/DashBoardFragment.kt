package com.gaushala.fragments.dashboard


import AnimationType
import addFragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaushala.MainActivity
import com.gaushala.R
import com.gaushala.fragments.commonfrag.HomeBaseFragment
import com.gaushala.model.dashboard.DashboardBean
import com.gaushala.utils.Common_Methods.Companion.calculateNoOfColumns
import com.gaushala.utils.Common_Methods.Companion.calculateNoOfRows
import com.gaushala.utils.Common_Methods.Companion.convertDpToPixels
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.toolbar_header.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardFragment : HomeBaseFragment(), View.OnClickListener {

    var dashBoardAdapter: DashBoardAdapter? = null


    //private var prefREM: SharedPreferences? = null
    private var dashboardList: ArrayList<DashboardBean> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        init()
    }

    private fun init() {
        Log.e("TAG", "init: auth token" + appPref.getAuthToken())
        addDashBoardItem()
    }

    private fun addDashBoardItem() {
        dashboardList = ArrayList()
        dashboardList.add(
            DashboardBean(
                R.drawable.milkinward,
                "",
                "Milk \n Inward"
            )
        )
        dashboardList.add(
            DashboardBean(
                R.drawable.milkdistribution,
                "",
                "Milk Distribution"
            )
        )
        dashboardList.add(
            DashboardBean(
                R.drawable.paymentreceived,
                "",
                "Payment Receipt"
            )
        )

        dashBoardAdapter = DashBoardAdapter(mActivity, dashboardList)
        rvDashboard!!.layoutManager =
            GridLayoutManager(mActivity, 3, RecyclerView.VERTICAL, false)
        rvDashboard!!.adapter = dashBoardAdapter
        dashBoardAdapter!!.notifyDataSetChanged()
    }

    private fun setHeader() {
       // mActivity.checkNavigationItem(0)
        tvHeader.text = "Gaushala Home"
        ivLogout.visibility= View.VISIBLE
        ivLogout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivLogout -> {
                val intent = Intent(mActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            /*R.id.iv_menu -> {
                mActivity.hideKeyboard()
                Common_Methods.avoidDoubleClicks(v)
                mActivity.openDrawer()
            }*/
        }
    }

    inner class DashBoardAdapter(
        private val activity: Activity,
        val dashboardList: ArrayList<DashboardBean>
    ) : RecyclerView.Adapter<DashBoardAdapter.DashBoardHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DashBoardHolder {
            return DashBoardHolder(
                LayoutInflater.from(activity).inflate(R.layout.raw_dashboard, viewGroup, false)
            )
        }

        override fun onBindViewHolder(holder: DashBoardHolder, position: Int) {
            val imageWidth = convertDpToPixels(85f, activity)
                .toInt()
            val imageHeight = convertDpToPixels(90f, activity)
                .toInt()
            val imageLayoutParams = holder.ivDashBoard.layoutParams
            imageLayoutParams.width = imageWidth
            imageLayoutParams.height = imageHeight
            holder.ivDashBoard.layoutParams = imageLayoutParams
            val weight = calculateNoOfColumns(activity, 3.0)
            val height = calculateNoOfRows(activity, 5.0)
            val layoutParams = holder.clParentRaw.layoutParams
            layoutParams.width = weight
            //            layoutParams.height = height;
            holder.clParentRaw.layoutParams = layoutParams
            if (dashboardList[position].imageMenu > 0) {
                holder.ivDashBoard.setImageResource(dashboardList[position].imageMenu)
            }
            holder.tvDashBoard.text = dashboardList[position].textMenu

            holder.ivDashBoard.setOnClickListener {
                when (holder.adapterPosition) {
                    0 -> {
                        mActivity.addFragment(
                            CommonFormFragment.newInstance(1),//Milk Inward
                            true,
                            true,
                            animationType = AnimationType.fadeInfadeOut
                        )
                    }
                    1 -> {
                        mActivity.addFragment(
                            CommonFormFragment.newInstance(2),//Milk Distribution
                            true,
                            true,
                            animationType = AnimationType.fadeInfadeOut
                        )
                    }
                    2 ->{
                        mActivity.addFragment(
                            CommonFormFragment.newInstance(3),//Milk Distribution
                            true,
                            true,
                            animationType = AnimationType.fadeInfadeOut
                        )
                    }
                }
            }

        }

        override fun getItemCount(): Int {
            return dashboardList.size
        }

        inner class DashBoardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var ivDashBoard: ImageView = itemView.findViewById(R.id.ivDashBoard)
            var tvDashBoard: TextView = itemView.findViewById(R.id.tvDashBoardName)
            var clParentRaw: RelativeLayout = itemView.findViewById(R.id.clParentRaw)

        }
    }








}