package org.third.medicalapp.user

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.R
import org.third.medicalapp.community.CommunityData
import org.third.medicalapp.community.MyAdapter
import org.third.medicalapp.databinding.FragmentLikeHospitalBinding
import org.third.medicalapp.databinding.FragmentLikePharmacyBinding
import org.third.medicalapp.hospital.adapter.HospitalAdapter
import org.third.medicalapp.hospital.model.HospitalLike
import org.third.medicalapp.hospital.model.HospitalList
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.email
import retrofit2.Call
import retrofit2.Response

class LikeHospitalFragment : Fragment() {

    private var _binding: FragmentLikeHospitalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLikeHospitalBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = activity as UserMainActivity
        val sharedPref = activity.getSharedPreferences("User", AppCompatActivity.MODE_PRIVATE)
        val emailList = mutableListOf<String>()
        val hospitalList= mutableListOf<Long>()
        MyApplication.db.collection("hospital_like")
            .whereEqualTo("email", MyApplication.email)
            .get()
            .addOnSuccessListener { result ->
                val emailList = mutableListOf<String>()
                for (document in result) {
                    val item = document.toObject(HospitalLike::class.java)
                    var email = item.email
                    var email = item.
                    itemList.add(item)
                }
                // RecyclerView 설정
//                if(itemList.size!=0 && itemList!=null){
//                    binding.hospitalRecyclerView.layoutManager = LinearLayoutManager(context)
//                    binding.myWriteRecycler.adapter = MyAdapter(activity as UserMainActivity, itemList)
//                    binding.nonWrite.visibility=View.GONE
//                    binding.myWriteRecycler.visibility=View.VISIBLE
//                }else{
//                    binding.nonWrite.visibility=View.VISIBLE
//                    binding.myWriteRecycler.visibility=View.GONE
//                }
            }
            .addOnFailureListener { exception ->
                Log.d("aaaa", "error... getting document..", exception)
                Toast.makeText(context, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
        val networkService = (activity.applicationContext as MyApplication).hospitalServie
//        전체리스트 호출
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


    return root
}

// on create 종료
override fun onStart() {
    super.onStart()

    val activity = activity as UserMainActivity
    //회원정보 값 받아오기
    val sharedPref = activity.getSharedPreferences("User", AppCompatActivity.MODE_PRIVATE)
    sharedPref.getString("nickName", "-")


}


override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}