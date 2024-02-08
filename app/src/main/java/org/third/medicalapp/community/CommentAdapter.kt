package org.third.medicalapp.community

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.third.medicalapp.databinding.ItemCommunityCommentBinding
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.email

class CommentViewHolder(val binding: ItemCommunityCommentBinding) :
    RecyclerView.ViewHolder(binding.root)

class CommentAdapter(val context: Context, val itemList: MutableList<CommentData>) :
    RecyclerView.Adapter<CommentViewHolder>() {

    init {
        Log.d("CommentAdapter", "Item list size: ${itemList.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(ItemCommunityCommentBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val data = itemList.get(position)
        Log.d("Comment","${data.comment}")
        Log.d("date","${data.date}")
        holder.binding.run{
            tvCommentWriter.text = "${data.email}"
            tvCommentDate.text = data.date
            tvComment.text = data.comment

            commentDelete.setOnClickListener {
                if (MyApplication.email == data.email) {
                    if (data.commentId != null) {
                        MyApplication.db.collection("comment").document(data.commentId!!).delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                Log.e("deleteComment", "Error deleting document", exception)
                                Toast.makeText(context, "게시글 삭제 실패", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(context, "작성자만 삭제 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}