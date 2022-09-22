package com.gaushala.fragments.dashboard


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaushala.R
import com.gaushala.fragments.commonfrag.HomeBaseFragment
import com.gaushala.model.dashboard.ChatMessage
import com.gaushala.model.dashboard.MessageUserResponse
import com.gaushala.model.dashboard.SendMessageResponse
import com.gaushala.utils.*
import com.gaushala.utils.Common_Methods.Companion.avoidDoubleClicks
import com.gaushala.utils.Common_Methods.Companion.hideLoading
import com.gaushala.webservices.WebApiClient
import com.google.firebase.firestore.*
import com.google.gson.JsonObject
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.fragment_send_message.*
import kotlinx.android.synthetic.main.toolbar_header.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.hypot


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SendMessageFragment : HomeBaseFragment(), View.OnClickListener {

    var chatMessages: ArrayList<ChatMessage> = ArrayList()
    var chatAdapter: ChatAdapter? = null
    var dataBase: FirebaseFirestore? = null
    var isGroup: Boolean = false
    lateinit var userData: MessageUserResponse.UserSearchData
    var arrAttachmentImages = arrayOf(
        R.drawable.ic_camera,
        R.drawable.ic_gallery,
        R.drawable.ic_audio,
        R.drawable.ic_location,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userData = it.getParcelable(ARG_PARAM1) ?: MessageUserResponse.UserSearchData()
            if (userData.groupMasterId!! > 0) {
                isGroup = true
            }
        }
    }

    lateinit var dialogView: View
    lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_send_message, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setHeader()
        listenMessages()
        etMessage.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etMessage.getRight() - etMessage.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    //setPermission()
                    openAttachmentDialog()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun initView() {
        iv_back.setOnClickListener(this)
        ivSend.setOnClickListener(this)
        chatMessages = ArrayList()
        chatAdapter = ChatAdapter(chatMessages, appPref.getFCMUserId().toString())
        val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
        layoutManager.stackFromEnd = true
        rvMessages.layoutManager = layoutManager
        rvMessages.adapter = chatAdapter
        dataBase = FirebaseFirestore.getInstance()
    }

    private fun setHeader() {
        tvHeader.text = userData.name
        iv_back.visibility = View.VISIBLE
        ivFilter.visibility = View.VISIBLE
    }

    private fun listenMessages() {

        val userID = if (isGroup) {
            userData.groupFirebaseId ?: ""
        } else {
            userData.toUserMasterFireBaseId ?: ""
        }

        if (isGroup) {
            dataBase?.collection(KEY_COLLECTION_GROUP_CHAT)
                ?.document(userID)
                ?.collection(userData.name.toString())
                ?.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val count = chatMessages.size
                        for (documentChange in snapshot.documentChanges) {
                            if (documentChange.type == DocumentChange.Type.ADDED) {
                                val chatMessage: ChatMessage = ChatMessage()
                                chatMessage.senderId =
                                    documentChange.document.getString(KEY_SENDER_ID)
                                chatMessage.receiverName = documentChange.document.getString(
                                    KEY_SENDER_NAME
                                )
                                //chatMessage.receiverId =
                                //  documentChange.document.getString(KEY_RECEIVER_ID)
                                chatMessage.message = documentChange.document.getString(KEY_MESSAGE)
                                chatMessage.dateTime =
                                    getReadableDateTime(
                                        documentChange.document.getDate(
                                            KEY_TIME_STAMP
                                        )!!
                                    )
                                chatMessage.dateObject =
                                    documentChange.document.getDate(KEY_TIME_STAMP)
                                chatMessages.add(chatMessage)
                            }
                        }
                        Collections.sort(chatMessages,
                            Comparator<ChatMessage?> { o1, o2 -> o1.dateObject!!.compareTo(o2.dateObject!!) })
                        if (count == 0) {
                            chatAdapter?.notifyDataSetChanged()
                        } else {
                            chatAdapter?.notifyItemRangeInserted(
                                chatMessages.size,
                                chatMessages.size
                            )
                            if(rvMessages != null){
                                rvMessages.smoothScrollToPosition(chatMessages.size - 1)
                            }

                        }
                        if (rvMessages != null){
                            rvMessages.visibility = View.VISIBLE
                        }
                    }
                    hideLoading()

                }

        } else {

            dataBase?.collection(KEY_COLLECTION_CHAT)
                ?.whereEqualTo(KEY_SENDER_ID, appPref.getFCMUserId())
                ?.whereEqualTo(KEY_RECEIVER_ID, userID)
                ?.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val count = chatMessages.size
                        for (documentChange in snapshot.documentChanges) {
                            if (documentChange.type == DocumentChange.Type.ADDED) {
                                val chatMessage: ChatMessage = ChatMessage()
                                chatMessage.senderId =
                                    documentChange.document.getString(KEY_SENDER_ID)
                                chatMessage.receiverId =
                                    documentChange.document.getString(KEY_RECEIVER_ID)
                                chatMessage.message = documentChange.document.getString(KEY_MESSAGE)
                                chatMessage.dateTime =
                                    getReadableDateTime(
                                        documentChange.document.getDate(
                                            KEY_TIME_STAMP
                                        )!!
                                    )
                                chatMessage.dateObject =
                                    documentChange.document.getDate(KEY_TIME_STAMP)
                                chatMessages.add(chatMessage)
                            }
                        }
                        Collections.sort(chatMessages,
                            Comparator<ChatMessage?> { o1, o2 -> o1.dateObject!!.compareTo(o2.dateObject!!) })
                        if (count == 0) {
                            chatAdapter?.notifyDataSetChanged()
                        } else {
                            chatAdapter?.notifyItemRangeInserted(
                                chatMessages.size,
                                chatMessages.size
                            )
                            if(rvMessages != null){
                                rvMessages.smoothScrollToPosition(chatMessages.size - 1)
                            }

                        }
                        if (rvMessages != null){
                            rvMessages.visibility = View.VISIBLE
                        }
                    }
                    hideLoading()

                }
            dataBase?.collection(KEY_COLLECTION_CHAT)
                ?.whereEqualTo(KEY_SENDER_ID, userID)
                ?.whereEqualTo(KEY_RECEIVER_ID, appPref.getFCMUserId())
                ?.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val count = chatMessages.size
                        for (documentChange in snapshot.documentChanges) {
                            if (documentChange.type == DocumentChange.Type.ADDED) {
                                val chatMessage: ChatMessage = ChatMessage()
                                chatMessage.senderId =
                                    documentChange.document.getString(KEY_SENDER_ID)
                                chatMessage.receiverId =
                                    documentChange.document.getString(KEY_RECEIVER_ID)
                                chatMessage.message = documentChange.document.getString(KEY_MESSAGE)
                                chatMessage.dateTime =
                                    getReadableDateTime(
                                        documentChange.document.getDate(
                                            KEY_TIME_STAMP
                                        )!!
                                    )
                                chatMessage.dateObject =
                                    documentChange.document.getDate(KEY_TIME_STAMP)
                                chatMessages.add(chatMessage)
                            }
                        }
                        Collections.sort(chatMessages,
                            Comparator<ChatMessage?> { o1, o2 -> o1.dateObject!!.compareTo(o2.dateObject!!) })
                        if (count == 0) {
                            chatAdapter?.notifyDataSetChanged()
                        } else {
                            chatAdapter?.notifyItemRangeInserted(
                                chatMessages.size,
                                chatMessages.size
                            )

                            if(rvMessages != null){
                                rvMessages.smoothScrollToPosition(chatMessages.size - 1)
                            }

                        }
                        if (rvMessages != null){
                            rvMessages.visibility = View.VISIBLE
                        }
                    }
                    hideLoading()
                }
        }
    }


    private fun sendMessage() {

        val userID = if (isGroup) {
            userData.groupFirebaseId ?: ""
        } else {
            userData.toUserMasterFireBaseId ?: ""
        }

        if (!isGroup) {
            val message: HashMap<String, Any> = HashMap()
            message[KEY_SENDER_ID] = appPref.getFCMUserId()!!
            message[KEY_RECEIVER_ID] = userID ?: ""
            message[KEY_MESSAGE] = etMessage.text.toString().trim()
            message[KEY_TIME_STAMP] = Date()
            dataBase?.collection(KEY_COLLECTION_CHAT)?.add(message)
            etMessage.text = null
        } else {
            val message: HashMap<String, Any> = HashMap()
            message[KEY_SENDER_ID] = appPref.getFCMUserId()!!
            message[KEY_SENDER_NAME] = appPref.getLoggedUserName() ?: ""
            //message[KEY_RECEIVER_ID] = userID ?: ""
            message[KEY_MESSAGE] = etMessage.text.toString().trim()
            message[KEY_TIME_STAMP] = Date()
            dataBase?.collection(KEY_COLLECTION_GROUP_CHAT)
                ?.document(userID)?.collection(userData.name.toString())?.add(message)
            etMessage.text = null
        }


        //Log.e("TAG", "sendMessage: parentMessageMasterID ${appPref.getUserMasterId()} toUserMasterID : ${userData.userMasterId}", )
        sendMessageApi()
    }

    private fun sendMessageApi() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("messageMasterId", "0")
        jsonObject.addProperty("messageText", etMessage.text.toString().trim())
        jsonObject.addProperty("attechmentData", "")
        jsonObject.addProperty("attechmentName", "")
        jsonObject.addProperty("messageType", 0)
        jsonObject.addProperty("parentMessageMasterId", appPref.getUserMasterId())
        jsonObject.addProperty("toUserMasterId", userData.userMasterId)
        jsonObject.addProperty("groupMasterId", 0)
        jsonObject.addProperty("responseRequired", true)
        jsonObject.addProperty("messageDetailsId", 0)
        jsonObject.addProperty("entryUserMasterId", 0)
        jsonObject.addProperty("entryVersion", "")

        Log.e("TAG", "sendMessageFragment: $jsonObject")

        val sendMessageCall = WebApiClient.getInstance(mActivity)
            .webApi_with_header(appPref.getAuthToken()!!)?.sendMessage(jsonObject)
        sendMessageCall?.enqueue(object : retrofit2.Callback<SendMessageResponse> {
            override fun onResponse(
                call: Call<SendMessageResponse>,
                response: Response<SendMessageResponse>
            ) {
                response.let {
                    if (response.isSuccessful) {

                    }
                }
            }

            override fun onFailure(call: Call<SendMessageResponse>, t: Throwable) {

            }

        })
    }

    override fun onClick(v: View?) {
        avoidDoubleClicks(v!!)
        when (v.id) {
            R.id.iv_back -> {
                mActivity.onBackPressed()
            }
            R.id.ivSend -> {
                if (etMessage.text.toString().trim().isNotEmpty()) {
                    sendMessage()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun openAttachmentDialog() {
        dialogView = View.inflate(mActivity, R.layout.dialog_attachment_layout, null)
        dialog = Dialog(mActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)
        val rvAttachMent: RecyclerView = dialog.findViewById(R.id.rvAttachment)
        rvAttachMent.layoutManager = GridLayoutManager(mActivity, 3)
        val adapter = AttachMentAdapter(
            mActivity,
            resources.getStringArray(R.array.attachment_dialog_string),
            arrAttachmentImages
        )
        rvAttachMent.adapter = adapter

        dialog.setOnShowListener {
            revealShow(dialogView, true, dialog)
        }


        //  revealShow(dialogView, false, dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        val wmlp = dialog.window!!.attributes

        wmlp.gravity = Gravity.BOTTOM or Gravity.RIGHT
        wmlp.x = 50

        wmlp.y = 200


        dialog.show()
    }

    inner class AttachMentAdapter(
        context: Context,
        arrAttachment: Array<String>,
        arrAttachmentImages: Array<Int>
    ) :
        RecyclerView.Adapter<AttachMentAdapter.ViewHolder>() {
        var arrAttachmentList: Array<String> = arrAttachment
        var arrAttachmentImages: Array<Int> = arrAttachmentImages
        var context: Context = context

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AttachMentAdapter.ViewHolder {
            val view = LayoutInflater.from(context).inflate(
                R.layout.item_attachment_layout,
                parent,
                false
            )
            return ViewHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onBindViewHolder(holder: AttachMentAdapter.ViewHolder, position: Int) {
            holder.tvAttach.text = arrAttachmentList[position]
            holder.imageAttchIcon.setImageResource(arrAttachmentImages[position])
            holder.imageAttchIcon.setOnClickListener {
                if (dialog != null && dialogView != null)
                    revealShow(dialogView, false, dialog)
            }
        }

        override fun getItemCount(): Int {
            return arrAttachmentList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvAttach: TextView = itemView.findViewById(R.id.tvAttachment)
            val imageAttchIcon: ImageView = itemView.findViewById(R.id.imgAttachIcon)
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun revealShow(dialogView: View, b: Boolean, dialog: Dialog) {
        val view: View = dialogView.findViewById(R.id.dialog)
        val w = view.width
        val h = view.height
        val endRadius = hypot(w.toDouble(), h.toDouble()).toInt()

        val cx: Int = ((view.top) + view.bottom + 20).toInt()
        val cy: Int = (view.left).toInt()

        if (b) {
            val revealAnimator =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, endRadius.toFloat())
            view.visibility = View.VISIBLE
            revealAnimator.duration = 700
            revealAnimator.start()
        } else {
            val anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius.toFloat(), 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    dialog.dismiss()
                    view.visibility = View.INVISIBLE
                }
            })
            anim.duration = 700
            anim.start()
        }
    }

    inner class ChatAdapter(val chatMessage: List<ChatMessage>, val senderId: String) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val VIEW_TYPE_SENT = 1
        private val VIEW_TYPE_RECEIVED = 0
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == VIEW_TYPE_SENT) {
                val view: View =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_container_sent_message, parent, false)
                SentMessageViewHolder(view)
            } else {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_container_received_message, parent, false)
                ReceivedMessageViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder.itemViewType) {
                VIEW_TYPE_SENT -> {
                    val holder = holder as SentMessageViewHolder
                    holder.tvMessageSent.text = chatMessages[position].message ?: ""
                    holder.tvSentDatetime.text = chatMessages[position].dateTime ?: ""
                }
                VIEW_TYPE_RECEIVED -> {
                    val holder = holder as ReceivedMessageViewHolder
                    if (chatMessage[position].receiverName != "") {
                        holder.tvReceiverName.visibility = View.VISIBLE
                    } else {
                        holder.tvReceiverName.visibility = View.GONE
                    }

                    holder.tvMessageReceived.text = chatMessages[position].message ?: ""
                    holder.tvReceiverName.text = chatMessage[position].receiverName ?: ""
                    holder.tvReceivedDatetime.text = chatMessages[position].dateTime ?: ""
                }
            }
        }

        override fun getItemCount(): Int {
            return chatMessages.size
        }

        override fun getItemViewType(position: Int): Int {
            return if (chatMessages[position].senderId.equals(senderId)) {
                VIEW_TYPE_SENT
            } else {
                VIEW_TYPE_RECEIVED
            }
        }

        inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvMessageSent: TextView = itemView.findViewById(R.id.tvMessageSent)
            var tvSentDatetime: TextView = itemView.findViewById(R.id.tvSentDateTime)
        }

        inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvMessageReceived: TextView = itemView.findViewById(R.id.tvMessageReceived)
            var tvReceivedDatetime: TextView = itemView.findViewById(R.id.tvReceivedDateTime)
            var tvReceiverName: TextView = itemView.findViewById(R.id.tvReceiverName)
        }

    }

    companion object {
        fun newInstance(user: MessageUserResponse.UserSearchData): SendMessageFragment {
            val args = Bundle()
            args.putParcelable(ARG_PARAM1, user)
            val fragment = SendMessageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getReadableDateTime(date: Date): String {
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            KotlinPermissions.with(mActivity) // where this is an FragmentActivity instance
                .permissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                )
                .onAccepted { permissions ->
                    if (permissions.size == 3) {
                        openAttachmentDialog()
                    }
                }
                .onDenied {
                    setPermission()
                }
                .onForeverDenied {
                    Common_Methods.showSettingsAlert(mActivity)
                }
                .ask()
        } else {
            KotlinPermissions.with(mActivity) // where this is an FragmentActivity instance
                .permissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
                .onAccepted { permissions ->
                    if (permissions.size == 4) {
                        openAttachmentDialog()
                    }
                }
                .onDenied {
                    setPermission()
                }
                .onForeverDenied {
                    Common_Methods.showSettingsAlert(mActivity)
                }
                .ask()
        }
    }

}

