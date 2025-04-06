package gamer.botixone.moodtracker.domain.repository.question

import gamer.botixone.moodtracker.data.local_datasource.model.QuestionEntity
import gamer.botixone.moodtracker.domain.result.QuestionResult

interface QuestionRepository {
    suspend fun createQuestion(questionEntity: QuestionEntity): QuestionResult<QuestionEntity>
    suspend fun fetchQuestionsByStatus(status: Int): QuestionResult<List<QuestionEntity>>
}