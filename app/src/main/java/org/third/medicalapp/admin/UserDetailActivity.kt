package org.third.medicalapp.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityUserDetailBinding
import org.third.medicalapp.util.MyApplication.Companion.email

class UserDetailActivity : AppCompatActivity() {
    lateinit var binding:ActivityUserDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        binding.userNameText.text=intent.getStringExtra("userName")
        binding.nickNameText.text=intent.getStringExtra("nickName")
        binding.phoneNumberText.text=intent.getStringExtra("phoneNumber")
        binding.regiDateText.text=intent.getStringExtra("regiDate")
    }
}