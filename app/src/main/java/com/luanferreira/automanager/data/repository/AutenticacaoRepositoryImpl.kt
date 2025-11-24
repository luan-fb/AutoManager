package com.luanferreira.automanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.luanferreira.automanager.domain.repository.AutenticacaoRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AutenticacaoRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AutenticacaoRepository
{
    override val usuarioAtual: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun login(email: String, senha: String): Flow<Result<Boolean>> = flow {
        try {
            auth.signInWithEmailAndPassword(email, senha).await()
            emit(Result.success(true))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun cadastrar(nome: String, email: String, senha: String): Flow<Result<Boolean>> = flow {
        try {
            val resultado = auth.createUserWithEmailAndPassword(email, senha).await()
            val user = resultado.user

            if (user != null && nome.isNotBlank()) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build()
                user.updateProfile(profileUpdates).await()
            }

            emit(Result.success(true))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }
}