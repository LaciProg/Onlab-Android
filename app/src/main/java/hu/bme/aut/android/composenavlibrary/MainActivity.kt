package hu.bme.aut.android.composenavlibrary

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import hu.bme.aut.android.composenavlibrary.ui.Home
import hu.bme.aut.android.composenavlibrary.ui.Message
import hu.bme.aut.android.composenavlibrary.ui.components.TabBar
import hu.bme.aut.android.composenavlibrary.ui.pages
import hu.bme.aut.android.composenavlibrary.ui.theme.ComposeNavLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNavLibraryTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                // Fetch your currentDestination:
                val currentDestination = currentBackStack?.destination
                // Change the variable to this and use Overview as a backup screen if this returns null
                val currentScreen = pages.find { it.route == currentDestination?.route } ?: Home
                val formDataViewModel = FormDataViewModel()
                formDataViewModel.buildFormDataTypes()
                NavHostModel(navController = navController, formDataViewModel.formData  /*formDataTypes = formDataViewModel.formDataTypes*/)



                // A surface container using the 'background' color from the theme
                /*Scaffold(
                    topBar = {
                        TabBar(
                            allScreens = pages,
                            onTabSelected = {newScreen ->
                                navController.navigateSingleTopTo(newScreen.route)
                            },
                            currentScreen = currentScreen
                        )
                    }
                ){ innerPaddings ->
                    Surface(
                        modifier = Modifier.padding(innerPaddings),
                    ) {
                        Greeting("Android")
                    }
                }*/
            }
        }
    }
}

@Composable
fun NavHostModel(
    navController: NavHostController,
    formData: FormData,
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
                navArgument("name") { type = NavType.StringType },
                navArgument("age") { type = NavType.StringType }
            )
        ) {
            MainForm(
                navController = navController,
                data = formData,
                onClickSend = {
                    navController.navigateSingleTopTo("message/${formData.name}/${formData.age}")
                },

            )
        }
        composable(route = Message.route) { backStackEntry ->
            MessageScreen(
                navController = navController,
                name = backStackEntry.arguments?.getString("name") ?: "",
                age = backStackEntry.arguments?.getString("age") ?: ""
            )
        }
    }

}

class FormData(){
    var name by mutableStateOf("")
    var age by mutableStateOf("")
}

class FormDataViewModel() : ViewModel() {
    /*var name by mutableStateOf("")
    var age by mutableStateOf("")*/

    val formData = FormData()

    val formDataTypes = HashMap<String, FormData>()

    fun buildFormDataTypes() {
        formDataTypes["form1"] = FormData()
    }


    /*fun name(name: String){ this.name = name }
    fun age(age: String){ this.age = age }
    fun name() = name
    fun age() = age*/
}

@Composable
fun MainForm(
    modifier: Modifier = Modifier,
    data: FormData,
    navController: NavHostController = rememberNavController(),
    onClickSend : () -> Unit = {},
){
    /*var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }*/

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
    }
}


@Composable
fun MessageScreen(
    navController: NavHostController,
    name: String ,
    age: String
){
    Column {
        Text(text = "Hello $name, this is a $age old message screen!")
    }
}

/*
@Composable
@Preview(showBackground = true)
fun MainFormPreview(){
    ComposeNavLibraryTheme {
        MainForm()
    }
}*/

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