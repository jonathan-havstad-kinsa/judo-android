package app.judo.sdk.core.implementations

import app.judo.sdk.core.robots.AbstractRobotTest
import app.judo.sdk.core.robots.DataSourceServiceRobot
import app.judo.sdk.core.services.DataSourceService
import app.judo.sdk.utils.TestJSON
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class TheDataSourceService : AbstractRobotTest<DataSourceServiceRobot>() {

    override fun robotSupplier(): DataSourceServiceRobot {
        return DataSourceServiceRobot()
    }

    @Test
    fun `GET request can accept arbitrary headers`() = runBlocking(robot.testCoroutineDispatcher) {
        // Arrange
        val expected = DataSourceService.Result.Success(
            body = TestJSON.dummy_api_response
        )

        val url = "https://dummyapi.io/data/api/user?limit=10"

        val headers: Map<String, String> = mapOf(
            "app-id" to "609561d4e8fed0600a0a26b8"
        )

        // Act
        val actual = robot.environment.dataSourceService.getData(
            url = url,
            headers = headers
        )

        // Assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `PUT request can accept arbitrary headers`() = runBlocking(robot.testCoroutineDispatcher) {
        // Arrange
        val expected = DataSourceService.Result.Success(
            body = TestJSON.dummy_api_response
        )

        val url = "https://dummyapi.io/data/api/user?limit=10"

        val headers: Map<String, String> = mapOf(
            "app-id" to "609561d4e8fed0600a0a26b8"
        )

        // Act
        val actual = robot.environment.dataSourceService.putData(
            url = url,
            headers = headers
        )

        // Assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `PUT request can accept a body`() = runBlocking(robot.testCoroutineDispatcher) {
        // Arrange
        val expected = DataSourceService.Result.Success(
            body = """{"result":"Success"}"""
        )

        val url = "https://dummyapi.io/data/api/user/put"

        val headers: Map<String, String> = mapOf(
            "app-id" to "609561d4e8fed0600a0a26b8"
        )

        val body = """{"name":"User"}"""

        // Act
        val actual = robot.environment.dataSourceService.putData(
            url = url,
            headers = headers,
            body = body
        )

        // Assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `PUT request can accept a null body`() = runBlocking(robot.testCoroutineDispatcher) {
        // Arrange
        val expected = DataSourceService.Result.Success(
            body = """{"result":"Success"}"""
        )

        val url = "https://dummyapi.io/data/api/user/put/null"

        val headers: Map<String, String> = mapOf(
            "app-id" to "609561d4e8fed0600a0a26b8"
        )

        // Act
        val actual = robot.environment.dataSourceService.putData(
            url = url,
            headers = headers,
        )

        // Assert
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `POST request can accept arbitrary headers`() = runBlocking(robot.testCoroutineDispatcher) {
        // Arrange
        val expected = DataSourceService.Result.Success(
            body = TestJSON.dummy_api_response
        )

        val url = "https://dummyapi.io/data/api/user?limit=10"

        val headers: Map<String, String> = mapOf(
            "app-id" to "609561d4e8fed0600a0a26b8"
        )

        // Act
        val actual = robot.environment.dataSourceService.postData(
            url = url,
            headers = headers
        )

        // Assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `POST request can accept a body`() = runBlocking(robot.testCoroutineDispatcher) {
        // Arrange
        val expected = DataSourceService.Result.Success(
            body = """{"result":"Success"}"""
        )

        val url = "https://dummyapi.io/data/api/user/post"

        val headers: Map<String, String> = mapOf(
            "app-id" to "609561d4e8fed0600a0a26b8"
        )

        val body = """{"name":"User"}"""

        // Act
        val actual = robot.environment.dataSourceService.postData(
            url = url,
            headers = headers,
            body = body
        )

        // Assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `POST request can accept a null body`() = runBlocking(robot.testCoroutineDispatcher) {
        // Arrange
        val expected = DataSourceService.Result.Success(
            body = """{"result":"Success"}"""
        )

        val url = "https://dummyapi.io/data/api/user/post/null"

        val headers: Map<String, String> = mapOf(
            "app-id" to "609561d4e8fed0600a0a26b8"
        )

        // Act
        val actual = robot.environment.dataSourceService.postData(
            url = url,
            headers = headers,
        )

        // Assert
        Assert.assertEquals(expected, actual)
    }


}