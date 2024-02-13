package org.third.medicalapp.pharmacy.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityPhNameBinding

class PhNameActivity : AppCompatActivity() {
    lateinit var binding: ActivityPhNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ph_name)
    }
}