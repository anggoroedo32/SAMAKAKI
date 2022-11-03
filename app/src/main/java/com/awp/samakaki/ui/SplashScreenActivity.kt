package com.awp.samakaki.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivitySplashScreenBinding
import com.awp.samakaki.ui.authentication.LoginActivity
import com.awp.samakaki.ui.authentication.RegisterActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    // creating a variable for our text view
    private lateinit var messageTV: TextView
    var invitToken : String? = null
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

        // initializing our variable
        messageTV = findViewById(R.id.tv_token)
        // getting the data from our intent in our uri.
        uri = intent.data

        // checking if the uri is null or not.
        if (uri != null) {
            // if the uri is not null then we are getting
            // the path segments and storing it in list.
            val parameters = uri!!.pathSegments

            // after that we are extracting string
            // from that parameters.
            val param = parameters[parameters.size - 1]

            // on below line we are setting that
            // string to our text view which
            // we got as params.


            messageTV.text = param
            invitToken = param

        }

        Log.d("isi_param", "param $invitToken")
        Log.d("isi_parameters", "parameters $invitToken")
        val btnGetStarted = binding.btnGetstarted
        btnGetStarted.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("invit", invitToken)
            startActivity(intent)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("invit", invitToken)
            startActivity(intent)
            finish()
        }, 3000)



    }
}