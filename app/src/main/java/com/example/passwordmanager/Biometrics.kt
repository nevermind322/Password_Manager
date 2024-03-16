package com.example.passwordmanager

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class Biometrics(val activity: FragmentActivity) {

    val biometricManager = BiometricManager.from(activity)
    val executor = ContextCompat.getMainExecutor(activity)

    fun canAuthenticate() =
        biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS


    private fun getAuthenticationCallback(
        onError: (Int, CharSequence) -> Unit,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onFail: () -> Unit
    ) = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onFail()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onSuccess(result)
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onError(errorCode, errString)
        }
    }

    fun getPrompt(
        onError: (Int, CharSequence) -> Unit,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onFail: () -> Unit
    ) = BiometricPrompt(
        activity,
        executor,
        getAuthenticationCallback(onError = onError, onSuccess = onSuccess, onFail = onFail)
    )

}