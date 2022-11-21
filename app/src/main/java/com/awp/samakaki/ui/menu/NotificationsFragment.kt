package com.awp.samakaki.ui.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.awp.samakaki.R
import com.awp.samakaki.adapter.NotificationsAdapter
import com.awp.samakaki.adapter.ibAcceptClickListener
import com.awp.samakaki.databinding.FragmentNotificationsBinding
import com.awp.samakaki.helper.ConnectivityStatus
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.UnreadItem
import com.awp.samakaki.viewmodel.FamilyTreeViewModel
import com.awp.samakaki.viewmodel.NotificationsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment(), ibAcceptClickListener {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val notificationsViewModel by viewModels<NotificationsViewModel>()
    private val familyTreeViewModel by viewModels<FamilyTreeViewModel>()
    private lateinit var notificationsAdapter: NotificationsAdapter
    var relationName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConnectivity()

        val btnBack = binding.btnBack
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


        val token = SessionManager.getToken(requireContext())
        notificationsViewModel.getNotifications("Bearer $token")
        notificationsViewModel.getNotifications.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    it.data?.let { it1 -> rvUndangan(it1.data!!.unread) }
                }

                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

    }


    override fun onCard(movie: UnreadItem) {

        val token = SessionManager.getToken(requireContext())
        val invitationToken = SessionManager.getInvitation(requireContext())
        val getData = view?.findViewById<Spinner>(R.id.relation)
        val dataSpinner = getData?.selectedItem.toString()
        Log.e("TAG", "onCard: $dataSpinner", )
        familyTreeViewModel.updateRelation("Bearer $token", movie.invitationToken.toString(), dataSpinner)
        familyTreeViewModel.updateRelations.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    it.data
                    textMessage("Anda telah menerima permintaan undangan")
                }
                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }
    }

    private fun checkConnectivity() {
        val connectivity = ConnectivityStatus(requireContext())
        connectivity.observe(viewLifecycleOwner, Observer {
                isConnected ->
            if(!isConnected){
                textMessageLong("Tidak ada koneksi internet")
            }
        })
    }

    private fun rvUndangan(list: List<UnreadItem>) {

        notificationsAdapter = NotificationsAdapter(list, requireContext())
        notificationsAdapter.listener = this
        binding.rvUndangan.layoutManager = LinearLayoutManager(activity)
        binding.rvUndangan.adapter = notificationsAdapter
        binding.rvUndangan.setHasFixedSize(true)
    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }

    private fun textMessageLong(s: String) {
        Toast.makeText(requireContext(),s, Toast.LENGTH_LONG).show()
    }

}