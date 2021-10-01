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

package app.judo.sdk.core.implementations

import app.judo.sdk.api.events.Event
import app.judo.sdk.core.data.JsonParser
import app.judo.sdk.core.environment.Environment
import app.judo.sdk.core.services.ProfileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

internal class ProfileServiceImpl(
    private val environment: Environment
): ProfileService {

    private val keyValueCache get() = environment.keyValueCache
    private val logger get() = environment.logger
    private val evenBus get() = environment.eventBus
    private val mainDispatcher get() = environment.mainDispatcher

    companion object {
        private const val TAG = "ProfileService"
        private const val ANONYMOUS_ID_KEY = "profile-anonymous-id"
        private const val TRAITS_KEY = "profile-traits"
        private const val USER_ID_KEY = "profile-user-id"
    }

    override val userInfo: Map<String, Any>
        get() {
            // mix in the userId and anonymousId into traits, so that they may be used as user info.
            // Notice the capitalization; that is the formatting expected by interpolation
            // strings generated by the editor for the user.
            val withAnonymousId = traits.plus(
                mapOf(
                    Pair("anonymousID", anonymousId)
                )
            )

            return if (userId != null) {
                withAnonymousId.plus(
                    Pair("userID", userId as Any),
                )
            } else {
                withAnonymousId
            }
        }

    override val anonymousId: String
        get() {
            return keyValueCache.retrieveString(ANONYMOUS_ID_KEY) ?: rotateAnonymousId()
        }

    override var userId: String?
        get() = keyValueCache.retrieveString(USER_ID_KEY)
        private set(value) {
            if (value == null) {
                keyValueCache.remove(USER_ID_KEY)
            } else {
                keyValueCache.putString(Pair(USER_ID_KEY, value))
            }
        }

    override var traits: Map<String, Any>
        get() {
            return try {
                keyValueCache.retrieveString(TRAITS_KEY)?.let { jsonString ->
                    JsonParser.parseDictionaryMap(jsonString)
                } ?: emptyMap()
            } catch (exception:  Exception) {
                logger.e(TAG, "Invalid user traits data in storage, yielding empty user traits (reason: ${exception.message}).")
                emptyMap()
            }
        }
        private set(value) {
            val jsonString = JsonParser.encodeDictionaryMap(value)
            keyValueCache.putString(Pair(TRAITS_KEY, jsonString))
        }

    override fun reset() {
        rotateAnonymousId()
        keyValueCache.remove(TRAITS_KEY)
        keyValueCache.remove(USER_ID_KEY)

        CoroutineScope(mainDispatcher).launch {
            evenBus.publish(
                Event.Identified
            )
        }
    }

    override fun identify(userId: String?, traits: Map<String, Any>) {
        if ((this.userId?.isBlank() == false) && this.userId != userId) {
            // cycle user if if previously identified as different user.
            rotateAnonymousId()
        }
        this.userId = userId
        this.traits = traits
        CoroutineScope(mainDispatcher).launch {
            evenBus.publish(
                Event.Identified
            )
        }
        logger.d(TAG, "Identified with userId $userId")
    }

    private fun rotateAnonymousId(): String {
        val newId = UUID.randomUUID().toString()
        keyValueCache.putString(Pair(ANONYMOUS_ID_KEY, newId))
        logger.d(TAG, "Generated new anonymousId: $newId")
        return newId
    }
}