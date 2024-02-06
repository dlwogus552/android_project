package org.third.medicalapp.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.third.medicalapp.community.MyViewHolder
import org.third.medicalapp.databinding.ItemUserBinding
import java.util.Objects

class UserViewHolder(val binding:ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

class UserAdapter(val context: Context, val itemList:MutableList<Objects>):RecyclerView.Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(ItemUserBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val data = itemList.get(position)
    }
}