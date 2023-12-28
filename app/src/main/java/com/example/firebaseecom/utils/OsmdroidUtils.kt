package com.example.firebaseecom.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.location.GpsStatus
import android.location.LocationManager
import android.util.Log
import com.example.firebaseecom.model.ProductOrderModel
import org.osmdroid.api.IMapController
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class OsmdroidUtils(val context: Context) : MapListener, GpsStatus.Listener {
    private lateinit var controller: IMapController
    private lateinit var mMyLocationOverlay: MyLocationNewOverlay


    @SuppressLint("MissingPermission")
    fun showTrackProduct(mMap: MapView, productOrder: ProductOrderModel) {

        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.mapCenter
        mMap.setMultiTouchControls(true)
        mMap.setBuiltInZoomControls(true)
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

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            val endPoint = GeoPoint(location.latitude, location.longitude)
            Log.d("GPS_end", endPoint.toString())
            val endMarker = Marker(mMap)
            endMarker.position = endPoint
            endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mMap.overlays.add(endMarker)
            mMap.invalidate()
            controller.setCenter(startPoint)

            val polyLine = Polyline(mMap)
            polyLine.addPoint(startPoint)
            polyLine.addPoint(endPoint)
            mMap.overlays.add(polyLine)
            mMap.invalidate()


        } else {
            Log.d("GPS", "location null")
        }
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        // event?.source?.getMapCenter()
        Log.e("TAG", "onCreate:la ${event?.source?.mapCenter?.latitude}")
        Log.e("TAG", "onCreate:lo ${event?.source?.mapCenter?.longitude}")
        //  Log.e("TAG", "onScroll   x: ${event?.x}  y: ${event?.y}", )
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        //  event?.zoomLevel?.let { controller.setZoom(it) }


        Log.e("TAG", "onZoom zoom level: ${event?.zoomLevel}   source:  ${event?.source}")
        return false
    }

    override fun onGpsStatusChanged(event: Int) {


        TODO("Not yet implemented")
    }
}