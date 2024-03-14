package com.example.passwordmanager.vm

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.CryptoManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

const val PASSWORD_HASH_KEY = "password_hash"

class SignupViewModel : ViewModel() {

    val state = MutableStateFlow<SignupUIState>(SignupUIState.Start)

    private fun validate(password: String) = (password.length in (8..32))
            && password.contains(Regex("[0-9]"))
            && password.contains(Regex("[A-Z]"))
            && password.contains(Regex("[a-z]"))
            && password.contains(Regex("[_&?%*]"))


    fun createMasterPassword(password: String, prefs: SharedPreferences) {

        viewModelScope.launch {
            state.value = SignupUIState.Start
            if (validate(password)) {
                try {
                    state.value = SignupUIState.Loading
                    val hash = CryptoManager.sha256(password.encodeToByteArray())
                    prefs.edit {
                        putString(PASSWORD_HASH_KEY, hash.decodeToString())
                    }

                    state.value = SignupUIState.Success
                } catch (e: Exception) {
                    Log.e("prefs", e.message ?: "unknown error")
                }
            } else
                state.value = SignupUIState.PasswordNotValid
        }

    }

    fun notValidPasswordMessageShown() {
        viewModelScope.launch {
            state.value = SignupUIState.Start
        }
    }
}


sealed class SignupUIState {
    data object Start : SignupUIState()
    data object Loading : SignupUIState()
    data object PasswordNotValid : SignupUIState()
    data object Success : SignupUIState()
}