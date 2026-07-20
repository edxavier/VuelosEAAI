package com.edxavier.vueloseaai.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edxavier.vueloseaai.core.FlightType
import com.edxavier.vueloseaai.core.ui.UiState
import com.edxavier.vueloseaai.data.repo.FlightsRepo
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray

class FlightsViewModel: ViewModel(){
    var onShowInterstitial: (() -> Unit)? = null
    private val repo = FlightsRepo()
    private val natEndpoints = listOf(FlightsEndpoint.NatArrivals, FlightsEndpoint.NatDepartures)
    private val intEndpoints = listOf(FlightsEndpoint.IntArrivals, FlightsEndpoint.IntDepartures)

    private val _uiState = MutableStateFlow(UiState())
    var flightId: String = ""
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _eaaiNacUrl = MutableStateFlow("https://www.eaai.com.ni/pvnac/vuelos_dias_pvnac.php")
    val eaaiNacUrl: StateFlow<String> = _eaaiNacUrl.asStateFlow()

    private val _eaaiIntUrl = MutableStateFlow("https://www.eaai.com.ni/fids/vuelos_dias_fids.php")
    val eaaiIntUrl: StateFlow<String> = _eaaiIntUrl.asStateFlow()

    private val _scrapeVuelosInt = MutableStateFlow(true)
    val scrapeVuelosInt: StateFlow<Boolean> = _scrapeVuelosInt.asStateFlow()

    init {
        getRemoteConfig()
    }

    fun setLoadingState(loading: Boolean = true){
        _uiState.update { state ->
            state.copy(
                isLoading = loading,
            )
        }
    }

    private fun getRemoteConfig(){
        try {
            val remoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 60 }
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(
                mapOf(
                    "eaai_int_url" to "https://www.eaai.com.ni/fids/vuelos_dias_fids.php",
                    "eaai_nac_url" to "https://www.eaai.com.ni/pvnac/vuelos_dias_pvnac.php",
                    "scrape_vuelos_int" to true,
                )
            )

            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _eaaiIntUrl.value = remoteConfig.getString("eaai_int_url")
                    _eaaiNacUrl.value = remoteConfig.getString("eaai_nac_url")
                    _scrapeVuelosInt.value = remoteConfig.getBoolean("scrape_vuelos_int")
                }
            }
        }catch (_: Exception){}
    }

    fun loadFlights(flightType: FlightType, pageIndex: Int){
        val endpoint = if(flightType == FlightType.International) {
            intEndpoints[pageIndex].endpointUrl
        }else {
            natEndpoints[pageIndex].endpointUrl
        }
        viewModelScope.launch(Dispatchers.IO) {
            setLoadingState()
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