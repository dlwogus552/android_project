package org.third.medicalapp.pharmacy

import android.content.Intent
import android.net.Uri
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import org.third.medicalapp.R
import org.third.medicalapp.MainActivity
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityPharmacyDetailBinding
import org.third.medicalapp.pharmacy.apater.PharmacyReviewAdapter
import org.third.medicalapp.pharmacy.model.PharmacyReview
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.dateToString
import java.util.Date

class PharmacyDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityPharmacyDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MapView()

        val id = intent.getLongExtra("id", 0)

        val networkService = (application as MyApplication).pharmacyService
        val pharmacyListCall = networkService.doGetPharmacyId(id)
        pharmacyListCall.enqueue(object : retrofit2.Callback<Pharmacy> {
            override fun onResponse(call: Call<Pharmacy>, response: Response<Pharmacy>) {

                binding.tvPharmacyName.text = intent.getStringExtra("pharmacy")
                binding.tvPharmacyAddress.text = intent.getStringExtra("addr")
                binding.tvPhoneNumber.text = intent.getStringExtra("tel")
            }

            override fun onFailure(call: Call<Pharmacy>, t: Throwable) {
                call.cancel()
            }

        })
        val tel = intent.getStringExtra("tel")
        binding.btnCall.setOnClickListener { //버튼을 누르면 전화가 되게끔
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$tel")
            startActivity(intent)
        }
    }

    private fun MapView() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment_PH) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment_PH, it).commit()
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

        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.title = "약국 정보"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 저장 메뉴 아이템을 선택한 경우
        if (item.itemId == R.id.menu_main) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    // 리뷰 데이터를 Firestore에 저장하는 함수
    fun saveStore() {
        val data = mapOf(
            "pharmacyName" to binding.tvPharmacyName.text.toString(),
            "email" to MyApplication.email,
            "review" to binding.edReview.text.toString(),
            "date" to dateToString(Date()),
            "isLiked" to false
        )

        MyApplication.db.collection("PharmacyReview")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "약국 후기가 추가되었습니다.", Toast.LENGTH_SHORT).show()

                // 댓글이 성공적으로 추가되면 RecyclerView를 새로 고침
                makePharmacyReviewRecyclerView(intent.getStringExtra("pharmacyName"))

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
    fun makePharmacyReviewRecyclerView(docId: String?) {
        // Firestore에서 커뮤니티 게시물 데이터 가져오기
        MyApplication.db.collection("PharmacyReview")
            .whereEqualTo("pharmacyName", binding.tvPharmacyName.text.toString())
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<PharmacyReview>()
                // 가져온 데이터를 CommentData 객체로 변환하여 itemList에 추가
                for (document in result) {
                    val item = document.toObject(PharmacyReview::class.java)
                    item.pharmacyReviewId = document.id
                    itemList.add(item)
                }

                // RecyclerView 설정
                binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.reviewRecyclerView.adapter = PharmacyReviewAdapter(this, itemList)

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