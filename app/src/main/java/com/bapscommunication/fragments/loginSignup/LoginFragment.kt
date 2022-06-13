package com.bapscommunication.fragments.loginSignup


import ValidationUtil.Companion.isRequiredField
import addFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bapscommunication.HomeActivity
import com.bapscommunication.R
import com.bapscommunication.fragments.commonfrag.MainBaseFragment
import com.bapscommunication.model.dashboard.TokenResponse
import com.bapscommunication.model.dashboard.User
import com.bapscommunication.model.dashboard.UserSearchResponse
import com.bapscommunication.utils.*
import com.bapscommunication.utils.Common_Methods.Companion.showToast
import com.bapscommunication.webservices.WebApiClient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.HashMap

private const val ARG_PARAM1 = "param1"

class LoginFragment : MainBaseFragment(), View.OnClickListener {

    var userMasterId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvSignup.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
    }

    private fun callLoginApi() {
        val username = etUserName.text.toString().trim()
        val password = etPassword.text.toString().trim()
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            Common_Methods.showLoading(mActivity)
            val getUserToken = WebApiClient.getInstance(mActivity)
                .webApi_without()?.getToken(
                    "password",
                    username,
                    password,
                    ""
                )

            getUserToken?.enqueue(object : retrofit2.Callback<TokenResponse> {
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    Common_Methods.hideLoading()
                    when {
                        response.code() == 200 -> {
                            response.body().let {
                                appPref.setLogin(true)
                                appPref.setLoggedInUserName(it?.userName ?: "")
                                appPref.setAuthToken(it?.accessToken ?: "")
                                if (it?.userMasterId != null) {
                                   userMasterId = it.userMasterId.toString()
                                    appPref.setUserMasterId(userMasterId)
                                    appPref.setFCMUserID(it.firebaseId ?: "")

                                    val intent = Intent(mActivity, HomeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    //isUserExistInFirebase(it)
                                }
                            }
                        }
                        response.code() == 400 -> {
                            Common_Methods.hideLoading()
                            Toast.makeText(
                                mActivity,
                                "Invalid username or password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Common_Methods.hideLoading()
                            Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    Common_Methods.hideLoading()
                    Log.e("TAG", "onFailure: ${t.message}")
                }

            })
        } else {
            showToast(mActivity, "No Internet Connection")
        }
    }

    private fun firebaseLogin() {
        Common_Methods.showLoading(mActivity)
        val database = FirebaseFirestore.getInstance()
        database.collection(KEY_COLLECTION_USERS)
            .whereEqualTo(KEY_NAME, etUserName.text.toString().trim())
            .get()
            .addOnCompleteListener { task ->
                Common_Methods.hideLoading()
                if (task.isSuccessful && task.result != null && task.result!!.documents.size > 0) {
                    val documentSnapShot = task.result!!.documents[0]
                    appPref.setFCMUserID(documentSnapShot.id)
                    val intent = Intent(mActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(mActivity, "Invalid username or password", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvSignup -> {
                mActivity.addFragment(
                    SignupFragment(),
                    true,
                    true,
                    animationType = AnimationType.fadeInfadeOut
                )
            }
            R.id.btnLogin -> {
                if (isValidate()) {
                    callLoginApi()
                }
            }
        }
    }

    private fun isValidate(): Boolean {
        if (!isRequiredField(etUserName.text.toString().trim())) {
            Toast.makeText(mActivity, "Please enter username", Toast.LENGTH_SHORT).show()
            return false
        } else if (!isRequiredField(etPassword.text.toString().trim())) {
            Toast.makeText(mActivity, "Please enter password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    companion object {
        fun newInstance(loginType: Boolean): LoginFragment {
            val args = Bundle()
            args.putBoolean(ARG_PARAM1, loginType)
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun isUserExistInFirebase(loginResponse: TokenResponse) {
        Common_Methods.showLoading(mActivity)
        var isUserExist = false
        val database = FirebaseFirestore.getInstance()
        database.collection(KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    for (queryDocumentSnapShot in task.result!!) {
                        val userId = queryDocumentSnapShot.getString(KEY_USER_ID)
                        if (userId.equals(userMasterId)) {
                            isUserExist = true
                        }
                    }
                    if (!isUserExist) {
                        val database = FirebaseFirestore.getInstance()
                        val user: HashMap<String, Any> = HashMap<String, Any>()
                        user[KEY_USER_ID] = userMasterId
                        user[KEY_NAME] = etUserName.text.toString().trim() ?: ""
                        user[KEY_PASSWORD] = etPassword.text.toString().trim() ?: ""
                        user[KEY_AUTH_TOKEN] = ""
                        database.collection(KEY_COLLECTION_USERS)
                            .add(user)
                            .addOnSuccessListener {
                                Common_Methods.hideLoading()


                                getUsersFromFirebase()
                            }
                            .addOnFailureListener() {
                                Common_Methods.hideLoading()
                                Log.e("TAG", "firebaseAuthenticate: ${it.message}")
                            }
                    } else {
                        firebaseLogin()
                    }
                }
            }

    }

    private fun getUsersFromFirebase() {
        Common_Methods.showLoading(mActivity)
        var userListFromFirebase: ArrayList<User> = ArrayList()
        val database = FirebaseFirestore.getInstance()
        database.collection(KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    Common_Methods.hideLoading()
                    for (queryDocumentSnapShot in task.result!!) {
                        val user: User = User()
                        user.name = queryDocumentSnapShot.getString(KEY_NAME)
                        user.authToken = queryDocumentSnapShot.getString(KEY_AUTH_TOKEN)
                        user.userId = queryDocumentSnapShot.getString(KEY_USER_ID)
                        userListFromFirebase.add(user)
                    }
                    getUsersListFromApi(userListFromFirebase)
                }
            }

    }

    private fun getUsersListFromApi(userListFromFirebase: ArrayList<User>) {
        Common_Methods.showLoading(mActivity)
        val newUsers: ArrayList<UserSearchResponse.UserSearchData> = ArrayList()
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            val getUserToken = WebApiClient.getInstance(mActivity)
                .webApi_with_header(appPref.getAuthToken() ?: "")?.getUserSearch(
                    "admin", "", ""
                )

            getUserToken?.enqueue(object : retrofit2.Callback<UserSearchResponse> {
                override fun onResponse(
                    call: Call<UserSearchResponse>,
                    response: Response<UserSearchResponse>
                ) {
                    if (response.code() == 200) {
                        Common_Methods.hideLoading()
                        response.body().let {
                            Log.e("TAG", "onResponse: $it")
                            for (i in it?.data!!) {
                                var isFound = false
                                for (j in userListFromFirebase) {
                                    if (i.userMasterId.toString() == j.userId) {
                                        isFound = true
                                    }
                                }
                                if (!isFound) {
                                    newUsers.add(i)
                                }
                            }
                            firebaseAuthenticate(newUsers)
                        }
                    }
                    Log.e("TAG", "onResponse: $newUsers")
                }

                override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                    Log.e("TAG", "onFailure: ${t.message}")
                }

            })
        }
    }

    fun firebaseAuthenticate(userList: ArrayList<UserSearchResponse.UserSearchData>) {

        if (userList.isNotEmpty() && userList != null) {
            for (i in userList) {
                val database = FirebaseFirestore.getInstance()
                val user: HashMap<String, Any> = HashMap<String, Any>()
                user[KEY_USER_ID] = i.userMasterId.toString() ?: ""
                user[KEY_NAME] = i.userName ?: ""
                user[KEY_PASSWORD] = i.password ?: ""
                user[KEY_AUTH_TOKEN] = ""
                database.collection(KEY_COLLECTION_USERS)
                    .add(user)
                    .addOnSuccessListener {
                        Log.e("TAG", "firebaseAuthenticate: AUTHENTICATE SUCCESSFUL")
                    }
                    .addOnFailureListener() {
                        Log.e("TAG", "firebaseAuthenticate: ${it.message}")
                    }
            }

            gotoActivity(HomeActivity::class.java, null, true)
        }

        //isUserExistInFirebase()

    }

}