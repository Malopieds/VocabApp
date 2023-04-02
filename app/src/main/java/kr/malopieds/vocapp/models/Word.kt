package kr.malopieds.vocapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Word")
data class Word (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val word: String? = null,
    val translation: String? = null,
    val desc: String? = null
)