package org.third.medicalapp.community

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.third.medicalapp.databinding.ItemCommunityBinding
import org.third.medicalapp.util.MyApplication

class MyViewHolder(val binding : ItemCommunityBinding) : RecyclerView.ViewHolder(binding.root)

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
            itemEmailView.text = "${data.docId}, ${data.email}"
            itemDateView.text = data.date
            itemContentView.text = data.content
        }
        val imgRef = MyApplication.storage.reference.child("images/${data.docId}.jpg")
        imgRef.downloadUrl.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                Glide.with(context)
                    .load(task.result)
                    .into(holder.binding.itemImageView)
            }
        }
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, CommunityDetailActivity::class.java)
            intent.putExtra("docId", data.docId)
            context.startActivity(intent)
        }
    }
}