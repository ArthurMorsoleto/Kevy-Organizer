package com.amb.kevyorganizer.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.amb.core.usecase.UpdateProductPhotoUseCase
import com.amb.kevyorganizer.data.di.AppModule
import com.amb.kevyorganizer.data.di.components.DaggerViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CameraFragmentViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var updateProductPhotoUseCase: UpdateProductPhotoUseCase

    init {
        DaggerViewModelComponent.builder()
            .appModule(AppModule(getApplication()))
            .build()
            .inject(this)
    }

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun updateProductPhoto(id: Long, filePath: String) {
        coroutineScope.launch {
            updateProductPhotoUseCase(id, filePath)
        }
    }

}