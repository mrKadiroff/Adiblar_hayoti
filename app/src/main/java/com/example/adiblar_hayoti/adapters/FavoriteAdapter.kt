package com.example.adiblar_hayoti.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adiblar_hayoti.R
import com.example.adiblar_hayoti.databinding.AdibListBinding
import com.example.adiblar_hayoti.room.Adib_Entity

class FavoriteAdapter (var favoritelist: List<Adib_Entity>, var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<FavoriteAdapter.Vh>() {

    inner class Vh(var adibListBinding: AdibListBinding) :
        RecyclerView.ViewHolder(adibListBinding.root){

        fun onBind(adibEntity: Adib_Entity) {


            adibListBinding.liner.setBackgroundResource(R.drawable.circle_shape)
            adibListBinding.collection.setImageResource(R.drawable.saved)


            adibListBinding.poetName.text = adibEntity.name
            adibListBinding.year.text =  "${adibEntity.birth_date}-${adibEntity.death_date}"
            Glide.with(adibListBinding.root.context).load(adibEntity.photoUrl).into(adibListBinding.profile)


            adibListBinding.liner.setOnClickListener {
                onItemClickListener.onItemFavoriteClick(adibListBinding,adibEntity,adapterPosition)
            }

            adibListBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(adibEntity,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(AdibListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(favoritelist[position])
    }

    override fun getItemCount(): Int = favoritelist.size

    interface OnItemClickListener{
        fun onItemFavoriteClick(adibListBinding: AdibListBinding,adibEntity: Adib_Entity,position: Int)
        fun onItemClick(adibEntity: Adib_Entity,position: Int)
    }
    fun filterList(filteredList: ArrayList<Adib_Entity>) {
        favoritelist = filteredList
        notifyDataSetChanged()
    }
}