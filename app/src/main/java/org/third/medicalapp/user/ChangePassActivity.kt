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
        val drawerLayout: DrawerLayout = binding.drawerLayout
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.drawer_open, R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navView = binding.navView
        navView.setNavigationItemSelectedListener { menuItem ->
            // 클릭된 아이템에 따라 동작 처리
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    Toast.makeText(baseContext, "login", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_my_page -> {
                    Toast.makeText(baseContext, "My Page", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserMainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_admin -> {
                    Toast.makeText(baseContext, "User List", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserListActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_community -> {
                    val intent = Intent(this, CommunityActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_medical_info -> {
                    val intent = Intent(this, MedicalInfoActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        binding.checkPassBtn.setOnClickListener {
            MyApplication.auth.signInWithEmailAndPassword(
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