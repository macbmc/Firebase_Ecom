package com.example.firebaseecom

import android.content.Context
import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.databinding.AdCorousalViewBinding


class CarousalAdapter(val context: Context):RecyclerView.Adapter<CarousalAdapter.CarousalViewHolder>()
{
    private  var imageList = emptyList<String>()
    private lateinit var carousalBinding: AdCorousalViewBinding

    fun setCarousal(imageList: List<String>){
        this.imageList=imageList
        notifyDataSetChanged()
    }
    class CarousalViewHolder(val carousalBinding: AdCorousalViewBinding,val context: Context):RecyclerView.ViewHolder(carousalBinding.root)
    {
        fun bind(imageUrl:String){
            Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.placeholder_image)
                .into(carousalBinding.adView)
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarousalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        carousalBinding = DataBindingUtil.inflate(inflater,R.layout.ad_corousal_view,parent,false)
        return CarousalViewHolder(carousalBinding,context)

    }

    override fun onBindViewHolder(holder:CarousalViewHolder, position: Int) {
        holder.bind(imageList[position])

    }

    override fun getItemCount(): Int {
       return imageList.count()
    }



}