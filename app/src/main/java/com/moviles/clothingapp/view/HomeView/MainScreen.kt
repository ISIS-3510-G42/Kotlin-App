package com.moviles.clothingapp.view.HomeView

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.moviles.clothingapp.R
import com.moviles.clothingapp.viewmodel.HomeViewModel
import com.moviles.clothingapp.viewmodel.WeatherViewModel

@Composable
fun MainScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    weatherViewModel: WeatherViewModel = viewModel()
) {
    val banner = weatherViewModel.bannerType.observeAsState()
    val searchText = remember { mutableStateOf("") }
    val isConnected = homeViewModel.isConnected.collectAsState()

    val trace: Trace = remember { FirebasePerformance.getInstance().newTrace("MainScreen_Loading") }
    LaunchedEffect(Unit) {
        trace.start()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Alerta de conexión
            if (!isConnected.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No internet connection",
                        color = Color.White
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(80.dp)
                    )
                }
                item {
                    SearchBar(
                        searchText = searchText.value,
                        onSearchTextChange = { newText -> searchText.value = newText },
                        onSearchSubmit = {
                            navController.navigate("discover/${searchText.value}")
                        }
                    )
                }
                item { QuickActions() }
                item { PromoBanner(bannerType = banner.value, navController = navController) }
                item { CategorySection(categoryList = categoryList) }
                item { FeaturedProducts(homeViewModel) }
            }
        }
    }

    LaunchedEffect(banner.value) {
        trace.stop()
    }
}

