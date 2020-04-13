package pl.mik.itemstorage.ui.scanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_scanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import pl.mik.itemstorage.R

class ScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private lateinit var scannerView: ZXingScannerView
    private val CAMERA_PERMISSION_RC = 1
    private var cameraPermission: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        setContentView(R.layout.activity_scanner)
        scannerView = findViewById(R.id.scanner)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_RC)
        else
            this.cameraPermission = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_RC && permissions[0] == Manifest.permission.CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                this.cameraPermission = true
            else
                finish()
        }
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result?) {
        Toast.makeText(this, result!!.text, Toast.LENGTH_LONG).show()

        val resultIntent = Intent()
        resultIntent.putExtra("CODE", result.text)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
