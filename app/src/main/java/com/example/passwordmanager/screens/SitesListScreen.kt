package com.example.passwordmanager.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordmanager.vm.SitesListUIState
import com.example.passwordmanager.vm.SitesListViewModel

@Composable
fun SitesListScreen(prefs: SharedPreferences, vm: SitesListViewModel = viewModel()) {

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.getSites(prefs) }

    when (state) {
        is SitesListUIState.Error -> Text(text = (state as SitesListUIState.Error).msg)
        SitesListUIState.Loading -> CircularProgressIndicator()
        is SitesListUIState.Success -> LazyColumn {
            items((state as SitesListUIState.Success).data) {
                Column {
                    Text(text = it.site)
                    Text(text = it.password)
                }
            }
        }
    }


}