package com.example.firebaseecom.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firebaseecom.R
import com.example.firebaseecom.model.ProductOrderModel
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


@Suppress("DEPRECATION")
class OsmdroidUtils(val context: Context) {
    private lateinit var controller: IMapController
    private var endPoint = GeoPoint(0.00, 0.00)

    val mapLoadingInfo = MutableLiveData<String>()
    val locationLiveData = MutableLiveData<String>()


    @SuppressLint("MissingPermission")
    fun showTrackProduct(mMap: MapView, productOrder: ProductOrderModel): LiveData<String> {


        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.mapCenter
        mMap.setMultiTouchControls(true)
        mMap.getLocalVisibleRect(Rect())



        controller = mMap.controller
        controller.setZoom(6.0)
        val startPoint = GeoPoint(productOrder.currentGeoPoint[0], productOrder.currentGeoPoint[1])
        mMap.maxZoomLevel = 13.0
        mMap.minZoomLevel = 6.0


        val startMarker = Marker(mMap)
        startMarker.position = startPoint
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mMap.overlays.add(startMarker)
        mMap.invalidate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Geocoder(context).getFromLocationName(
                productOrder.deliveryLocation.toString(),
                1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        endPoint = GeoPoint(addresses[0].latitude, addresses[0].longitude)
                        drawPolyline(mMap, startPoint, endPoint)
                        Log.d("GPS_in", addresses.toString())
                        mapLoadingInfo.postValue("success")
                        val endMarker = Marker(mMap)
                        Log.d("GPS_end", endPoint.toString())
                        endMarker.position = endPoint
                        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        mMap.overlays.add(endMarker)
                        controller.setCenter(startPoint)
                        mMap.invalidate()

                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        Log.e("GPS-e", errorMessage.toString())
                        mapLoadingInfo.postValue(errorMessage.toString())

                    }
                }
            )
        } else {
            val address =
                Geocoder(context).getFromLocationName(productOrder.deliveryLocation.toString(), 1)
            if (address.isNullOrEmpty())
                mapLoadingInfo.postValue("failed")
            else
                mapLoadingInfo.postValue("success")
            endPoint = GeoPoint(address?.get(0)!!.latitude, address[0]!!.longitude)
            Log.d("GPS_in", address.toString())
            val endMarker = Marker(mMap)
            endMarker.title = context.getString(R.string.deliveryLocation)
            Log.d("GPS_end", endPoint.toString())
            endMarker.position = endPoint
            endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mMap.overlays.add(endMarker)
            controller.setCenter(startPoint)
            drawPolyline(mMap, startPoint, endPoint)
            mMap.invalidate()


        }
        return mapLoadingInfo

    }

    private fun drawPolyline(mMap: MapView, startPoint: GeoPoint, endPoint: GeoPoint) {
        Log.d("GPS-Polyline", "created")
        val polyLine = Polyline(mMap)
        polyLine.addPoint(startPoint)
        polyLine.addPoint(endPoint)
        mMap.overlays.add(polyLine)
        mMap.invalidate()

    }

    fun getLocationFromGeoPoint(productOrderModel: ProductOrderModel) {
        val geocoder = Geocoder(context)
        val location = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                productOrderModel.currentGeoPoint[0],
                productOrderModel.currentGeoPoint[1],
                1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        Log.e("Location-in", addresses[0].locality.toString())
                        locationLiveData.postValue(addresses[0].locality.toString())

                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        Log.e("Location", errorMessage.toString())
                        locationLiveData.postValue(location)

                    }
                })
        } else {
            val addressList = geocoder.getFromLocation(
                productOrderModel.currentGeoPoint[0],
                productOrderModel.currentGeoPoint[1],
                1
            )
            locationLiveData.postValue(addressList?.get(0)!!.locality.toString())

        }
    }
}


