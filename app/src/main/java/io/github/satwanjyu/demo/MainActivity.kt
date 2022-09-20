package io.github.satwanjyu.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.satwanjyu.demo.ui.theme.DemoTheme
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    private val client = buildClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LiveReleases(client = client)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        client.close()
    }
}

fun buildClient() =
    HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

@Composable
fun LiveReleases(client: HttpClient) {
    val releases = remember { mutableStateListOf<Release>() }

    LaunchedEffect(releases) {
        val response = getKotlinReleases(client)
        releases.clear()
        releases.addAll(response)
    }

    Releases(releases)
}

suspend fun getKotlinReleases(client: HttpClient): List<Release> {
    return client
        .get("https://api.github.com/repos/jetbrains/kotlin/tags")
        .body()
}

@Serializable
data class Release(val name: String)

@Composable
fun Releases(releases: List<Release>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Kotlin Releases:")

        if (releases.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(text = "Loading...", modifier = Modifier.padding(start = 16.dp))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(releases) {
                    Text(it.name, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReleases() {
    val releases = List(100) { Release("Kotlin 1.0.$it") }
    DemoTheme {
        Releases(releases)
    }
}