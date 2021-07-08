package com.dxn.noiseandpollutiontracker.ui.fragments.airquality

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.dxn.noiseandpollutiontracker.R
import com.dxn.noiseandpollutiontracker.databinding.AirQualityFragmentBinding
import com.dxn.noiseandpollutiontracker.models.Feed
import com.dxn.noiseandpollutiontracker.ui.fragments.MainViewModel
import com.example.noisemonitor.util.RepeatHelper
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement

class AirQualityFragment : Fragment(R.layout.air_quality_fragment) {

    private lateinit var viewModel: MainViewModel
    private var _binding: AirQualityFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AirQualityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        val aaChartView = binding.aaChartView
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .yAxisMax(5000.00f)
            .yAxisTitle("particulate concentration (PPM) ---->")
            .xAxisLabelsEnabled(true)
        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        val observer = Observer<List<Feed>> { feeds ->
            val data = mutableListOf<Double>()
            val labels = mutableListOf<String>()
            feeds.forEach {
                data.add(it.airConcentration/100.5)
                labels.add(it.createdAt)
            }
            aaChartModel.categories = labels.toTypedArray()
            aaChartModel.series =
                arrayOf(
                    AASeriesElement().data(data.toTypedArray()).name("Air quality in PPM")
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

}