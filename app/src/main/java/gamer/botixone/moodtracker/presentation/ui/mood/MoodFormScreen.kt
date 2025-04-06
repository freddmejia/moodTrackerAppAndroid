package gamer.botixone.moodtracker.presentation.ui.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.MoodType
import gamer.botixone.moodtracker.domain.result.CreateMoodUIState
import gamer.botixone.moodtracker.domain.result.MoodResult
import gamer.botixone.moodtracker.presentation.viewmodels.mood.MoodViewModel
import kotlinx.coroutines.delay


/*
Contenido:
Texto superior: “¿Cómo te sientes hoy?”
Emojis (o íconos) representando distintos estados de ánimo: 😄 🙂 😐 😞 😡
Slider opcional para matiz del sentimiento (1–10).
Campo de texto opcional: “¿Qué te hizo sentir así?”
Botón “Registrar estado de ánimo”.

Al presionar registrar:
Guarda en la base de datos:
Fecha (timestamp)
Estado seleccionado (enum o ID)
Intensidad (opcional)
Nota del usuario
 */
@Composable
fun MoodScreen(
    modifier: Modifier,
    viewModel: MoodViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Box (
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        contentAlignment = Alignment.TopStart
    ){
        MoodFormScreen(
            viewModel = viewModel,
            snackbarHostState = snackbarHostState
        )
        // Este host es el que renderiza los snackbars
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun MoodFormScreen(
    viewModel: MoodViewModel,
    snackbarHostState: SnackbarHostState
){
    val listEmotions = mapOf(
        MoodType.SAD to "😢",
        MoodType.SHY to "😳",
        MoodType.ANGRY to "😠",
        MoodType.FEAR to "😨",
        MoodType.HAPPY to "😄"
    )
    var selectedMood by remember { mutableStateOf<MoodType?>(null) }
    var note by remember { mutableStateOf("") }

    var intensity by remember { mutableStateOf(5f) } // Valor inicial
    val loading: Boolean by viewModel.loading.observeAsState(initial = false)
    val uiEvent = viewModel.uiEvent

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is CreateMoodUIState.Success -> {
                    snackbarHostState.showSnackbar("¡Estado de ánimo guardado!")
                    selectedMood = null
                    intensity = 5f
                    note = ""
                }

                is CreateMoodUIState.Error -> {
                    snackbarHostState.showSnackbar("Error al crear el estado de ánimo.")
                }
                else -> {}
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally // opcional

    ) {
        Text(text = "Cómo te sientes hoy?")
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items = listEmotions.entries.toList(),
                key = { it.key }){
                    entry ->
                val moodType = entry.key
                val isSelected = selectedMood == moodType
                val emoji = entry.value
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 7.dp)
                        .size(48.dp)
                        .background(
                            if (isSelected) Color.Cyan else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedMood = moodType },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 24.sp)
                }

            }

        }
        MoodIntensitySlider(
            value = intensity,
            onValueChange = { intensity = it }
        )
        Spacer(modifier = Modifier.padding(10.dp))
        CompactTextField(
            value = note,
            onValueChange = { note = it },
            hint = "Qué te hizo sentir así? (opcional)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))
        /*SimpleTextField(
            name = "",
            onTextFieldChange = {},
            title = "Qué te hizo sentir así? (opcional)"
        )
        Spacer(modifier = Modifier.padding(10.dp))*/

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (loading) {
                CircularProgressIndicator()
            } else {
                SimpleButtonLogin(
                    text = "Guardar",
                    buttonColors = ButtonDefaults.buttonColors()
                ) {
                    if (selectedMood != null) {
                        val mood = MoodEntity(
                            id = 0,
                            timestamp = System.currentTimeMillis(),
                            intensity = intensity.toInt(),
                            note = note,
                            questionModel = 1,
                            moodType = selectedMood!!,
                            status = 1
                        )
                        viewModel.createMood(mood)
                    }
                }
            }
        }


    }

}

@Composable
fun CompactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
        decorationBox = { innerTextField ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart // 👈 Este es el que faltaba
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        style = LocalTextStyle.current.copy(
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    )
                }
                innerTextField()
            }
        }
    )
}



@Composable
fun SimpleTextField(name: String,
              onTextFieldChange: (String) -> Unit,
              title: String
) {
    TextField(
        value = name,
        onValueChange = { onTextFieldChange(it) },
        modifier = Modifier
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        placeholder = {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = title
            ) },
        singleLine = true,
        maxLines = 3,
    )
}

@Composable
fun SimpleButtonLogin(text: String, buttonColors: ButtonColors, simpleOnClickAction: () -> Unit) {
    Button(onClick = { simpleOnClickAction() },
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
        colors = buttonColors
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun ShowMoodFormScreen(){
    MoodScreen(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
fun MoodIntensitySlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 5.dp
            )
    ) {
        Text(
            text = "Matiz del sentimiento: ${value.toInt()}",
            style = MaterialTheme.typography.bodyMedium
        )

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..10f,
            steps = 8, // 10 niveles = 9 segmentos => steps = niveles - 2
            modifier = Modifier.fillMaxWidth()
        )
    }
}
