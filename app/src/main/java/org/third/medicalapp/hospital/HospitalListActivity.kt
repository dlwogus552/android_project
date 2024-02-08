package org.third.medicalapp.hospital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        binding.btnDepSelect.setOnClickListener {
            val intent = Intent(this, DepartSelectActivity::class.java)
            startActivity(intent)
        }

        binding.btnLocalSelect.setOnClickListener {
            val intent = Intent(this, LocationSelectActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onStart() {
        super.onStart()
        val networkService = (applicationContext as MyApplication).hospitalServie
        val hospitalListCall = networkService.doGetHospitalList()
        hospitalListCall.enqueue(object : retrofit2.Callback<HospitalList> {
            override fun onResponse(call: Call<HospitalList>,response: Response<HospitalList>) {
                if (response.isSuccessful) {
                    binding.recyclerListView.layoutManager = LinearLayoutManager(this@HospitalListActivity)
                    val hospital = response.body()?.hospitalList
                    val adapter = HospitalAdapter(this@HospitalListActivity,hospital)
                    binding.recyclerListView.adapter= adapter
                    binding.recyclerListView.addItemDecoration(DividerItemDecoration(this@HospitalListActivity, LinearLayoutManager.VERTICAL)
                    )
                }

            }

            override fun onFailure(call: Call<HospitalList>, t: Throwable) {
                call.cancel()
            }

        })


    }
}