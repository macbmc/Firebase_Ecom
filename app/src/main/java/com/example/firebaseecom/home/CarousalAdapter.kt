package com.example.firebaseecom.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.AdCorousalViewBinding

class CarousalAdapter(private val context: Context):RecyclerView.Adapter<CarousalAdapter.CarousalViewHolder>() {

    var imageList= emptyList<String?>()
    var lastPosition=-1
    private lateinit var adCorousalViewBinding: AdCorousalViewBinding

    fun setAd(imageList: List<String>)
    {
        this.imageList=imageList
        notifyDataSetChanged()
    }
    class CarousalViewHolder(adCorousalViewBinding: AdCorousalViewBinding):RecyclerView.ViewHolder(adCorousalViewBinding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarousalViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        adCorousalViewBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.ad_corousal_view,parent
            ,false)
        return CarousalViewHolder(adCorousalViewBinding)

    }

    override fun onBindViewHolder(holder: CarousalViewHolder, position: Int) {
        val imageUrl=imageList[position]
        setAnimation(holder.itemView,position)
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.adView))

    }

    override fun getItemCount(): Int {
       return imageList.count()
    }
    private fun setAnimation(itemView: View, position: Int) {
        if(position>lastPosition)
        {
            val animation= AnimationUtils.loadAnimation(context,R.anim.slide_down)
            itemView.startAnimation(animation)
            lastPosition=position
        }

    }
}