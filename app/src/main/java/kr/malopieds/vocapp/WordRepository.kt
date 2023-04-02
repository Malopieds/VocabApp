package kr.malopieds.vocapp

import kotlinx.coroutines.flow.Flow
import kr.malopieds.vocapp.models.Word

interface WordRepository {
    fun getWord(): Flow<List<Word>>

    suspend fun addWord(word: Word)

    suspend fun updateWord(word: Word)

    suspend fun deleteWord(word: Word)
}