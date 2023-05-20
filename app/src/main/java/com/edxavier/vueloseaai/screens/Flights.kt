package com.edxavier.vueloseaai.screens

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edxavier.vueloseaai.R
import com.edxavier.vueloseaai.core.FlightDirection
import com.edxavier.vueloseaai.core.FlightType
import com.edxavier.vueloseaai.core.ui.BannerAdView
import com.edxavier.vueloseaai.core.ui.ErrorIndicator
import com.edxavier.vueloseaai.core.ui.LoadingIndicator
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.edxavier.vueloseaai.data.PageResult
import com.google.android.gms.ads.AdSize
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Flights(
    flightType: FlightType,
    viewModel: FlightsViewModel,
    adSize: AdSize
) {
    val state by viewModel.uiState.collectAsState()
    val tabs = listOf(FlightDirection.Arrival, FlightDirection.Departure)
    val pagerState = rememberPagerState()
    val listState = rememberLazyListState()

    Scaffold(

    ) { paddingValues ->

        LaunchedEffect(pagerState.currentPage) {
            viewModel.loadFlights(flightType, pagerState.currentPage)
        }
        Column(Modifier.padding(paddingValues)){
            val coroutineScope = rememberCoroutineScope()
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    )
                }
            ) {
                tabs.forEachIndexed { index, s ->
                    Tab(
                        text = { Text(text = s.title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                pageCount = tabs.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { _ ->
                if(state.isLoading) {
                    LoadingIndicator()
                }else{
                    when(val result = state.pageResult){
                        is PageResult.Error -> {
                            ErrorIndicator(
                                title = "Aviso!",
                                icon = ImageVector.vectorResource(
                                    id = R.drawable.world_error
                                ),
                                description = result.message
                            )
                        }
                        is PageResult.Timeout -> {
                            ErrorIndicator(
                                title = "Error de conexion",
                                icon = ImageVector.vectorResource(
                                    id = R.drawable.no_wifi
                                ),
                                description = result.message
                            )
                        }
                        is PageResult.Success -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 2.dp, vertical = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                state = listState
                            ){
                                items(items = result.flights){
                                   Flight(data = it)
                                }
                            }
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }
}