package gamer.botixone.moodtracker.domain.use_case.question

import gamer.botixone.moodtracker.data.local_datasource.model.QuestionEntity
import gamer.botixone.moodtracker.domain.repository.question.QuestionRepository
import gamer.botixone.moodtracker.domain.result.QuestionResult

class CreateQuestionUseCase(
    private val questionRepository: QuestionRepository
) {
    suspend fun execute(questionEntity: QuestionEntity): QuestionResult<QuestionEntity> {
        return questionRepository.createQuestion(
            questionEntity = questionEntity
        )
    }
}