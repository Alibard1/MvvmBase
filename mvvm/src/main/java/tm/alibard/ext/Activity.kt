package tm.alibard.ext

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings


fun Activity.launchAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}