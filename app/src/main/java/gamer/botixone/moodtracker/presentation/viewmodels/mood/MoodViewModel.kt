package gamer.botixone.moodtracker.presentation.viewmodels.mood

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.MoodType
import gamer.botixone.moodtracker.domain.model.MoodWtQuestion
import gamer.botixone.moodtracker.domain.result.CreateMoodUIState
import gamer.botixone.moodtracker.domain.result.MoodResult
import gamer.botixone.moodtracker.domain.use_case.mood.CreateMoodUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.DeleteMoodUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.GetAllModsOfWeekUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.GetAllMoodsByMoodTypeStatusUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.GetAllMoodsByStatusLimitOffsetUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.GetAllMoodsByStatusUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.UpdateMoodUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MoodViewModel @Inject constructor(
    private val createMoodUseCase: CreateMoodUseCase,
    private val updateMoodUseCase: UpdateMoodUseCase,
    private val deleteMoodUseCase: DeleteMoodUseCase,
    private val getAllMoodsByMoodTypeStatusUseCase: GetAllMoodsByMoodTypeStatusUseCase,
    private val getAllMoodsByStatusUseCase: GetAllMoodsByStatusUseCase,
    private val getAllModsOfWeekUseCase: GetAllModsOfWeekUseCase,
    private val getAllMoodsByStatusLimitOffsetUseCase: GetAllMoodsByStatusLimitOffsetUseCase
) : ViewModel() {

    private val _moods = MutableLiveData<MoodResult<List<MoodWtQuestion>>>()
    val moods: LiveData<MoodResult<List<MoodWtQuestion>>> = _moods

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _uiEvent = MutableSharedFlow<CreateMoodUIState>()
    val uiEvent: SharedFlow<CreateMoodUIState> = _uiEvent.asSharedFlow()

    private var currentOffset = 0
    private val limitPerPage = 5
    private val maxStored = 100

    fun loadMoodsBy(status: Int) = viewModelScope.launch {
        _moods.value = getAllMoodsByStatusUseCase.execute(status = status)
    }

    fun loadMoodsBy(status: Int, moodType: MoodType) = viewModelScope.launch {
        _moods.value = getAllMoodsByMoodTypeStatusUseCase.execute(status = status, moodType = moodType)
    }

    fun loadMoodsBy(status: Int, limit: Int, offset: Int) = viewModelScope.launch {
        _moods.value = getAllMoodsByStatusLimitOffsetUseCase.execute(
            status = status,
            limit = limit,
            offset = offset
        )
    }

    fun loadMoreMoods(status: Int) = viewModelScope.launch {
        val previousList = (_moods.value as? MoodResult.Success)?.data ?: emptyList()

        val result = getAllMoodsByStatusLimitOffsetUseCase.execute(
            status = status,
            limit = limitPerPage,
            offset = currentOffset
        )

        if (result is MoodResult.Success) {
            val combined = (previousList + result.data)
                .distinctBy { it.mood.id }
                .takeLast(maxStored)

            _moods.value = MoodResult.Success(combined)
            currentOffset += limitPerPage
        } else {
            _moods.value = result
        }
        Log.e("DATA11", _moods.value.toString())
    }

    fun resetMoods() {
        _moods.value = MoodResult.Success(emptyList())
        currentOffset = 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadWeeklyMoods(status: Int) = viewModelScope.launch {
        _loading.value = true
        delay(800)
        val startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY)
        val weekStartTimestamp = startOfWeek
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        _moods.value = getAllModsOfWeekUseCase.execute(status, weekStartTimestamp)
        _loading.value = false
    }



    fun createMood(mood: MoodEntity) = viewModelScope.launch {
        _loading.value = true
        val resultCreated = createMoodUseCase.execute(mood)

        val event = when (resultCreated) {
            is MoodResult.Success -> CreateMoodUIState.Success
            is MoodResult.Error -> CreateMoodUIState.Error
            MoodResult.ErrorEmpty -> CreateMoodUIState.Error
        }
        _uiEvent.emit(event)
        delay(800)
        _loading.value = false
    }

    fun updateMood(intensity: Int, note: String, moodType: MoodType, id: Int) = viewModelScope.launch {
        updateMoodUseCase.execute(
            intensity = intensity,
            note = note,
            moodType = moodType,
            id = id
        )
    }

    fun deleteMood(status: Int, id: Int) = viewModelScope.launch {
        deleteMoodUseCase.execute(
            status = status,
            id = id
        )
    }
}
