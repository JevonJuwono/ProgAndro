package com.example.tas_jevonjuwono

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SearchData : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        firestore = FirebaseFirestore.getInstance()

        val cari = getIntent().getStringExtra("cari")
        val txtCari = findViewById<TextView>(R.id.txtFilter)

        txtCari.setText("Cari berdasarkan kata: " + cari.toString())

        firestore!!.collection("film")
            .whereGreaterThanOrEqualTo("film", cari.toString())
            .whereLessThanOrEqualTo("film", cari.toString() + "~")
            .orderBy("film", Query.Direction.ASCENDING).get()
            .addOnSuccessListener {
                var listFilmSearch = ArrayList<Film>()
                listFilmSearch.clear()

                for(doc in it){
                    val storageRef = FirebaseStorage.getInstance().reference.child("images/${doc.data["id"]}"+".jpg")
                    val localfile = File.createTempFile("temptImage","jpg")
                    storageRef.getFile(localfile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        val bitmap2 = findViewById<ImageView>(R.id.imageView)
                        bitmap2.setImageBitmap(bitmap)
                    }
                    listFilmSearch.add(Film("${doc.data["film"]}", "${doc.data["produser"]}", "${doc.data["tahun"]}", "${doc.data["genre"]}","${doc.data["pemeranUtm1"]}","${doc.data["pemeranUtm2"]}","${doc.data["pemeranUtm3"]}","${doc.data["id"]}"))
                }

                val rcySearch = findViewById<RecyclerView>(R.id.rcySearch)
                rcySearch.layoutManager = LinearLayoutManager(this)
                val adapter = FilmAdapter(listFilmSearch)
                rcySearch.adapter = adapter
            }
            .addOnFailureListener{
                Log.d("Load Data", "Pengambilan Data Gagal!")
            }
    }

    fun btnKembali(view: android.view.View) {
        auth = FirebaseAuth.getInstance()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}