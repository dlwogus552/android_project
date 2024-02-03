package org.third.medicalapp.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.third.medicalapp.databinding.ActivityLoginBinding
import org.third.medicalapp.util.MyApplication

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //구글 로그인 인텐트 결과 받아오기
        //인텐트 결과 받아오기
//        val requestLauncher=registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()){
//            val task = GoogleSignIn.getSignedInAccountFromIntent(it?.data)
//            try{
//                val account=task.getResult(ApiException::class.java)
//                val credential= GoogleAuthProvider.getCredential(account.idToken,null)
//                MyApplication.auth.signInWithCredential(credential)
//                    .addOnCompleteListener(this){task->
//                        if(task.isSuccessful){
//                            Log.d("aaa","구글 성공")
//                            MyApplication.email= account.email
//                            var user=Firebase.auth.currentUser
//                            var userModel=UserModel(account.email.toString(),"Google User-"+user?.uid.toString(),null,"user")
//                            val networkService =
//                                (applicationContext as MyApplication).netWorkService
//                            val result = networkService.insert(userModel)
//                            result.enqueue(object : Callback<String> {
//                                override fun onResponse(
//                                    call: Call<String>,
//                                    response: Response<String>
//                                ) {
//                                    response.body().toString()
//                                    Log.d("aaa","성공")
//                                }
//
//                                override fun onFailure(call: Call<String>, t: Throwable) {
//                                    Log.d("aaa","실패")
//                                    call.cancel()
//                                }
//                            })
//                        }else{
//                            Log.d("aaa","구글 실패")
//                        }
//                    }
//            }catch (e:Exception){e.printStackTrace()}
//        }
        //로그인 처리
        binding.loginBtn.setOnClickListener{
            val email = binding.loginUsernameEditTextView.text.toString()
            val password=binding.loginPassEditTextView.text.toString()

            MyApplication.auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        if(MyApplication.checkAuth()){
                            MyApplication.email=email
                            finish()
                        }else{
                            Log.d("aaa","로그인 실패 / 인증처리 안됨")
                        }
                    }else{
                        Log.d("aaa","로그인 실패")
                    }
                }
        }
        binding.goRegisterTextView.setOnClickListener{
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.forgotPasswordTextView.setOnClickListener{

        }
    }
}