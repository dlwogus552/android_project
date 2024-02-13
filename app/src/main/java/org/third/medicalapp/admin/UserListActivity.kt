package org.third.medicalapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.R
import org.third.medicalapp.community.CommunityActivity
import org.third.medicalapp.databinding.ActivityUserListBinding
import org.third.medicalapp.medicalInfo.MedicalInfoActivity
import org.third.medicalapp.sign.LoginActivity
import org.third.medicalapp.sign.model.UserModel
import org.third.medicalapp.sign.model.UserModelList
import org.third.medicalapp.user.UserMainActivity
import org.third.medicalapp.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListActivity : AppCompatActivity() {
    lateinit var binding:ActivityUserListBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.title="My Page"
        //네비게이션 및 drawer 설정
        setSupportActionBar(binding.appBarMain.toolbar)
        var drawerLayout: DrawerLayout = binding.drawerLayout
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
    }

    override fun onStart() {
        super.onStart()
        // 리스트 가져오기
        val networkService =
            (applicationContext as MyApplication).netWorkService
        val check = networkService.getUserList()
        val itemList = mutableListOf<UserModel>()
        check.enqueue(object : Callback<UserModelList>{
            override fun onResponse(call: Call<UserModelList>, response: Response<UserModelList>) {
                for(data in response.body()?.users!!){
                    if(data.roles!="admin"){
                        itemList.add(data)
                    }
                }
                binding.recyclerUserListView.layoutManager = LinearLayoutManager(baseContext)
                binding.recyclerUserListView.adapter = UserAdapter(this@UserListActivity, itemList)
                binding.recyclerUserListView.addItemDecoration(DividerItemDecoration(baseContext,LinearLayoutManager.VERTICAL))

            }
            override fun onFailure(call: Call<UserModelList>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_admin_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}