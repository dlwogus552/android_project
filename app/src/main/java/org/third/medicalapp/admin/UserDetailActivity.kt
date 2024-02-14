package org.third.medicalapp.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.R
import org.third.medicalapp.community.CommunityData
import org.third.medicalapp.community.MyAdapter
import org.third.medicalapp.databinding.ActivityUserDetailBinding
import org.third.medicalapp.user.UserMainActivity
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.email

class UserDetailActivity : AppCompatActivity() {
    lateinit var binding:ActivityUserDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        binding.userNameText.text=intent.getStringExtra("userName")
        binding.nickNameText.text=intent.getStringExtra("nickName")
        binding.phoneNumber.text=intent.getStringExtra("phoneNumber")
        binding.regiDate.text=intent.getStringExtra("regiDate")

        setSupportActionBar(binding.appBarMain.toolbar)

        val sharedPref = getSharedPreferences("User", MODE_PRIVATE)
        MyApplication.db.collection("community")
            .whereEqualTo("email",intent.getStringExtra("userName"))
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<CommunityData>()
                for (document in result) {
                    val item = document.toObject(CommunityData::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                // RecyclerView 설정
                if(itemList.size!=0 && itemList!=null){
                    binding.myWriteRecycler.layoutManager = LinearLayoutManager(this)
                    binding.myWriteRecycler.adapter = MyAdapter(this, itemList)
                    binding.nonWrite.visibility= View.GONE
                    binding.myWriteRecycler.visibility= View.VISIBLE
                }else{
                    binding.nonWrite.visibility= View.VISIBLE
                    binding.myWriteRecycler.visibility= View.GONE
                }
            }
            .addOnFailureListener { exception ->
                Log.d("aaaa", "error... getting document..", exception)
                Toast.makeText(this, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }
}