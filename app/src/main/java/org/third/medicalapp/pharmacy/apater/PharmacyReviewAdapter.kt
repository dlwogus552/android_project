package org.third.medicalapp.pharmacy.apater

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.third.medicalapp.databinding.ItemReviewBinding
import org.third.medicalapp.pharmacy.model.PharmacyReview


class PharmacyReviewViewHolder(val binding: ItemReviewBinding) :
    RecyclerView.ViewHolder(binding.root)
class PharmacyReviewAdapter(val context: Context, val itemList: MutableList<PharmacyReview>) :
    RecyclerView.Adapter<PharmacyReviewViewHolder>() {

    init {
        Log.d("PharmacyReviewAdapter", "Item list size: ${itemList.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyReviewViewHolder {
        return PharmacyReviewViewHolder(ItemReviewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: PharmacyReviewViewHolder, position: Int) {
        val data = itemList.get(position)
        holder.binding.run{
            tvWriter.text = "${data.email}"
            tvDate.text = data.date
            tvReview.text = data.review
        }
    }


}