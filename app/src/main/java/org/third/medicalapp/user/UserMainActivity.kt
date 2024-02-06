package org.third.medicalapp.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import org.third.medicalapp.R
import org.third.medicalapp.community.CommunityActivity
import org.third.medicalapp.databinding.ActivityUserMainBinding
import org.third.medicalapp.medicalInfo.MedicalInfoActivity
import org.third.medicalapp.sign.LoginActivity
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.auth
import org.third.medicalapp.util.MyApplication.Companion.email

class UserMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserMainBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        //네비게이션 및 drawer 설정
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController=navHostFragment.navController

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView = binding.navView
        navView.menu.clear()
        navView.inflateMenu(R.menu.activity_main_login_drawer)
        if (MyApplication.checkAdmin()) {
            navView.menu.findItem(R.id.nav_admin).setVisible(true)
            navView.menu.findItem(R.id.nav_my_page).setVisible(false)
        }else{
            navView.menu.findItem(R.id.nav_admin).setVisible(false)
            navView.menu.findItem(R.id.nav_my_page).setVisible(true)
        }
        navView.setNavigationItemSelectedListener { menuItem ->
            // 클릭된 아이템에 따라 동작 처리
            when (menuItem.itemId) {
                R.id.nav_my_info -> {
                    navController.navigate(R.id.nav_my_page)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_my_write -> {
                    navController.navigate(R.id.nav_write)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_my_write -> {
                    navController.navigate(R.id.nav_my_review)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_my_page,menu)
        return super.onCreateOptionsMenu(menu)
    }
}