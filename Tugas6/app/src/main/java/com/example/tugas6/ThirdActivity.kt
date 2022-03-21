package com.example.tugas6

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.third_activity)
        val btnSatu = findViewById<Button>(R.id.buttonTiga)
        btnSatu.setOnClickListener {
            val j = Intent(this, MainActivity::class.java)
            startActivity(j)
        }
    }
}