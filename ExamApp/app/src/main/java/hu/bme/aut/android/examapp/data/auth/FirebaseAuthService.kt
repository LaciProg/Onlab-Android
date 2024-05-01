package hu.bme.aut.android.examapp.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.examapp.api.ExamAppApiService
import hu.bme.aut.android.examapp.api.dto.UserDto
import hu.bme.aut.android.examapp.api.dto.UserFireBase
import hu.bme.aut.android.examapp.data.repositories.inrefaces.UserRepository
import hu.bme.aut.android.examapp.domain.usecases.AuthenticateUseCase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val retrofitService: ExamAppApiService,
    private val authenticateUseCase: AuthenticateUseCase
) : AuthService {
    override val currentUserId: String? get() = firebaseAuth.currentUser?.uid
    override val hasUser: Boolean get() = firebaseAuth.currentUser != null
    override val currentUser: Flow<UserFireBase?> get() = callbackFlow {
        this.trySend(currentUserId?.let { UserFireBase(it) })
        val listener =
            FirebaseAuth.AuthStateListener { auth ->
                this.trySend(auth.currentUser?.let { UserFireBase(it.uid) })
            }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }


    override suspend fun signUp(email: String, password: String) = suspendCoroutine { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {  result ->
                val user = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(user?.email?.substringBefore('@'))
                    .build()
                user?.updateProfile(profileChangeRequest)
                continuation.resume(Unit)
            }
            .addOnFailureListener { continuation.resumeWithException(it) }

    }

    override suspend fun registerInApi(email: String, password: String) {
        try {
            val user = retrofitService.postUser(
                UserDto(
                    name = email,
                    password = password
                )
            )

            if (user != null) {
                userRepository.insertUser(user)
            } else {
                throw Exception("User not created")
            }
        } catch(e: IOException){
            throw Exception("Network error")
        } catch(e: HttpException){
            throw Exception("Server error")
        }

    }

    override suspend fun authenticate(email: String, password: String) = suspendCoroutine { continuation ->
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { continuation.resume(Unit) }
            .addOnFailureListener { continuation.resumeWithException(it) }

    }

    override suspend fun authenticateInApi(email: String, password: String) {
        try {
            var user : UserDto?
            runBlocking {
                try{
                    user = retrofitService.getUserByName(email)
                } catch(e: IOException){
                    throw Exception("Network error")
                } catch(e: HttpException){
                    throw Exception("Server error")
                }
                authenticateUseCase(user!!)
            }
            if (user != null ){
                val userInDB = userRepository.getUserByUid(user!!.uuid)
                if(userInDB == null) {
                    userRepository.insertUser(user!!)
                } else if(userInDB.password != user!!.password || userInDB.name != user!!.name){
                    userRepository.updateUser(user!!)
                }
            } else {
                throw Exception("User not found")
            }
        } catch(e: IOException){
            throw Exception("Network error")
        } catch(e: HttpException){
            throw Exception("Server error")
        }
    }

    override suspend fun sendRecoveryEmail(email: String)  = suspendCoroutine { continuation ->
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener { continuation.resume(Unit) }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    override suspend fun deleteAccount() = suspendCoroutine { continuation ->
        firebaseAuth.currentUser!!.delete()
            .addOnSuccessListener { continuation.resume(Unit) }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun getCurrentUser(): UserDto? {
        try {
            return retrofitService.getUserByName(firebaseAuth.currentUser?.email!!)
        } catch(e: IOException){
            throw Exception("Network error")
        } catch(e: HttpException){
            throw Exception("Server error")
        }
    }
}