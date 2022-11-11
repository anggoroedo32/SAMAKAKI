package com.awp.samakaki.ui.menu_silsilah_keluarga

import android.os.Bundle
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
import com.awp.samakaki.databinding.FragmentEditprofileBinding
import com.awp.samakaki.databinding.FragmentTryBinding

class TryFragment : Fragment() {

    private var _binding: FragmentTryBinding? = null
    private val binding get() = _binding!!

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

}