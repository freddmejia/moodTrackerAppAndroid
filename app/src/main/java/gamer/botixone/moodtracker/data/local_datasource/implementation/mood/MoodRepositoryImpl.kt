package gamer.botixone.moodtracker.data.local_datasource.implementation.mood

import gamer.botixone.moodtracker.data.local_datasource.dao.MoodDao
import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.MoodType
import gamer.botixone.moodtracker.domain.model.MoodWtQuestion
import gamer.botixone.moodtracker.domain.repository.mood.MoodRepository
import gamer.botixone.moodtracker.domain.result.MoodResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoodRepositoryImpl @Inject constructor(
    private val moodDao: MoodDao
): MoodRepository {
    override suspend fun createMood(moodEntity: MoodEntity): MoodResult<MoodEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val id = moodDao.createMood(moodEntity).toInt()
                val newMood = moodDao.fetchMoodBy(id = id)
                MoodResult.Success(newMood!!)
            }catch (e: Exception){
                MoodResult.Error(e)
            }
        }
    }

    override suspend fun updateMood(intensity: Int, note: String, moodType: MoodType, id: Int): MoodResult<MoodEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val mood = moodDao.fetchMoodBy(id = id)
                moodDao.updateMood(
                    intensity = intensity,
                    note = note,
                    moodType = moodType,
                    id = id
                )
                mood?.intensity = intensity
                mood?.note = note
                mood?.moodType = moodType
                MoodResult.Success(mood!!)
            }
            catch (e: Exception){
                MoodResult.Error(e)
            }
        }
    }

    override suspend fun fetchAllMoodsBy(status: Int): MoodResult<List<MoodWtQuestion>> {
        return withContext(Dispatchers.IO) {
            try {
                val moods = moodDao.fetchAllMoodsBy(status = status)
                val filteredMoods = moods.filter { it.question?.status == 1 }
                MoodResult.Success(filteredMoods)
            }
            catch (e: Exception){
                MoodResult.Error(e)
            }
        }
    }

    override suspend fun fetchAllMoodsBy(
        status: Int,
        limit: Int,
        offset: Int
    ): MoodResult<List<MoodWtQuestion>> {
        return withContext(Dispatchers.IO) {
            try {
                val moods = moodDao.fetchAllMoodsBy(
                    status = status,
                    limit = limit,
                    offset = offset
                )
                val filteredMoods = moods.filter { it.question?.status == 1 }
                MoodResult.Success(filteredMoods)
            }
            catch (e: Exception){
                MoodResult.Error(e)
            }
        }
    }

    override suspend fun fetchAllMoodsBy(moodType: MoodType, status: Int): MoodResult<List<MoodWtQuestion>> {
        return withContext(Dispatchers.IO) {
            try {
                val moods = moodDao.fetchAllMoodsBy(
                    status = status,
                    moodType = moodType
                )
                val filteredMoods = moods.filter { it.question?.status == 1 }
                MoodResult.Success(filteredMoods)
            }
            catch (e: Exception){
                MoodResult.Error(e)
            }
        }
    }

    override suspend fun fetchMoodsOfWeek(
        status: Int,
        weekStartTimestamp: Long
    ): MoodResult<List<MoodWtQuestion>> {
        return withContext(Dispatchers.IO) {
            try {
                val moods = moodDao.fetchMoodsOfWeek(
                    status = status,
                    weekStartTimestamp = weekStartTimestamp
                )
                val filteredMoods = moods.filter { it.question?.status == 1 }
                MoodResult.Success(filteredMoods)
            }
            catch (e: Exception){
                MoodResult.Error(e)
            }
        }
    }

    override suspend fun deleteMood(status: Int, id: Int): MoodResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                moodDao.deleteMood(
                    status = status,
                    id = id
                )
                MoodResult.Success(true)
            }
            catch (e: Exception){
                MoodResult.Error(e)
            }
        }
    }
}