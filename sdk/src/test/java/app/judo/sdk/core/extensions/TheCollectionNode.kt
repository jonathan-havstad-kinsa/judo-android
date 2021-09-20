/*
 * Copyright (c) 2020-present, Rover Labs, Inc. All rights reserved.
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Rover.
 *
 * This copyright notice shall be included in all copies or substantial portions of
 * the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.judo.sdk.core.extensions

import app.judo.sdk.api.models.Collection
import app.judo.sdk.api.models.Limit
import org.junit.Assert
import org.junit.Test

class TheCollectionNode {

    @Test
    fun `Does not break with items and show of size one`() {
        // Arrange
        val expectedItem = "Item 1"
        val expected = listOf(expectedItem)

        val collection = Collection(
            id = "0",
            metadata = null,
            childIDs = emptyList(),
            filters = emptyList(),
            keyPath = "data.data",
            name = "Collection 0",
            sortDescriptors = emptyList(),
            limit = Limit(show = 1, startAt = 1)
        ).apply {
            items = listOf(expectedItem)
        }

        // Act
        collection.limit()

        val actual = collection.items

        // Assert
        Assert.assertEquals(expected, actual)
    }

}