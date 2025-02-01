package org.example.project.storage


actual class TokenStorage actual constructor() {
    private var token: String? = null
    private var email: String? = null

    actual fun saveToken(token: String) {
        this.token = token
    }

    actual fun getToken(): String? {
        return token
    }

    actual fun clearToken() {
        token = null
    }

    actual fun saveEmail(email: String) {
        this.email = email
    }

    actual fun getEmail(): String? {
        return email
    }

    actual fun clearEmail() {
        email = null
    }
}