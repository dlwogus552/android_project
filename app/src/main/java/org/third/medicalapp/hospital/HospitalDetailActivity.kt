package org.third.medicalapp.hospital

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
import org.third.medicalapp.hospital.model.HospitalReview
import org.third.medicalapp.pharmacy.apater.HospitalReviewAdapter
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.dateToString
import retrofit2.Call
import retrofit2.Response
import java.util.Date
import javax.security.auth.callback.Callback

class HospitalDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityHospitalDetailBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private var hospitalName: String? = null

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
                hospitalName = hospital?.hname
                binding.tvHospitalName.text = "${hospital?.hname}"
                binding.tvHospitalCode.text = "${hospital?.hcode}"
                binding.tvHospitalAddress.text = "${hospital?.addr}"
                binding.tvHospitalDepart.text = "${hospital?.hcode}"
                binding.tvPhoneNumber.text = "${hospital?.tel}"

                hospitalName?.let { name ->
                    makeHospitalReviewRecyclerView(name)
                }
            }

            override fun onFailure(call: Call<Hospital>, t: Throwable) {
                call.cancel()
            }

        })

        val tel = intent.getStringExtra("tel")
        binding.btnCall.setOnClickListener {
            //버튼을 누르면 전화가 되게끔
        }

        binding.icoSend.setOnClickListener {
            // 이미지가 선택되었고 제목이 입력되었는지 확인
            if (binding.edReview.text.isNotEmpty()) {
                // stroe에 데이터 저장
                saveStore()
                binding.edReview.setText("")
                binding.edReview.clearFocus()
                // 키보드 숨기기
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.icoSend.windowToken, 0)
            } else {
                Toast.makeText(
                    baseContext,
                    "데이터 업로드에 실패하였습니다. 필수 항목을 모두 작성해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            hospitalName?.let { name ->
                makeHospitalReviewRecyclerView(name)
            }
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

    // 리뷰 데이터를 Firestore에 저장하는 함수
    fun saveStore() {
        val data = mapOf(
            "hospitalName" to binding.tvHospitalName.text.toString(),
            "email" to MyApplication.email,
            "review" to binding.edReview.text.toString(),
            "date" to dateToString(Date()),
            "isLiked" to false
        )

        MyApplication.db.collection("HospitalReview")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "병원 후기가 추가되었습니다.", Toast.LENGTH_SHORT).show()

                // 댓글이 성공적으로 추가되면 RecyclerView를 새로 고침
                hospitalName?.let { name ->
                    makeHospitalReviewRecyclerView(name)
                }

                binding.edReview.setText("")
                binding.edReview.clearFocus()
            }
            .addOnFailureListener { exception ->
                // 데이터 저장 실패 시 메시지 표시
                Log.d("saveStore", "Error adding document", exception)
                Toast.makeText(this, "데이터 저장 실패", Toast.LENGTH_SHORT).show()
            }
    }

    // ReviewRecyclerView를 생성하고 데이터를 로드하는 함수
    fun makeHospitalReviewRecyclerView(name: String?) {

        Log.d("Hospital Review", "Hospital Review")

        // Firestore에서 커뮤니티 게시물 데이터 가져오기
        MyApplication.db.collection("HospitalReview").whereEqualTo("hospitalName", name)
            .get()
            .addOnSuccessListener { result ->
                Log.d("-------Hospital Review", "$name")
                val itemList = mutableListOf<HospitalReview>()
                // 가져온 데이터를 CommentData 객체로 변환하여 itemList에 추가
                for (document in result) {
                    val item = document.toObject(HospitalReview::class.java)
                    item.hospitalReviewId = document.id
                    itemList.add(item)
                }

                // RecyclerView 설정
                binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.reviewRecyclerView.adapter = HospitalReviewAdapter(this, itemList)

                // 댓글 목록이 변경될 때마다 RecyclerView의 높이를 다시 계산하여 설정
                binding.reviewRecyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                    val height = binding.reviewRecyclerView.computeVerticalScrollRange()
                    binding.reviewRecyclerView.layoutParams.height = height
                    binding.reviewRecyclerView.requestLayout()
                }
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 로그와 메시지 출력
                Toast.makeText(this, "데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }
}