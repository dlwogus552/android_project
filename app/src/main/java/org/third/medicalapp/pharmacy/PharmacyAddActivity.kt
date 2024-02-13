package org.third.medicalapp.pharmacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityPharmacyAddBinding

class PharmacyAddActivity : AppCompatActivity() {
    lateinit var binding: ActivityPharmacyAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacyAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}