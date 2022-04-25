package id.ac.ukdw.pertemuan8_toolbar2_a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar_default))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val viewPager = findViewById<ViewPager2>(R.id.pager)
        val listFragment: ArrayList<Fragment> = arrayListOf(love(),message())
        val adapter = PagerAdapter(this, listFragment)

        viewPager.adapter = adapter
        viewPager.setCurrentItem(0)
    }

    class PagerAdapter(val fa: FragmentActivity, val listFragment: ArrayList<Fragment>): FragmentStateAdapter(fa){
        override fun getItemCount(): Int = listFragment.size
        override fun createFragment(position: Int): Fragment = listFragment[position]

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_default, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.love -> {
//                Toast.makeText(this,"ini menu profile", Toast.LENGTH_SHORT).show()
                val viewPager = findViewById<ViewPager2>(R.id.pager)
                viewPager.setCurrentItem(0)
                true
            }
            R.id.chat -> {
//                Toast.makeText(this,"ini menu setting", Toast.LENGTH_SHORT).show()
                val viewPager = findViewById<ViewPager2>(R.id.pager)
                viewPager.setCurrentItem(1)
                true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}