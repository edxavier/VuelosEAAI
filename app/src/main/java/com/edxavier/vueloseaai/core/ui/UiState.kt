package com.edxavier.vueloseaai.core.ui

import com.edxavier.vueloseaai.data.FlightData
import com.edxavier.vueloseaai.data.PageResult

data class UiState (
    var isLoading:Boolean = true,
    var bannerLoadFail:Boolean = false,
    var pageResult: PageResult = PageResult.Success(listOf())
)