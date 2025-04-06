package gamer.botixone.moodtracker.data.local_datasource.implementation.question

import gamer.botixone.moodtracker.data.local_datasource.dao.QuestionDao
import gamer.botixone.moodtracker.data.local_datasource.model.QuestionEntity
import gamer.botixone.moodtracker.domain.repository.question.QuestionRepository
import gamer.botixone.moodtracker.domain.result.QuestionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao
): QuestionRepository {
    override suspend fun createQuestion(questionEntity: QuestionEntity): QuestionResult<QuestionEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val id = questionDao.createQuestion(questionEntity = questionEntity).toInt()
                val mewQuestion = questionDao.fetchQuestionBy(id = id)
                QuestionResult.Success(mewQuestion!!)
            }
            catch (e: Exception){
                QuestionResult.Error(e)
            }
        }
    }

    override suspend fun fetchQuestionsByStatus(status: Int): QuestionResult<List<QuestionEntity>> {
        return withContext(Dispatchers.IO){
            try {
                val questions = questionDao.fetchQuestionsByStatus(status = status)
                QuestionResult.Success(questions)
            }
            catch(e: Exception){
                QuestionResult.Error(e)
            }
        }
    }
}