package com.zackjp.android.edgeai.chat.data

import com.zackjp.android.edgeai.chat.domain.ChatAgent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.LocalTime
import kotlin.time.Duration.Companion.milliseconds

class FakeAgent : ChatAgent {

    private val _agentMessages = MutableSharedFlow<String>()

    override fun response(): Flow<String> = _agentMessages.asSharedFlow()

    /**
     * Simulates a real-time stream of A2UI JSON operations.
     */
    override suspend fun sendMessage(userPrompt: String) {
        if ("weather" == userPrompt) {
            emitWeatherCardResponse()
        }
    }

    private suspend fun emitWeatherCardResponse() {
        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "createSurface": {
                    "surfaceId": "weather-surface",
                    "catalogId": "https://a2ui.org/specification/v0_9_1/catalogs/basic/catalog.json"
                }
            }
        """.trimIndent())

        delay(500.milliseconds)
        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "updateComponents": {
                    "surfaceId": "weather-surface",
                    "components": [
                        {
                          "id": "root",
                          "component": "Card",
                          "child": "weather-content"
                        },
                        {
                            "id": "weather-content",
                            "component": "Column",
                            "children": [
                                "hero-section"
                            ]
                        },
                        {
                            "id": "hero-section",
                            "component": "Column",
                            "children": [
                                "hero-city",
                                "updated-time",
                                "temperature",
                                "sky-condition",
                                "feels-like"
                            ]
                        },
                        {
                            "id": "hero-city",
                            "component": "Text",
                            "text": {
                                "path": "/city"
                            },
                            "variant": "h4"
                        },
                        {
                            "id": "updated-time",
                            "component": "Text",
                            "text": {
                                "path": "/updated"
                            },
                            "variant": "h5"
                        },
                        {
                            "id": "temperature",
                            "component": "Text",
                            "text": "62°",
                            "variant": "h1"
                        },
                        {
                            "id": "sky-condition",
                            "component": "Text",
                            "text": "Partly cloudy",
                            "variant": "h5"
                        },
                        {
                            "id": "feels-like",
                            "component": "Text",
                            "text": "Feels like 59° · H 66° · L 51°"
                        }
                    ]
                }
            }
        """.trimIndent())

        delay(1500.milliseconds)
        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "updateDataModel": {
                    "surfaceId": "weather-surface",
                    "path": "/city",
                    "value": "New York, NY"
                }
            }
            """.trimIndent()
        )

        delay(250.milliseconds)
        val currentTime = LocalTime.now()
        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "updateDataModel": {
                    "surfaceId": "weather-surface",
                    "path": "/updated",
                    "value": "Updated ${currentTime.hour % 12}:${currentTime.minute}"
                }
            }
            """.trimIndent()
        )

    }

}
