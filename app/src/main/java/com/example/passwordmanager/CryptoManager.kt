package com.example.passwordmanager

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyProperties
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.passwordmanager.vm.PASSWORD_HASH_KEY
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

private const val PREFS_FILENAME = "prefs"
private const val HASH_FILENAME = "hash_prefs"


object CryptoManager {

    private val keyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    fun getSharedPrefs(context: Context): SharedPreferences = EncryptedSharedPreferences.create(
        PREFS_FILENAME,
        keyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    fun sha256(byteArray: ByteArray): ByteArray {
        val digest = try {
            MessageDigest.getInstance("SHA-256")
        } catch (e: NoSuchAlgorithmException) {
            MessageDigest.getInstance("SHA")
        }

        return with(digest) {
            update(byteArray)
            digest()
        }
    }

    fun getPasswordHash(context: Context): String? {
        val prefs = context.getSharedPreferences(HASH_FILENAME, Context.MODE_PRIVATE)
        return prefs.getString(PASSWORD_HASH_KEY, null)
    }

    fun createPasswordHash(context: Context, password: String) {
        val prefs = context.getSharedPreferences(HASH_FILENAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(PASSWORD_HASH_KEY, sha256(password.encodeToByteArray()).decodeToString())
        }
    }
}