package com.amb.kevyorganizer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.amb.kevyorganizer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddProductFragment : Fragment() {

    private val btnSaveProduct by lazy { view?.findViewById(R.id.btnSaveProduct) as FloatingActionButton }
    private val npProductAmount by lazy { view?.findViewById(R.id.npProductAmount) as NumberPicker }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnSaveProduct.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        npProductAmount.apply {
            minValue = 1
            maxValue = 100
            value = 1
        }
    }


}
