package org.third.medicalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.third.medicalapp.community.CommunityActivity
import org.third.medicalapp.databinding.ActivityMainBinding
import org.third.medicalapp.medicalInfo.MedicalInfoActivity
import org.third.medicalapp.hospital.HospitalListActivity
import org.third.medicalapp.sign.LoginActivity
import org.third.medicalapp.user.UserMainActivity
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.email
import org.third.medicalapp.util.myCheckPermission

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding
    lateinit var navView:NavigationView
//    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = org.third.medicalapp.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myCheckPermission(this)

        setSupportActionBar(binding.appBarMain.toolbar)
        // 왼쪽 상단 버튼 만들기
//        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        val drawerLayout: DrawerLayout = binding.drawer

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.drawer_open, R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.imageView2.setOnClickListener{
            var intent = Intent(this,HospitalListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        navView = binding.navView
        if(MyApplication.checkAuth()){
            navView.menu.findItem(R.id.nav_login).setVisible(false)
            navView.menu.findItem(R.id.nav_my_page).setVisible(true)
            if(MyApplication.checkAdmin()){
                navView.menu.findItem(R.id.nav_admin).setVisible(true)
                navView.menu.findItem(R.id.nav_my_page).setVisible(false)
            }
        }else{
            navView.menu.findItem(R.id.nav_login).setVisible(true)
            navView.menu.findItem(R.id.nav_my_page).setVisible(false)
        }
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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }
}