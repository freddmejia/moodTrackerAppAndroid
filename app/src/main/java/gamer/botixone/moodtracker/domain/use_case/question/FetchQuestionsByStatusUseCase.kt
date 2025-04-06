package gamer.botixone.moodtracker.domain.use_case.question

import gamer.botixone.moodtracker.data.local_datasource.model.QuestionEntity
import gamer.botixone.moodtracker.domain.repository.question.QuestionRepository
import gamer.botixone.moodtracker.domain.result.QuestionResult

class FetchQuestionsByStatusUseCase(
    private val questionRepository: QuestionRepository
) {
    suspend fun execute(status: Int): QuestionResult<List<QuestionEntity>> {
        return questionRepository.fetchQuestionsByStatus(
            status = status
        )
    }

}