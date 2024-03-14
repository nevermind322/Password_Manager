package com.example.passwordmanager

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordmanager.screens.AddSiteScreen
import com.example.passwordmanager.screens.LoginScreen
import com.example.passwordmanager.screens.SignupScreen
import com.example.passwordmanager.screens.SitesListScreen
import com.example.passwordmanager.ui.theme.PasswordManagerTheme
import com.example.passwordmanager.vm.PASSWORD_HASH_KEY

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = CryptoManager.getSharedPrefs(applicationContext)

        setContent {
            PasswordManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    var hash by remember {
                        mutableStateOf(
                            prefs.getString(
                                PASSWORD_HASH_KEY,
                                null
                            )
                        )
                    }
                    if (hash == null)
                        SignupScreen(
                            prefs = prefs,
                            onSignup = { hash = prefs.getString(PASSWORD_HASH_KEY, null) })
                    else
                        Scaffold(
                            topBar = { AppBar(navController = navController) },
                            floatingActionButton = {
                                FloatingActionButton(onClick = {
                                    navController.navigate(
                                        ADD_SITE_ROUTE
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "add site"
                                    )
                                }
                            }
                        ) {

                            MainNavHost(
                                navController = navController,
                                preferences = prefs,
                                hash = hash!!,
                                modifier = Modifier.padding(it)
                            )
                        }
                }
            }
        }
    }
}


const val LOGIN_SCREEN_ROUTE = "login"
const val SITES_LIST_ROUTE = "sites_list"
const val ADD_SITE_ROUTE = "add_site"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController) {
    TopAppBar(title = { Text(text = "Password Manager") }, navigationIcon = {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "return back",
            modifier = Modifier.clickable { navController.navigateUp() }
        )
    })
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    preferences: SharedPreferences,
    hash: String,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LOGIN_SCREEN_ROUTE,
        modifier = modifier
    ) {
        composable(ADD_SITE_ROUTE) {
            AddSiteScreen(prefs = preferences)
        }
        composable(SITES_LIST_ROUTE) {
            SitesListScreen(prefs = preferences)
        }
        composable(LOGIN_SCREEN_ROUTE) {
            LoginScreen(hash = hash, onLogin = { navController.navigate(SITES_LIST_ROUTE) })
        }
    }
}

