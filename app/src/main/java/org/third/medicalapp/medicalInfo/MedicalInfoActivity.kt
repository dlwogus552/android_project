package org.third.medicalapp.medicalInfo

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.medicalInfo.adapter.MediInfoAdapter
import org.third.medicalapp.databinding.ActivityMedicalInfoBinding
import org.third.medicalapp.medicalInfo.model.MediInfo

class MedicalInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityMedicalInfoBinding
    var datas : MutableList<MediInfo>? = mutableListOf()
    lateinit var db : SQLiteDatabase
    val tableName = "mediInfo_db"
    lateinit var adapter : MediInfoAdapter

    companion object {
        const val MODIFY_REQUEST_CODE = 1001
        const val DELETE_REQUEST_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        myCheckPermission(this) // 퍼미션 체크

        db = MedicalInfoDBHelper(this).readableDatabase
        binding.writeFab.setOnClickListener{
            val intent = Intent(this, MedicalInfoWriteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        loadDataFromDatabase()
    }

    private fun loadDataFromDatabase() {
        datas?.clear()
        val sql = "SELECT * FROM $tableName"
        val cursor = db.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(0)
            val siteName = cursor.getString(1)
            val siteUrl = cursor.getString(2)
            val siteIntro = cursor.getString(3)
            val mediInfo = MediInfo(id, siteName.toString(), siteUrl.toString(),
                siteIntro.toString())

            datas?.add(mediInfo)
        }

        // 데이터를 어댑터에 설정
        binding.recyclerViewCommunity.layoutManager = LinearLayoutManager(this)
        adapter = MediInfoAdapter(this, datas ?: mutableListOf())
        binding.recyclerViewCommunity.adapter = adapter
//        binding.recyclerViewCommunity.addItemDecoration(
//            DividerItemDecoration(
//                this,
//                LinearLayoutManager.VERTICAL
//            )
//        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MODIFY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newName = data?.getStringExtra("newName")
            val newUrl = data?.getStringExtra("newUrl")
            val newIntro = data?.getStringExtra("newIntro")

            // 수정된 값이 있으면 해당 값을 업데이트
            datas?.forEach { item ->
                if (item.siteName == newName) {
                    item.apply {
                        siteName = newName ?: siteName
                        siteUrl = newUrl ?: siteUrl
                        siteIntro = newIntro ?: siteIntro
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }  else if (requestCode == DELETE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val deleteName = data?.getStringExtra("deleteName")

            // 삭제된 값이 있으면 해당 값을 삭제
            val deleteItem = datas?.find { it.siteName == deleteName }
            deleteItem?.let {
                datas?.remove(it)
                adapter.notifyDataSetChanged()
            }
        }
    }
}