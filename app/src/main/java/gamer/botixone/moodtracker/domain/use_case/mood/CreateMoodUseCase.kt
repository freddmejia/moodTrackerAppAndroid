package gamer.botixone.moodtracker.domain.use_case.mood

import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.domain.repository.mood.MoodRepository
import gamer.botixone.moodtracker.domain.result.MoodResult

class CreateMoodUseCase(
    private val moodRepository: MoodRepository
) {
    suspend fun execute(moodEntity: MoodEntity): MoodResult<MoodEntity> {
        return moodRepository.createMood(
            moodEntity = moodEntity
        )
    }
}