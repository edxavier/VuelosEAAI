package com.edxavier.vueloseaai.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edxavier.vueloseaai.core.FlightType
import com.edxavier.vueloseaai.core.ui.UiState
import com.edxavier.vueloseaai.data.repo.FlightsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightsViewModel: ViewModel(){
    private val repo = FlightsRepo()
    private val natEndpoints = listOf(FlightsEndpoint.NatArrivals, FlightsEndpoint.NatDepartures)
    private val intEndpoints = listOf(FlightsEndpoint.IntArrivals, FlightsEndpoint.IntDepartures)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private fun setLoadingState(loading: Boolean = true){
        _uiState.update { state ->
            state.copy(
                isLoading = loading
            )
        }
    }

    fun loadFlights(flightType: FlightType, pageIndex: Int){
        val endpoint = if(flightType == FlightType.International) {
            intEndpoints[pageIndex].endpointUrl
        }else {
            natEndpoints[pageIndex].endpointUrl
        }
        viewModelScope.launch(Dispatchers.IO) {
            setLoadingState(true)
            val res = repo.getFlights(endpoint)
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    pageResult = res
                )
            }
        }
    }
}