package com.moviles.clothingapp.viewmodel

import androidx.lifecycle.*
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.repository.PostRepository
import kotlinx.coroutines.launch

/*
*  HomeViewModel:
*   - Fetches the information from the post repository (the one that connects with the API),
*     and sends the information of the products to the Categories and FeaturedProducts views.
*   - Manages loading state to show a progress bar while fetching data.
*/

class HomeViewModel : ViewModel() {

    data class ProductUI(
        val image: String,
        val name: String,
        val brand: String,
        val price: String,
        val color: String,
        val size: String,
        val group: String // Genero: hombre o mujer.
    )

    private val postRepository = PostRepository()

    // LiveData to hold post data
    private val _postData = MutableLiveData<List<PostData>>()
    val postData: LiveData<List<ProductUI>> = _postData.map { posts ->
        posts.map { product ->
            ProductUI(
                image = product.image,
                name = product.name,
                brand = product.brand,
                price = "$${product.price}",
                color = product.color,
                size = product.size,
                group = product.group
            )
        }
    }

    // LiveData to manage loading state (show/hide progress bar)
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Initialize data fetch
    init {
        getPostData()
    }

    // Function to fetch post data asynchronously
    fun getPostData() {
        // Show loading indicator before starting the background task
        _isLoading.value = true

        // Launch the API call in a coroutine
        viewModelScope.launch {
            // Fetch data from repository (this runs in a background thread)
            val postResult = postRepository.fetchRepository()

            // Post result back to UI thread
            _postData.postValue(postResult ?: emptyList())

            // Hide the loading indicator once the data is fetched
            _isLoading.value = false
        }
    }
}
