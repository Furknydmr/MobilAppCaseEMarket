package com.example.mobilappcaseemarket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.mobilappcaseemarket.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import androidx.fragment.app.setFragmentResult

//Bu modalı bir Bottom Sheet olarak açıyorsun, yani ekranın altından kayan özel bir fragment.
//Bu bir BottomSheetDialogFragment (modal aşağıdan kayan bir sheet).
//
//Filtre seçenekleri burada gösterilecek.
//
//Kullanıcı seçim yapınca modal kapanacak ve sonucu ana Fragment’a geri gönderecek.
class FilterModalFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_filter_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnDefault).setOnClickListener {
            sendResult("DEFAULT")
        }

        view.findViewById<Button>(R.id.btnPriceAsc).setOnClickListener {
            sendResult("PRICE_ASC")
        }

        view.findViewById<Button>(R.id.btnPriceDesc).setOnClickListener {
            sendResult("PRICE_DESC")
        }

        view.findViewById<Button>(R.id.btnNameAZ).setOnClickListener {
            sendResult("NAME_ASC")
        }

        view.findViewById<Button>(R.id.btnNameZA).setOnClickListener {
            sendResult("NAME_DESC")
        }

        view.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            dismiss()
        }
    }

    private fun sendResult(type: String) {
        setFragmentResult(
            "filter_result",
            Bundle().apply {
                putString("sort_type", type)
            }
        )
        dismiss()
    }
}


