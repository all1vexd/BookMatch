package ru.itis.bookmatch.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.itis.bookmatch.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): ProfileRepository {

    override suspend fun getUserNickname(userId: String): String {
        return try {
            firestore.collection("users")
                .document(userId)
                .get()
                .await()
                .getString("nickname") ?: "User"
        } catch (e: Exception) {
            "User"
        }
    }

    override suspend fun updateNickname(userId: String, nickname: String) {
        firestore.collection("users")
            .document(userId)
            .set(mapOf("nickname" to nickname))
            .await()
    }
}