package org.third.medicalapp.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.databinding.ActivityUserListBinding
import org.third.medicalapp.sign.model.UserModel
import org.third.medicalapp.sign.model.UserModelList
import org.third.medicalapp.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListActivity : AppCompatActivity() {
    lateinit var binding:ActivityUserListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkService =
            (applicationContext as MyApplication).netWorkService
        val check = networkService.getUserList()
        val itemList = mutableListOf<UserModel>()
        check.enqueue(object : Callback<UserModelList>{
            override fun onResponse(call: Call<UserModelList>, response: Response<UserModelList>) {
                for(data in response.body()?.users!!){
                    if(data.roles!="admin"){
                        itemList.add(data)
                    }
                }
                binding.recyclerUserListView.layoutManager = LinearLayoutManager(baseContext)
                binding.recyclerUserListView.adapter = UserAdapter(this@UserListActivity, itemList)
                binding.recyclerUserListView.addItemDecoration(DividerItemDecoration(baseContext,LinearLayoutManager.VERTICAL))

            }
            override fun onFailure(call: Call<UserModelList>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}