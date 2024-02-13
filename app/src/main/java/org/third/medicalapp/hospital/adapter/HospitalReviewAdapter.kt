package org.third.medicalapp.pharmacy.apater

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.third.medicalapp.databinding.ItemReviewBinding
import org.third.medicalapp.hospital.model.HospitalReview


class HospitalReviewViewHolder(val binding: ItemReviewBinding) :
    RecyclerView.ViewHolder(binding.root)
class HospitalReviewAdapter(val context: Context, val itemList: MutableList<HospitalReview>) :
    RecyclerView.Adapter<HospitalReviewViewHolder>() {

    init {
        Log.d("HospitalReviewAdapter", "Item list size: ${itemList.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalReviewViewHolder {
        return HospitalReviewViewHolder(ItemReviewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HospitalReviewViewHolder, position: Int) {
        val data = itemList.get(position)
        holder.binding.run{
            tvWriter.text = "${data.email}"
            tvDate.text = data.date
            tvReview.text = data.review
        }
    }


}