package com.example.bionime.ui

import androidx.lifecycle.*
import com.example.bionime.data.Mask
import com.example.bionime.repository.MaskRepository
import kotlinx.coroutines.launch

class MaskViewModel(private val repository: MaskRepository) : ViewModel() {
    private val _masks = MutableLiveData<List<Mask>>()
    val masks: LiveData<List<Mask>> = _masks

    private val _towns = MutableLiveData<List<String>>()
    val towns: LiveData<List<String>> = _towns

    private var currentTown: String? = null


    init {
        viewModelScope.launch {
            repository.refreshMasks()
            updateTowns()
            updateMasks()
        }
    }

    private suspend fun updateTowns() {
        val towns = repository.getAllTowns()
        _towns.value = listOf("全部") + towns
    }

    fun updateMasks(town: String? = null) {
        viewModelScope.launch {
            currentTown = town
            _masks.value = if (town == null || town == "全部") {
                repository.getAllMasks()
            } else {
                repository.getMasksByTown(town)
            }
        }
    }

    fun deleteMask(name: String) {
        viewModelScope.launch {
            repository.deleteMaskByName(name)
            updateMasks(currentTown)
        }
    }

    suspend fun fetchQuote(): String {
        return repository.fetchQuote()
    }

}

class MaskViewModelFactory(private val repository: MaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}