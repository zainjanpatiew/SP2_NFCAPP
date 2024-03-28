package com.example.nfccard
import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.nfccard.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder



class MainActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val name= Preferences(this).userId?.split("#")?.get(0)
        binding.HelloMessage.text= "Hello! $name"
        initService()

    }



    private fun initService() {
        binding.btnSetOutMessage.setOnClickListener {
            val intent = Intent(this@MainActivity, KHostApduService::class.java)
            intent.putExtra("ndefMessage", Preferences(this@MainActivity).userId)
            startService(intent)
        }
    }



    override fun onResume() {
        super.onResume()
        if (nfcAdapter?.isEnabled == true) {
            initService()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this, KHostApduService::class.java)
        stopService(serviceIntent)
    }
}