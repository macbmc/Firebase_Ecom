package com.example.firebaseecom.customerChat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.RecieverLayoutBinding
import com.example.firebaseecom.databinding.SendLayoutBinding
import com.example.firebaseecom.model.BrainShopModel

class CustomerChatAdapter(private val brainList: List<BrainShopModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var recieverLayoutBinding: RecieverLayoutBinding
    private lateinit var sendLayoutBinding: SendLayoutBinding
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        sendLayoutBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.send_layout, parent, false)
        recieverLayoutBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.reciever_layout, parent, false)
        return when (viewType) {
            0 -> {

                SendLayoutViewHolder(sendLayoutBinding)

            }

            else -> {

                ReceiveLayoutViewHolder(recieverLayoutBinding)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = brainList[position]

        when (model.code) {
            0 -> {
                val user = holder as SendLayoutViewHolder
                user.sendText.text = model.cnt
                Glide.with(holder.itemView)
                    .load(R.drawable.placeholder_image)
                    .into(user.sendImage)
            }

            1 -> {
                val sender = holder as ReceiveLayoutViewHolder
                sender.receiveText.text = model.cnt
            }
        }


    }

    inner class SendLayoutViewHolder(sendLayoutBinding: SendLayoutBinding) :
        RecyclerView.ViewHolder(sendLayoutBinding.root) {
        var sendText = sendLayoutBinding.SendText
        var sendImage = sendLayoutBinding.userImg

    }

    inner class ReceiveLayoutViewHolder(recieverLayoutBinding: RecieverLayoutBinding) :
        RecyclerView.ViewHolder(recieverLayoutBinding.root) {
        var receiveText = recieverLayoutBinding.BotText


    }


    override fun getItemCount(): Int {


        return brainList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (brainList[position].code) {
            0 -> 0
            1 -> 1
            else -> -1
        }
    }


}