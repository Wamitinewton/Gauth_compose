package com.example.gauth.presentation.sign_in

import android.content.Context
import android.content.IntentSender
import com.example.gauth.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {

    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        var results = try {
            oneTapClient.beginSignIn(
                // call the build request here.......
                buildSigninRequest()
            ).await()
        } catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return results?.pendingIntent?.intentSender
    }

    private fun buildSigninRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}