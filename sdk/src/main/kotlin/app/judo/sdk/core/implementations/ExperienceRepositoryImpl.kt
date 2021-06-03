package app.judo.sdk.core.implementations

import app.judo.sdk.api.errors.ExperienceError
import app.judo.sdk.api.models.Experience
import app.judo.sdk.core.data.Resource
import app.judo.sdk.core.errors.ErrorMessages
import app.judo.sdk.core.log.Logger
import app.judo.sdk.core.repositories.ExperienceRepository
import app.judo.sdk.core.services.ExperienceService
import app.judo.sdk.core.services.FontResourceService
import app.judo.sdk.core.utils.TranslationLoader
import app.judo.sdk.core.utils.TypeFaceLoader
import app.judo.sdk.core.utils.visitorsOf
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okio.IOException
import java.util.*

internal class ExperienceRepositoryImpl(
    private val experienceServiceSupplier: () -> ExperienceService,
    private val fontResourceServiceSupplier: () -> FontResourceService,
    private val loggerSupplier: () -> Logger? = { null }
) : ExperienceRepository {

    companion object {
        private const val TAG = "ExperienceRepositoryImpl"
    }

    private val inMemoryExperiences = mutableMapOf<String, Experience>()

    override fun retrieveExperience(
        aURL: String,
        ignoreCache: Boolean,
    ): Flow<Resource<Experience, ExperienceError>> {
        return flow {
            val logger = loggerSupplier()
            val service = experienceServiceSupplier()
            emit(Resource.Loading())
            val response = service.getExperience(aURL, ignoreCache)
            if (response.isSuccessful && response.body() != null) {

                val experience = response.body()!!

                val typeFaceLoader = fontResourceServiceSupplier().let { faceService ->

                    val typefaceMap = faceService.getTypefacesFor(experience.fonts, ignoreCache)

                    TypeFaceLoader(typefaces = typefaceMap)
                }

                val translationLoader = experience.localization.let { translations ->

                    val translator = TranslatorImpl(theTranslationMap = translations) {
                        listOf(Locale.getDefault().toLanguageTag())
                    }

                    TranslationLoader(translator = translator::translate)

                }

                experience.accept(visitorsOf(typeFaceLoader, translationLoader))

                emit(Resource.Success(experience))

            } else {
                val error = when (response.code()) {
                    404 -> {
                        ExperienceError.ExperienceNotFoundError(message = ErrorMessages.EXPERIENCE_NOT_FOUND)
                    }

                    // TODO: 2021-03-23 - Add UnauthorizedAccessError for 401 responses

                    else -> {
                        ExperienceError.UnexpectedError(message = ErrorMessages.UNKNOWN_ERROR)
                    }
                }

                emit(Resource.Error(error = error))
            }
        }.catch {
            val error = when (it) {
                is IOException -> {
                    ExperienceError.NetworkError(
                        message = it.message,
                        cause = it
                    )
                }

                is JsonDataException, is JsonEncodingException -> {
                    ExperienceError.MalformedExperienceError(
                        message = it.message,
                        cause = it
                    )
                }

                else -> {
                    ExperienceError.UnexpectedError(
                        message = it.message,
                        cause = it
                    )
                }
            }
            emit(Resource.Error(error))
        }
    }

    override fun put(experience: Experience, key: String?): Experience? {
        return inMemoryExperiences.put(key ?: experience.id, experience)
    }

    override fun retrieveById(key: String): Experience? {
        return inMemoryExperiences[key]
    }
}