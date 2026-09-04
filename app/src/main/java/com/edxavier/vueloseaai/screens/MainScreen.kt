package com.edxavier.vueloseaai.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.edxavier.vueloseaai.BuildConfig
import com.edxavier.vueloseaai.R
import com.edxavier.vueloseaai.core.ui.BannerAdView
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.edxavier.vueloseaai.navigation.*

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: FlightsViewModel
) {
    val bottomNavItems = listOf(
        BottomNavItem(
            name = "Internacionales",
            route = Destinations.Internationals.route,
            icon = ImageVector.vectorResource(id = R.drawable.earth)
        ),
        BottomNavItem(
            name = "Nacionales",
            route = Destinations.Nationals.route,
            icon = ImageVector.vectorResource(id = R.drawable.flag_nic),
        ),
        BottomNavItem(
            name = "Informacion",
            route = Destinations.Information.route,
            icon = Icons.Rounded.Info,
        )
    )

    Scaffold(
        topBar = {
            val configuration = LocalConfiguration.current
            val adSize = remember(configuration.screenWidthDp) {
                AdSize(configuration.screenWidthDp, 60)
            }
            Box(modifier = Modifier.statusBarsPadding()) {
                BannerAdView(
                    isTest = BuildConfig.DEBUG,
                    adSize = adSize
                )
            }
        },
        bottomBar = {
            BottomNavBar(items = bottomNavItems, navController = navController)
        }
    ) { paddingValues ->
        NavigationHost(
            navController = navController,
            viewModel = viewModel,
            paddingValues = paddingValues
        )
    }
}
