package org.third.medicalapp.hospital.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.third.medicalapp.databinding.ActivityHospitalListBinding
import org.third.medicalapp.databinding.ItemListBinding
import org.third.medicalapp.hospital.HospitalDetailActivity
import org.third.medicalapp.hospital.model.Hospital

class ListViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
class HospitalAdapter(val context: Context, val data: MutableList<Hospital>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder(ItemListBinding.inflate(
            LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return data?.size?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding =(holder as ListViewHolder).binding
        val hospital = data?.get(position)

        binding.tvHospitalName.text = hospital?.h_name
        binding.tvHospitalCode.text = hospital?.h_code
        binding.tvHospitalAddress.text = hospital?.addr
//        binding.tvHospitalOpen.text = 현재시간에 따라 진료중 // 진료종료 표시 기능 넣어보자
// 가능하다면 현재 내 위치와 x,y좌표값을 비교하여 거리가 얼마나 떨어져 있는지 알아보는 기능 넣어보자

        //test 입력값  나중에 retrofit으로 받을 것
//        val hospitalName = arrayOf("치과 의원", "정형외과")
//        val openTime = arrayOf("10:00~18:00", "10:00~18:00")
//        val department = arrayOf("치과", "정형외과")
//        val x = arrayOf("x좌표1","x좌표2")
//        val y = arrayOf("y좌표1", "y좌표2")
//        for (i in 0..1) {
//            data.add(Hospital(hospitalName[i], openTime[i], department[i], x[i], y[i]))
//        }
        binding.itemHospital.setOnClickListener {
            val intent = Intent(context, HospitalDetailActivity::class.java)
            intent.putExtra("h_name", hospital?.h_name)
            intent.putExtra("h_code", hospital?.h_code)
            intent.putExtra("addr", hospital?.addr)
            intent.putExtra("x", hospital?.x)
            intent.putExtra("y", hospital?.y)
            intent.putExtra("tel", hospital?.tel)
            context.startActivity(intent)
        }

    }

}