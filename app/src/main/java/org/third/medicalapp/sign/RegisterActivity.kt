package org.third.medicalapp.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import org.third.medicalapp.databinding.ActivityRegisterBinding
import org.third.medicalapp.sign.model.UserModel
import org.third.medicalapp.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cancelBtn.setOnClickListener{
            finish()
        }

        // 가입
        val networkService = (applicationContext as MyApplication).netWorkService
        binding.registerBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val nickName = binding.nickNameEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val userModel = UserModel(email, nickName, phone, "user")
            var check: Boolean = false
            //닉네임 중복확인
            networkService.checkNick(nickName).enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.d("aaa", "Response : ${response.body()}")
                    check = true
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    check = false
                    call.cancel()
                }
            })
//            if(check){
            //회원가입
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.emailEditText.text.clear()
                    binding.passwordEditText.text.clear()
                    binding.nickNameEditText.text.clear()
                    binding.phoneEditText.text.clear()
                    if (task.isSuccessful) {
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask ->
                                if (sendTask.isSuccessful) {
                                    Toast.makeText(baseContext, "인증성공", Toast.LENGTH_SHORT).show()
                                    Log.d("aaaa", "인증성공")
                                    val result = networkService.insert(userModel)
                                    result.enqueue(object : Callback<String> {
                                        override fun onResponse(
                                            call: Call<String>,
                                            response: Response<String>
                                        ) {
                                            response.body().toString()
                                            Log.d("aaa", "성공")
                                        }

                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Log.d("aaa", "실패")
                                            call.cancel()
                                        }
                                    })
                                } else {
                                    Toast.makeText(baseContext, "인증실패", Toast.LENGTH_SHORT).show()
                                    Log.d("aaaa", "인증실패")
                                }
                            }
                    } else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        binding.userNameError.visibility= View.VISIBLE
                        Log.d("aaaa", "회원가입 실패")
                    }
                }
//            }else{
//                Toast.makeText(this,"중복된 닉네임",Toast.LENGTH_SHORT).show()
//                Log.d("aaa","nickName 중복")
//            }
        }
    }
}
