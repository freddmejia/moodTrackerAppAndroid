package gamer.botixone.moodtracker.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.QuestionEntity

data class MoodWtQuestion (
    @Embedded val mood: MoodEntity,
    @Relation(
        parentColumn = "questionModel",
        entityColumn = "id"
    )
    val question: QuestionEntity?
)

