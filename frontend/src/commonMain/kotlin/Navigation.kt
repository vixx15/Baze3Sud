import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import androidx.compose.runtime.getValue
import ui.feature.cost.CostScreen
import ui.feature.courtCase.overview.CourtCasesScreen
import ui.feature.judge.overview.JudgesScreen
import ui.feature.judgementStatusChange.overview.JudgementStatusChangeScreen
import ui.feature.testimony.overview.TestimoniesScreen
import ui.feature.witnesses.overview.WitnessessScreen

@Composable
fun AppNavigation() {
    val navigator = rememberNavigator()
    val currentEntry by navigator.currentEntry.collectAsState(null)
    val currentRoute = currentEntry?.route?.route
    Column {
        TopNavigationTabs(
            navigator = navigator,
            currentRoute = currentRoute ?: ""
        )
        NavHost(navigator = navigator, initialRoute = "/testimonies") {

            scene("/testimonies") {
                TestimoniesScreen()
            }

            scene("/feature/witnesses") {
                WitnessessScreen()
            }

            scene("/judges") {
                JudgesScreen()
            }

            scene("/courtCases") {
                CourtCasesScreen()
            }

            scene("/judgementStatus") {
                JudgementStatusChangeScreen()
            }
            scene("/expenses") {
                CostScreen()
            }
        }
    }
}

@Composable
fun TopNavigationTabs(
    navigator: Navigator,
    currentRoute: String
) {
    Row {
        TopTab.entries.forEach { tab ->
            val selected = currentRoute == tab.route

            Text(
                text = tab.title,
                modifier = Modifier
                    .clickable {
                        if (!selected) {
                            navigator.navigate(tab.route)
                        }
                    }
                    .padding(16.dp),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

enum class TopTab(
    val route: String,
    val title: String
) {
    TESTIMONIES("/testimonies", "Svedocenja"),
    WITNESSES("/feature/witnesses", "Svedoci"),
    JUDGES("/judges", "Sudije"),
    COURT_CASES("/courtCases", "Predmeti"),
    JUDGEMENT_STATUS("/judgementStatus", "Statusi presuda"),
    EXPENSES("/expenses", "Troskovi")
}