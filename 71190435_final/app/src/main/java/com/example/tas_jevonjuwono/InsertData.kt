package com.example.tas_jevonjuwono

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*

class InsertData : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null
    val GALLERY_REQUEST_CODE = 1
    val id = UUID.randomUUID().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val namaFilm = findViewById<EditText>(R.id.edtFilm)
        val namaProduser = findViewById<EditText>(R.id.edtProduser)
        val tahunFilm = findViewById<EditText>(R.id.edtTahun)
        val genreFilm = findViewById<EditText>(R.id.edtGenre)
        val pemeranUtm1 = findViewById<EditText>(R.id.edtPemeranUtm1)
        val pemeranUtm2 = findViewById<EditText>(R.id.edtPemeranUtm2)
        val pemeranUtm3 = findViewById<EditText>(R.id.edtPemeranUtm3)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        btnSimpan.setOnClickListener {

            if (namaFilm.text.toString().isEmpty()) {
                namaFilm.error = "Nama film tidak boleh kosong!"
                namaFilm.requestFocus()
                return@setOnClickListener
            }
            if (namaProduser.text.toString().isEmpty()){
                namaProduser.error = "Nama Produser tidak boleh kosong!"
                namaProduser.requestFocus()
                return@setOnClickListener
            }
            if (pemeranUtm1.text.toString().isEmpty()){
                pemeranUtm1.error = "Nama Pemeran Utama tidak boleh kosong!"
                pemeranUtm1.requestFocus()
                return@setOnClickListener
            }

            if (tahunFilm.text.toString().isEmpty()) {
                tahunFilm.error = "Tahun tidak boleh kosong!"
                tahunFilm.requestFocus()
                return@setOnClickListener
            }

            if (tahunFilm.text.toString().toInt() < 0) {
                tahunFilm.error = "Tahun tidak valid! Harus >= 0"
                tahunFilm.requestFocus()
                return@setOnClickListener
            }
            if (genreFilm.text.toString().isEmpty()) {
                genreFilm.error = "Genre tidak boleh kosong!"
                genreFilm.requestFocus()
                return@setOnClickListener
            }

            val film = Film(
                namaFilm.text.toString(),
                namaProduser.text.toString(),
                tahunFilm.text.toString(),
                genreFilm.text.toString(),
                pemeranUtm1.text.toString(),
                pemeranUtm2.text.toString(),
                pemeranUtm3.text.toString(),
                id
            )
            firestore?.collection("film")?.add(film)
                ?.addOnSuccessListener {
                    Toast.makeText(this, "Film inserted!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }?.addOnFailureListener {
                    Toast.makeText(this, "Film gagal ditambahkan!", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun selectImageFromGallery() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),
            GALLERY_REQUEST_CODE
        )
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )


        if (requestCode == GALLERY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null
        ) {

            // Get the Uri of data
            val file_uri = data.data
            if (file_uri != null) {
                uploadImageToFirebase(file_uri)
            }
        }
    }
    private fun uploadImageToFirebase(fileUri: Uri) {

        if (fileUri != null) {

            val database = FirebaseDatabase.getInstance()
            val refStorage = FirebaseStorage.getInstance().reference.child("images/$id"+".jpg")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                        }
                    })

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }
    }

    fun btnBatal(view: android.view.View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun btnUpload(view: android.view.View) {
        selectImageFromGallery()
    }
}
