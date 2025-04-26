package com.moviles.clothingapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel (): ViewModel() {
    private val repository = PostRepository()

    private val _posts = MutableStateFlow(emptyList<PostData>())
    val posts: StateFlow<List<PostData>> get() = _posts

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> get() = _isConnected

    private val _post = MutableStateFlow<PostData?>(null)
    val post: StateFlow<PostData?> get() = _post

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> get() = _imageUrl

    init {
        fetchPostsFiltered()
    }

    private fun fetchPostsFiltered() {
        viewModelScope.launch {
            try {
                val result = repository.fetchPostsFiltered()
                _posts.value = result ?: emptyList()
            } catch (e: Exception) {
                Log.e("PostViewModel", "Crash fetching posts: ${e.message}")
                _posts.value = emptyList()
            }
        }
    }



    /* Fetch products by category (weather) */
    fun fetchPostsByCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                val result = repository.fetchPostsByCategory(categoryId)
                _posts.value = result ?: emptyList()
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error fetching category $categoryId: ${e.message}")
                _posts.value = emptyList()
            }
        }
    }

    /* Fetch post by ID */
    fun fetchPostById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.fetchPostById(id)
                _post.value = result
                result?.image?.let { fetchImageUrl(it) }

            } catch (e: Exception) {
                Log.e("PostViewModel", "Error fetching post by ID $id: ${e.message}")
                _post.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateConnectionStatus(isConnected: Boolean) {
        _isConnected.value = isConnected
    }

    fun fetchImageUrl(fileId: String) {
        val projectId = "moviles"
        val bucketId = "67ddf3860035ee6bd725"
        val url = "https://cloud.appwrite.io/v1/storage/buckets/$bucketId/files/$fileId/view?project=$projectId"
        _imageUrl.value = url
    }
}

