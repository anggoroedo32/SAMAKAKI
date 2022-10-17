package com.awp.samakaki.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbarHomepage
        toolbar.inflateMenu(R.menu.menu_home)

        val spin = binding.searchFamily
        spin.onItemSelectedListener = this

        val ad: ArrayAdapter<*> = ArrayAdapter(requireContext(), R.layout.spinner_item, familyName)
        ad.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        spin.adapter = ad

        val btnPost = binding.btnPost
        btnPost.setOnClickListener {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu?.findItem(R.id.auto_complete)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                Toast.makeText(context, "Looking for $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("Not yet implemented")
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.auto_complete -> {
                Toast.makeText(context, "Looking for autocomplete", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}