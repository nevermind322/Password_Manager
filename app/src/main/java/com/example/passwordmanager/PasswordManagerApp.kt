package com.example.passwordmanager

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache

class PasswordManagerApp : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this).diskCache {
            DiskCache.Builder()
                .directory(cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()        }
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .fallback(R.drawable.placeholder)
            .build()
}