package com.example.tugas6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val btnSatu = findViewById<Button>(R.id.buttonSatu)
        btnSatu.setOnClickListener{
            val j = Intent(this, SecondActivity::class.java)
            startActivity(j)
        }
        }
}