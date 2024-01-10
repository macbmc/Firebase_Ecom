package com.example.firebaseecom.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.AdCorousalViewBinding

class CarousalAdapter(private val context: Context, private val activityFunctionClass: HomeActivity.ActivityFunctionClass):RecyclerView.Adapter<CarousalAdapter.CarousalViewHolder>() {

    private var imageList= emptyList<String?>()
    private var lastPosition=-1
    private lateinit var adCorousalViewBinding: AdCorousalViewBinding

    interface ActivityFunctionInterface{
        fun navToOfferZone()
    }

    fun setAd(imageList: List<String>)
    {
        this.imageList=imageList
        notifyDataSetChanged()
    }
    class CarousalViewHolder(adCorousalViewBinding: AdCorousalViewBinding):RecyclerView.ViewHolder(adCorousalViewBinding.root)

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
        val imageUrl=imageList[position % imageList.size]
        setAnimation(holder.itemView,position)
        Glide.with(holder.itemView)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.adView))
        holder.itemView.setOnClickListener {
            activityFunctionClass.navToOfferZone()
        }

    }

    override fun getItemCount(): Int {
        return if(imageList.isEmpty())
            0
        else
            Integer.MAX_VALUE

    }
    private fun setAnimation(itemView: View, position: Int) {
        if(position>lastPosition)
        {
            val animation= AnimationUtils.loadAnimation(context,R.anim.slide_from_right)
            itemView.startAnimation(animation)
            lastPosition=position
        }

    }
}