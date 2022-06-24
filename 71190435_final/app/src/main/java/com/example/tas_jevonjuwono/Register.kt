package com.example.tas_jevonjuwono

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val fullname = findViewById<EditText>(R.id.fullname)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val phone = findViewById<EditText>(R.id.phone)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val login = findViewById<TextView>(R.id.login)
        val progress = findViewById<ProgressBar>(R.id.progressBar)

        btnRegister.setOnClickListener {
            if(fullname.text.toString().isEmpty()){
                fullname.error = "Nama lengkap harus diisi!"
                fullname.requestFocus()
                return@setOnClickListener
            }

            if(email.text.toString().isEmpty()){
                email.error = "Email harus diisi!"
                email.requestFocus()
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
                email.error = "Masukkan email yang valid!"
                email.requestFocus()
                return@setOnClickListener
            }

            if(password.text.toString().isEmpty()){
                password.error = "Password harus diisi!"
                password.requestFocus()
                return@setOnClickListener
            }

            if(password.text.length < 6){
                password.error = "Panjang password harus >=6"
                password.requestFocus()
                return@setOnClickListener
            }

            if(password.text.length > 12){
                password.error = "Panjang password harus <=12"
                password.requestFocus()
                return@setOnClickListener
            }

            if(phone.text.toString().isEmpty()){
                phone.error = "No HP harus diisi!"
                phone.requestFocus()
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val intent = Intent(this, Login::class.java)
                        val user = User(fullname.text.toString(), email.text.toString(), phone.text.toString())
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            firestore?.collection("user")?.document(uid)?.set(user)
                        }else{
                            firestore?.collection("user")?.add(user)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Registrasi gagal! " + task.exception?.message, Toast.LENGTH_SHORT).show()
                        progress.visibility = View.INVISIBLE
                    }
                }
        }

        login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}