package com.awp.samakaki.ui.menu_silsilah_keluarga

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.awp.samakaki.R
import android.widget.LinearLayout.LayoutParams
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.awp.samakaki.databinding.FragmentEditprofileBinding
import com.awp.samakaki.databinding.FragmentTryBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.FamilyTreeViewModel
import com.awp.samakaki.viewmodel.NotificationsViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TryFragment : Fragment() {

    private var _binding: FragmentTryBinding? = null
    private val binding get() = _binding!!
    private val familyTreeViewModel by viewModels<FamilyTreeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = context?.let { SessionManager.getToken(it) }
        familyTreeViewModel.findUserRelations("Bearer $token")
        familyTreeViewModel.findUserRelations.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {

                    val imgCurrentUser = binding.imgDummy1
                    val nameCurrentUser = binding.nameDummy1
                    val fetchCurrentUsser = it.data?.data?.currentUser
                    nameCurrentUser.setText(fetchCurrentUsser)

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
                        name.setText(username)
                    }

                    if (getBapak?.isNotEmpty() == true) {
                        val avatar = binding.imgDummy7
                        val name = binding.nameDummy7
                        val ss = findRelation?.find { it?.relationName == "bapak" }
                        val username = ss?.userRelated
                        name.setText(username)
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


//        val linearLayout = view.findViewById(R.id.wrap_dummy1) as LinearLayout
//
//        // Add textview 1
//
//        // Add textview 1
//        val imageview1 = ImageView(requireContext())
//        val textView1 = TextView(requireContext())
//        imageview1.layoutParams = LayoutParams(
//            LayoutParams.WRAP_CONTENT,
//            LayoutParams.WRAP_CONTENT
//        )
//        textView1.layoutParams = LayoutParams(
//            LayoutParams.WRAP_CONTENT,
//            LayoutParams.WRAP_CONTENT
//        )
//        imageview1.setImageResource(R.drawable.dummy_avatar)
//        textView1.text = "programmatically created TextView1"
//        textView1.setBackgroundColor(-0x99009a) // hex color 0xAARRGGBB
//
//        textView1.setPadding(20, 20, 20, 20) // in pixels (left, top, right, bottom)
//
//        linearLayout.addView(textView1)
//        linearLayout.addView(imageview1)
//
//        // Add textview 2
//
//        // Add textview 2
//        val textView2 = TextView(requireContext())
//        val layoutParams = LayoutParams(
//            LayoutParams.WRAP_CONTENT,
//            LayoutParams.WRAP_CONTENT
//        )
//        layoutParams.gravity = Gravity.RIGHT
//        layoutParams.setMargins(10, 10, 10, 10) // (left, top, right, bottom)
//
//        textView2.layoutParams = layoutParams
//        textView2.text = "programmatically created TextView2"
//        textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
//        textView2.setBackgroundColor(-0x2425) // hex color 0xAARRGGBB
//
//        linearLayout.addView(textView2)

    }

    protected fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }

}