package kr.malopieds.vocapp

import kotlinx.coroutines.flow.Flow
import kr.malopieds.vocapp.models.Word

class WordRepositoryImpl(
    private val wordDao: WordDao
): WordRepository {
    override fun getWord(): Flow<List<Word>> {
        return wordDao.getWords()
    }

    override suspend fun addWord(word: Word) {
        return wordDao.addWord(word)
    }

    override suspend fun updateWord(word: Word) {
        return wordDao.updateWord(word)
    }

    override suspend fun deleteWord(word: Word) {
        return wordDao.deleteWord(word)
    }
}