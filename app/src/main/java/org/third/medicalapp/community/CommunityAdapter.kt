package org.third.medicalapp.community

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ItemCommunityBinding
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.email
import org.third.medicalapp.util.deleteLike
import org.third.medicalapp.util.isSavedLike
import org.third.medicalapp.util.saveLikeStore
import org.third.medicalapp.util.updateLikeCount

class MyViewHolder(val binding: ItemCommunityBinding) : RecyclerView.ViewHolder(binding.root) {
    val imageLike = binding.imageLike
    val textViewLike = binding.likeCount
    val commentCount = binding.commentCount

}

class MyAdapter(val context: Context, val itemList: MutableList<CommunityData>) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemCommunityBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
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
        imgRef.downloadUrl.addOnCompleteListener { task ->
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
            intent.putExtra("likeCount", data.likeCount)
            context.startActivity(intent)
        }

        // 좋아요 상태에 따라 이미지 설정
        CoroutineScope(Dispatchers.Main).launch {
            val isLiked = isSavedLike(data.docId, email)
            if (isLiked == false) {
                holder.imageLike.setImageResource(R.drawable.like)
            } else {
                holder.imageLike.setImageResource(R.drawable.like_full)
            }
        }

        // 좋아요 텍스트 설정
        holder.textViewLike.text = data.likeCount.toString()

        // 좋아요 이미지를 클릭했을 때 이벤트 처리
        holder.imageLike.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val isLiked = isSavedLike(data.docId, email)
                // 여기에서 isLiked를 사용하여 다음 작업 수행
                if (isLiked == false) {
                    // 좋아요 버튼을 누르면
                    saveLikeStore(data.docId, MyApplication.email)
                    updateLikeCount(data.docId, 1)
                    holder.imageLike.setImageResource(R.drawable.like_full)
                } else {
                    // 좋아요 취소 버튼을 누르면
                    deleteLike(data.docId, MyApplication.email)
                    updateLikeCount(data.docId, -1)
                    holder.imageLike.setImageResource(R.drawable.like)
                }
            }
        }

        // RecyclerView 갱신
//        notifyItemChanged(position)
    }

    // 비동기 방식으로 댓글 수를 가져와서 commentCount에 설정
    fun getCommentCount(docId: String?, callback: (Int?) -> Unit) {
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
            callback(null)
            // docId가 null인 경우 null을 전달
        }
    }

}