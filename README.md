# Önlab-Android

[Google docs](https://docs.google.com/document/d/1D7lr9TgBw7O94kjjwaaYgsh5cv8KVEfFRkGB9cDpLTU/edit?usp=sharing)

# 1. Hét

Egyszerű compose alapú form elkészítése, a megadott adatok továbbítása másik compoable viewba [androidx.navigation](https://developer.android.com/jetpack/compose/navigation) segítségével.

## Fontosabb lépések
### 1.  NavController beállítása
[app\src\main\java\hu\bme\aut\android\composenavlibrary\MainActivity.kt](https://github.com/LaciProg/Onlab-Android/blob/NavComp/app/src/main/java/hu/bme/aut/android/composenavlibrary/MainActivity.kt)
~~~kt
//Get the NavControllet entity
val navController = rememberNavController()
val currentBackStack by navController.currentBackStackEntryAsState()
// Fetch your currentDestination:
val currentDestination = currentBackStack?.destination
// Change the variable to this and use Overview as a backup screen if this returns null
val currentScreen = pages.find { it.route == currentDestination?.route } ?: Home
~~~ 
### 2. EndPointok megtervezése
[app\src\main\java\hu\bme\aut\android\composenavlibrary\ui\Pages.kt](https://github.com/LaciProg/Onlab-Android/blob/NavComp/app/src/main/java/hu/bme/aut/android/composenavlibrary/ui/Pages.kt)
~~~kt
//For collection and similar behaviours
interface Pages {
    val icon: ImageVector
    val route: String   
}
//One exeample
object Message : Pages {
    override val icon = Icons.Filled.AddCircle  //eg tabbar ican
    override val route = "message/{form}" //{attribute key} can have multiple
}
//Example endpoint list
val pages = listOf(Home, JSONMessage, Message) 
~~~
### 3. NavHost elkészítése
Érdemes egy külön @Composable függvénybe kiszervezni, és paraméternek átadni az általa használt adatokat
~~~kt
@Composable
fun NavHostModel(
    navController: NavHostController,
    formDataTypes: HashMap<String, FormData>,
    modifier: Modifier = Modifier
)
~~~
Fontos megjegyezni, hogy az 1. pontban elkészített NavHostCotrollert összekössük a NavHosttal, ezen kívül esetlegesen modifierekket és egyéb adatokat is átadhatunk.

Maga a NavHost felparaméterezése a következő képp nézhet ki:
~~~kt
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) 
~~~
Ezen belül a composable() függvény segítségével adhatunk meg endpontokat:
~~~kt
composable(
    route = Home.route,
    arguments = listOf(
        navArgument("form") { type = NavType.StringType }
    )
)
~~~
A fenti példa a küldő oldal, meg kell adni a routot, illetve lehtőség van argumentumok megadására is, alapértelmezetten minden string típusú.
Ezt követően a megjelenítendő @Composable függvényt kell megadni. A nevigációt megvalóstíndóan callback függvényt adtam át a formnak:
~~~kt
navController.navigateSingleTopTo("message/${formDataTypes["MyForm1"]?.form}")

navController.navigateSingleTopTo("jsonmessage/$it")

//Extension function, navController.navigate is also correct for simple cases
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
~~~
Fogadó oldali megoldás:
~~~kt
composable(route = JSONMessage.route) { backStackEntry ->
    JSONMessageScreen(
        form = backStackEntry.arguments?.getString("json") ?: ""
    )
}
~~~

## Adattovábbítási megoldások
### 1. Kevés adat esetén
Stringgé átalakítás és argumentumként átadás
~~~kt
navController.navigateSingleTopTo("message/${formDataTypes["MyForm1"]?.name}/${formDataTypes["MyForm1"]?.age}/${formDataTypes["MyForm1"]?.form}")
~~~
### 2. Nagyobb/bonyultabb adatszerkezet esetén
#### 2.1. Json stringgé sorsítás
Egy data class elkészítése ami csak a szükéséges adatokat tartalmazza, majd annak json sorsítása és stringént argumentum formájában továbbítása. [GSon](https://github.com/google/gson)
~~~kt
data class FormDataDataClass(
    val name : String,
    val age : String,
    )

Gson().toJson(FormDataDataClass(formData.name, formData.age))

val formData = Gson().fromJson(form, FormDataDataClass::class.java)
~~~
#### 2.2 State hoisting segítségével
Az adatokat feljebb visszük a hívási láncban, módosítások callback függvények segítségével készülnek el
#### 2.3 ViewHolder osztály segítésgével
Az osztályt a setContent{} előtt példányostíva használhatjuk, vagy companion object{}-ként is kezelhetjük
~~~kt
//Important to tell the compoition to wath these variables
class FormData {
    var name by mutableStateOf("")
    var age by mutableStateOf("")
    var form by mutableStateOf("")
}

class FormDataViewModel : ViewModel() {

    //Can store multiple data, the argument can be only the key
    val formDataTypes = HashMap<String, FormData>()

    fun buildFormDataTypes() {
        formDataTypes["MyForm1"] = FormData()

    }

    //Without class
    /*var name by mutableStateOf("")
    var age by mutableStateOf("")*/
    /*fun name(name: String){ this.name = name }
    fun age(age: String){ this.age = age }
    fun name() = name
    fun age() = age*/
}
~~~
Amennyiben HasMapet (vagy bármilyen hasonló megoldást választunk) elég ha a neki szánt adat kulcsát kapja meg argumentumként, amennyiben el tudja érni valahogy a HashMapet-
Az említett két megoláds:
~~~kt
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
~~~
## Összegzés
Adtaok/objektumok továbbítására több megoldás is lehetséges, mérlegelni kell, hogy az adott feladathoz melyik megoláds illik a legjobban

## Egyéb források
[Android CodeLab](https://developer.android.com/codelabs/jetpack-compose-navigation?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fjetpack-compose-for-android-developers-3%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-navigation#0)

[StackOverflow](https://stackoverflow.com/questions/67121433/how-to-pass-object-in-navigation-in-jetpack-compose)
