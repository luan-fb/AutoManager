package com.luanferreira.automanager.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObjects
import com.luanferreira.automanager.data.local.Dao.VeiculoDao
import com.luanferreira.automanager.data.model.Veiculo
import com.luanferreira.automanager.di.ApplicationScope
import com.luanferreira.automanager.domain.repository.VeiculoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VeiculoRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val dao: VeiculoDao,
    @ApplicationScope private val externalScope: CoroutineScope // 1. Scope injetado
) : VeiculoRepository {

    // 2. Variável para controlar o listener
    private var listenerRegistration: ListenerRegistration? = null

    override fun listarVeiculos(): Flow<List<Veiculo>> {
        val userId = auth.currentUser?.uid ?: return emptyFlow()

        // Inicia o listener apenas se mudou o usuário ou ainda não existe
        iniciarListenerFirebase(userId)

        return dao.listarPorUsuario(userId)
    }

    override suspend fun salvarVeiculo(veiculo: Veiculo): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("Usuário não logado")
            val id = veiculo.id.ifEmpty { firestore.collection("veiculos").document().id }
            val veiculoFinal = veiculo.copy(id = id, usuarioId = userId)

            // Salva no Firebase
            firestore.collection("veiculos").document(id).set(veiculoFinal).await()

            // O Listener vai atualizar o Room automaticamente,
            // mas salvar manualmente aqui garante update imediato na UI (Optimistic Update)
            dao.salvar(veiculoFinal)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sincronizarVeiculos() {
        // Implementado via listener automático
    }

    // Método novo para limpar recursos (chamar ao fazer logout)
    fun limparSessao() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    private fun iniciarListenerFirebase(userId: String) {
        listenerRegistration?.remove()

        listenerRegistration = firestore.collection("veiculos")
            .whereEqualTo("usuarioId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("VeiculoRepo", "Erro ao ouvir mudanças no Firebase", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val veiculos = snapshot.toObjects<Veiculo>()

                    // 3. Usando o escopo seguro injetado
                    externalScope.launch {
                        dao.salvarTodos(veiculos)
                    }
                }
            }
    }
}