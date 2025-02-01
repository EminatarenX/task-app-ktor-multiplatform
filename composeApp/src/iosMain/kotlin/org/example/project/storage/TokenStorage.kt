package org.example.project.storage

// TokenStorage.kt (implementación iOS)
import platform.Foundation.NSUserDefaults

actual class TokenStorage actual constructor() {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual fun saveToken(token: String) {
        userDefaults.setObject(token, forKey = "auth_token")
    }

    actual fun getToken(): String? {
        return userDefaults.stringForKey("auth_token")
    }

    actual fun clearToken() {
        userDefaults.removeObjectForKey("auth_token")
    }

    actual fun saveEmail(email: String) {
        userDefaults.setObject(email, forKey = "user_email")
    }

    actual fun getEmail(): String? {
        return userDefaults.stringForKey("user_email")
    }

    actual fun clearEmail() {
        userDefaults.removeObjectForKey("user_email")
    }
}