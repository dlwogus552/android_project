package org.third.medicalapp.util

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.third.medicalapp.hospital.util.HospitalNetworkService
import org.third.medicalapp.pharmacy.util.PharmacyNetworkService
import org.third.medicalapp.sign.util.INetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MyApplication : MultiDexApplication(){
    companion object {
        lateinit var auth : FirebaseAuth
        var email : String? = null
        lateinit var db : FirebaseFirestore
        lateinit var storage : FirebaseStorage

        fun checkAuth() : Boolean {
            var currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            }?: let {
                false
            }
        }
        fun checkAdmin():Boolean {
            if(email.toString().equals("admin@example.com")){
                return true
            }
            return false
        }
    }

    var netWorkService: INetworkService
    var hospitalServie: HospitalNetworkService
    var pharmacyService: PharmacyNetworkService


    val retrofit: Retrofit
        get()= Retrofit.Builder()
//            .baseUrl("http://10.100.105.168:8082/")
            .baseUrl("http://10.100.105.216:8082/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    init {
        netWorkService=retrofit.create(INetworkService::class.java)
    }

    init {
        hospitalServie = retrofit.create(HospitalNetworkService::class.java)
    }
    init {
        pharmacyService = retrofit.create(PharmacyNetworkService::class.java)
    }

    override fun onCreate() {
        super.onCreate()

//        // Firebase 초기화
        FirebaseApp.initializeApp(this);



        // 파이어베이스 인증 객체 얻기
        auth = Firebase.auth

        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage
    }
}