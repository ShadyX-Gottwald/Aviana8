package student.projects.aviana8.Services

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import student.projects.aviana8.Viewmodels.LoginUserDTO


interface ILoginRegister {
    fun signUpToFirebase(email: String, password: String): Task<AuthResult>
    fun loginToFirebase(user: LoginUserDTO): Task<AuthResult>
}
class LoginRegisterImpl(
    //private val store: FirebaseFirestore ,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

): ILoginRegister {
    // @SuppressLint("SuspiciousIndentation")
    override fun signUpToFirebase(
        email: String,
        password: String
    ): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    override fun loginToFirebase(user: LoginUserDTO): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(user.email, user.password)
    }

}