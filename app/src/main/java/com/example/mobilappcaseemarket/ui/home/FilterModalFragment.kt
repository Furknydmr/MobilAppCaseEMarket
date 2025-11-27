package com.example.mobilappcaseemarket.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterModalFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_filter_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = ProductRepository()

        viewModel = ViewModelProvider(
            requireActivity(),
            HomeViewModelFactory(repo)
        )[HomeViewModel::class.java]

        view.findViewById<Button>(R.id.btnPriceAsc).setOnClickListener {
            viewModel.sortByPriceAsc()
            dismiss()
        }
        view.findViewById<Button>(R.id.btnPriceDesc).setOnClickListener {
            viewModel.sortByPriceDesc()
            dismiss()
        }
        view.findViewById<Button>(R.id.btnNameAZ).setOnClickListener {
            viewModel.sortByNameAZ()
            dismiss()
        }
        view.findViewById<Button>(R.id.btnNameZA).setOnClickListener {
            viewModel.sortByNameZA()
            dismiss()
        }

        view.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            dismiss()
        }
    }
}

