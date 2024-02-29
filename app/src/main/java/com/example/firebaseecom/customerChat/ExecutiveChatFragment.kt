package com.example.firebaseecom.customerChat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebaseecom.R

class ExecutiveChatFragment : Fragment() {

    companion object {
        fun newInstance() = ExecutiveChatFragment()
    }

    private lateinit var viewModel: ExecutiveChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_executive_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ExecutiveChatViewModel::class.java]
        // TODO: Use the ViewModel
    }

}