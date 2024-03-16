package com.example.passwordmanager.screens

import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.passwordmanager.ICON_DOWNLOAD_API_ADDRESS
import com.example.passwordmanager.model.SiteWithPassword
import com.example.passwordmanager.vm.SitesListUIState
import com.example.passwordmanager.vm.SitesListViewModel

@Composable
fun SitesListScreen(
    prefs: SharedPreferences,
    onEdit: (String) -> Unit,
    vm: SitesListViewModel = viewModel()
) {

    val state by vm.state.collectAsState()
    LaunchedEffect(Unit) { vm.getSites(prefs) }
    Column {
        when (state) {
            is SitesListUIState.Error -> Text(text = (state as SitesListUIState.Error).msg)
            SitesListUIState.Loading -> CircularProgressIndicator()
            is SitesListUIState.Success -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items((state as SitesListUIState.Success).data) {
                    Site(
                        it,
                        onEdit = { onEdit(it.site) },
                        onDelete = { vm.deleteSite(it.site, prefs) })
                }
            }
        }
    }
}

@Composable
fun Site(siteWithPassword: SiteWithPassword, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = "$ICON_DOWNLOAD_API_ADDRESS${siteWithPassword.site}&sz=64",
            contentDescription = "site icon",
            modifier = Modifier.size(32.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Column {
                Text(text = siteWithPassword.site)
                Text(text = siteWithPassword.password)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "",
                    Modifier.clickable { onEdit() })
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    Modifier.clickable { onDelete() })
            }
        }
    }
}