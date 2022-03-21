package com.example.tugas6

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        val btnSatu = findViewById<Button>(R.id.buttonDua)
        btnSatu.setOnClickListener {
            val j = Intent(this, ThirdActivity::class.java)
            startActivity(j)
        }
    }
}