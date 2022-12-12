package com.awp.samakaki.ui.menu_silsilah_keluarga

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentFamilyBinding
import com.awp.samakaki.helper.ConnectivityStatus
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.RelationItem
import com.awp.samakaki.viewmodel.FamilyTreeViewModel
import com.awp.samakaki.viewmodel.NotificationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.nikartm.support.ImageBadgeView


@AndroidEntryPoint
open class SilsilahKeluargaFragment : Fragment() {

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
        checkConnectivity()

        val familyTree = binding.wrapFamilyTree
        val isiProfil = binding.wrapIsiProfil
        val token = context?.let { SessionManager.getToken(it) }

        familyTreeViewModel.findUserRelations("Bearer $token")
        familyTreeViewModel.findUserRelations.observe(viewLifecycleOwner) {
            when (it) {
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
        val relationshipDropdownAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, relationshipDropdown)
        val autoCompleteRelationship = binding.etHubungan
        autoCompleteRelationship.setText("Ibu")
        autoCompleteRelationship.setAdapter(relationshipDropdownAdapter)

        val toolbar = binding.toolbarHomepage
        toolbar.inflateMenu(R.menu.menu_home)
        initNotificationCounter()
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
//                R.id.notification -> findNavController().navigate(R.id.action_navigation_family_to_notificationsFragment)
                R.id.settings -> findNavController().navigate(R.id.action_navigation_family_to_settingsFragment)
            }
            true
        }

        val fab = binding.fab
        fab.setOnClickListener {
            familyTree.visibility = View.GONE
            isiProfil.visibility = View.VISIBLE
            familyTree.visibility = View.GONE
        }


        val btnAddProfile = binding.btnAddProfile
        btnAddProfile.setOnClickListener {
            val name = binding.etNama.text.trim().toString()
            val phone = binding.etNoTelp.text.trim().toString()
            val relationship = binding.etHubungan.text.toString()

            when {
                relationship == "Adek Pertama" -> dataRelationship = "adek_pertama"
                relationship == "Adek Kedua" -> dataRelationship = "adek_kedua"
                relationship == "Adek Ketiga" -> dataRelationship = "adek_ketiga"
                relationship == "Kakak Pertama" -> dataRelationship = "kakak_pertama"
                relationship == "Kakak Kedua" -> dataRelationship = "kakak_kedua"
                relationship == "Kakak Ketiga" -> dataRelationship = "kakak_ketiga"
                relationship == "Bapak" -> dataRelationship = "bapak"
                relationship == "Ibu" -> dataRelationship = "ibu"
                relationship == "Suami" -> dataRelationship = "husband"
                relationship == "Istri" -> dataRelationship = "wife"
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
                    familyTreeViewModel.createUserRelations(
                        "Bearer $token",
                        name,
                        dataRelationship.toString()
                    )
                    Log.d("isinya_data_relation", dataRelationship.toString())
                    familyTreeViewModel.createUserRelations.observe(viewLifecycleOwner) {
                        it.getContentIfNotHandled()?.let {
                            when (it) {
                                is BaseResponse.Success -> {
                                    it.data
                                    SessionManager.removeInvitationToken(requireContext())
                                    isiProfil.visibility = View.GONE
                                    familyTree.visibility = View.VISIBLE
                                    binding.etNama.text.clear()
                                    binding.etNoTelp.text.clear()
                                    binding.etHubungan.text.clear()
                                    val invitationToken = it.data?.data?.invitationToken
                                    showDialog(link = it.data?.data?.invitationToken)
                                    Log.d("isinya_token", it.data?.data?.invitationToken.toString())
                                }

                                is BaseResponse.Error -> {
                                    (it.msg.toString())
                                }
                            }
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
            when (it) {
                is BaseResponse.Success -> {

                    val onClickedFab = binding.fabOnClick
                    val userInfo = binding.tvUser
                    val relationInfo = binding.tvRelation


                    val imgCurrentUser = binding.layoutFamily.imgDummy1
                    val nameCurrentUser = binding.layoutFamily.nameDummy1
                    val fetchCurrentUsser = it.data?.data?.currentUser
                    nameCurrentUser.text = fetchCurrentUsser

                    val dataUser = it.data?.data?.relation?.map { it?.userRelated }
                    val relationName = it.data?.data?.relation?.map { it?.relationName }
                    val findRelation = it.data?.data?.relation
                    Log.d("asd", "silsilahKeluarga: $findRelation")

                    val getBapak = findRelation?.filter { it?.code == "A1Kn1" }
                    val getBapakByCode = findRelation?.filter { it?.code == "A1Kr10,1" }

                    val getIbu = findRelation?.filter { it?.code == "A1Kr1" }
                    val getIbuByCode = findRelation?.filter { it?.code == "A1Kn10,1" }

                    val adekPertama = findRelation?.filter { it?.code == "Kn1" }
                    val adekKeuda = findRelation?.filter { it?.relationName == "adek_kedua" }
                    val adekKetiga = findRelation?.filter { it?.relationName == "adek_ketiga" }
                    val kakakPertama = findRelation?.filter { it?.code == "Kr1" }
                    val kakakKedua = findRelation?.filter { it?.relationName == "kakak_kedua" }
                    val kakakKetiga = findRelation?.filter { it?.relationName == "kakak_ketiga" }

                    val anakPertama = findRelation?.filter { it?.code == "B1Kr1" }
                    val anakpertamaByCode = findRelation?.filter {it?.code == "0,1B1Kr1"}

                    val anakKedua = findRelation?.filter { it?.code == "B1Kr2" }
                    val anakKetiga = findRelation?.filter { it?.code == "B1Kr3" }

                    val kakekDariBapak = findRelation?.filter { it?.relationName == "kakek_dari_bapak" }

                    val nenekDariBapak = findRelation?.filter { it?.relationName == "nenek_dari_bapak" }
                    val getNenekDariBapakByCode = findRelation?.filter { it?.code == "A1Kr10,1A1Kr1" }

                    val kakekDariIbu = findRelation?.filter { it?.relationName == "kakek_dari_ibu" }
                    val getKakekDariIbuByCode = findRelation?.filter { it?.code == "A1Kr1A1Kr10,1" }

                    val nenekDariIbu = findRelation?.filter { it?.relationName == "nenek_dari_ibu" }
                    val getNenekDariIbuByCode = findRelation?.filter { it?.code == "A1Kr1A1Kr1" }

                    val husband = findRelation?.filter { it?.code == "0,1" }
                    val wife = findRelation?.filter { it?.code == "0,1" }

                    if (kakekDariBapak?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy14
                        val name = binding.layoutFamily.nameDummy14
                        val ss = findRelation?.find { it?.relationName == "kakek_dari_bapak" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Kakek Dari Bapak"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy14
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "kakek_dari_bapak") {
                                relationInfo.setText("Kakek Dari Bapak")
                            }
                        }

                    }

                    if (getNenekDariBapakByCode?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy16
                        val name = binding.layoutFamily.nameDummy16
                        val ss = findRelation?.find { it?.code == "A1Kr10,1A1Kr1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Nenek Dari Bapak"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy16
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "nenek_dari_bapak") {
                                relationInfo.setText("Nenek Dari Bapak")
                            }
                        }
                    } else if (nenekDariBapak?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy16
                        val name = binding.layoutFamily.nameDummy16
                        val ss = findRelation?.find { it?.relationName == "nenek_dari_bapak" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Nenek Dari Bapak"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy16
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "nenek_dari_bapak") {
                                relationInfo.setText("Nenek Dari Bapak")
                            }
                        }
                    }

                    if (getKakekDariIbuByCode?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy15
                        val name = binding.layoutFamily.nameDummy15
                        val ss = findRelation?.find { it?.code == "A1Kr1A1Kr10,1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Kakek Dari Bapak"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy15
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "kakek_dari_bapak") {
                                relationInfo.setText("Kakek Dari Bapak")
                            }
                        }

                    } else if(kakekDariIbu?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy15
                        val name = binding.layoutFamily.nameDummy15
                        val ss = findRelation?.find { it?.relationName == "kakek_dari_ibu" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Kakek Dari Ibu"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy15
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "kakek_dari_ibu") {
                                relationInfo.setText("Kakek Dari Ibu")
                            }
                        }
                    }

                    if (getNenekDariIbuByCode?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy17
                        val name = binding.layoutFamily.nameDummy17
                        val ss = findRelation?.find { it?.code == "A1Kr1A1Kr1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Nenek Dari Ibu"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy17
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "nenek_dari_ibu") {
                                relationInfo.setText("Nenek Dari Ibu")
                            }
                        }
                    }

                    if(nenekDariIbu?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy17
                        val name = binding.layoutFamily.nameDummy17
                        val ss = findRelation?.find { it?.relationName == "nenek_dari_ibu" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Nenek Dari Ibu"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy17
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "nenek_dari_ibu") {
                                relationInfo.setText("Nenek Dari Ibu")
                            }
                        }
                    }

                    if (kakakPertama?.isNotEmpty() == true) {
                        val img = binding.layoutFamily.imgDummy11
                        val name = binding.layoutFamily.nameDummy11
                        val ss = findRelation?.find { it?.code == "Kr1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Kakak Pertama"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy11
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "kakak_pertama") {
                                relationInfo.setText("Kakak Pertama")
                            }
                        }
                    }

                    if (kakakKedua?.isNotEmpty() == true) {
                        val img = binding.layoutFamily.imgDummy10
                        val name = binding.layoutFamily.nameDummy10
                        val ss = findRelation?.find { it?.relationName == "kakak_kedua" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Kakak Kedua"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy10
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "kakak_kedua") {
                                relationInfo.setText("Kakak Kedua")
                            }
                        }
                    }

                    if (kakakKetiga?.isNotEmpty() == true) {
                        val img = binding.layoutFamily.imgDummy9
                        val name = binding.layoutFamily.nameDummy9
                        val ss = findRelation?.find { it?.relationName == "kakak_ketiga" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Kakak Ketiga"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy9
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "kakak_ketiga") {
                                relationInfo.setText("Kakak Ketiga")
                            }
                        }
                    }

                    if (adekPertama?.isNotEmpty() == true) {
                        val img = binding.layoutFamily.imgDummy7
                        val name = binding.layoutFamily.nameDummy7
                        val ss = findRelation?.find { it?.code == "Kn1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Adek Pertama"
                            name.setTextColor(Color.parseColor("#A3A3A3"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#706B6B"))
                        }

                        val person = binding.layoutFamily.wrapDummy7
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "adek_pertama") {
                                relationInfo.setText("Adek Pertama")
                            }
                        }
                    }

                    if (adekKeuda?.isNotEmpty() == true) {
                        val img = binding.layoutFamily.imgDummy12
                        val name = binding.layoutFamily.nameDummy12
                        val ss = findRelation?.find { it?.relationName == "adek_kedua" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Adek Kedua"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#706B6B"))
                        }

                        val person = binding.layoutFamily.wrapDummy12
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "adek_kedua") {
                                relationInfo.setText("Adek Kedua")
                            }
                        }

                    }

                    if (adekKetiga?.isNotEmpty() == true) {
                        val img = binding.layoutFamily.imgDummy13
                        val name = binding.layoutFamily.nameDummy13
                        val ss = findRelation?.find { it?.relationName == "adek_ketiga" }
                        val username = ss?.userRelated

                        if (username == null){
                            name.text = "Adek Ketiga"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#706B6B"))
                        }

                        val person = binding.layoutFamily.wrapDummy13
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "adek_ketiga") {
                                relationInfo.setText("Adek Ketiga")
                            }
                        }
                    }

                    if (getIbuByCode?.isNotEmpty() == true) {
                        val img = binding.layoutFamily.imgDummy6
                        val name = binding.layoutFamily.nameDummy6
                        val ss = findRelation?.find { it?.code == "A1Kn10,1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Ibu"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy6
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "ibu") {
                                relationInfo.setText("Ibu")
                            }
                        }
                    } else if (getIbu?.isNotEmpty() == true) {
                        val img = binding.layoutFamily.imgDummy6
                        val name = binding.layoutFamily.nameDummy6
                        val ss = findRelation?.find { it?.code == "A1Kr1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Ibu"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy6
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "ibu") {
                                relationInfo.setText("Ibu")
                            }
                        }
                    }

                    if (getBapakByCode?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy18
                        val name = binding.layoutFamily.nameDummy18
                        val ss = findRelation?.find { it?.code == "A1Kr10,1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Bapak"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy18
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "bapak") {
                                relationInfo.setText("Bapak")
                            }
                        }
                    }

                    if (getBapak?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy18
                        val name = binding.layoutFamily.nameDummy18
                        val ss = findRelation?.find { it?.code == "A1Kn1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Bapak"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#262626"))
                        }

                        val person = binding.layoutFamily.wrapDummy18
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "bapak") {
                                relationInfo.setText("Bapak")
                            }
                        }
                    }

                    if (anakpertamaByCode?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy4
                        val name = binding.layoutFamily.nameDummy4
                        val ss = findRelation?.find { it?.code == "0,1B1Kr1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Anak Pertama"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#706B6B"))
                        }

                        val person = binding.layoutFamily.wrapDummy4
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "anak_pertama") {
                                relationInfo.setText("Anak Pertama")
                            }
                        }
                    } else if (anakPertama?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy4
                        val name = binding.layoutFamily.nameDummy4
                        val ss = findRelation?.find { it?.code == "B1Kr1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Anak Pertama"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#706B6B"))
                        }

                        val person = binding.layoutFamily.wrapDummy4
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "anak_pertama") {
                                relationInfo.setText("Anak Pertama")
                            }
                        }
                    }

                    if (anakKedua?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy2
                        val name = binding.layoutFamily.nameDummy2
                        val ss = findRelation?.find { it?.code == "B1Kr2" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Anak Kedua"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#706B6B"))
                        }

                        val person = binding.layoutFamily.wrapDummy2
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "anak_kedua") {
                                relationInfo.setText("Anak Kedua")
                            }
                        }
                    }

                    if (anakKetiga?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy3
                        val name = binding.layoutFamily.nameDummy3
                        val ss = findRelation?.find { it?.code == "B1Kr3" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Anak Ketiga"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#706B6B"))
                        }

                        val person = binding.layoutFamily.wrapDummy3
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "anak_ketiga") {
                                relationInfo.setText("Anak Ketiga")
                            }
                        }
                    }

                    if (husband?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy8
                        val name = binding.layoutFamily.nameDummy8
                        val ss = findRelation?.find { it?.code == "0,1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Suami"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#706B6B"))
                        }

                        val person = binding.layoutFamily.wrapDummy8
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "husband") {
                                relationInfo.setText("Suami")
                            }
                        }
                    }

                    if (wife?.isNotEmpty() == true) {
                        val avatar = binding.layoutFamily.imgDummy8
                        val name = binding.layoutFamily.nameDummy8
                        val ss = findRelation?.find { it?.code == "0,1" }
                        val username = ss?.userRelated
                        if (username == null){
                            name.text = "Istri"
                            name.setTextColor(Color.parseColor("#737373"))
                        } else {
                            name.text = username
                            name.setTextColor(Color.parseColor("#706B6B"))
                        }

                        val person = binding.layoutFamily.wrapDummy8
                        person.setOnClickListener {
                            val animationSlideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                            onClickedFab.visibility = View.VISIBLE
                            onClickedFab.startAnimation(animationSlideUp)
                            userInfo.setText(ss?.userRelated)
                            if (ss?.relationName == "wife") {
                                relationInfo.setText("Istri")
                            }
                        }
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
            when (it) {
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

    private fun checkConnectivity() {
        val connectivity = ConnectivityStatus(requireContext())
        connectivity.observe(viewLifecycleOwner) {
                isConnected ->
            if(!isConnected){
                textMessageLong("Tidak ada koneksi internet")
            }
        }
    }

    private fun showDialog(link: String?) {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog)

        val btnClose = dialog.findViewById<ImageView>(R.id.close_btn)
        val edLink = dialog.findViewById<EditText>(R.id.ed_link)
        val btnCopy = dialog.findViewById<Button>(R.id.btn_copy)
        val btnShareLinkWhatsapp = dialog.findViewById<ImageView>(R.id.share_link_whatsapp)

        Log.d("link_edLinkValue", edLink.text.toString())


        edLink.setText("www.samakaki.com/$link")

        btnCopy.setOnClickListener {
            copyTextToClipboard(edLink.text.toString())
        }

        btnShareLinkWhatsapp.setOnClickListener {
            sendMessage("Hai, saya ingin mengundang anda untuk masuk ke family tree saya. Silahkan ikuti tautan berikut ini\nwww.samakaki.com/$link")
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
        val window: Window? = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    fun sendMessage(message: String) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(intent)
    }

    private fun copyTextToClipboard(link: String) {
        Log.d("link_copy", link)
        val clipboardManager: ClipboardManager =
            activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", link.toString())
        clipboardManager.setPrimaryClip(clipData)


        Toast.makeText(context, "Link telah di salin ke clipboard", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingState() {
        familyTreeViewModel.loading.observe(viewLifecycleOwner) {
            val progressBar = binding.prgbar
            progressBar.isVisible = !it
            progressBar.isVisible = it
        }
    }

    fun textMessage(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    fun textMessageLong(s: String) {
        Toast.makeText(requireContext(),s, Toast.LENGTH_LONG).show()
    }

}