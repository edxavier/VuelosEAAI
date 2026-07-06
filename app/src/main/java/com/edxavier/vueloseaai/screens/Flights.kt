package com.edxavier.vueloseaai.screens

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
                        ErrorIndicator(
                            title = "Aviso!",
                            icon = ImageVector.vectorResource(id = R.drawable.world_error),
                            description = result.message
                        )
                    }
                    is PageResult.Timeout -> {
                        ErrorIndicator(
                            title = "Error de conexion",
                            icon = ImageVector.vectorResource(id = R.drawable.no_wifi),
                            description = result.message
                        )
                    }
                    is PageResult.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            itemsIndexed(
                                items = result.flights,
                                key = { _, flight -> "${flight.flight}_${flight.time}" }
                            ) { index, flight ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn() + slideInVertically(
                                        initialOffsetY = { it / 4 }
                                    )
                                ) {
                                    Flight(
                                        data = flight,
                                        onDetailsClick = { id ->
                                            viewModel.flightId = id
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
}
