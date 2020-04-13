package pl.mik.itemstorage.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.R
import pl.mik.itemstorage.database.entities.User

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val username = findViewById<EditText>(R.id.signup_login_box)
        val password1 = findViewById<EditText>(R.id.signup_password1_box)
        val password2 = findViewById<EditText>(R.id.signup_password2_box)
        val button = findViewById<Button>(R.id.signup_btn)

        button.setOnClickListener {
            if (username.text == null || password1.text == null || password2.text == null) {
                Snackbar.make(it, "Fill all boxes", Snackbar.LENGTH_SHORT).show()
            } else if (username.text.length < 5 || password1.text.length < 5 || password2.text.length < 5) {
                Snackbar.make(it, "Minimum 5 characters", Snackbar.LENGTH_SHORT).show()
            } else if (password1.text.toString() != password2.text.toString()) {
                Snackbar.make(it, "Passwords must match", Snackbar.LENGTH_SHORT).show()
            } else if (App.database?.users()?.getUserByName(username.text.toString()) != null){
                Snackbar.make(it, "Given username is occupied", Snackbar.LENGTH_SHORT).show()
            } else {
                val user = User(name = username.text.toString(), password = password1.text.toString())
                App.database?.users()?.insert(user)
                Toast.makeText(applicationContext,"User created successfully",Toast.LENGTH_LONG).show()
//                val list = App.database?.users()?.getAllUsers()
//                if (list != null) {
//                    for (item in list) {
//                        println("${item.id} ${item.name} ${item.password}")
//                    }
//                }
                finish()
            }
        }

        username.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val textEntered = username.text.toString()
                if (textEntered.isNotEmpty() && textEntered.contains(" ")) {
                    username.setText(username.text.toString().replace(" ", ""));
                    username.setSelection(username.text!!.length);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        password1.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val textEntered = password1.text.toString()
                if (textEntered.isNotEmpty() && textEntered.contains(" ")) {
                    password1.setText(password1.text.toString().replace(" ", ""));
                    password1.setSelection(password1.text!!.length);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        password2.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val textEntered = password2.text.toString()
                if (textEntered.isNotEmpty() && textEntered.contains(" ")) {
                    password2.setText(password2.text.toString().replace(" ", ""));
                    password2.setSelection(password2.text!!.length);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}
