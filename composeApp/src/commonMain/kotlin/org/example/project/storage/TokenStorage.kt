package org.example.project.storage

expect class TokenStorage() {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
    fun saveEmail(email: String)
    fun getEmail(): String?
    fun clearEmail()
}
