package gamer.botixone.moodtracker.data.local_datasource.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val status: Int = 1
)