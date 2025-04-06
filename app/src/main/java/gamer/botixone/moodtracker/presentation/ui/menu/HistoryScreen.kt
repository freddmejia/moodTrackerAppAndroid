package gamer.botixone.moodtracker.presentation.ui.menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gamer.botixone.moodtracker.domain.model.MoodWtQuestion
import gamer.botixone.moodtracker.domain.result.MoodResult
import gamer.botixone.moodtracker.presentation.viewmodels.mood.MoodViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ShowHistoryScreen(){
    HistoryScreen()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen2(
    modifier: Modifier = Modifier,
    viewModel: MoodViewModel = hiltViewModel()
) {
    val moodsResult by viewModel.moods.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMoreMoods(status = 1)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetMoods()
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Tu historial emocional",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (val result = moodsResult) {
            is MoodResult.Success -> {
                val grouped = result.data.groupBy {
                    Instant.ofEpochMilli(it.mood.timestamp)
                        .atZone(ZoneId.systemDefault()).toLocalDate()
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    grouped.forEach { (date, entries) ->
                        item { DateHeader(date) }
                        items(entries) { entry ->
                            MoodEntryCard(entry.toUI())
                        }
                    }

                    item {
                        Button(
                            onClick = { viewModel.loadMoreMoods(status = 1) },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Cargar más")
                        }
                    }
                }
            }
            is MoodResult.ErrorEmpty -> {
                Text("No tienes datos a mostrar.")
            }
            is MoodResult.Error -> {
                Text("Error: ${result.exception.message}")
            }
            else -> {
                CircularProgressIndicator()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun List<MoodWtQuestion>.toMoodEntryUIList(): List<MoodEntryUI> {
    return this.map { it.toUI() }
}



@RequiresApi(Build.VERSION_CODES.O)
fun MoodWtQuestion.toUI(): MoodEntryUI {
    return MoodEntryUI(
        id = this.mood.id,
        date = Instant.ofEpochMilli(this.mood.timestamp).atZone(ZoneId.systemDefault()).toLocalDate(),
        moodType = MoodType.valueOf(this.mood.moodType.name),
        intensity = this.mood.intensity,
        note = this.mood.note
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: MoodViewModel = hiltViewModel()
) {
    val mockData = remember { getMockMoodHistory() }
    val moodsResult by viewModel.moods.observeAsState()
    var offset by remember { mutableStateOf(0) }
    val limit = 20

    LaunchedEffect(Unit) {
        viewModel.loadMoodsBy(status = 1, limit = limit, offset = offset)
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Tu historial emocional",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            mockData.forEach { (date, entries) ->
                item {
                    DateHeader(date)
                }
                items(entries) { entry ->
                    MoodEntryCard(entry)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateHeader(date: LocalDate) {
    Text(
        text = date.format(DateTimeFormatter.ofPattern("EEEE, dd MMM", Locale("es", "ES"))),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun MoodEntryCard(entry: MoodEntryUI) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = entry.moodType.backgroundColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.moodType.emoji,
                fontSize = 32.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.moodType.description,
                    style = MaterialTheme.typography.titleMedium
                )
                entry.note?.let {
                    Text(
                        text = "\"$it\"",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                text = "${entry.intensity ?: "?"}/10",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

/*
*     HAPPY,
    SAD,
    FEAR,
    SHY,
    ANGRY
    *         MoodType.SAD to "😢",
        MoodType.SHY to "😳",
        MoodType.ANGRY to "😠",
        MoodType.FEAR to "😨",
        MoodType.HAPPY to "😄"
        *
* */
enum class MoodType(val emoji: String, val description: String, val backgroundColor: Color) {
    HAPPY("😄", "Feliz", Color(0xFFD0F4DE)),
    FEAR("😨", "Temeroso", Color(0xFFD3E4CD)),
    SHY("😳", "Tìmido", Color(0xFFE4E4E4)),
    SAD("😢", "Triste", Color(0xFFFFD6D6)),
    ANGRY("😠", "Molesto", Color(0xFFFFB4A2))
}


data class MoodEntryUI(
    val id: Int,
    val date: LocalDate,
    val moodType: MoodType,
    val intensity: Int?,
    val note: String?
)

@RequiresApi(Build.VERSION_CODES.O)
fun getMockMoodHistory(): Map<LocalDate, List<MoodEntryUI>> {
    val today = LocalDate.now()
    return listOf(
        MoodEntryUI(1, today, MoodType.HAPPY, 9, "Salí a caminar y vi el atardecer 🌇"),
            MoodEntryUI(2, today, MoodType.FEAR, 7, "Me dio miedo!"),
        MoodEntryUI(3, today.minusDays(1), MoodType.SAD, 4, "Me sentí solo un rato"),
        MoodEntryUI(4, today.minusDays(2), MoodType.ANGRY, 5, "Tráfico y mucho ruido en casa"),
        MoodEntryUI(5, today.minusDays(2), MoodType.SHY, 6, null)
    ).groupBy { it.date }
}
