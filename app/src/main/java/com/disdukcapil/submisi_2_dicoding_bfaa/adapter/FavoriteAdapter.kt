package com.disdukcapil.submisi_2_dicoding_bfaa.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.disdukcapil.submisi_2_dicoding_bfaa.DetailActivity
import com.disdukcapil.submisi_2_dicoding_bfaa.R
import com.disdukcapil.submisi_2_dicoding_bfaa.databinding.DataItemBinding
import com.disdukcapil.submisi_2_dicoding_bfaa.entity.User

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<User>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    fun addItem(user: User) {
        this.listFavorite.add(user)
        notifyItemInserted(this.listFavorite.size - 1)
    }
    fun updateItem(position: Int, user: User) {
        this.listFavorite[position] = user
        notifyItemChanged(position, user)
    }
    fun removeItem(position: Int) {
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorite.size)
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding  = DataItemBinding.bind(itemView)
        fun bind(user: User){
            with(binding){
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .into(imgPhoto)

                txtUsername.text = user.username

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("data",user.toDataModel())
                    itemView.context.startActivity(intent)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

}