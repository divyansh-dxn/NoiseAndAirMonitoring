package com.dxn.noiseandpollutiontracker.ui.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dxn.noiseandpollutiontracker.models.Feed
import com.dxn.noiseandpollutiontracker.models.Node
import com.dxn.noiseandpollutiontracker.repository.FeedsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val feeds: MutableLiveData<List<Feed>> by lazy {
        MutableLiveData<List<Feed>>()
    }
    val node: MutableLiveData<Node> by lazy {
        MutableLiveData<Node>()
    }
    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData(true)
    }

    private val repository = FeedsRepository()

    init {
        refreshData()
    }

    fun refreshData() {
        isLoading.value = true
        GlobalScope.launch(Dispatchers.IO) {
            val data = repository.fetchData()
            val f = data["feeds"] as List<Feed>
            val n = data["node"] as Node
            feeds.postValue(f)
            node.postValue(n)
            isLoading.postValue(false)
        }
    }
}