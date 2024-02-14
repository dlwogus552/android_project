package org.third.medicalapp.hospital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityHospitalSearchBinding

class HospitalSearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityHospitalSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, HospitalListActivity::class.java)

        binding.btnCloseX.setOnClickListener {
            startActivity(intent)
            finish()
        }

        binding.btnDepartSelect.setOnClickListener {
            val intent = Intent(this, DepartSelectActivity::class.java)
            startActivity(intent)
        }

        binding.btnSearch.setOnClickListener {
            val hname = binding.tvHname.text.toString()
            val dong = binding.tvDong.text.toString()
            intent.putExtra("hname", hname)
            intent.putExtra("dong", dong)
            startActivity(intent)
            finish()
        }
    }
}