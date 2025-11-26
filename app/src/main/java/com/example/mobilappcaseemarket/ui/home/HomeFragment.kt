package com.example.mobilappcaseemarket.ui.home

import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.example.mobilappcaseemarket.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.search)

        // Hint ve text rengini ayarlamak için
        val searchEditText = searchView.findViewById<EditText>(
            resources.getIdentifier("android:id/search_src_text", null, null)
        )

        searchEditText?.let {
            it.setTextColor(Color.BLACK)      // Kullanıcının yazdığı yazı
            it.setHintTextColor(Color.GRAY)  // Hint rengi
        }
    }
}
