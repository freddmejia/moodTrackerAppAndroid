package gamer.botixone.moodtracker.domain.repository.mood

import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.MoodType
import gamer.botixone.moodtracker.domain.model.MoodWtQuestion
import gamer.botixone.moodtracker.domain.result.MoodResult

interface MoodRepository {
    suspend fun createMood(moodEntity: MoodEntity): MoodResult<MoodEntity>
    suspend fun updateMood(intensity: Int, note: String, moodType: MoodType, id: Int): MoodResult<MoodEntity>
    suspend fun fetchAllMoodsBy(status: Int): MoodResult<List<MoodWtQuestion>>
    suspend fun fetchAllMoodsBy(status: Int, limit: Int, offset: Int): MoodResult<List<MoodWtQuestion>>
    suspend fun fetchMoodsOfWeek(status: Int, weekStartTimestamp: Long): MoodResult<List<MoodWtQuestion>>
    suspend fun fetchAllMoodsBy(moodType: MoodType, status: Int): MoodResult<List<MoodWtQuestion>>
    suspend fun deleteMood(status: Int, id: Int): MoodResult<Boolean>
}