import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import moe.tlaster.precompose.PreComposeApp

@Composable
fun App() {
    MaterialTheme {
        AppNavigation()
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Court Management") {
        PreComposeApp { App() }
    }
}
