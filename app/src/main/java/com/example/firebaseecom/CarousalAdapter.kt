package com.example.firebaseecom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CarousalAdapter(val context: Context):RecyclerView.Adapter<CarousalAdapter.CarousalViewHolder>()
{
    val imageList:List<Int> = listOf(R.drawable.placeholder_image,R.drawable.placeholder_image)
    class CarousalViewHolder(view: View,context: Context):RecyclerView.ViewHolder(view)
    {
        var adView: ImageView = view.findViewById(R.id.adView)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarousalViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.ad_corousal_view, parent, false)
        return CarousalViewHolder(view, context)
    }

    override fun onBindViewHolder(holder:CarousalViewHolder, position: Int) {
        Glide.with(context)
            .load(imageList.get(position))
            .into(holder.itemView.findViewById(R.id.adView))
    }

    override fun getItemCount(): Int {
       return imageList.count()
    }



}