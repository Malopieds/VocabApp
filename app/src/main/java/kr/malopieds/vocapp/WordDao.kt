package kr.malopieds.vocapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.malopieds.vocapp.models.Word

@Dao
interface WordDao {
    @Query("SELECT * FROM Word")
    fun getWords(): Flow<List<Word>>

    @Insert(onConflict = REPLACE)
    fun addWord(word: Word)

    @Update
    fun updateWord(word: Word)

    @Delete
    fun deleteWord(word: Word)
}