package org.third.medicalapp.community

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.third.medicalapp.databinding.ItemCommunityCommentBinding

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
        }
    }
}