//package org.lmh.firebaseapp
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.GoogleAuthProvider
//import org.third.medicalapp.R
//import org.third.medicalapp.databinding.ActivityAuthBinding
//
//class AuthActivity : AppCompatActivity() {
//    lateinit var binding: ActivityAuthBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAuthBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        if (MyApplication.checkAuth()) {
//            changeVisibility("login")
//        } else {
//            changeVisibility("logout")
//        }
//
//        val requestLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//                MyApplication.auth.signInWithCredential(credential)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            // 구글 로그인 성공 시 구글 이메일 인증서 저장
//                            MyApplication.email = account.email
//                            changeVisibility("login")
//                        } else {
//                            // 구글 로그인 실패
//                            changeVisibility("logout")
//                        }
//                    }
//            } catch (e: ApiException) {
//                changeVisibility("logout")
//            }
//        }
//
//        // 구글 인증 처리
//        binding.googleLoginBtn.setOnClickListener {
//            // 구글 로그인을 위한 옵션 생성
//            val gso = GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()
//            // 인텐트 객체 생성
//            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
//            // 인텐트 시작
//            requestLauncher.launch(signInIntent)
//        }
//
//        binding.goSignInBtn.setOnClickListener {
//            changeVisibility("signin")
//        }
//
//        // 회원가입 처리
//        binding.signBtn.setOnClickListener {
//            val email = binding.authEmailEditView.text.toString()
//            val password = binding.authPasswordEditView.text.toString()
//
//            // 파이어베이스에 이메일/비밀번호를 등록
//            MyApplication.auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    binding.authEmailEditView.text.clear()
//                    binding.authPasswordEditView.text.clear()
//
//                    // 인증 메일 발송하기(등록 성공했을 때)
//                    if (task.isSuccessful) {
//                        MyApplication.auth.currentUser?.sendEmailVerification()
//                            ?.addOnCompleteListener { sendTask ->
//                                if (sendTask.isSuccessful) {
//                                    Toast.makeText(
//                                        baseContext,
//                                        "회원가입 메일 유효성 검사 성공",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                    changeVisibility("logout")
//                                } else {
//                                    Toast.makeText(
//                                        baseContext,
//                                        "회원가입 메일 유효성 검사 실패",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                    changeVisibility("logout")
//                                }
//                            }
//                    } else {
//                        Toast.makeText(baseContext, "회원가입 메일 유효성 검사 실패", Toast.LENGTH_SHORT).show()
//                        changeVisibility("logout")
//                    }
//                }
//        }
//
//        // 로그인 처리
//        binding.loginBtn.setOnClickListener {
//            val email = binding.authEmailEditView.text.toString()
//            val password = binding.authPasswordEditView.text.toString()
//
//            MyApplication.auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    binding.authEmailEditView.text.clear()
//                    binding.authPasswordEditView.text.clear()
//
//                    // 로그인 성공했을 때 처리
//                    if (task.isSuccessful) {
//                        if (MyApplication.checkAuth()) {
//                            MyApplication.email = email
//                            changeVisibility("login")
//                        } else {
//                            Toast.makeText(
//                                baseContext,
//                                "전송된 이메일로 인증처리가 되지 않았습니다.",
//                                Toast.LENGTH_SHORT
//                            )
//                        }
//                    } else {
//                        Toast.makeText(baseContext, "전송된 이메일로 인증처리가 되지 않았습니다.", Toast.LENGTH_SHORT)
//                    }
//                }
//        }
//
//
//        // 로그아웃 처리
//        binding.logoutBtn.setOnClickListener {
//            MyApplication.auth.signOut()
//            MyApplication.email = null
//            changeVisibility("logout")
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_auth, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        finish()
//        return super.onOptionsItemSelected(item)
//    }
//
//    fun changeVisibility(mode: String) {
//        if (mode == "login") {
//            binding.run {
//                authMainTextView.text = "${MyApplication.email} 님 반갑습니다."
//                logoutBtn.visibility = View.VISIBLE
//                goSignInBtn.visibility = View.GONE
//                googleLoginBtn.visibility = View.GONE
//                authEmailEditView.visibility = View.GONE
//                authPasswordEditView.visibility = View.GONE
//                signBtn.visibility = View.GONE
//                loginBtn.visibility = View.GONE
//            }
//        } else if (mode == "logout") {
//            binding.run {
//                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
//                logoutBtn.visibility = View.GONE
//                goSignInBtn.visibility = View.VISIBLE
//                googleLoginBtn.visibility = View.VISIBLE
//                authEmailEditView.visibility = View.VISIBLE
//                authPasswordEditView.visibility = View.VISIBLE
//                signBtn.visibility = View.GONE
//                loginBtn.visibility = View.VISIBLE
//            }
//        } else if (mode == "signin") {
//            binding.run {
//                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
//                logoutBtn.visibility = View.GONE
//                goSignInBtn.visibility = View.GONE
//                googleLoginBtn.visibility = View.GONE
//                authEmailEditView.visibility = View.VISIBLE
//                authPasswordEditView.visibility = View.VISIBLE
//                signBtn.visibility = View.VISIBLE
//                loginBtn.visibility = View.GONE
//            }
//        }
//    }
//}