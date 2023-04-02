package kr.malopieds.vocapp

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.malopieds.vocapp.models.Word


@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordsDb: RoomDatabase() {
    abstract fun wordDao(): WordDao
}