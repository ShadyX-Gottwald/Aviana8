package student.projects.aviana8.Services

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleSignInHelper(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

//    fun getGoogleSignInClient(): GoogleSignInClient {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getWebClientId())
//            .requestEmail()
//            .build()
//
//        return GoogleSignIn.getClient(context, gso)
//    }

    private fun getWebClientId(): String {
        // Replace this with your actual web client ID from google-services.json
        // You can find it in google-services.json under:
        // client[0].oauth_client[0].client_id
        return "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
    }

//    suspend fun signInWithGoogle(data: Intent?): Result<FirebaseUser> {
//        return try {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            val account = task.getResult(ApiException::class.java)
//                ?: throw Exception("Google Sign-In failed: Account is null")
//
//            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//            val authResult = auth.signInWithCredential(credential).await()
//
//            val user = authResult.user
//            if (user != null) {
//                Result.success(user)
//            } else {
//                Result.failure(Exception("Firebase authentication failed"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        } as Result<FirebaseUser>
//    }
//
//    fun signOut() {
//        auth.signOut()
//        getGoogleSignInClient().signOut()
//    }
//
//    fun getCurrentUser() = auth.currentUser
//    fun isUserLoggedIn() = auth.currentUser != null
}