package org.third.medicalapp.hospital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.databinding.ActivityHospitalListBinding
import org.third.medicalapp.hospital.adapter.HospitalAdapter
import org.third.medicalapp.hospital.model.Hospital
import org.third.medicalapp.hospital.model.HospitalList
import org.third.medicalapp.util.MyApplication
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HospitalListActivity : AppCompatActivity() {
    lateinit var binding: ActivityHospitalListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hcode = intent.getStringExtra("hcode")
        val location = intent.getStringExtra("location")


        binding.btnDepSelect.setOnClickListener {
            val intent = Intent(this, DepartSelectActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLocalSelect.setOnClickListener {
            val intent = Intent(this, LocationSelectActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (MyApplication.checkAdmin()) {
            binding.addFab.visibility = View.VISIBLE
        }
        binding.addFab.setOnClickListener {
            startActivity(Intent(this, HospitalAddActivity::class.java))
        }


    }

    override fun onStart() {
        super.onStart()
        val hCode = intent.getStringExtra("hcode")
        val city = intent.getStringExtra("city")
        val sigun = intent.getStringExtra("sigun")
        val dong = intent.getStringExtra("dong")

        Log.d("주소입력 확인", "city:${city}, sigun:${sigun}, dong:${dong}")
        val networkService = (applicationContext as MyApplication).hospitalServie
//        전체리스트 호출
        if (hCode == null || hCode == "" && city == null && sigun == null && dong == null) {
            val hospitalListCall = networkService.doGetHospitalList()
            hospitalListCall.enqueue(object : retrofit2.Callback<HospitalList> {
                override fun onResponse(
                    call: Call<HospitalList>,
                    response: Response<HospitalList>
                ) {
                    if (response.isSuccessful) {
                        binding.recyclerListView.layoutManager =
                            LinearLayoutManager(this@HospitalListActivity)
                        val hospital = response.body()?.hospitalList
                        val adapter = HospitalAdapter(this@HospitalListActivity, hospital)
                        binding.recyclerListView.adapter = adapter
                        binding.recyclerListView.addItemDecoration(
                            DividerItemDecoration(
                                this@HospitalListActivity,
                                LinearLayoutManager.VERTICAL
                            )
                        )
                    }

                }

                override fun onFailure(call: Call<HospitalList>, t: Throwable) {
                    call.cancel()
                }
            })
        }
//        진료과 선택 리스트 호출
        else if (hCode != null || hCode != "" && city == null && sigun == null && dong == null) {
            val hcodeListCall = networkService.doGetHcode(hcode = hCode)
            hcodeListCall.enqueue(object : retrofit2.Callback<HospitalList> {
                override fun onResponse(
                    call: Call<HospitalList>,
                    response: Response<HospitalList>
                ) {
                    binding.recyclerListView.layoutManager =
                        LinearLayoutManager(this@HospitalListActivity)
                    val hcodeHospital = response.body()?.hospitalList
                    val adapter = HospitalAdapter(this@HospitalListActivity, hcodeHospital)
                    binding.recyclerListView.adapter = adapter
                    binding.recyclerListView.addItemDecoration(
                        DividerItemDecoration(
                            this@HospitalListActivity,
                            LinearLayoutManager.VERTICAL
                        )
                    )
                }

                override fun onFailure(call: Call<HospitalList>, t: Throwable) {
                    call.cancel()
                }
            })
        }

//        지역검색 리스트 호출
        else if (dong != null && city == null && sigun == null) {
            val dongListCall = networkService.doGetDong(dong = dong)
            dongListCall.enqueue(object : retrofit2.Callback<HospitalList> {
                override fun onResponse(
                    call: Call<HospitalList>,
                    response: Response<HospitalList>
                ) {
                    if (response.isSuccessful) {
                        binding.recyclerListView.layoutManager =
                            LinearLayoutManager(this@HospitalListActivity)
                        val dongHospital = response.body()?.hospitalList
                        val adapter = HospitalAdapter(this@HospitalListActivity, dongHospital)
                        binding.recyclerListView.adapter = adapter
                        binding.recyclerListView.addItemDecoration(
                            DividerItemDecoration(
                                this@HospitalListActivity,
                                LinearLayoutManager.VERTICAL
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<HospitalList>, t: Throwable) {
                    call.cancel()
                }
            })
        }
        else if (city != null && dong == null && sigun == null){
            val cityListCall = networkService.doGetCity(city = city)
            cityListCall.enqueue(object : retrofit2.Callback<HospitalList>{
                override fun onResponse(
                    call: Call<HospitalList>,
                    response: Response<HospitalList>
                ) {
                    if (response.isSuccessful){
                        binding.recyclerListView.layoutManager = LinearLayoutManager(this@HospitalListActivity)
                        val cityHospital = response.body()?.hospitalList
                        val adapter = HospitalAdapter(this@HospitalListActivity, cityHospital)
                        binding.recyclerListView.adapter = adapter
                        binding.recyclerListView.addItemDecoration(DividerItemDecoration(this@HospitalListActivity, LinearLayoutManager.VERTICAL))
                    }
                }

                override fun onFailure(call: Call<HospitalList>, t: Throwable) {
                    call.cancel()
                }

            })
        }
        else if (sigun !=null && dong == null && city== null){
            val sigunListCall = networkService.doGetSignun(sigun = sigun)
            sigunListCall.enqueue(object : retrofit2.Callback<HospitalList>{
                override fun onResponse(
                    call: Call<HospitalList>,
                    response: Response<HospitalList>
                ) {
                    if (response.isSuccessful){
                        binding.recyclerListView.layoutManager = LinearLayoutManager(this@HospitalListActivity)
                        val sigunHospital = response.body()?.hospitalList
                        val adapter = HospitalAdapter(this@HospitalListActivity, sigunHospital)
                        binding.recyclerListView.adapter = adapter
                        binding.recyclerListView.addItemDecoration(DividerItemDecoration(this@HospitalListActivity, LinearLayoutManager.VERTICAL))
                    }
                }

                override fun onFailure(call: Call<HospitalList>, t: Throwable) {
                    call.cancel()
                }

            })
        }


    }

}