@file:Suppress("DEPRECATION")

package com.example.firebaseecom.customerChat

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.customerChat.CustomerChatViewModel.Companion.ifSucessfullOrderStatusFetch
import com.example.firebaseecom.customerChat.CustomerChatViewModel.Companion.ifSucessfullProductReviewFetch
import com.example.firebaseecom.customerChat.CustomerChatViewModel.Companion.incorrectReviewCounter
import com.example.firebaseecom.customerChat.CustomerChatViewModel.Companion.incorrectStatusCounter
import com.example.firebaseecom.databinding.ActivityCustomerChatBinding
import com.example.firebaseecom.model.BrainShopModel
import com.example.firebaseecom.model.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerChatActivity : AppCompatActivity() {

    private lateinit var chatBinding: ActivityCustomerChatBinding
    private lateinit var chatViewModel: CustomerChatViewModel
    private lateinit var adapter: CustomerChatAdapter
    private val sendKey = 0
    private var userData: UserModel? = null
    private val receiveKey = 1
    private val helloText = "Hi"
    private var brainList = mutableListOf<BrainShopModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("Customer_Chat", MODE_PRIVATE)

        editor = sharedPreferences.edit()
        editor.putInt("orderStatus", 0)
        editor.putInt("productRating", 0)
        editor.apply()
        chatBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_chat)
        chatViewModel = ViewModelProvider(this)[CustomerChatViewModel::class.java]
        val bundle = intent.extras
        userData = if (bundle?.getSerializable("userData") != null) {
            bundle.getSerializable("userData") as UserModel
        } else {
            null
        }
        chatBinding.apply {
            navPop.setOnClickListener {
                finish()
            }
            sendQuery.setOnClickListener {
                sendToBrain(chatQuery.text.toString())
            }
            adapter = CustomerChatAdapter(brainList, userData)
            chatView.adapter = adapter
            chatView.layoutManager =
                LinearLayoutManager(this@CustomerChatActivity, LinearLayoutManager.VERTICAL, false)
            sendToBrain(helloText)

        }

    }

    private fun sendToBrain(msgQuery: String) {
        chatBinding.progressBar.isVisible=true
        brainList.add(BrainShopModel(msgQuery, sendKey))
        adapter.notifyItemChanged(brainList.size - 1)
        chatBinding.chatQuery.text.clear()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                if (sharedPreferences.getInt("orderStatus", 0) == 1) {
                    brainList.add(
                        BrainShopModel(
                            chatViewModel.getOrderStatus(msgQuery),
                            receiveKey
                        )
                    )
                    chatBinding.progressBar.visibility= View.GONE
                    adapter.notifyItemChanged(brainList.size - 1)
                    if (ifSucessfullOrderStatusFetch|| incorrectStatusCounter>2) {
                        editor.putInt("orderStatus", 0)
                        editor.apply()
                        chatViewModel.resetStatusCounter()
                    }


                } else if (sharedPreferences.getInt("productRating", 0) == 1) {
                    brainList.add(
                        BrainShopModel(
                            chatViewModel.getProductReviews(msgQuery),
                            receiveKey
                        )
                    )
                    chatBinding.progressBar.visibility= View.GONE
                    adapter.notifyItemChanged(brainList.size - 1)
                    if (ifSucessfullProductReviewFetch|| incorrectReviewCounter>2) {
                        editor.putInt("productRating", 0)
                        editor.apply()
                        chatViewModel.resetReviewCounter()
                    }


                } else {
                    val botResponse = chatViewModel.getResponse(msgQuery)
                    if (botResponse.contains("[1]")) {
                        brainList.add(BrainShopModel(botResponse, receiveKey))
                        chatBinding.progressBar.visibility= View.GONE
                        adapter.notifyItemChanged(brainList.size - 1)
                        editor.putInt("orderStatus", 1)
                        editor.apply()
                    } else if (botResponse.contains("[2]")) {
                        brainList.add(BrainShopModel(botResponse, receiveKey))
                        chatBinding.progressBar.visibility= View.GONE
                        adapter.notifyItemChanged(brainList.size - 1)
                        editor.putInt("productRating", 1)
                        editor.apply()
                    } else {
                        brainList.add(BrainShopModel(botResponse, receiveKey))
                        chatBinding.progressBar.visibility= View.GONE
                        adapter.notifyItemChanged(brainList.size - 1)
                    }


                }


            }
        }


    }


}