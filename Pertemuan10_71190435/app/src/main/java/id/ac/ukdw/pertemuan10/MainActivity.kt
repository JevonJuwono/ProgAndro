package id.ac.ukdw.pertemuan10

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    var db: SQLiteDatabase? = null
    var dbHelper: SQLiteOpenHelper? = null
    var listPenduduk = ArrayList<String>()
    var adapter: PendudukAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        db = dbHelper?.writableDatabase

        val etNama = findViewById<EditText>(R.id.etNama)
        val etUsia = findViewById<EditText>(R.id.etUsia)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val searchNama = findViewById<EditText>(R.id.searchBar)
        val btnCari = findViewById<Button>(R.id.btnCari)

        btnSimpan.setOnClickListener {
            val values = ContentValues().apply {
                put(DatabaseContract.Penduduk.COLUMN_NAME_NAMA, etNama.text.toString())
                put(DatabaseContract.Penduduk.COLUMN_NAME_USIA, etUsia.text.toString())
            }
            db?.insert(DatabaseContract.Penduduk.TABLE_NAME, null, values)
            etNama.setText("")
            etUsia.setText("")
            refreshData()
        }

        btnCari.setOnClickListener {
            searchData(searchNama.text.toString())
            searchNama.setText("")
        }

        val rcyPenduduk = findViewById<RecyclerView>(R.id.rcyPenduduk)
        rcyPenduduk.layoutManager = LinearLayoutManager(this)
        adapter = PendudukAdapter(listPenduduk,db)
        rcyPenduduk.adapter = adapter
        refreshData()
    }

    fun searchData(a: String) {
        listPenduduk.clear()

        val cursor = db?.rawQuery("SELECT * FROM " + DatabaseContract.Penduduk.TABLE_NAME + " WHERE " + DatabaseContract.Penduduk.COLUMN_NAME_NAMA + " LIKE '" + a + "%' ", null)

        with(cursor!!) {
            while (moveToNext()) {
                val nama = getString(getColumnIndex(DatabaseContract.Penduduk.COLUMN_NAME_NAMA))
                val usia = getString(getColumnIndex(DatabaseContract.Penduduk.COLUMN_NAME_USIA))
                listPenduduk.add("${nama} (${usia})")
            }
            adapter?.notifyDataSetChanged()
        }
    }

    fun refreshData() {
        listPenduduk.clear()

        val columns = arrayOf(
            BaseColumns._ID,
            DatabaseContract.Penduduk.COLUMN_NAME_NAMA,
            DatabaseContract.Penduduk.COLUMN_NAME_USIA
        )

        val cursor = db?.query(
            DatabaseContract.Penduduk.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor!!) {
            while (moveToNext()) {
                val nama = getString(getColumnIndex(DatabaseContract.Penduduk.COLUMN_NAME_NAMA))
                val usia = getString(getColumnIndex(DatabaseContract.Penduduk.COLUMN_NAME_USIA))
                listPenduduk.add("${nama} (${usia})")
            }
            adapter?.notifyDataSetChanged()
        }
    }
}