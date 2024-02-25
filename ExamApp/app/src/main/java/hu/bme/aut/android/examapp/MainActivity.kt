package hu.bme.aut.android.examapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.android.examapp.ui.components.EditMultipleChoiceQuestion
import hu.bme.aut.android.examapp.ui.components.EditTrueFalseQuestion
import hu.bme.aut.android.examapp.ui.components.MultipleChoiceQuestion
import hu.bme.aut.android.examapp.ui.components.TrueFalseQuestion
import hu.bme.aut.android.examapp.ui.theme.ExamAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExamAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {

                        TrueFalseQuestion()
                        EditTrueFalseQuestion()
                        MultipleChoiceQuestion()
                        EditMultipleChoiceQuestion()
                    }
                    //Greeting("Android")
                }
            }
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
    ExamAppTheme {
        Greeting("Android")
    }
}