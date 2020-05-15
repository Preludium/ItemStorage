package pl.mik.itemstorage.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import pl.mik.itemstorage.*
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.database.AppDatabase
import pl.mik.itemstorage.database.Session
import pl.mik.itemstorage.database.entities.User
import pl.mik.itemstorage.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        App.database = AppDatabase.getAppDataBase(this)!!

        val username = findViewById<EditText>(R.id.login_box)
        val password = findViewById<EditText>(R.id.password_box)
        val login = findViewById<Button>(R.id.signin_btn)
        val register = findViewById<TextView>(R.id.signup_text)

        login.setOnClickListener {
            if (username.text.isEmpty() && password.text.isEmpty()) { // easy access for tests
                val user = User(name = "mik", password = "admin123")
                if (App.database?.users()?.getUserByName(user.name) != user) {
                    App.database?.users()?.insert(user)
                }
                App.session = App.database?.users()?.getUserByName("mik")?.id?.let { it1 ->
                    Session(it1)
                }
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            else {
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

        //Complete and destroy login activity once successful
//        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        App.database?.close()
    }
}
