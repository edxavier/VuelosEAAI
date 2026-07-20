package com.edxavier.vueloseaai.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.edxavier.vueloseaai.R
import com.edxavier.vueloseaai.core.FlightDirection
import com.edxavier.vueloseaai.core.FlightType
import com.edxavier.vueloseaai.core.ui.ErrorIndicator
import com.edxavier.vueloseaai.core.ui.FlightSkeletonLoader
import com.edxavier.vueloseaai.core.ui.NativeAdWithFallback
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.edxavier.vueloseaai.data.PageResult
import com.edxavier.vueloseaai.navigation.Destinations
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Flights(
    flightType: FlightType,
    viewModel: FlightsViewModel,
    navCtrl: NavHostController
) {
    val state by viewModel.uiState.collectAsState()
    val tabs = listOf(FlightDirection.Arrival, FlightDirection.Departure)
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) { tabs.size }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.loadFlights(flightType, pagerState.currentPage)
    }

    Column(Modifier.fillMaxSize()) {
        val coroutineScope = rememberCoroutineScope()

        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            divider = {},
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        ) {
            tabs.forEachIndexed { index, direction ->
                Tab(
                    text = {
                        Text(
                            text = direction.title,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            if (state.isLoading) {
                FlightSkeletonLoader()
            } else {
                when (val result = state.pageResult) {
                    is PageResult.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ErrorIndicator(
                                title = "Aviso!",
                                icon = ImageVector.vectorResource(id = R.drawable.world_error),
                                description = result.message,
                                onRetry = { viewModel.loadFlights(flightType, page) }
                            )
                        }
                    }
                    is PageResult.Timeout -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ErrorIndicator(
                                title = "Error de conexion",
                                icon = ImageVector.vectorResource(id = R.drawable.no_wifi),
                                description = result.message,
                                onRetry = { viewModel.loadFlights(flightType, page) }
                            )
                        }
                    }
                    is PageResult.Success -> {
                        val flights = result.flights
                        val nativeAdInterval = 6

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            itemsIndexed(
                                items = flights,
                                key = { _, flight -> "${flight.flight}_${flight.time}" }
                            ) { index, flight ->
                                if (index > 0 && index % nativeAdInterval == 0) {
                                    NativeAdWithFallback()
                                }
                                Flight(
                                    data = flight,
                                    onDetailsClick = { id ->
                                        viewModel.flightId = id
                                        viewModel.onShowInterstitial?.invoke()
                                        navCtrl.navigate(
                                            Destinations.FlightDetails.createRoute(id)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
