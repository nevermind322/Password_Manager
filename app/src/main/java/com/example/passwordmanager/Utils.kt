package com.example.passwordmanager

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

const val ICON_DOWNLOAD_API_ADDRESS = "https://www.google.com/s2/favicons?domain="
fun getHashString(message: String): String =
    CryptoManager.sha256(message.encodeToByteArray()).decodeToString()

val passwordKeyboardOptions
    get() = KeyboardOptions(
        capitalization = KeyboardCapitalization.None,
        autoCorrect = false,
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password
    )


const val PASSWORD_SPECIAL_SYMBOLS = "_&?%*"

val passwordRules = listOf(
    "be at least 8 symbols and at most 32 symbols long",
    "have at least 1 digit",
    "have at least 1 lowercase letter",
    "have at least 1 uppercase letter",
    "have at least 1 special symbol($PASSWORD_SPECIAL_SYMBOLS)",
    "no other symbols are allowed"
)

val siteKeyboardOptions
    get() = KeyboardOptions(
        capitalization = KeyboardCapitalization.None,
        autoCorrect = false,
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Uri
    )

val VisibilityIcon
    @Composable get() = Icon(
        imageVector = ImageVector.vectorResource(R.drawable.baseline_visibility_24),
        contentDescription = ""
    )

val VisibilityOffIcon
    @Composable get() = Icon(
        imageVector = ImageVector.vectorResource(R.drawable.baseline_visibility_off_24),
        contentDescription = "password visibility"
    )