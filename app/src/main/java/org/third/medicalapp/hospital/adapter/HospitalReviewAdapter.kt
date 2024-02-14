package org.third.medicalapp.pharmacy.apater

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ItemReviewBinding
import org.third.medicalapp.hospital.model.Hospital
import org.third.medicalapp.hospital.model.HospitalReview
import org.third.medicalapp.util.MyApplication
import retrofit2.Call
import retrofit2.Response


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
            tvWriter.text = data.nick
            tvDate.text = data.date
            tvReview.text = data.review

            tvGood.setOnClickListener {
                holder.binding.imageReview.setImageResource(R.drawable.good)
                tvGood.setText("")
                tvBad.setText("")
                tv.setText("")


//
//                MyApplication.db.collection("comment").whereEqualTo("docId", docId).get()
//                    .addOnSuccessListener { documents ->
//                        // documents에는 쿼리 결과에 해당하는 문서들이 포함됩니다.
//                        for (document in documents) {
//                            // 각 문서를 삭제합니다.
//                            MyApplication.db.collection("comment").document(document.id).delete()
//                                .addOnSuccessListener {
//                                    Log.d("comment_db delete success", "데이터 삭제에 성공하였습니다.")
//                                }
//                                .addOnFailureListener { e ->
//                                    Log.d("comment_db delete failure", "데이터 삭제에 실패하였습니다.")
//                                }
//                        }
//                    }
//




            }
            tvBad.setOnClickListener {
                holder.binding.imageReview.setImageResource(R.drawable.bad)
                tvGood.setText("")
                tvBad.setText("")
                tv.setText("")
            }
        }
    }



}