package org.third.medicalapp.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import org.third.medicalapp.R
import org.third.medicalapp.admin.UserListActivity
import org.third.medicalapp.community.CommunityActivity
import org.third.medicalapp.databinding.ActivityChangePassBinding
import org.third.medicalapp.medicalInfo.MedicalInfoActivity
import org.third.medicalapp.sign.LoginActivity
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.auth
import org.third.medicalapp.util.MyApplication.Companion.email

class ChangePassActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityChangePassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.checkPassBtn.setOnClickListener {
            auth.signInWithEmailAndPassword(
                email.toString(),
                binding.currentPass.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        binding.checkView.visibility= View.GONE
                        binding.changeView.visibility=View.VISIBLE
                    }else{
                        binding.checkPassError.visibility=View.VISIBLE
                    }
                }
        }
        binding.changePassBtn.setOnClickListener{
            if(binding.changePass.text.toString() == binding.changePassCheck.text.toString()){
                auth.currentUser?.updatePassword(binding.changePass.text.toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("aaaa", "비밀번호 변경성공")
                            Toast.makeText(this, "변경성공", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Log.d("aaaa", "비밀번호 변경실패")
                            Toast.makeText(this, "변경성공", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                binding.changePassCheckError.visibility=View.VISIBLE
            }
        }
    }
}