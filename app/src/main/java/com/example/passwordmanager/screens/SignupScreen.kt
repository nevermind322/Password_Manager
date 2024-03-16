package com.example.passwordmanager.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordmanager.VisibilityIcon
import com.example.passwordmanager.VisibilityOffIcon
import com.example.passwordmanager.passwordKeyboardOptions
import com.example.passwordmanager.passwordRules
import com.example.passwordmanager.vm.SignupUIState
import com.example.passwordmanager.vm.SignupViewModel

@Composable
fun SignupScreen(
    onSignup: () -> Unit,
    vm: SignupViewModel = viewModel()
) {
    Icons.Default.Face
    var password by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current.applicationContext
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is SignupUIState.PasswordNotValid) {
            Toast.makeText(context, "Password is not valid", Toast.LENGTH_SHORT).show()
            vm.notValidPasswordMessageShown()
        } else if (state is SignupUIState.Success) {
            Toast.makeText(context, "Successfully created password", Toast.LENGTH_LONG).show()
            onSignup()
        }
    }
    Box(contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                PasswordField(password = password, onPasswordChange = { password = it })
                Button(onClick = { vm.createMasterPassword(password, context) }) {
                    Text("Create password")
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            PasswordRules()
        }
    }
}

@Composable
fun PasswordRules() {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = Icons.Default.Info, contentDescription = null)
            Text(text = "Password rules")
        }

        for (rule in passwordRules)
            Text(text = "- $rule")

    }
}