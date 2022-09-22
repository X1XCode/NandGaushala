package com.gaushala.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.*
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter.formatIpAddress
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import com.gaushala.R
import java.io.File
import java.net.*
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


class Common_Methods {

    companion object {

        fun getRealPathFromUri(context: Context, contentUri: Uri): String {
            var cursor: Cursor? = null
            try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri, proj, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                return cursor.getString(column_index)
            } finally {
                cursor?.close()
            }
        }

        fun getdeviceModel(): String {
            val device_model = Build.MODEL
            return device_model
        }

        fun getdeviceVersion(): String {
            val device_version = Build.VERSION.RELEASE
            return device_version
        }

        fun getdevicename(): String {
            val device_name = Build.MANUFACTURER
            return device_name
        }

        /*fun getcurrentDate(): String {
            val sdfDate = SimpleDateFormat(DELIVER_DATE_FORMAT)//yyyy-MM-dd'T'HH:mm:ss.SSS
            val now = Date()
            return sdfDate.format(now)
        }*/

        var mProgressHUD: ProgressHUD? = null

        fun showLoading(mContext: Context) {
            try {
                if (mProgressHUD == null) {
                    mProgressHUD = ProgressHUD.show(
                        mContext,
                        "Please wait", false, false
                    )
                } else {
                    if (!mProgressHUD!!.isShowing) {
                        mProgressHUD = ProgressHUD.show(
                            mContext,
                            "Please wait", false, false
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun hideLoading() {
            try {
                if (mProgressHUD != null) {

                    if (mProgressHUD!!.isShowing) {
                        mProgressHUD!!.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun deleteDir(existFile: File?): Boolean {
            if (existFile != null && existFile.isDirectory) {
                val children = existFile.list()
                for (i in children.indices) {
                    val success = deleteDir(File(existFile, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }

            // The directory is now empty so delete it
            return existFile!!.delete()
        }

      /*  fun createDir(context: Context) {
            val directory = context.cacheDir.toString() + CACHE_FOLDER_NAME

            val file_dire = File(directory)
            if (!file_dire.exists()) {
                file_dire.mkdirs()
            }
        }

        fun createSignatureDir(context: Context, StoredPath: String): File {
            val directory = context.cacheDir.toString() + CACHE_FOLDER_NAME

            val file_dire = File(directory)
            if (!file_dire.exists()) {
                file_dire.mkdirs()
            }

            val file = File(directory, StoredPath)
            if (file.exists()) {
                deleteDir(file)
            }
            try {
                file.createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return file
        }
*/
        fun stringToURL(urlString: String): URL? {
            try {
                return URL(urlString)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            return null
        }




      /*  private val regexes = object : HashMap<CcnTypeEnum, String>() {
            init {
                put(CcnTypeEnum.AMERICAN_EXPRESS, "^3[47]\\d{2}([\\ \\-]?)\\d{4}\\1\\d{4}\\1\\d{3}$")
//                put(CcnTypeEnum.AMERICAN_EXPRESS, "^3[47]\\d\\d([\\ \\-]?)\\d{6}\\1\\d{5}$")
                put(CcnTypeEnum.VISA, "^4\\d{3}([\\ \\-]?)(?:\\d{4}\\1){2}\\d(?:\\d{3})?$")
                put(CcnTypeEnum.MASTERCARD, "^5[1-5]\\d{2}([\\ \\-]?)\\d{4}\\1\\d{4}\\1\\d{4}$")
                put(CcnTypeEnum.CHINA_UNIONPAY, "^62[0-5]\\d{13,16}$")
                put(
                    CcnTypeEnum.DISCOVER,
                    "^6(?:011|22(?:1(?=[\\ \\-]?(?:2[6-9]|[3-9]))|[2-8]|9(?=[\\ \\-]?(?:[01]|2[0-5])))|4[4-9]\\d|5\\d\\d)([\\ \\-]?)\\d{4}\\1\\d{4}\\1\\d{4}$"
                )
                put(CcnTypeEnum.JAPANESE_CREDIT_BUREAU, "^35(?:2[89]|[3-8]\\d)([\\ \\-]?)\\d{4}\\1\\d{4}\\1\\d{4}$")
                put(CcnTypeEnum.MAESTRO, "^(?:5[0678]\\d\\d|6304|6390|67\\d\\d)\\d{8,15}$")
            }
        }*/


        fun avoidDoubleClicks(view: View) {
            val DELAY_IN_MS: Long = 900
            if (!view.isClickable) {
                return
            }
            view.isClickable = false
            view.postDelayed({ view.isClickable = true }, DELAY_IN_MS)
        }

        fun isValid(url: String): Boolean {
            /* Try creating a valid URL */
            return try {
                URL(url).toURI()
                true
            } catch (e: Exception) {
                false
            }
            // If there was an Exception
            // while creating URL object
        }

        fun convertDpToPixel(dp: Float, context: Context): Float {
            val resources: Resources = context.resources
            val metrics: DisplayMetrics = resources.displayMetrics
            return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun convertDpToPixels(dp: Float, context: Context): Float {
            val resources: Resources = context.resources
            val metrics: DisplayMetrics = resources.displayMetrics
            return dp * metrics.density + 0.5F
        }

        fun calculateNoOfColumns(context: Context, v: Double): Int {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels.toFloat()
            return (dpWidth / v).toInt()
        }

        fun calculateNoOfRows(context: Context, v: Double): Int {
            val displayMetrics = context.resources.displayMetrics
            val dpHeight = displayMetrics.heightPixels.toFloat()
            return (dpHeight / v).toInt()
        }

        fun isSimExists(context: Context): Boolean {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val SIM_STATE = telephonyManager.simState
            if (SIM_STATE == TelephonyManager.SIM_STATE_READY)
                return true
            else {
                when (SIM_STATE) {
                    TelephonyManager.SIM_STATE_ABSENT //SimState = "No Sim Found!";
                    -> {
                    }
                    TelephonyManager.SIM_STATE_NETWORK_LOCKED //SimState = "Network Locked!";
                    -> {
                    }
                    TelephonyManager.SIM_STATE_PIN_REQUIRED //SimState = "PIN Required to access SIM!";
                    -> {
                    }
                    TelephonyManager.SIM_STATE_PUK_REQUIRED //SimState = "PUK Required to access SIM!"; // Personal Unblocking Code
                    -> {
                    }
                    TelephonyManager.SIM_STATE_UNKNOWN //SimState = "Unknown SIM State!";
                    -> {
                    }
                    TelephonyManager.SIM_STATE_CARD_RESTRICTED
                    ->{
                    }
                    TelephonyManager.SIM_STATE_CARD_IO_ERROR
                    ->{
                    }
                    TelephonyManager.SIM_STATE_NOT_READY
                    ->{
                    }
                    TelephonyManager.SIM_STATE_PERM_DISABLED
                    ->{
                    }
                }
                return false
            }
        }


        /*fun compressImage(filePath: String,context: Context): String {

            var scaledBitmap: Bitmap? = null

            val options = BitmapFactory.Options()

            //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
            //      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true
            var bmp = BitmapFactory.decodeFile(filePath, options)

            var actualHeight = options.outHeight
            var actualWidth = options.outWidth

            //      max Height and width values of the compressed image is taken as 816x612

            val maxHeight = 816.0f
            val maxWidth = 612.0f
            var imgRatio = (actualWidth / actualHeight).toFloat()
            val maxRatio = maxWidth / maxHeight

            //      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                when {
                    imgRatio < maxRatio -> {
                        imgRatio = maxHeight / actualHeight
                        actualWidth = (imgRatio * actualWidth).toInt()
                        actualHeight = maxHeight.toInt()
                    }
                    imgRatio > maxRatio -> {
                        imgRatio = maxWidth / actualWidth
                        actualHeight = (imgRatio * actualHeight).toInt()
                        actualWidth = maxWidth.toInt()
                    }
                    else -> {
                        actualHeight = maxHeight.toInt()
                        actualWidth = maxWidth.toInt()

                    }
                }
            }

            //      setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

            //      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false

            //      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)

            try {
                //          load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath, options)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f

            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

            val canvas = Canvas(scaledBitmap!!)
            canvas.setMatrix(scaleMatrix)
            canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

            //      check the rotation of the image and display it properly
            val exif: ExifInterface
            try {
                exif = ExifInterface(filePath)

                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0)
                val matrix = Matrix()
                when (orientation) {
                    6 -> {
                        matrix.postRotate(90F)
                    }
                    3 -> {
                        matrix.postRotate(180F)
                    }
                    8 -> {
                        matrix.postRotate(270F)
                    }
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height, matrix,
                    true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var out: FileOutputStream? = null
            val filename = getFilename(context)
            try {
                var imageQuality = 80
                when {
                    File(filename).length() / (1024 * 1024) >= 5 -> {
                        imageQuality = 10
                    }
                    File(filename).length() / (1024 * 1024) >= 2 -> {
                        imageQuality = 20
                    }
                    File(filename).length() / (1024 * 1024) >= 1 -> {
                        imageQuality = 50
                    }
                }
                //          write the compressed bitmap at the destination specified by filename.
                out = FileOutputStream(filename)

                //          write the compressed bitmap at the destination specified by filename.
                scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, imageQuality, out)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return filename
        }*/
        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
                val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }

            return inSampleSize
        }

        /*private fun getFilename(context:Context): String {
            val file = File(context.cacheDir.toString(), CACHE_FOLDER_NAME)
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"
        }*/

        fun distance(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double): Double {
            val theta = longitude1 - longitude2
            var dist = sin(deg2rad(latitude1)) * sin(deg2rad(latitude2)) + cos(deg2rad(latitude1)) * cos(
                deg2rad(latitude2)
            ) * cos(deg2rad(theta))
            dist = acos(dist)
            dist = rad2deg(dist)
            dist *= 60 * 1.1515
            val distanceMeter = dist/1000
            return (distanceMeter)
        }

        private fun deg2rad(deg: Double) : Double {
            return (deg * Math.PI / 180.0)
        }

        private fun rad2deg(rad: Double) : Double{
            return (rad * 180.0 / Math.PI)
        }

        fun getMobileIPAddress(): String? {
            try {
                val interfaces: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                    for (addr in addrs) {
                        if (!addr.isLoopbackAddress) {
                            if (addr is Inet4Address) {
                                return addr.hostAddress
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
            }
            return ""
        }

        fun getWifiIPAddress(context: Context): String? {
            val wifiMgr: WifiManager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            val wifiInfo: WifiInfo = wifiMgr.connectionInfo
            val ip: Int = wifiInfo.ipAddress
            return formatIpAddress(ip)
        }

        fun hasNavBar(context: Context): Boolean {
            val id: Int = context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
            return id > 0 && context.resources.getBoolean(id)
        }

        fun showToast(context: Context, msg: String){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        fun showSettingsAlert(context: Context) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(context.resources.getString(R.string.app_name))
            alertDialog.setMessage(context.resources.getString(R.string.str_allow_permission))
            alertDialog.setPositiveButton(
                context.resources.getString(R.string.action_settings)
            ) { dialog, which ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }


}