package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class,
        FeedbackEntity::class,
        UserPreferencesEntity::class,
        DocumentEntity::class,
        LanguageSettingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SabiDatabase : RoomDatabase() {

    abstract fun sabiDao(): SabiDao

    companion object {
        @Volatile
        private var INSTANCE: SabiDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SabiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SabiDatabase::class.java,
                    "sabi_ai_database"
                )
                    .addCallback(SabiDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class SabiDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.sabiDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        ensureKnowledgeSeeded(database.sabiDao())
                    }
                }
            }

            private suspend fun ensureKnowledgeSeeded(dao: SabiDao) {
                val count = dao.getDocumentsCount()
                if (count < com.example.data.context.NigerianKnowledgeData.allDocuments.size) {
                    dao.insertDocuments(com.example.data.context.NigerianKnowledgeData.allDocuments)
                }
            }

            private suspend fun populateInitialData(dao: SabiDao) {
                // Initial User Preference
                dao.saveUserPreferences(
                    UserPreferencesEntity(
                        id = "default_user",
                        displayName = "Nigerian Innovator",
                        email = "jumchwerandy@gmail.com",
                        defaultLanguage = "english",
                        defaultMode = "casual",
                        isGoogleLinked = true
                    )
                )

                // Initial Welcome Conversation
                val welcomeConvId = "welcome_conv_01"
                dao.insertConversation(
                    ConversationEntity(
                        id = welcomeConvId,
                        title = "Welcome to SABI AI 🇳🇬",
                        createdAt = System.currentTimeMillis() - 60000,
                        updatedAt = System.currentTimeMillis(),
                        lastMessage = "You fit ask me anything in Pidgin, English, or mix both!",
                        language = "pidgin",
                        mode = "casual",
                        isPinned = true
                    )
                )

                // Welcome messages
                dao.insertMessage(
                    MessageEntity(
                        id = UUID.randomUUID().toString(),
                        conversationId = welcomeConvId,
                        role = "user",
                        content = "How far SABI? Wetin you fit do for me?",
                        timestamp = System.currentTimeMillis() - 50000,
                        language = "pidgin",
                        mode = "casual"
                    )
                )

                dao.insertMessage(
                    MessageEntity(
                        id = UUID.randomUUID().toString(),
                        conversationId = welcomeConvId,
                        role = "assistant",
                        content = """I dey hail! I be SABI AI—the AI wey understand you, your words, and your everyday reality. 🇳🇬

Here wetin I fit do for you:
1. **Chat Naturally**: Speak Pidgin, Nigerian English, or mix dem together. No need to form grammar if you no want.
2. **Writing & Business**: Help you polish business proposals, write formal letters to Nigerian employers or clients, and draft polite replies to landlords.
3. **School & Academics**: Break down JAMB, WAEC, or university topics simply without long grammar.
4. **Cultural & Context Engine**: Explain local expressions, decode slang (from *Sapa* to *Japa*), and guide you on Nigerian situations.

Abeg feel free to ask me anything or tap any of the prompts below!""",
                        timestamp = System.currentTimeMillis() - 30000,
                        language = "pidgin",
                        mode = "casual",
                        feedbackState = 1
                    )
                )

                // Seed Complete Knowledge Base Documents (Nigerian Context Engine)
                dao.insertDocuments(com.example.data.context.NigerianKnowledgeData.allDocuments)

                // Language settings
                val languages = listOf(
                    LanguageSettingEntity("english", "English", true, "Fully active"),
                    LanguageSettingEntity("pidgin", "Nigerian Pidgin", true, "Fully active"),
                    LanguageSettingEntity("igbo", "Igbo (Asụsụ Igbo)", false, "Coming Soon - Model Training"),
                    LanguageSettingEntity("yoruba", "Yoruba (Èdè Yorùbá)", false, "Coming Soon - Model Training"),
                    LanguageSettingEntity("hausa", "Hausa (Harshen Hausa)", false, "Coming Soon - Model Training")
                )
                dao.insertLanguageSettings(languages)
            }
        }
    }
}
