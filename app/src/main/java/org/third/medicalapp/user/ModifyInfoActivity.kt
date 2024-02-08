package org.third.medicalapp.user

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityModifyInfoBinding
import org.third.medicalapp.sign.model.UserModel
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.email
import org.third.medicalapp.util.MyApplication.Companion.storage
import org.third.medicalapp.util.Result
import org.third.medicalapp.util.myCheckPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ModifyInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityModifyInfoBinding
    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        checkProfile()
        binding.modiProfilePicture.setOnClickListener {
            Log.d("aaaa", "click")
            showPopUpMenu(it)
        }


    }

    //사진값 가져오기
    val requestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode === android.app.Activity.RESULT_OK) {
            Glide
                .with(getApplicationContext())
                .load(it.data?.data)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.modiProfilePicture)

            val cursor = contentResolver.query(
                it.data?.data as Uri,
                arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null
            )
            cursor?.moveToFirst().let {
                filePath = cursor?.getString(0) as String
                Log.d("aaaa", "$filePath")
            }
        }
    }

    //팝업 메뉴 띄우기
    private fun showPopUpMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.menuInflater.inflate(R.menu.profile_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.choose_picture -> {
                    var intent = Intent(Intent.ACTION_PICK)
                    intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*"
                    )
                    requestLauncher.launch(intent)
                    true
                }

                R.id.base_image -> {
                    binding.modiProfilePicture.setImageResource(R.drawable.basic_profile)
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_my_modify, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                // db 수정
                val networkService =
                    (applicationContext as MyApplication).netWorkService
                val nickname = binding.modiEditNickName.text.toString()
                val phoneNumber = binding.modiPhoneNumberText.text.toString()
                var userModel = UserModel(email.toString(), nickname, phoneNumber, null, null)
                if (nickname != "") {
                    val result = networkService.modify(userModel)
                    result.enqueue(object : Callback<Boolean> {
                        override fun onResponse(
                            call: Call<Boolean>,
                            response: Response<Boolean>
                        ) {
                            if (response.body() == true) {
                                // 이미지 업로드
                                val profile = binding.modiProfilePicture.drawable
                                val baseProfile = ContextCompat.getDrawable(
                                    this@ModifyInfoActivity,
                                    R.drawable.basic_profile
                                )
                                Log.d(
                                    "aaa",
                                    "${profile?.constantState != baseProfile?.constantState}"
                                )
                                deleteImage()

                                if (profile?.constantState != baseProfile?.constantState) {
                                    uploadImage()
                                }
                                Log.d("aaa", "수정성공")
                            } else {
                                Toast.makeText(baseContext, "수정실패", Toast.LENGTH_SHORT).show()
                                Log.d("aaa", "수정 실패")
                            }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            Log.d("aaa", "modi 서버 연결 실패")
                            call.cancel()
                        }
                    })

                    finish()
                    return true
                } else {
                    Log.d("aaaa", "닉네임 입력")
                    return false
                }

            }

            R.id.cancel -> {
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun deleteImage() {
        val storage = MyApplication.storage
        val storageRef = storage.reference.child("images/profile/${email}.jpg")
        // 파일 존재 여부 확인
        storageRef.metadata.addOnSuccessListener { metadata ->
            val deleteTask: Task<Void> = storageRef.delete()
            deleteTask.addOnSuccessListener {
                Log.d("aaaa", "기존 profile 삭제")
            }.addOnFailureListener {
                Log.d("aaaa", "기존 profile 삭제 실패")
            }
        }.addOnFailureListener { exception ->
        }
    }

    fun uploadImage() {
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imgRef = storageRef.child("images/profile/${email}.jpg")
        val file = Uri.fromFile(File(filePath))
        imgRef.putFile(file)
            .addOnSuccessListener {
                Toast.makeText(this, "save ok...", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Log.d("kkang", "file save error", it)
            }
    }

    fun checkProfile(){
        val storageRef = MyApplication.storage.reference.child("images/profile/${email}.jpg")
        storageRef.metadata.addOnSuccessListener { metadata ->
            storageRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(this)
                        .load(task.result)
                        .into(binding.modiProfilePicture)
                }
            }
        }.addOnFailureListener {
            binding.modiProfilePicture.setImageResource(R.drawable.basic_profile)
        }
    }
}