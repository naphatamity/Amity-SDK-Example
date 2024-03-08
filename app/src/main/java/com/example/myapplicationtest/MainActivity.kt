package com.example.myapplicationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.core.session.AccessTokenRenewal
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.session.SessionHandler
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AmityCoreClient.setup(
            apiKey = "apikey",
            AmityEndpoint.US
        ).subscribe()
        observeSessionState()
    }

    private fun observeSessionState() {
        AmityCoreClient.observeSessionState().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnNext { sessionState: SessionState ->
                // SessionState
                updateSessionUi(sessionState)
            }.doOnError {
                // Exception
            }.subscribe()
    }
    private fun updateSessionUi(sessionState: SessionState) {
        Log.e("SessionState", sessionState.toString())
        when (sessionState) {
            is SessionState.NotLoggedIn, is SessionState.Establishing -> {
                Log.e("SessionState", sessionState.toString())
                // openLoginPage()
                createUser("Mark Social")
            }
            is SessionState.Established, is SessionState.TokenExpired -> {
                // openHomePage()
            }
            is SessionState.Terminated -> {
                // openUserBanPage()
            }
        }
    }
    private fun createUser(displayName: String) {
        AmityCoreClient.login(userId = "Amity_Support_001", object : SessionHandler {
            override fun sessionWillRenewAccessToken(renewal: AccessTokenRenewal) {
                renewal.renew()
            }
        }).displayName(displayName = "Amity_Support_001")
            .build().submit().doOnComplete {
                    Log.e("Farrarari","Faseaeas")
            }.doOnError {
                // Exception
                Log.e("Farrarari","ERRORRR")
            }.subscribe()
    }
}
