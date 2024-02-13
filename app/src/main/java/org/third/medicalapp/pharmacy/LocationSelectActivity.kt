package org.third.medicalapp.pharmacy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityLocationSelectBinding

class LocationSelectActivity : AppCompatActivity() {
    lateinit var binding: ActivityLocationSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, PharmacyListActivity::class.java)

        binding.btnCloseX.setOnClickListener {
            startActivity(intent)
            finish()
        }

        binding.btnDongSearch.setOnClickListener {
            val dong = binding.tvDong.text.toString()
            intent.putExtra("dong", dong)
            startActivity(intent)
            finish()
        }

    }
}