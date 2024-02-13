package org.third.medicalapp.hospital

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
        val intent = Intent(this, HospitalListActivity::class.java)

        binding.btnCloseX.setOnClickListener {
            startActivity(intent)
            finish()
        }

        binding.btnDongSearch.setOnClickListener {
            val location = binding.tvDong.text.toString()
            val searchSection = binding.tvDong.text.toString().substring(-1)
            if (searchSection == "동" || searchSection == "읍" || searchSection == "면") {
                intent.putExtra("dong", location)
            }else if (searchSection == "시" || searchSection=="군"|| searchSection=="구"){
                intent.putExtra("sigun", location)
            }else if (location == "서울" || location == "부산" || location =="대구" || location =="")
            startActivity(intent)
            finish()
        }

    }
}