package com.moviles.clothingapp.view.Discover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.moviles.clothingapp.view.HomeView.BottomNavigationBar
import com.moviles.clothingapp.view.HomeView.SearchBar
import com.moviles.clothingapp.viewmodel.PostViewModel
import com.moviles.clothingapp.viewmodel.HomeViewModel



/*  Discover Screen/Section:
*   - Features cards of all the posts -> using PostItem composable.
*   - Features filter button which triggers the FilterDialog composable and displays the results of filtering.
*   - Features the same search bar used in main page, from the SearchBar composable.
*   - Observes changes in state of the search bar and filters to update UI dynamically.
 */

@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: PostViewModel,
    query: String,
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel() //
) {
    val posts by viewModel.posts.collectAsState()
    val isConnected = homeViewModel.isConnected.collectAsState() //
    var searchQuery by remember { mutableStateOf(query) }
    val trace: Trace = FirebasePerformance.getInstance().newTrace("DiscoverScreen_trace")
    trace.start()

    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var selectedColor by remember { mutableStateOf("Todos") }
    var selectedSize by remember { mutableStateOf("Todos") }
    var selectedGroup by remember { mutableStateOf("Todos") }
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }

    LaunchedEffect(posts) {
        if (posts.isNotEmpty()) {
            trace.stop()
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Descubrir",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showFilterDialog = true }) {
                    Icon(Icons.Rounded.FilterAlt, contentDescription = "Filter")
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!isConnected.value) {
                androidx.compose.material3.Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = androidx.compose.ui.graphics.Color.Red
                ) {
                    Text(
                        text = "No internet connection",
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally),
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            }

            SearchBar(
                searchText = searchQuery,
                onSearchTextChange = { searchQuery = it },
                onSearchSubmit = {
                    navController.navigate("discover/${searchQuery}")
                }
            )

            val filteredPosts = posts.filter { post ->
                val postPrice = post.price.replace(".", "").replace(",", "").toIntOrNull() ?: 0
                (selectedCategory == "Todos" || post.category == selectedCategory) &&
                        (selectedColor == "Todos" || post.color == selectedColor) &&
                        (selectedSize == "Todos" || post.size == selectedSize) &&
                        (selectedGroup == "Todos" || post.group == selectedGroup) &&
                        (searchQuery.isEmpty() || post.name.contains(searchQuery, ignoreCase = true)) &&
                        (minPrice.toIntOrNull()?.let { postPrice >= it } ?: true) &&
                        (maxPrice.toIntOrNull()?.let { postPrice <= it } ?: true)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(filteredPosts) { post ->
                    PostItem(post) {
                        navController.navigate("detailedPost/${post.id}")
                    }
                }
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            selectedColor = selectedColor,
            onColorChange = { selectedColor = it },
            selectedSize = selectedSize,
            onSizeChange = { selectedSize = it },
            selectedGroup = selectedGroup,
            onGroupChange = { selectedGroup = it },
            minPrice = minPrice,
            onMinPriceChange = { minPrice = it },
            maxPrice = maxPrice,
            onMaxPriceChange = { maxPrice = it }
        )
    }
}


