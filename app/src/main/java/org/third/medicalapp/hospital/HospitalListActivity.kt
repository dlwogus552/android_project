package org.third.medicalapp.hospital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            if (hcode != null) {
                intent.putExtra("hcode", hcode)
            }
            startActivity(intent)
            finish()
        }


    }

    override fun onStart() {
        super.onStart()
        val hCode = intent.getStringExtra("hcode")
        val location = intent.getStringExtra("location")
        val networkService = (applicationContext as MyApplication).hospitalServie
//        전체리스트 호출
        if (hCode == null || hCode == "" && location == null) {
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
        else if (hCode != null || hCode != "" && location == null) {
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
        else if(location != null) {
            val dongListCall = networkService.doGetDong(dong = "dong")
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


    }

}