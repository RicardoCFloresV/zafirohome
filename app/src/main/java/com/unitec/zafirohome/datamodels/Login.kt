package com.unitec.zafirohome.datamodels

import com.google.gson.annotations.SerializedName

// Petición para enviar al endpoint /auth/login
// En authRouter.js esperas: req.body.login y req.body.password
data class LoginRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)

// Respuesta del servidor
// En authRouter.js devuelves: { success: boolean, message: string, isAdmin: boolean, ... }
data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("isAdmin") val isAdmin: Boolean? = false,
    @SerializedName("isUser") val isUser: Boolean? = false,
    @SerializedName("username") val username: String? = null
)