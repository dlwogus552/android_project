package org.third.medicalapp.user

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.third.medicalapp.R
import org.third.medicalapp.community.CommunityData
import org.third.medicalapp.databinding.ItemUserListBinding
import org.third.medicalapp.databinding.ItemUserWriteListBinding
import org.third.medicalapp.sign.model.UserModel
import org.third.medicalapp.util.MyApplication

class UserWriteViewHolder(val binding: ItemUserWriteListBinding) : RecyclerView.ViewHolder(binding.root)

class UserWriteAdapter(val context: Context, val itemList: MutableList<CommunityData>) :RecyclerView.Adapter<UserWriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserWriteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserWriteViewHolder(ItemUserWriteListBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: UserWriteViewHolder, position: Int) {
        val data = itemList.get(position)
    }
}