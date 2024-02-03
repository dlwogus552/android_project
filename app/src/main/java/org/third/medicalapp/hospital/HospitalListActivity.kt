package org.third.medicalapp.hospital

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityHospitalListBinding

class HospitalListActivity : AppCompatActivity() {
    lateinit var binding: ActivityHospitalListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = mutableListOf<Hospital>()
        //test 입력값  나중에 retrofit으로 받을 것
        val hospitalName = arrayOf("치과 의원", "정형외과")
        val openTime = arrayOf("10:00~18:00", "10:00~18:00")
        val department = arrayOf("치과", "정형외과")
        val x = arrayOf("x좌표1","x좌표2")
        val y = arrayOf("y좌표1", "y좌표2")
        for (i in 0..1) {
            data.add(Hospital(hospitalName[i], openTime[i], department[i], x[i], y[i]))
        }

        binding.recyclerListView.layoutManager = LinearLayoutManager(this)
        binding.recyclerListView.adapter = ListAdapter(this,data,binding)
        binding.recyclerListView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }
}