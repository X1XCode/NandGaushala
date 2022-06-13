package com.bapscommunication.fragments.dashboard


import addFragment
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bapscommunication.R
import com.bapscommunication.fragments.commonfrag.HomeBaseFragment
import com.bapscommunication.model.dashboard.MessageUserResponse
import com.bapscommunication.model.dashboard.User
import com.bapscommunication.model.dashboard.UserSearchResponse
import com.bapscommunication.utils.*
import com.bapscommunication.webservices.WebApiClient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.toolbar_header.*
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.ArrayList

class UserListFragment : HomeBaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        getUserFromApi()
        //getUsersListFromApi()
    }

    private fun setHeader() {
        tvHeader.text = "Chats"
        ivMore.visibility = View.VISIBLE
        ivMore.setOnClickListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_profile -> {
                Toast.makeText(mActivity, "Profile CLicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.nav_logout -> {
                logout()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    private fun getUserFromApi(){
        Common_Methods.showLoading(mActivity)
        val newUsers: ArrayList<UserSearchResponse.UserSearchData> = ArrayList()
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            val getUserToken = WebApiClient.getInstance(mActivity)
                .webApi_with_header(appPref.getAuthToken() ?: "")?.getMessageUser()

            getUserToken?.enqueue(object : retrofit2.Callback<MessageUserResponse> {
                override fun onResponse(
                    call: Call<MessageUserResponse>,
                    response: Response<MessageUserResponse>
                ) {
                    if (response.code() == 200) {
                        Common_Methods.hideLoading()
                        response.body().let {
                            Log.e("TAG", "onResponse: $it")
                            val messageUserList = it?.data ?: ArrayList()
                            if (messageUserList.size > 0) {
                                val adapter = UserAdapter(mActivity, messageUserList)
                                rvUsers.layoutManager =
                                    LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                                rvUsers.adapter = adapter
                            } else {
                                tvNoUser.visibility = View.VISIBLE
                            }
                        }
                    }
                    Log.e("TAG", "onResponse: $newUsers")
                }

                override fun onFailure(call: Call<MessageUserResponse>, t: Throwable) {
                    Log.e("TAG", "onFailure: ${t.message}")
                }

            })
        }
    }

   /* private fun getUsers() {
        isLoading(true)
        val database = FirebaseFirestore.getInstance()
        database.collection(KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener { task ->
                isLoading(false)
                val currentUserId = appPref.getFCMUserId()
                if (task.isSuccessful && task.result != null) {
                    isLoading(false)
                    val users: ArrayList<User> = ArrayList()
                    for (queryDocumentSnapShot in task.result!!) {
                        if (currentUserId.equals(queryDocumentSnapShot.id)) {
                            continue
                        }
                        val user: User = User()
                        user.name = queryDocumentSnapShot.getString(KEY_NAME)
                        user.mobileNumber = queryDocumentSnapShot.getString(KEY_MOBILE)
                        user.userId = queryDocumentSnapShot.id
                        users.add(user)
                    }
                    if (users.size > 0) {
                        val adapter = UserAdapter(mActivity, users)
                        rvUsers.layoutManager =
                            LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                        rvUsers.adapter = adapter
                    } else {
                        tvNoUser.visibility = View.VISIBLE
                    }
                }
            }
    }*/

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            Common_Methods.showLoading(mActivity)
        } else {
            Common_Methods.hideLoading()
        }
    }


    inner class UserAdapter(
        private val context: Context,
        private val userList: ArrayList<MessageUserResponse.UserSearchData>
    ) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UserViewHolder {
            return UserViewHolder(
                LayoutInflater.from(activity).inflate(R.layout.item_user, viewGroup, false)
            )
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

            holder.tvUser.text = userList[position].name
            if (userList[position].groupMasterId!! > 0){
                holder.cardMain.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            }

            //holder.tvMobile.text = userList[position].

            holder.llMain.setOnClickListener {
                mActivity.addFragment(
                    SendMessageFragment.newInstance(userList[position]),
                    true,
                    true,
                    animationType = AnimationType.fadeInfadeOut
                )
            }

        }

        override fun getItemCount(): Int {
            return userList.size
        }

        inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvUser: TextView = itemView.findViewById(R.id.tvUserName)
            var tvMobile: TextView = itemView.findViewById(R.id.tvMobileNumber)
            var llMain: RelativeLayout = itemView.findViewById(R.id.rlMain)
            var cardMain: CardView = itemView.findViewById(R.id.cardMain)

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivMore -> {
                openPopupDialog()
            }
        }
    }

    private fun openPopupDialog() {
        val popupMenu = PopupMenu(mActivity, ivMore)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    mActivity.addFragment(
                        ProfileFragment(),
                        true,
                        false,
                        animationType = AnimationType.fadeInfadeOut
                    )
                }
                R.id.nav_logout -> {
                    logout()
                }
            }
            true
        }
        popupMenu.show()

    }

    private fun getUsersListFromApi() {
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            Common_Methods.showLoading(mActivity)
            val getUserToken = WebApiClient.getInstance(mActivity)
                .webApi_with_header(appPref.getAuthToken() ?: "")?.getUserSearch(
                    appPref.getLoggedUserName()!!, "", ""
                )

            getUserToken?.enqueue(object : retrofit2.Callback<UserSearchResponse> {
                override fun onResponse(
                    call: Call<UserSearchResponse>,
                    response: Response<UserSearchResponse>
                ) {
                    Common_Methods.hideLoading()
                    response.body().let {
                        Log.e("TAG", "onResponse: $it")
                    }
                }

                override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                    Common_Methods.hideLoading()
                    Log.e("TAG", "onFailure: ${t.message}")
                }

            })
        } else {
            Common_Methods.showToast(mActivity, "No Internet Connection")
        }
    }

}