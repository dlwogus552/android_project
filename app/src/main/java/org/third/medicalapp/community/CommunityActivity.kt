package org.third.medicalapp.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityCommunityBinding
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.myCheckPermission

class CommunityActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myCheckPermission(this) // 퍼미션 체크
//        binding.addFab.setOnClickListener {
//            if (MyApplication.checkAuth()) {
//                val intent = Intent(this, CommunityWriteActivity::class.java)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "인증진행을 해주세요", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
        if (MyApplication.checkAuth()) {        // 로그인 되었을 때
            binding.logoutTextView.visibility = View.GONE
            binding.mainRecyclerView.visibility = View.VISIBLE
            makeRecyclerVIew()
        } else {        // 로그인 되어있지 않을 때
            binding.logoutTextView.visibility = View.VISIBLE
            binding.mainRecyclerView.visibility = View.GONE
        }
    }

    fun makeRecyclerVIew() {
        MyApplication.db. collection("news")
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<CommunityData>()
                for(document in result) {
                    val item = document.toObject(CommunityData::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.mainRecyclerView.adapter = MyAdapter(this, itemList)
            }
            .addOnFailureListener { exception ->
                Log.d("aaaa", "error... getting document..", exception)
                Toast.makeText(this, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_community, menu)
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        startActivity(Intent(this, AuthActivity::class.java))
//        return super.onOptionsItemSelected(item)
//    }
}