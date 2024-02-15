package hu.bme.aut.android.composenavlibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import hu.bme.aut.android.composenavlibrary.MainActivity.Companion.formDataViewModel
import hu.bme.aut.android.composenavlibrary.ui.Home
import hu.bme.aut.android.composenavlibrary.ui.JSONMessage
import hu.bme.aut.android.composenavlibrary.ui.Message
import hu.bme.aut.android.composenavlibrary.ui.components.TabBar
import hu.bme.aut.android.composenavlibrary.ui.pages
import hu.bme.aut.android.composenavlibrary.ui.theme.ComposeNavLibraryTheme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames

class MainActivity : ComponentActivity() {

    companion object{
        val formDataViewModel = FormDataViewModel()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val formDataViewModel = FormDataViewModel()
        formDataViewModel.buildFormDataTypes()

        setContent {
            ComposeNavLibraryTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                // Fetch your currentDestination:
                val currentDestination = currentBackStack?.destination
                // Change the variable to this and use Overview as a backup screen if this returns null
                val currentScreen = pages.find { it.route == currentDestination?.route } ?: Home

                NavHostModel(navController = navController, formDataTypes = formDataViewModel.formDataTypes)

            }
        }
    }
}

@Composable
fun NavHostModel(
    navController: NavHostController,
    formDataTypes: HashMap<String, FormData>,
    modifier: Modifier = Modifier
) {

    //val formData = FormData()

    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(
            route = Home.route,
            arguments = listOf(
                navArgument("form") { type = NavType.StringType }
            )
        ) {
            MainForm(
                //navController = navController,
                data = formDataTypes["MyForm1"],
                onClickSend = {
                    navController.navigateSingleTopTo("message/${formDataTypes["MyForm1"]?.form}")

                    //navController.navigateSingleTopTo("message/${formDataTypes["MyForm1"]?.name}/${formDataTypes["MyForm1"]?.age}/${formDataTypes["MyForm1"]?.form}")
                },
                onClickJSONSend = {
                    navController.navigateSingleTopTo("jsonmessage/$it")
                }


            )
        }
        composable(route = Message.route) { backStackEntry ->
            MessageScreen(
                //navController = navController,
                //name = backStackEntry.arguments?.getString("name") ?: "",
                //age = backStackEntry.arguments?.getString("age") ?: "",
                forms = formDataTypes,
                form = backStackEntry.arguments?.getString("form") ?: ""
            )
        }

        composable(route = JSONMessage.route) { backStackEntry ->
            JSONMessageScreen(
                form = backStackEntry.arguments?.getString("json") ?: ""
            )
        }
    }

}


class FormData {
    var name by mutableStateOf("")
    var age by mutableStateOf("")
    var form by mutableStateOf("")
}


data class FormDataDataClass(
    val name : String,
    val age : String,
    )

class FormDataViewModel : ViewModel() {
    /*var name by mutableStateOf("")
    var age by mutableStateOf("")*/

    val formDataTypes = HashMap<String, FormData>()

    fun buildFormDataTypes() {
        formDataTypes["MyForm1"] = FormData()

    }


    /*fun name(name: String){ this.name = name }
    fun age(age: String){ this.age = age }
    fun name() = name
    fun age() = age*/
}

@Composable
fun MainForm(
    modifier: Modifier = Modifier,
    data: FormData?,
    //navController: NavHostController = rememberNavController(),
    onClickSend : () -> Unit = {},
    onClickJSONSend:  (json : String) -> Unit = { }
){
    /*var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }*/

    if(data == null) return
    data.form = "MyForm1"
    Column(modifier = modifier) {
        Text(
            text = "Hello, please give me your name and age!",
            modifier = Modifier.padding(16.dp)
        )
        TextField(
            value = data.name,
            onValueChange = {data.name = it},
            label = { Text("Name") },
            modifier = Modifier.padding(16.dp)
        )
        TextField(
            value = data.age,
            onValueChange = {data.age = it},
            label = { Text("Age") },
            modifier = Modifier.padding(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Text(
            text = "Hello ${data.name}, you are ${data.age} years old!",
            modifier = Modifier.padding(16.dp)
        )

        Button(onClick = onClickSend) {
            Text(text = "Send")
        }


        val formData by remember { mutableStateOf(FormData()) }

        Text(
            text = "Hello, please give me your name and age!",
            modifier = Modifier.padding(16.dp)
        )
        TextField(
            value = formData.name,
            onValueChange = {formData.name = it},
            label = { Text("Name") },
            modifier = Modifier.padding(16.dp)
        )
        TextField(
            value = formData.age,
            onValueChange = {formData.age = it},
            label = { Text("Age") },
            modifier = Modifier.padding(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Text(
            text = Gson().toJson(FormDataDataClass(formData.name, formData.age)),
            modifier = Modifier.padding(16.dp)
        )

        Button(onClick = { onClickJSONSend(Gson().toJson(FormDataDataClass(formData.name, formData.age))) }) {
            Text(text = "Send")
        }
    }
}


@Composable
fun MessageScreen(
    //navController: NavHostController,
    //name: String ,
    //age: String,
    forms: HashMap<String, FormData>,
    form: String
){
    Column {
        //                   Companion object                                           Parameters
        Text(text = "Hello ${formDataViewModel.formDataTypes[form]?.name}, this is a ${forms[form]?.age} old message screen!")
    }
}

@Composable
fun JSONMessageScreen(
    form: String
){
    val formData = Gson().fromJson(form, FormDataDataClass::class.java)
    Column {
        //                   Companion object                                           Parameters
        Text(text = "Hello ${formData.name}, this is a ${formData.age} old message screen!")
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }