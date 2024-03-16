package com.example.passwordmanager.vm

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.model.SiteWithPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SitesListViewModel : ViewModel() {

    val state = MutableStateFlow<SitesListUIState>(SitesListUIState.Loading)

    fun getSites(preferences: SharedPreferences) {
        viewModelScope.launch {
            state.value = try {
                val data = preferences.all.map {
                    SiteWithPassword(
                        site = it.key, password = it.value.toString()
                    )
                }
                SitesListUIState.Success(data)
            } catch (e: Exception) {
                SitesListUIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteSite(site: String, preferences: SharedPreferences) {
        viewModelScope.launch {
            preferences.edit {
                remove(site)
            }
            getSites(preferences)
        }
    }
}


sealed class SitesListUIState {
    data object Loading : SitesListUIState()
    data class Success(val data: List<SiteWithPassword>) : SitesListUIState()
    data class Error(val msg: String) : SitesListUIState()
}