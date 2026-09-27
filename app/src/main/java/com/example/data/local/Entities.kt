package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "conversations",
    indices = [Index(value = ["updatedAt"])]
)
data class ConversationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastMessage: String = "",
    val language: String = "english",
    val mode: String = "casual",
    val isPinned: Boolean = false
)

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["conversationId"]), Index(value = ["timestamp"])]
)
data class MessageEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val conversationId: String,
    val role: String, // "user" or "assistant"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val language: String = "english",
    val mode: String = "casual",
    val feedbackState: Int = 0, // 0 = none, 1 = helpful, -1 = unhelpful
    val feedbackCategory: String? = null,
    val feedbackComment: String? = null
)

@Entity(tableName = "feedback")
data class FeedbackEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val messageId: String,
    val conversationId: String,
    val userQuery: String,
    val aiResponse: String,
    val category: String,
    val comment: String,
    val language: String,
    val mode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val reviewed: Boolean = false
)

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: String = "default_user",
    val displayName: String = "Nigerian Innovator",
    val email: String = "jumchwerandy@gmail.com",
    val defaultLanguage: String = "english",
    val defaultMode: String = "casual",
    val isGoogleLinked: Boolean = true
)

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String, // History, Culture, Business, Education, Slang, Geography
    val content: String,
    val keywords: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "language_settings")
data class LanguageSettingEntity(
    @PrimaryKey val languageCode: String,
    val displayName: String,
    val isSupported: Boolean,
    val statusMessage: String
)
