package com.example.passwordmanager.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.passwordmanager.model.SiteWithPassword
import com.example.passwordmanager.vm.SitesListUIState
import com.example.passwordmanager.vm.SitesListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SitesListScreen(prefs: SharedPreferences, vm: SitesListViewModel = viewModel()) {

    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { vm.getSites(prefs) }
    Column {


        when (state) {
            is SitesListUIState.Error -> Text(text = (state as SitesListUIState.Error).msg)
            SitesListUIState.Loading -> CircularProgressIndicator()
            is SitesListUIState.Success -> LazyColumn {
                items((state as SitesListUIState.Success).data) {
                    Site(it)
                }
            }
        }
    }
}

const val ICON_DOWNLOAD_API_ADDRESS = "https://www.google.com/s2/favicons?domain="

@Composable
fun Site(siteWithPassword: SiteWithPassword) {

    Row {

        AsyncImage(
            model = "$ICON_DOWNLOAD_API_ADDRESS${siteWithPassword.site}&sz=64",
            contentDescription = "site icon",
            modifier = Modifier.size(32.dp)
        )
        Column {
            Text(text = siteWithPassword.site)
            Text(text = siteWithPassword.password)
        }
    }
}