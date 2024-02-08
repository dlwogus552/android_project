package org.third.medicalapp.community

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityCommunityDetailBinding
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.dateToString
import org.third.medicalapp.util.updateLikeCount
import java.util.Date

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val imageLike = findViewById<ImageView>(R.id.imageLike)
//        var isLiked = false
//
//        imageLike.setOnClickListener {
//            isLiked = !isLiked
//            if (isLiked) {
//                imageLike.setImageResource(R.drawable.like_full)
//            } else {
//                imageLike.setImageResource(R.drawable.like)
//            }
//        }

        // Intent로부터 전달된 데이터 받기
        val docId = intent.getStringExtra("docId")
        val email = intent.getStringExtra("email")
        val date = intent.getStringExtra("date")
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        var likeCount = intent.getIntExtra("likeCount",0)

        // 데이터를 화면에 바인딩
        binding.apply {
            tvTitle.text = title
            tvContent.text = content
            tvWriter.text = email
            tvDate.text = date

            // 좋아요 상태에 따라 이미지 설정
            CoroutineScope(Dispatchers.Main).launch {
                val isLiked = org.third.medicalapp.util.isSavedLike(docId, MyApplication.email)
                if (isLiked == false) {
                    imageLike.setImageResource(R.drawable.like)
                } else {
                    imageLike.setImageResource(R.drawable.like_full)
                }
            }


            // 좋아요 텍스트 설정
            likeCountView.text = likeCount.toString()
        }

        val imgRef = MyApplication.storage.reference.child("images/$docId.jpg")
        imgRef.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this@CommunityDetailActivity)
                    .load(task.result)
                    .into(binding.imageView)
            }
        }

        // 좋아요 이미지를 클릭했을 때 이벤트 처리
        binding.imageLike.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val isLiked = org.third.medicalapp.util.isSavedLike(docId, MyApplication.email)
                // 여기에서 isLiked를 사용하여 다음 작업 수행
                if (isLiked == false) {
                    // 좋아요 버튼을 누르면
                    org.third.medicalapp.util.saveLikeStore(docId, MyApplication.email)
                    updateLikeCount(docId, 1)
                    binding.imageLike.setImageResource(R.drawable.like_full)
                } else {
                    // 좋아요 취소 버튼을 누르면
                    org.third.medicalapp.util.deleteLike(docId, MyApplication.email)
                    updateLikeCount(docId, -1)
                    binding.imageLike.setImageResource(R.drawable.like)
                }
            }
        }


        binding.icoSend.setOnClickListener {
            // 이미지가 선택되었고 제목이 입력되었는지 확인
            if (binding.edComment.text.isNotEmpty()) {
                // stroe에 데이터 저장
                saveStore()
                binding.edComment.setText("")
                binding.edComment.clearFocus()
                // 키보드 숨기기
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.icoSend.windowToken, 0)
            } else {
                Toast.makeText(
                    baseContext,
                    "댓글 업로드에 실패하였습니다. 필수 항목을 모두 작성해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        makeCommentRecyclerVIew(intent.getStringExtra("docId"))
        getCommentCount(intent.getStringExtra("docId"))
    }

    // 댓글 데이터를 Firestore에 저장하는 함수
    fun saveStore() {
        val data = mapOf(
            "docId" to intent.getStringExtra("docId"),
            "email" to MyApplication.email,
            "comment" to binding.edComment.text.toString(),
            "date" to dateToString(Date()),
            "isLiked" to false
        )

        MyApplication.db.collection("comment")
            .add(data)
            .addOnSuccessListener { documentReference ->
                // 댓글이 성공적으로 추가되면 RecyclerView를 새로 고침
                makeCommentRecyclerVIew(intent.getStringExtra("docId"))
                getCommentCount(intent.getStringExtra("docId"))
            }
            .addOnFailureListener { exception ->
                // 데이터 저장 실패 시 메시지 표시
                Log.d("saveStore", "Error adding document", exception)
                Toast.makeText(this, "댓글 데이터 저장 실패", Toast.LENGTH_SHORT).show()
            }
    }

    // CommentRecyclerView를 생성하고 데이터를 로드하는 함수
    fun makeCommentRecyclerVIew(docId: String?) {
        // Firestore에서 커뮤니티 게시물 데이터 가져오기
        if (docId != null) {
            MyApplication.db.collection("comment").whereEqualTo("docId", docId)
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<CommentData>()
                    // 가져온 데이터를 CommentData 객체로 변환하여 itemList에 추가
                    for (document in result) {
                        val item = document.toObject(CommentData::class.java)
                        item.commentId = document.id
                        itemList.add(item)
                    }

                    // RecyclerView 설정
                    binding.commentRecyclerView.layoutManager = LinearLayoutManager(this)
                    binding.commentRecyclerView.adapter = CommentAdapter(this, itemList)

                    // 댓글 목록이 변경될 때마다 RecyclerView의 높이를 다시 계산하여 설정
                    binding.commentRecyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                        val height = binding.commentRecyclerView.computeVerticalScrollRange()
                        binding.commentRecyclerView.layoutParams.height = height
                        binding.commentRecyclerView.requestLayout()
                    }
                }
                .addOnFailureListener { exception ->
                    // 데이터 가져오기 실패 시 로그와 메시지 출력
                    Log.d("aaaa", "error... getting document..", exception)
                    Toast.makeText(this, "댓글 데이터 획득 실패", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.e("makeCommentRecyclerVIew", "docId is null")
            Toast.makeText(this, "게시물 ID를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCommentCount(docId: String?) {
        if (docId != null) {
            MyApplication.db.collection("comment").whereEqualTo("docId", docId)
                .get()
                .addOnSuccessListener { result ->
                    val commentCount = result.size() // 댓글 수 계산
                    updateCommentCountUI(commentCount) // UI에 댓글 수 업데이트
                }
                .addOnFailureListener { exception ->
                    Log.d("getCommentCount", "Failed to get comment count: $exception")
                }
        }
    }

    fun updateCommentCountUI(commentCount: Int) {
        binding.commentCount.text = commentCount.toString() // commentCount 텍스트뷰에 값을 설정
    }


}