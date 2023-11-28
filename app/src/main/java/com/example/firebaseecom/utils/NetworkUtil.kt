package com.example.firebaseecom.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkUtil(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun observeNetworkState(): Flow<NetworkState> {
        return callbackFlow {
            if(connectivityManager.activeNetwork==null)
            {
                Log.d("networkStateUtil","unavialble")
                launch {
                    send(NetworkState.UNAVAILABLE)
                }
            }
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.d("networkStateUtil","Avaialble")
                    launch {
                        send(NetworkState.AVAILABLE)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    Log.d("networkStateUtil","Losing")
                    launch {
                        send(NetworkState.LOSING)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.d("networkStateUtil","Lost")
                    launch {
                        send(NetworkState.LOST)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Log.d("networkStateUtil","unavialble")
                    launch {
                        send(NetworkState.UNAVAILABLE)
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()

    }


}