package com.qatros.samakaki.helper

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.qatros.samakaki.R
import com.qatros.samakaki.ui.authentication.LoginActivity

object ShowDialog {

    fun showDialogEmailConfirmation(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_confirmation)

        dialog.setCancelable(true)
        dialog.show()
        val window: Window? = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}