package com.moviles.clothingapp.model.cache

import android.util.LruCache
import com.moviles.clothingapp.model.PostData

object ProductCache {
    private val cacheSize = 20 // Número máximo de productos en caché
    private val lruCache = LruCache<String, PostData>(cacheSize)

    fun put(product: PostData) {
        lruCache.put(product.id, product)
    }

    fun get(productId: String): PostData? {
        return lruCache.get(productId)
    }

    fun getAll(): List<PostData> {
        return lruCache.snapshot().values.toList()
    }

    fun clear() {
        lruCache.evictAll()
    }
}

