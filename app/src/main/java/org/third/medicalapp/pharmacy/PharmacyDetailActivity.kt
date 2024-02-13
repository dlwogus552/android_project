package org.third.medicalapp.pharmacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import org.third.medicalapp.databinding.ActivityPharmacyDetailBinding
import org.third.medicalapp.pharmacy.apater.PharmacyReviewAdapter
import org.third.medicalapp.pharmacy.model.PharmacyReview
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.dateToString
import java.util.Date

class PharmacyDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityPharmacyDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        MyApplication.db.collection("PharmacyReview").whereEqualTo("pharmacyName", binding.tvPharmacyName.text.toString())
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