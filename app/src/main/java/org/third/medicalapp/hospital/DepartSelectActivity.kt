package org.third.medicalapp.hospital

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityDepartSelectBinding

class DepartSelectActivity : AppCompatActivity() {
    lateinit var binding:ActivityDepartSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepartSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCloseX.setOnClickListener {
            finish()
        }
    }
}