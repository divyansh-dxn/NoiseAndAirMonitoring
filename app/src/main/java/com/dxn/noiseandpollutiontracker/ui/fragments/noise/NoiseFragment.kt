package com.dxn.noiseandpollutiontracker.ui.fragments.noise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dxn.noiseandpollutiontracker.R
import com.dxn.noiseandpollutiontracker.databinding.FragmentNoiseBinding
import com.dxn.noiseandpollutiontracker.models.Feed
import com.dxn.noiseandpollutiontracker.ui.fragments.MainViewModel
import com.example.noisemonitor.util.RepeatHelper
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement

class NoiseFragment : Fragment(R.layout.fragment_noise) {

    private val TAG = "NoiseFragment"
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentNoiseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoiseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val aaChartView = binding.aaChartView
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .yAxisMax(150.00f)
            .yAxisTitle("decibels ---->")
            .xAxisLabelsEnabled(true)
        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        val observer = Observer<List<Feed>> { feeds ->
            val data = mutableListOf<Double>()
            val labels = mutableListOf<String>()
            feeds.forEach {
                data.add(it.noiseLevel)
                labels.add(it.createdAt)
            }
            aaChartModel.categories = labels.toTypedArray()
            aaChartModel.series =
                arrayOf(
                    AASeriesElement().data(data.toTypedArray()).name("noise levels in decibel")
                        .color("#6200EE")
                )
            aaChartView.aa_refreshChartWithChartModel(aaChartModel)
        }

        val loadObserver = Observer<Boolean> { isLoading ->
            if (isLoading) {
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.GONE
            }
        }

        viewModel.feeds.observe(viewLifecycleOwner, observer)
        viewModel.isLoading.observe(viewLifecycleOwner, loadObserver)

        RepeatHelper.repeatDelayed(20000L) {
            viewModel.refreshData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                Log.d(TAG, "onOptionsItemSelected: Refreshed")
                viewModel.refreshData()
                true
            }
            else -> false
        }
    }

}