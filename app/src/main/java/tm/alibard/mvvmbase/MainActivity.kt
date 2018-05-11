package tm.alibard.mvvmbase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import tm.alibard.utils.CroppedImageHandler
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView.setOnClickListener {
            croppedImageHandler.showImageCaptureMethodDialog(R.string.test,
                    File.createTempFile(
                            System.currentTimeMillis().toString(),
                            ".jpg",
                            this.cacheDir))
        }
    }

    private val croppedImageHandler by lazy {
        CroppedImageHandler(this, {
            Log.d("MainActivity", "$it")
        })
    }
    override fun onSaveInstanceState(outState: Bundle?) {
        croppedImageHandler.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        croppedImageHandler.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        croppedImageHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        croppedImageHandler.onRestoreInstanceState(savedInstanceState)
    }
}
