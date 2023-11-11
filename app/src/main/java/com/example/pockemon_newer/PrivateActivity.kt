package com.example.pockemon_newer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pockemon_newer.databinding.ActivityPrivateBinding

class PrivateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_private)
        binding = ActivityPrivateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.submit.setOnClickListener {
            val privateKeyTekl = binding.privatekey.text.toString()
            val latitude= binding.lat.text.toString()
            val longtitude= binding.longtitude.text.toString()
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("KEY", privateKeyTekl)
            intent.putExtra("LAT",latitude)
            intent.putExtra("LONG",longtitude)
            startActivity(intent)
        }
        binding.minpay.setOnClickListener {  val url = "https://minipay.opera.com/invite?referrer=%2B254700465809"
            val packageName = "com.opera.mini.native.beta" // Package name for Google Chrome

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.setPackage(packageName)

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
                val intent1 = Intent(this, MapsActivity::class.java)
                startActivity(intent1)

            } else {
                // Google Chrome is not installed on the device
                // Provide an option to download it from the Play Store
                val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))

                if (playStoreIntent.resolveActivity(packageManager) != null) {

                    startActivity(playStoreIntent)


                } else {
                    // Handle the case where the Play Store app is not available
                    // You can open the Play Store website in a web browser as an alternative.
                    val playStoreWebIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
                    startActivity(playStoreWebIntent)
                }
            }  }


    }
}