package com.example.tas_jevonjuwono

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import android.app.Activity


class FilmAdapter (var listFilm: ArrayList<Film>): RecyclerView.Adapter<FilmAdapter.FilmHolder>() {

    class FilmHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var firestore: FirebaseFirestore? = null
        var film: Film? = null

        fun bind(film: Film) {
            firestore = FirebaseFirestore.getInstance()
            this.film = film
            Log.d("test",film.toString())
            view.findViewById<TextView>(R.id.tvFilm).text = film.film
            view.findViewById<TextView>(R.id.produser).text = film.produser
            view.findViewById<TextView>(R.id.tvtahun).text = "${film.tahun}"
            view.findViewById<TextView>(R.id.tvGenre).text = film.genre
            view.findViewById<TextView>(R.id.pemeranUtm1).text = film.pemeranUtm1
            view.findViewById<TextView>(R.id.pemeranUtm2).text = film.pemeranUtm2
            view.findViewById<TextView>(R.id.pemeranUtm3).text = film.pemeranUtm3

            view.findViewById<Button>(R.id.btnEdit).setOnClickListener {
                showUpdateDialog(film)
            }
        }

        fun showUpdateDialog(film: Film) {
            val builder = AlertDialog.Builder(view.context)
            firestore = FirebaseFirestore.getInstance()

            builder.setTitle("Edit Data")

            val inflater = LayoutInflater.from(view.context)
            val v = inflater.inflate(R.layout.activity_edit, null)

            val etFilm = v.findViewById<EditText>(R.id.etFilm)
            val etProduser = v.findViewById<EditText>(R.id.etProduser)
            val etPemeranUtm1 = v.findViewById<EditText>(R.id.etPemeranUtm1)
            val etPemeranUtm2 = v.findViewById<EditText>(R.id.etPemeranUtm2)
            val etPemeranUtm3 = v.findViewById<EditText>(R.id.etPemeranUtm3)
            val etTahun = v.findViewById<EditText>(R.id.etTahun)
            val etGenre = v.findViewById<EditText>(R.id.etGenre)

            etFilm.setText(film.film)
            etProduser.setText(film.produser)
            etPemeranUtm1.setText(film.pemeranUtm1)
            etPemeranUtm2.setText(film.pemeranUtm2)
            etPemeranUtm3.setText(film.pemeranUtm3)
            etTahun.setText(film.tahun)
            etGenre.setText(film.genre)


            builder.setView(v)

            builder.setPositiveButton("Update") { p0, p1 ->
                Log.d("Testing","testing")
                val namaFilm = etFilm.text.toString()
                val namaProduser = etProduser.text.toString()
                val namaPemeran1 = etPemeranUtm1.text.toString()
                val tahunFilm = etTahun.text.toString()
                val genreFilm = etGenre.text.toString()
                if (namaFilm.isEmpty()) {
                    etFilm.error = "Judul film tidak boleh kosong!"
                    etFilm.requestFocus()
                    return@setPositiveButton
                }

                if (namaProduser.isEmpty()){
                    etProduser.error="Nama Produser tidak boleh Kosong!"
                    etProduser.requestFocus()
                    return@setPositiveButton
                }
                if (namaPemeran1.isEmpty()){
                    etProduser.error="Nama Pemeran tidak boleh Kosong!"
                    etProduser.requestFocus()
                    return@setPositiveButton
                }
                if (tahunFilm.isEmpty()) {
                    etTahun.error = "Tahun film tidak boleh kosong!"
                    etTahun.requestFocus()
                    return@setPositiveButton
                }

                if (genreFilm.isEmpty()) {
                    etGenre.error = "Genre film tidak boleh kosong!"
                    etGenre.requestFocus()
                    return@setPositiveButton
                }
                firestore!!.collection("film")
                    .whereEqualTo("film", film.film)
                    .whereEqualTo("tahun", film.tahun)
                    .whereEqualTo("pemeranUtm1", film.pemeranUtm1)
                    .whereEqualTo("pemeranUtm2", film.pemeranUtm2)
                    .whereEqualTo("pemeranUtm3", film.pemeranUtm3)
                    .whereEqualTo("genre", film.genre).get()
                    .addOnSuccessListener {

                        for (doc in it) {
                            firestore!!.collection("film").document(doc.id)
                                .update(
                                    "film",
                                    etFilm.text.toString(),
                                    "tahun",
                                    etTahun.text.toString(),
                                    "pemeranUtm1",
                                    etPemeranUtm1.text.toString(),
                                    "pemeranUtm2",
                                    etPemeranUtm2.text.toString(),
                                    "pemeranUtm3",
                                    etPemeranUtm3.text.toString(),
                                    "genre",
                                    etGenre.text.toString(),
                                )
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        view.context,
                                        "Film updated!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    (view.context as Activity).recreate()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Log.d("Update data", "Data gagal diubah!")
                    }
            }

            builder.setNeutralButton("Delete") { p0, p1 ->
                firestore = FirebaseFirestore.getInstance()

                firestore!!.collection("film")
                    .whereEqualTo("film", film.film)
                    .whereEqualTo("tahun", film.tahun)
                    .whereEqualTo("pemeranUtm1", film.pemeranUtm1)
                    .whereEqualTo("pemeranUtm2", film.pemeranUtm2)
                    .whereEqualTo("pemeranUtm3", film.pemeranUtm3)
                    .whereEqualTo("genre", film.genre).get()
                    .addOnSuccessListener {
                        for (doc in it) {
                            firestore!!.collection("film").document(doc.id)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        view.context,
                                        "Film deleted!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    (view.context as Activity).recreate()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Log.d("Delete data", "Data gagal diubah!")
                    }
            }
            builder.setNegativeButton("No") { p0, p1 ->

            }

            val alert = builder.create()
            alert.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmAdapter.FilmHolder {
        val fh = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return FilmHolder(fh)
    }

    override fun onBindViewHolder(holder: FilmAdapter.FilmHolder, position: Int) {
        holder.bind(listFilm[position])
    }

    override fun getItemCount(): Int {
        return listFilm.size
    }
}