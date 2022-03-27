package com.example.adiblar_hayoti.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adiblar_hayoti.R
import com.example.adiblar_hayoti.databinding.AdibListBinding
import com.example.adiblar_hayoti.models.Adib
import com.example.adiblar_hayoti.room.Adib_Entity

class AdibAdapter  (var list: List<Adib>, var favoritelist: List<Adib_Entity>, var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<AdibAdapter.Vh>() {

    inner class Vh(var adibListBinding: AdibListBinding) :
        RecyclerView.ViewHolder(adibListBinding.root){

        fun onBind(adib: Adib) {
            for (adibEntity in favoritelist) {
                if (adibEntity.name == adib.name){
                    adibListBinding.liner.setBackgroundResource(R.drawable.circle_shape)
                    adibListBinding.collection.setImageResource(R.drawable.saved)
                }
            }


            adibListBinding.poetName.text = adib.name
            adibListBinding.year.text =  "${adib.birth_date}-${adib.death_date}"
            Glide.with(adibListBinding.root.context).load(adib.photoUrl).into(adibListBinding.profile)

//            if (adib.selected == true){
//                adibListBinding.liner.setBackgroundResource(R.drawable.circle_shape)
//                adibListBinding.collection.setImageResource(R.drawable.saved)
//            }else{
//                adibListBinding.liner.setBackgroundResource(R.color.white)
//                adibListBinding.collection.setImageResource(R.drawable.ribbon)
//            }

            adibListBinding.liner.setOnClickListener {
                onItemClickListener.onItemFavoriteClick(adibListBinding,adib,adapterPosition)
            }

            adibListBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(adib,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(AdibListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener{
        fun onItemFavoriteClick(adibListBinding: AdibListBinding,adib: Adib,position: Int)
        fun onItemClick(adib: Adib,position: Int)
    }
    fun filterList(filteredList: ArrayList<Adib>) {
        list = filteredList
        notifyDataSetChanged()
    }
}