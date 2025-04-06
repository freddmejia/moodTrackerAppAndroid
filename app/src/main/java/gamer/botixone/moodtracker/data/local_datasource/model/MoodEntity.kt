package gamer.botixone.moodtracker.data.local_datasource.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity class MoodEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long = 0,
    var intensity: Int = 0,
    var note: String = "",
    val questionModel: Int,
    var moodType: MoodType,
    val status: Int = 1
)