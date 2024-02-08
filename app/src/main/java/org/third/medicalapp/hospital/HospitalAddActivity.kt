package org.third.medicalapp.hospital

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityHospitalAddBinding

class HospitalAddActivity : AppCompatActivity() {
    lateinit var binding:ActivityHospitalAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}