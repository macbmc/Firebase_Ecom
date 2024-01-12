package com.example.firebaseecom.customerChat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityCustomerChatBinding
import com.example.firebaseecom.model.BrainShopModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerChatActivity : AppCompatActivity() {

    private lateinit var chatBinding: ActivityCustomerChatBinding
    private lateinit var chatViewModel: CustomerChatViewModel
    private lateinit var adapter: CustomerChatAdapter
    private val sendKey = 0
    private val receiveKey = 1
    private var brainList = mutableListOf<BrainShopModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_chat)
        chatViewModel = ViewModelProvider(this)[CustomerChatViewModel::class.java]
        chatBinding.apply {
            navPop.setOnClickListener {
                finish()
            }
            sendQuery.setOnClickListener {
                sendToBrain(chatQuery.text.toString())
            }
            adapter = CustomerChatAdapter(brainList)
            chatView.adapter = adapter
            chatView.layoutManager =
                LinearLayoutManager(this@CustomerChatActivity, LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun sendToBrain(msgQuery: String) {
        brainList.add(BrainShopModel(msgQuery, sendKey))
        adapter.notifyItemChanged(brainList.size-1)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                brainList.add(BrainShopModel(chatViewModel.getResponse(msgQuery),receiveKey))
                adapter.notifyItemChanged(brainList.size-1)
            }
        }


    }
}