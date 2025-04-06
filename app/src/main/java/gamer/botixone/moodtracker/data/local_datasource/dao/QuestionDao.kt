package gamer.botixone.moodtracker.data.local_datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.QuestionEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createQuestion(questionEntity: QuestionEntity): Long

    @Query("select * from questionentity where status = :status")
    fun fetchQuestionsByStatus(status: Int): List<QuestionEntity>

    @Query("select * from questionentity where id = :id")
    fun fetchQuestionBy(id: Int): QuestionEntity?
}