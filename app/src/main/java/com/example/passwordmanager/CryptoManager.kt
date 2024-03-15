package com.example.passwordmanager

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.passwordmanager.vm.PASSWORD_HASH_KEY
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

private const val PREFS_FILENAME = "prefs"
private const val HASH_FILENAME = "hash_prefs"


object CryptoManager {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

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


    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"


}