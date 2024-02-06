package org.third.medicalapp.community

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.third.medicalapp.databinding.ItemCommunityBinding
import org.third.medicalapp.util.MyApplication

class MyViewHolder(val binding : ItemCommunityBinding) : RecyclerView.ViewHolder(binding.root) {
    val imageLike = binding.imageLike
    val textViewLike = binding.textView
    val commentCount = binding.commentCount
//    var isLiked = false
//
//    init {
//        // 이미지를 클릭했을 때 이벤트 처리
//        imageLike.setOnClickListener {
//            // 좋아요 상태를 토글
//            isLiked = !isLiked
//            // 좋아요 상태에 따라 이미지 변경
//            if (isLiked) {
//                imageLike.setImageResource(R.drawable.like_full)
//            } else {
//                imageLike.setImageResource(R.drawable.like)
//            }
//            // 좋아요 상태에 따라 텍스트 변경
//            if (isLiked) {
//                // 좋아요 텍스트 증가 로직 추가
//            } else {
//                // 좋아요 텍스트 감소 로직 추가
//            }
//        }
//    }
}

class MyAdapter(val context : Context, val itemList : MutableList<CommunityData>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemCommunityBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run{
            tvWriter.text = "${data.email}"
            tvDate.text = data.date
            tvTitle.text = data.title
            tvContent.text = data.content


            // 비동기적으로 댓글 수 가져오기
            getCommentCount(data.docId) { count ->
                count?.let {
                    holder.commentCount.text = it.toString()
                }
            }
        }
        val imgRef = MyApplication.storage.reference.child("images/${data.docId}.jpg")
        imgRef.downloadUrl.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                Glide.with(context)
                    .load(task.result)
                    .into(holder.binding.imageView)
            }
        }
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, CommunityDetailActivity::class.java)
            intent.putExtra("docId", data.docId)
            intent.putExtra("email", data.email)
            intent.putExtra("date", data.date)
            intent.putExtra("title", data.title)
            intent.putExtra("content", data.content)
            intent.putExtra("isLiked", data.isLiked)
            context.startActivity(intent)
        }
    }

    // 비동기 방식으로 댓글 수를 가져와서 commentCount에 설정
    private fun getCommentCount(docId: String?, callback: (Int?) -> Unit) {
        if (docId != null) {
            MyApplication.db.collection("comment").whereEqualTo("docId", docId)
                .get()
                .addOnSuccessListener { result ->
                    val commentCount = result.size() // 댓글 수 계산
                    callback(commentCount)
                }
                .addOnFailureListener { exception ->
                    Log.d("getCommentCount", "Failed to get comment count: $exception")
                    callback(null) // 실패 시 null을 전달
                }
        } else {
            callback(null) // docId가 null인 경우 null을 전달
        }
    }

}