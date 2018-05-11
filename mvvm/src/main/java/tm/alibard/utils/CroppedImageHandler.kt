//package tm.alibard.utils
//
//import android.Manifest
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.provider.MediaStore
//import android.support.annotation.StringRes
//import android.support.design.widget.Snackbar
//import android.support.v4.app.Fragment
//import android.support.v4.content.FileProvider
//import android.support.v4.content.PermissionChecker
//import com.afollestad.materialdialogs.MaterialDialog
//import com.afollestad.materialdialogs.Theme
//import com.theartofdev.edmodo.cropper.CropImage
//import com.theartofdev.edmodo.cropper.CropImageView
//import tm.alibard.ext.launchAppSettings
//import tm.alibard.mvvm.R
//import java.io.File
//
//
//class CroppedImageHandler {
//
//    companion object {
//        private const val CHOOSE_PHOTO = 315
//        private const val TAKE_PHOTO = 316
//        private const val REQUEST_CHOOSE_PHOTO = 317
//        private const val CROPPED_IMAGE_SIZE = 280
//        private const val CURRENT_URI = "current_uri"
//    }
//
//    constructor(fragment: Fragment,
//                callback: (Uri) -> Unit,
//                isNotCrop: Boolean = false) {
//        this.context = fragment.context
//        this.fragment = fragment
//        this.callback = callback
//        this.isNotCrop = isNotCrop
//    }
//
//    constructor(activity: Activity,
//                callback: (Uri) -> Unit,
//                isNotCrop: Boolean = false) {
//        this.context = activity
//        this.activity = activity
//        this.callback = callback
//    }
//
//    private val context: Context
//    private val callback: (Uri) -> Unit
//    private var fragment: Fragment? = null
//    private var activity: Activity? = null
//    private var isNotCrop: Boolean = false
//    private var takenPhotoUri: Uri? = null
//    private var originalPhotoLocation: File? = null
//
//    fun showImageCaptureMethodDialog(
//            @StringRes titleRes: Int,
//            takePhotoFileLocation: File) {
//
//        MaterialDialog.Builder(context)
//                .content(titleRes)
//                .theme(Theme.LIGHT)
//                .positiveText(R.string.photo_take)
//                .negativeText(R.string.photo_choose)
//                .onPositive { _, _ -> takePhoto(takePhotoFileLocation) }
//                .onNegative { _, _ ->
//                    choosePhoto()
//                }
//                .show()
//    }
//
//    private fun choosePhoto() {
//        when {
//            isReadExternalPermissionGranted() -> {
//                val intent = Intent()
//                intent.type = "image/*"
//                intent.action = Intent.ACTION_GET_CONTENT
//                startActivityForResult(Intent.createChooser(intent, null), CHOOSE_PHOTO)
//            }
//            shouldShowRationaleForReadExternal() -> showRationaleForReadExternal()
//            else -> requestReadExternalPermission()
//        }
//    }
//
//    private fun takePhoto(file: File) {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        originalPhotoLocation = file
//
//        val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", originalPhotoLocation)
//        takenPhotoUri = uri
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
//        startActivityForResult(Intent.createChooser(intent, null), TAKE_PHOTO)
//    }
//
//    private fun showRationaleForReadExternal() {
//        MaterialDialog.Builder(context)
//                .content(R.string.photos_external_perm_rationale)
//                .positiveText(R.string.photos_external_perm_rationale_action_positive)
//                .negativeText(R.string.photos_external_perm_rationale_action_negative)
//                .onPositive { _, _ -> requestReadExternalPermission() }
//                .onNegative { _, _ -> showDeniedForCamera() }
//                .show()
//    }
//
//    private fun showDeniedForCamera() {
//        val rootView = if (fragment != null) fragment!!.view!!.rootView
//        else activity!!.window.decorView.findViewById(android.R.id.content)
//
//        Snackbar.make(rootView,
//                R.string.photos_external_perm_denied,
//                Snackbar.LENGTH_LONG)
//                .setAction(R.string.photos_external_perm_denied_action_enable, { launchAppSettings() })
//                .show()
//    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
//                    val result = CropImage.getActivityResult(data)
//                    originalPhotoLocation?.delete()
//                    callback(result.uri)
//                }
//                CHOOSE_PHOTO -> {
//                    if (isNotCrop) {
//                        callback(data?.data!!)
//                    } else {
//                        data?.data?.let { launchCropImageActivity(it) }
//                    }
//                }
//                TAKE_PHOTO -> {
//                    if (isNotCrop) {
//                        callback(takenPhotoUri!!)
//                    } else {
//                        launchCropImageActivity(takenPhotoUri!!)
//                    }
//                }
//            }
//        }
//    }
//
//    fun onRequestPermissionsResult(
//            requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        when (requestCode) {
//            REQUEST_CHOOSE_PHOTO -> {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    choosePhoto()
//                } else {
//                    showDeniedForCamera()
//                }
//            }
//        }
//    }
//
//    private fun isReadExternalPermissionGranted() = PermissionChecker
//            .checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED
//
//    private fun startActivityForResult(intent: Intent, requestCode: Int) {
//        if (fragment != null) fragment!!.startActivityForResult(intent, requestCode)
//        else if (activity != null) activity!!.startActivityForResult(intent, requestCode)
//    }
//
//    private fun launchCropImageActivity(uri: Uri) {
//        val cropImageBuilder = CropImage.activity(uri)
//                .setFixAspectRatio(true)
//                .setRequestedSize(CROPPED_IMAGE_SIZE, CROPPED_IMAGE_SIZE)
//                .setCropShape(CropImageView.CropShape.OVAL)
//
//        if (fragment != null) cropImageBuilder.start(context, fragment!!)
//        else if (activity != null) cropImageBuilder.start(activity!!)
//    }
//
//    private fun launchAppSettings() {
//        if (fragment != null) fragment!!.launchAppSettings()
//        else if (activity != null) activity!!.launchAppSettings()
//    }
//
//    private fun shouldShowRationaleForReadExternal() =
//            if (fragment != null) {
//                fragment!!.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
//            } else if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                activity!!.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
//            } else false
//
//    private fun requestReadExternalPermission() {
//        if (fragment != null) {
//            fragment!!.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CHOOSE_PHOTO)
//        } else if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            activity!!.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CHOOSE_PHOTO)
//        }
//    }
//
//    fun onSaveInstanceState(outState: Bundle?) {
//        outState?.putString(CURRENT_URI, takenPhotoUri?.toString())
//    }
//
//    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        if (savedInstanceState?.getString(CURRENT_URI).isNullOrEmpty()) {
//            takenPhotoUri = Uri.parse(savedInstanceState?.getString(CURRENT_URI))
//        }
//
//    }
//}