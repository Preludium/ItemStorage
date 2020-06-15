package pl.mik.itemstorage.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_login.*
import pl.mik.itemstorage.*
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.database.AppDatabase
import pl.mik.itemstorage.database.Session
import pl.mik.itemstorage.database.entities.User
import pl.mik.itemstorage.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private val STORAGE_PERM = 0
    private var storagePermission: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        App.database = AppDatabase.getAppDataBase(this)!!
        val username = findViewById<EditText>(R.id.login_box)
        val password = findViewById<EditText>(R.id.password_box)
        val login = findViewById<Button>(R.id.signin_btn)
        val register = findViewById<TextView>(R.id.signup_text)

        login.setOnClickListener {
            if(username.text.isEmpty() || password.text.isEmpty()) {
                Snackbar.make(it, "Fill all boxes", Snackbar.LENGTH_LONG).show()
            } else {
                val user: User? = App.database?.users()?.getUserByName(username.text.toString())
                if (user == null) {
                    Snackbar.make(it, "This user does not exist", Snackbar.LENGTH_LONG).show()
                } else {
                    if (user.password != password.text.toString()) {
                        Snackbar.make(it, "Login or password is incorrect", Snackbar.LENGTH_LONG)
                            .show()
                        return@setOnClickListener;
                    }
                    App.session = Session(user.id)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
            }
        }

        register.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }

        import_db.setOnClickListener {
            checkPermission()
            if(storagePermission) {
                AppDatabase.importDatabaseFile(this)
                Snackbar.make(it, "Database has been imported from Download folder", Snackbar.LENGTH_SHORT).show()
            }
        }

        export_db.setOnClickListener {
            checkPermission()
            if(storagePermission) {
                AppDatabase.exportDatabaseFile(this)
                Snackbar.make(it, "Database has been exported to Download folder", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERM
            )
        } else {
            this.storagePermission = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.isNotEmpty()) {
            if (requestCode == STORAGE_PERM
                && permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE
                && permissions[1] == Manifest.permission.READ_EXTERNAL_STORAGE) {
                this.storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        App.database?.close()
    }
}
