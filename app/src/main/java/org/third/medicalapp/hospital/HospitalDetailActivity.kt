package org.third.medicalapp.hospital

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.common.collect.MapMaker
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityHospitalDetailBinding
import org.third.medicalapp.hospital.model.Hospital
import org.third.medicalapp.util.MyApplication
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HospitalDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityHospitalDetailBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MapView()

        val id = intent.getLongExtra("id", 0)
        val networkService = (application as MyApplication).hospitalServie
        val hospitalListCall = networkService.doGetHospitalId(id)
        hospitalListCall.enqueue(object : retrofit2.Callback<Hospital> {
            override fun onResponse(call: Call<Hospital>, response: Response<Hospital>) {
                val hospital = response.body()
                binding.tvHospitalName.text = "${hospital?.hname}"
                binding.tvHospitalCode.text = "${hospital?.hcode}"
                binding.tvHospitalAddress.text = "${hospital?.addr}"
                binding.tvHospitalDepart.text = "${hospital?.hcode}"
                binding.tvPhoneNumber.text = "${hospital?.tel}"
            }

            override fun onFailure(call: Call<Hospital>, t: Throwable) {
                call.cancel()
            }

        })

        val tel = intent.getStringExtra("tel")
        binding.btnCall.setOnClickListener {
            //버튼을 누르면 전화가 되게끔
        }

    }


    private fun MapView() {

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        val x = intent.getDoubleExtra("x", 0.0)
        val y = intent.getDoubleExtra("y", 0.0)
        val marker = Marker()

        marker.position = LatLng(y, x)
        marker.map = naverMap

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(y, x))
            .animate(CameraAnimation.None, 0)
        naverMap.moveCamera(cameraUpdate)
    }


}