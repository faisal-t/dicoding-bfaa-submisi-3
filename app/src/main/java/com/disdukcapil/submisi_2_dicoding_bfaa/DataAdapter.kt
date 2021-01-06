package com.disdukcapil.submisi_2_dicoding_bfaa


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.disdukcapil.submisi_2_dicoding_bfaa.databinding.DataItemBinding

class DataAdapter (private val listData: ArrayList<DataModel>) : RecyclerView.Adapter<DataAdapter.DataViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = DataItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    class DataViewHolder(private val binding: DataItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModel: DataModel){
            with(binding){
                Glide.with(itemView.context)
                    .load(dataModel.avatar)
                    .into(imgPhoto)

                txtUsername.text = dataModel.username


                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("data",dataModel)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }


}