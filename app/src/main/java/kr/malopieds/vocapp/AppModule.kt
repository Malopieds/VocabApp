package kr.malopieds.vocapp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideWordDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        WordsDb::class.java,
        "Word"
    ).build()

    @Provides
    fun provideWordDao(
        wordsDb: WordsDb
    ) = wordsDb.wordDao()

    @Provides
    fun provideWordRepository(
        WordDao: WordDao
    ): WordRepository = WordRepositoryImpl(
        wordDao = WordDao
    )
}