package pl.mik.itemstorage.ui.searchScanner

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.RingtoneManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import pl.mik.itemstorage.R


class SearchScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private lateinit var scannerView: ZXingScannerView
    private val CAMERA_PERMISSION_RC = 1
    private var cameraPermission: Boolean = false
    private lateinit var toScan: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        setContentView(R.layout.activity_scanner)
        scannerView = findViewById(R.id.scanner)
        toScan = intent.getSerializableExtra("ScannedCode") as String
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
        if (result.text == toScan) {
            val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            toneGen1.startTone(ToneGenerator.TONE_CDMA_LOW_SS, 150)
            finish()
        } else {
            val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
            scannerView.setResultHandler(this)
            scannerView.startCamera()
        }
    }
}
