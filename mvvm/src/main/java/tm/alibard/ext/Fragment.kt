package tm.alibard.ext

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.Fragment


fun Fragment.launchAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context!!.packageName, null)
    intent.data = uri
    startActivity(intent)
}