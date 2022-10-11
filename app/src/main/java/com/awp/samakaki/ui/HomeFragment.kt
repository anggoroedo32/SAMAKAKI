package com.awp.samakaki.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var familyName = arrayOf<String?>(
        "Suharto Family",
        "Keluarga Cemara",
        "Arisan Keluarga"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spin = binding.searchFamily
        spin.onItemSelectedListener = this

        val ad: ArrayAdapter<*> = ArrayAdapter(requireContext(), R.layout.spinner_item, familyName)
        ad.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        spin.adapter = ad

        val searchUser = binding.searchUser
        searchUser.setOnClickListener {
            val intent = Intent(context, DataFamilyTree::class.java)
            startActivity(intent)
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(context,
            familyName[position],
            Toast.LENGTH_LONG)
            .show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}