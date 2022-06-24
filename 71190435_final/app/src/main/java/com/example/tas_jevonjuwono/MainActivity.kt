package com.example.tas_jevonjuwono

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null
    val cari: EditText? by lazy { findViewById(R.id.edtSearch) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        val txtUser = findViewById<TextView>(R.id.txtUser)
        val uid = auth.currentUser?.uid
        txtUser.setText("Selamat datang")

        firestore!!.collection("film").get()
            .addOnSuccessListener {
                var listFilm = ArrayList<Film>()
                listFilm.clear()

                for(doc in it){
                    listFilm.add(Film("${doc.data["film"]}", "${doc.data["produser"]}","${doc.data["tahun"]}", "${doc.data["genre"]}","${doc.data["pemeranUtm1"]}","${doc.data["pemeranUtm2"]}","${doc.data["pemeranUtm3"]}","${doc.data["id"]}"))
                }

                val rcyFilm = findViewById<RecyclerView>(R.id.rcyFilm)
                rcyFilm.layoutManager = LinearLayoutManager(this)
                val adapter = FilmAdapter(listFilm)
                rcyFilm.adapter = adapter
            }
            .addOnFailureListener{
                Log.d("Load Data", "Pengambilan Data Gagal!")
            }
    }

    fun btnlogout(view: android.view.View) {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    fun btnInsert(view: android.view.View) {
        auth = FirebaseAuth.getInstance()
        val intent = Intent(this, InsertData::class.java)
        startActivity(intent)
        finish()
    }

    fun btnCari(view: android.view.View) {
        auth = FirebaseAuth.getInstance()
        val intent = Intent(this, SearchData::class.java)
        intent.putExtra("cari", cari?.text.toString())
        startActivity(intent)
        finish()
    }
}