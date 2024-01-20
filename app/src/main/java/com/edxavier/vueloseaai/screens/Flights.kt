package com.edxavier.vueloseaai.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edxavier.vueloseaai.R
import com.edxavier.vueloseaai.core.FlightDirection
import com.edxavier.vueloseaai.core.FlightType
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
) {
    val state by viewModel.uiState.collectAsState()
    val tabs = listOf(FlightDirection.Arrival, FlightDirection.Departure)
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) { tabs.size }
    // val listState = rememberLazyListState()
    Scaffold(
    ) { paddingValues ->
        LaunchedEffect(pagerState.currentPage) {
            viewModel.loadFlights(flightType, pagerState.currentPage)
        }
        Column(Modifier.padding(paddingValues)){
            val coroutineScope = rememberCoroutineScope()
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    )
                }
            ) {
                tabs.forEachIndexed { index, s ->
                    Tab(
                        text = {
                            Text(
                                text = s.title,
                                fontSize = 12.sp
                            ) },
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
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
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
                                    .padding(horizontal = 1.dp, vertical = 2.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                                // state = listState,
                            ){
                                items(items = result.flights){
                                    Flight(data = it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}