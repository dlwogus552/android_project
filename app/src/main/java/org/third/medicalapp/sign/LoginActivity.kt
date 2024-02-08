package org.third.medicalapp.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityLoginBinding
import org.third.medicalapp.sign.model.UserModel
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.auth
import org.third.medicalapp.util.MyApplication.Companion.email
import org.third.medicalapp.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //구글 로그인 인텐트 결과 받아오기
        //인텐트 결과 받아오기
        val requestLauncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it?.data)
            try{
                val account=task.getResult(ApiException::class.java)
                val credential= GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this){task->
                        if(task.isSuccessful){
                            Log.d("aaa","구글 성공")
                            email= account.email
                            var user= Firebase.auth.currentUser
                            val networkService =
                                (applicationContext as MyApplication).netWorkService
                            val check = networkService.checkUser(email.toString())
                            var userModel: UserModel? =null
                            check.enqueue(object : Callback<UserModel>{
                                override fun onResponse(
                                    call: Call<UserModel>,
                                    response: Response<UserModel>
                                ) {
                                    //같은아이디가 없으면
                                    if(response.body()==null) {
                                        userModel = UserModel(
                                            account.email.toString(),
                                            "Google User-" + user?.uid.toString(),
                                            null,
                                            null,
                                            "user"
                                        )
                                        val result = networkService.insert(userModel!!)
                                        result.enqueue(object : Callback<Result> {
                                            override fun onResponse(
                                                call: Call<Result>,
                                                response: Response<Result>
                                            ) {


                                                Log.d("aaa","성공")
//                                                onStart()
                                                finish()
                                            }

                                            override fun onFailure(call: Call<Result>, t: Throwable) {
                                                Log.d("aaa","서버 연동 실패")
                                                call.cancel()
                                            }
                                        })
                                    }else{
//                                        onStart()
                                        finish()
                                    }
                                }
                                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                                    Log.d("aaa","서버 연동 실패")
                                    call.cancel()
                                }
                            })
                        }else{
                            Log.d("aaa","구글 실패")
                        }
                    }
            }catch (e:Exception){e.printStackTrace()}
        }
        //구글 로그인
        binding.googleLoginBtn.setOnClickListener{
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent=GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)
        }

        //로그인 처리
        binding.loginBtn.setOnClickListener{
            val email = binding.loginUsernameEditTextView.text.toString()
            val password=binding.loginPassEditTextView.text.toString()

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        MyApplication.email=email
                        if(MyApplication.checkAuth()){
                            val networkService =
                                (applicationContext as MyApplication).netWorkService
                            val check = networkService.checkUser(email)
                            var userModel: UserModel?
                            val sharedPref = getSharedPreferences("User", MODE_PRIVATE)
                            val editor=sharedPref.edit()
                            check.enqueue(object : Callback<UserModel>{
                                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                                    Log.d("aaa","${response.body()?.userName}")
                                    if(response.body()!=null){
                                        userModel=response.body()
                                        editor.putString("nickName",userModel?.nickName)
                                        editor.putString("phoneNumber",userModel?.phoneNumber)
                                        editor.putString("regiDate",userModel?.regiDate?.substring(0,10))
                                        editor.apply()
                                    }else{
                                        Log.d("aaa","error")
                                    }
                                }
                                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                                    Log.d("aaaa","info 서버연결 실패")
                                    call.cancel()
                                }
                            })
                            finish()
//                            onStart()
                        }else{
                            MyApplication.email=null
                            Toast.makeText(this,"메일인증을 진행해주세요",Toast.LENGTH_SHORT).show()
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