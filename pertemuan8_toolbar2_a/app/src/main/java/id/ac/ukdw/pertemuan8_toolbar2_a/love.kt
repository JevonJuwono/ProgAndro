package id.ac.ukdw.pertemuan8_toolbar2_a
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class love: Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View?{
            return inflater.inflate(R.layout.page_love, container, false)
        }
}