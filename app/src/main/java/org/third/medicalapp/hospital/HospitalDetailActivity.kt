package org.third.medicalapp.hospital

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.common.collect.MapMaker
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityHospitalDetailBinding

class HospitalDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityHospitalDetailBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val h_name =intent.getStringExtra("h_name")
        val h_code = intent.getStringExtra("h_code")
        val addr = intent.getStringExtra("addr")
        val city = intent.getStringExtra("city")
        val si_gun = intent.getStringExtra("si_gun")
        val dong = intent.getStringExtra("dong")
        val x = intent.getDoubleExtra("x", 0.0)
        val y = intent.getDoubleExtra("y", 0.0)
        val tel = intent.getStringExtra("tel")




        initMapView()

    }


    private fun initMapView() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }
        override fun onMapReady(naverMap: NaverMap) {
            val marker = Marker()
            marker.position = LatLng(37.5670135, 126.9783740)
            marker.map =  naverMap
        }


    }