package gamer.botixone.moodtracker.presentation.ui.menu


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
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
@Composable
fun MoodBarChart(entries: List<MoodEntryUI>) {
    val groupedByDay = entries.groupBy { it.date }

    val points = groupedByDay.map { (date, moods) ->
        val avg = moods.mapNotNull { it.intensity }.average().toFloat()
        BarChartData.Bar(
            label = date.dayOfWeek.name.take(3), // e.g., MON
            value = avg,
            color = moods.first().moodType.backgroundColor
        )
    }

    BarChart(
        barChartData = BarChartData(points),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(vertical = 16.dp),
        animation = simpleChartAnimation(),
        barDrawer = SimpleBarDrawer()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResumeScreen2(
    modifier: Modifier = Modifier,
    viewModel: MoodViewModel = hiltViewModel()
) {
    val moodState by viewModel.moods.observeAsState()
    val loading: Boolean by viewModel.loading.observeAsState(initial = false)

    LaunchedEffect(Unit) {
        viewModel.loadWeeklyMoods(status = 1)
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Resumen de la semana",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else {

            when (moodState) {
                is MoodResult.Success<List<MoodWtQuestion>> -> {
                    val data = (moodState as MoodResult.Success<List<MoodWtQuestion>>).data

                    val moodEntryUIList = (moodState as MoodResult.Success<List<MoodWtQuestion>>).data.toMoodEntryUIList()

                    // Agrupar por día
                    val grouped = data.groupBy {
                        Instant.ofEpochMilli(it.mood.timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    MoodBarChart(entries = moodEntryUIList)
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        grouped.forEach { (date, entries) ->
                            item { DateHeader(date) }
                            items(entries) { entry ->
                                MoodEntryCard(entry.toUI())
                            }
                        }
                    }
                }

                is MoodResult.Error -> {
                    Text(
                        text = "Error al cargar los estados de ánimo.",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    Text("No hay datos aún.")
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResumeScreen(
    modifier: Modifier = Modifier,

) {
    val entries = getMockMoodHistory().values.flatten()

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Resumen semanal",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        MoodBarChart(entries = entries)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(entries) { entry ->
                MoodEntryCard(entry)
            }
        }
    }
}


