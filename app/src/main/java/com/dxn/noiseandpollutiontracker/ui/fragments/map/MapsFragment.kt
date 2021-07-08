package com.dxn.noiseandpollutiontracker.ui.fragments.map

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dxn.noiseandpollutiontracker.R
import com.dxn.noiseandpollutiontracker.models.Feed
import com.dxn.noiseandpollutiontracker.ui.fragments.MainViewModel
import com.dxn.noiseandpollutiontracker.util.Constants
import com.example.noisemonitor.util.RepeatHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    @ColorInt var CIRCLE_COLOR = Constants.GREEN

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        RepeatHelper.repeatDelayed(15000) {
            viewModel.refreshData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val callback = OnMapReadyCallback { googleMap ->
        val node1 = LatLng(24.224230, 83.215666)

        val marker = googleMap.addMarker(MarkerOptions().position(node1).title("Loading..."))
        marker?.showInfoWindow()
        googleMap.addCircle(CircleOptions().center(node1).radius(50.0).fillColor(Constants.GREEN).strokeWidth(0f))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(node1, 17f))

        val observer = Observer<List<Feed>> { feeds ->
            var avgNoise = 0.0
            var avgPPM = 0.0
            feeds.forEach {
                avgNoise += it.noiseLevel
                avgPPM += it.airConcentration
            }
            avgNoise /= feeds.size
            avgPPM /= feeds.size
            CIRCLE_COLOR = when {
                avgNoise > 85 -> {
                    Constants.RED
                }
                avgNoise in 70.0..85.0 -> {
                    Constants.YELLOW
                }
                else -> {
                    Constants.GREEN
                }
            }
            googleMap.clear()
            val marker = googleMap.addMarker(MarkerOptions().position(node1).title("Noise: ${avgNoise.toInt()} dB").snippet("PPM: ${avgPPM.toInt()}"))
            marker?.showInfoWindow()
            googleMap.addCircle(CircleOptions().center(node1).radius(50.0).fillColor(CIRCLE_COLOR).strokeWidth(0f))
        }
        viewModel.feeds.observe(this, observer)
    }
}