package com.awp.samakaki.ui.menu_silsilah_keluarga

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentFamilyBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.*
import com.awp.samakaki.utils.Graph
import com.awp.samakaki.viewmodel.FamilyTreeViewModel
import com.awp.samakaki.viewmodel.NotificationsViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.nikartm.support.ImageBadgeView
import java.util.*


@AndroidEntryPoint
class SilsilahKeluargaFragment : Fragment() {

    private var _binding: FragmentFamilyBinding? = null
    private val binding get() = _binding!!
    private val familyTreeViewModel by viewModels<FamilyTreeViewModel>()
    private val notificationsViewModel by viewModels<NotificationsViewModel>()
    private var listRelation = listOf<RelationItem>()


    private var imageBadgeView: ImageBadgeView? = null

    var dataRelationship: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamilyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingState()

        val familyTree = binding.wrapFamilyTree
        val isiProfil = binding.wrapIsiProfil
        val token = context?.let { SessionManager.getToken(it) }

        familyTreeViewModel.findUserRelations("Bearer $token")
        familyTreeViewModel.findUserRelations.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    val relationData = it.data?.data?.relation
                    Log.d("relation_data", "hasilnya $relationData")
                    if (relationData.isNullOrEmpty()) {
                        isiProfil.visibility = View.VISIBLE
                    } else {
                        familyTree.visibility = View.VISIBLE
                    }
                }

                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }



        //Dropdown Relationship
        val relationshipDropdown = resources.getStringArray(R.array.relationship)
        val relationshipDropdownAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,relationshipDropdown)
        val autoCompleteRelationship = binding.etHubungan
        autoCompleteRelationship.setAdapter(relationshipDropdownAdapter)

        val toolbar = binding.toolbarHomepage
        toolbar.inflateMenu(R.menu.menu_home)
        initNotificationCounter()
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
//                R.id.notification -> findNavController().navigate(R.id.action_navigation_family_to_notificationsFragment)
                R.id.settings -> findNavController().navigate(R.id.action_navigation_family_to_settingsFragment)
            }
            true
        }

        val fab = binding.fab
        fab.setOnClickListener {
            familyTree.visibility = View.GONE
            isiProfil.visibility = View.VISIBLE
        }


        val btnAddProfile = binding.btnAddProfile
        btnAddProfile.setOnClickListener {
            val name = binding.etNama.text.trim().toString()
            val phone = binding.etNoTelp.text.trim().toString()
            val relationship = binding.etHubungan.text.toString()

            when{
                relationship == "Adek Pertama" -> dataRelationship = "adek_pertama"
                relationship == "Adek Kedua" -> dataRelationship = "adek_kedua"
                relationship == "Adek Ketiga" -> dataRelationship = "adek_ketiga"
                relationship == "Bapak" -> dataRelationship = "bapak"
                relationship == "Ibu" -> dataRelationship = "ibu"
                relationship == "Anak Pertama" -> dataRelationship = "anak_pertama"
                relationship == "Anak Kedua" -> dataRelationship = "anak_kedua"
                relationship == "Anak Ketiga" -> dataRelationship = "anak_ketiga"
                relationship == "Kakek Dari Bapak" -> dataRelationship = "kakek_dari_bapak"
                relationship == "Nenek Dari Bapak" -> dataRelationship = "nenek_dari_bapak"
                relationship == "Kakek Dari Ibu" -> dataRelationship = "kakek_dari_ibu"
                relationship == "Nenek Dari Ibu" -> dataRelationship = "nenek_dari_ibu"
            }


            when {
                name.isEmpty() -> {
                    binding.etNama.error = getString(R.string.err_empty_family_member)
                }
                phone.isEmpty() -> {
                    binding.etNoTelp.error = getString(R.string.err_empty_phone)
                }
                else -> {
                    familyTreeViewModel.createUserRelations("Bearer $token", name, dataRelationship.toString())
                    Log.d("isinya_data_relation" , dataRelationship.toString())
                    familyTreeViewModel.createUserRelations.observe(viewLifecycleOwner) {
                        when(it) {
                            is BaseResponse.Success -> {
                                it.data
                                isiProfil.visibility = View.GONE
                                familyTree.visibility = View.VISIBLE
                                val invitationToken = it.data?.data?.invitaionToken
                                showDialog(link = it.data?.data?.invitaionToken)
                                Log.d("isinya_token" , it.data?.data?.invitaionToken.toString())
                            }

                            is BaseResponse.Error -> (it.msg.toString())
                        }
                    }
                }

            }
        }

        silsilahKeluarga()

    }

    private fun silsilahKeluarga() {

        val token = context?.let { SessionManager.getToken(it) }
        familyTreeViewModel.findUserRelations("Bearer $token")
        familyTreeViewModel.findUserRelations.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {

                    val imgCurrentUser = binding.imgDummy1
                    val nameCurrentUser = binding.nameDummy1
                    val fetchCurrentUsser = it.data?.data?.currentUser
                    nameCurrentUser.text = fetchCurrentUsser

                    val dataUser = it.data?.data?.relation?.map { it?.userRelated }
                    val relationName = it.data?.data?.relation?.map { it?.relationName }
                    val findRelation = it.data?.data?.relation

                    val getBapak = findRelation?.filter { it?.relationName == "bapak" }
                    val getIbu = findRelation?.filter { it?.relationName == "ibu" }
                    val adekPertama = findRelation?.filter { it?.relationName == "adek_pertama" }
                    val adekKeuda = findRelation?.filter { it?.relationName == "adek_kedua" }
                    val adekKetiga = findRelation?.filter { it?.relationName == "adek_ketiga" }
                    val kakakPertama = findRelation?.filter { it?.relationName == "kakak_pertama" }
                    val kakakKedua = findRelation?.filter { it?.relationName == "kakak_kedua" }
                    val kakakKetiga = findRelation?.filter { it?.relationName == "kakak_ketiga" }
                    val anakPertama = findRelation?.filter { it?.relationName == "anak_pertama" }
                    val anakKeuda = findRelation?.filter { it?.relationName == "anak_kedua" }
                    val anakKetiga = findRelation?.filter { it?.relationName == "anak_ketiga" }
                    val kakekDariBapak = findRelation?.filter { it?.relationName == "kakek_dari_bapak" }
                    val nenekDariBapak = findRelation?.filter { it?.relationName == "nenek_dari_bapak" }
                    val kakekDariIbu = findRelation?.filter { it?.relationName == "kakek_dari_ibu" }
                    val nenekDariIbu = findRelation?.filter { it?.relationName == "nenek_dari_ibu" }
                    val husband = findRelation?.filter { it?.relationName == "husband" }
                    val wife = findRelation?.filter { it?.relationName == "wife" }

                    if (getIbu?.isNotEmpty() == true) {
                        val img = binding.imgDummy6
                        val name = binding.nameDummy6
                        val ss = findRelation?.find { it?.relationName == "ibu" }
                        val username = ss?.userRelated
                        name.text = username
                    }

                    if (getBapak?.isNotEmpty() == true) {
                        val avatar = binding.imgDummy7
                        val name = binding.nameDummy7
                        val ss = findRelation?.find { it?.relationName == "bapak" }
                        val username = ss?.userRelated
                        name.text = username
                    }

                    if (anakPertama?.isNotEmpty() == true) {
                        val avatar = binding.imgDummy4
                        val name = binding.nameDummy4
                        val ss = findRelation?.find { it?.relationName == "anak_pertama" }
                        val username = ss?.userRelated
                        name.setText(username)
                    }

                    if (anakKeuda?.isNotEmpty() == true) {
                        val avatar = binding.imgDummy2
                        val name = binding.nameDummy2
                        val ss = findRelation?.find { it?.relationName == "anak_kedua" }
                        val username = ss?.userRelated
                        name.setText(username)
                    }

                    if (anakKetiga?.isNotEmpty() == true) {
                        val avatar = binding.imgDummy3
                        val name = binding.nameDummy3
                        val ss = findRelation?.find { it?.relationName == "anak_ketiga" }
                        val username = ss?.userRelated
                        name.setText(username)
                    }

                    if (husband?.isNotEmpty() == true) {
                        val avatar = binding.imgDummy8
                        val name = binding.nameDummy8
                        val ss = findRelation?.find { it?.relationName == "husband" }
                        val username = ss?.userRelated
                        name.setText(username)
                    }

                    if (wife?.isNotEmpty() == true) {
                        val avatar = binding.imgDummy8
                        val name = binding.nameDummy8
                        val ss = findRelation?.find { it?.relationName == "husband" }
                        val username = ss?.userRelated
                        name.setText(username)
                    }



                }

                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }


    }

    private fun initNotificationCounter() {
        val token = SessionManager.getToken(requireContext())
        imageBadgeView = view?.findViewById(R.id.notification_menu_icon)

        notificationsViewModel.getNotifications("Bearer $token")
        notificationsViewModel.getNotifications.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    imageBadgeView?.badgeValue = it.data?.data?.unread?.size!!
                }

                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

        imageBadgeView?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_family_to_notificationsFragment)
        }
    }

    private fun showDialog(link: String?) {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog)

        val btnClose = dialog.findViewById<ImageView>(R.id.close_btn)
        val edLink = dialog.findViewById<EditText>(R.id.ed_link)
        val btnCopy = dialog.findViewById<Button>(R.id.btn_copy)

        Log.d("link_edLinkValue", edLink.text.toString())


        edLink.setText("www.samakaki.com/$link")

        btnCopy.setOnClickListener {
            copyTextToClipboard(edLink.text.toString())
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()

    }

    private fun copyTextToClipboard(link: String) {
        Log.d("link_copy", link)
        val clipboardManager: ClipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", link.toString())
        clipboardManager.setPrimaryClip(clipData)


        Toast.makeText(context, "Link telah di salin ke clipboard", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingState(){
        familyTreeViewModel.loading.observe(viewLifecycleOwner){
            val progressBar =  binding.prgbar
            progressBar.isVisible = !it
            progressBar.isVisible = it
        }
    }

    protected fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }
}