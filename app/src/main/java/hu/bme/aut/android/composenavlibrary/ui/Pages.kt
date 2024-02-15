package hu.bme.aut.android.composenavlibrary.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

interface Pages {
    val icon: ImageVector
    val route: String
}

object Home : Pages {
    override val icon = Icons.Filled.Person
    override val route = "home"
}

object Accounts : Pages {
    override val icon = Icons.Filled.AccountBox
    override val route = "accounts"
}

object Message : Pages {
    override val icon = Icons.Filled.AddCircle
    override val route = "message/{name}/{age}"
}

val pages = listOf(Home, Accounts, Message)