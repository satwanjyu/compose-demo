package io.github.satwanjyu.demo

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DemoUnitTest {
    @Test
    fun httpClientTest() {
        val client = buildClient()
        val result = runBlocking {
            client.get("https://www.google.com").bodyAsText()
        }
        println(result)
        assert(result.isNotEmpty())
    }

    @Test
    fun getKotlinReleases() {
        val client = buildClient()
        val result = runBlocking {
            getKotlinReleases(client)
        }
        println(result)
        assert(result.isNotEmpty())
    }
}