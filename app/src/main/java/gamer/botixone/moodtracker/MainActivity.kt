package gamer.botixone.moodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.MoodType
import gamer.botixone.moodtracker.data.local_datasource.model.QuestionEntity
import gamer.botixone.moodtracker.presentation.ui.menu.MainScreen
import gamer.botixone.moodtracker.presentation.ui.mood.MoodFormScreen
import gamer.botixone.moodtracker.presentation.ui.mood.MoodScreen
import gamer.botixone.moodtracker.presentation.viewmodels.mood.MoodViewModel
import gamer.botixone.moodtracker.presentation.viewmodels.question.QuestionViewModel
import gamer.botixone.moodtracker.ui.theme.MoodTrackerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val moodViewModel: MoodViewModel by viewModels()
    private val questionViewModel: QuestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val question = QuestionEntity(
            id = 0,
            title = "Como te sientes hoy?",
            status = 1
        )
        questionViewModel.createQuestion(questionEntity = question)

        val mood = MoodEntity(
            id = 0,
            timestamp = 0,
            intensity = 0,
            note = "",
            questionModel = 1,
            moodType = MoodType.SAD,
            status = 1
        )
        moodViewModel.createMood(mood)

        moodViewModel.loadMoodsBy(status = 1)*/

        enableEdgeToEdge()
        setContent {
            MenuMoodTracker()
            /*MoodTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MoodScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                    /*Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                }
            }*/
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoodTrackerTheme {
        Greeting("Android")
    }
}

@Composable
fun MenuMoodTracker(){
    val navBarController = rememberNavController()
    MainScreen(
        navBarController = navBarController,
    )
}