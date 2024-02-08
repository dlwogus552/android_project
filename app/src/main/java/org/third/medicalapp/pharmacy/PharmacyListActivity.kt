package org.third.medicalapp.pharmacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityPharmacyListBinding
import org.third.medicalapp.hospital.adapter.HospitalAdapter
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

        binding.btnLocalSelect.setOnClickListener {

        }

    }

    override fun onStart() {
        super.onStart()
        val networkService = (applicationContext as MyApplication).pharmacyService
        val pharmacyListCall = networkService.doGetPharmacyList()
        pharmacyListCall.enqueue(object : retrofit2.Callback<PharmacyList>{
            override fun onResponse(call: Call<PharmacyList>, response: Response<PharmacyList>) {
                if (response.isSuccessful){
                    binding.recyclerListViewPH.layoutManager = LinearLayoutManager(this@PharmacyListActivity)
                    val pharmacy = response.body()?.pharmacyList
                    val adapter = PharmacyAdapter(this@PharmacyListActivity, pharmacy)
                    binding.recyclerListViewPH.adapter = adapter
                    binding.recyclerListViewPH.addItemDecoration(DividerItemDecoration(this@PharmacyListActivity, LinearLayoutManager.VERTICAL))
                }
            }

            override fun onFailure(call: Call<PharmacyList>, t: Throwable) {
                call.cancel()
            }

        })
    }
}