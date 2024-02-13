package org.third.medicalapp.pharmacy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.MainActivity
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityPharmacyListBinding
import org.third.medicalapp.hospital.NameSearchActivity
import org.third.medicalapp.pharmacy.apater.PharmacyAdapter
import org.third.medicalapp.pharmacy.model.PharmacyList
import org.third.medicalapp.util.MyApplication
import retrofit2.Call
import retrofit2.Response

class PharmacyListActivity : AppCompatActivity() {
    lateinit var binding: ActivityPharmacyListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.title = "약국 찾기"

        binding.btnLocalSelect.setOnClickListener {

        }

        binding.btnNameSelect.setOnClickListener {

        }

        if(MyApplication.checkAdmin()){
            binding.addFab.visibility= View.VISIBLE
        }
        binding.addFab.setOnClickListener{
            startActivity(Intent(this,PharmacyAddActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 저장 메뉴 아이템을 선택한 경우
        if (item.itemId == R.id.menu_main) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        val networkService = (applicationContext as MyApplication).pharmacyService
//        전체 약국호출
        val pharmacyListCall = networkService.doGetPharmacyList()
        pharmacyListCall.enqueue(object : retrofit2.Callback<PharmacyList> {
            override fun onResponse(call: Call<PharmacyList>, response: Response<PharmacyList>) {
                if (response.isSuccessful) {
                    binding.recyclerListViewPH.layoutManager =
                        LinearLayoutManager(this@PharmacyListActivity)
                    val pharmacy = response.body()?.pharmacyList
                    val adapter = PharmacyAdapter(this@PharmacyListActivity, pharmacy)
                    binding.recyclerListViewPH.adapter = adapter
                    binding.recyclerListViewPH.addItemDecoration(
                        DividerItemDecoration(
                            this@PharmacyListActivity,
                            LinearLayoutManager.VERTICAL
                        )
                    )
                }
            }

            override fun onFailure(call: Call<PharmacyList>, t: Throwable) {
                call.cancel()
            }

        })
    }
}