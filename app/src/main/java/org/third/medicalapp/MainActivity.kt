package org.third.medicalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import org.third.medicalapp.medicalInfo.MedicalInfoActivity
import org.third.medicalapp.databinding.ActivityMainBinding
import org.third.medicalapp.hospital.HospitalListActivity

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding // 바인딩 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView2.setOnClickListener{
            val intent =Intent(this,HospitalListActivity::class.java)
            startActivity(intent)
        }
        // 툴바를 액션바로 설정
        setSupportActionBar(binding.appBarMain.toolbar)

        // 네비게이션 드로어 토글 설정
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,  // DrawerLayout 객체 전달
            binding.appBarMain.toolbar,  // Toolbar 객체 전달
            R.string.drawer_open,  // 열기 메시지 리소스
            R.string.drawer_close  // 닫기 메시지 리소스
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        // 액션바에 네비게이션 아이콘 추가
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 네비게이션 아이템 클릭 이벤트 처리
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            // 클릭된 아이템에 따라 동작 처리
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // 홈으로 이동하는 동작 정의
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_hospital -> {
                    // 병원으로 이동하는 동작 정의
                    true
                }
                R.id.nav_pharmacy -> {
                    // 약국으로 이동하는 동작 정의
                    true
                }
                R.id.nav_community -> {
                    // 커뮤니티로 이동하는 동작 정의
                    true
                }
                R.id.nav_medical_info -> {
                    // 의료 정보로 이동하는 동작 정의
                    val intent = Intent(this, MedicalInfoActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 액션바에 네비게이션 아이콘 클릭 시 동작 처리
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}