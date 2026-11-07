package com.zackjp.android.edgeai.chat.data

import com.zackjp.android.edgeai.chat.domain.ChatAgent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.LocalTime
import kotlin.time.Duration.Companion.milliseconds

class StreamingFakeAgent : ChatAgent {

    private val _agentMessages = MutableSharedFlow<String>()

    override fun response(): Flow<String> = _agentMessages.asSharedFlow()

    /**
     * Simulates a real-time stream of A2UI JSON operations.
     */
    override suspend fun sendMessage(userPrompt: String) {
        if ("weather" == userPrompt.trim()) {
            emitWeatherCardResponse()
        }
    }

    private suspend fun emitWeatherCardResponse() {
        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "createSurface": {
                    "surfaceId": "weather-surface",
                    "catalogId": "https://com.zackjp.android/catalogs/edgeai/v1/catalog.json"
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
                            "component": "WeatherCard",
                            "location": {
                                "path": "/city"
                            },
                            "currentTemp": {
                                "path": "/currentTemp"
                            },
                            "lastUpdated": {
                                "path": "/lastUpdated"
                            },
                            "weatherCondition": {
                                "path": "/currentWeatherCondition"
                            }
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

        delay(500.milliseconds)
        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "updateDataModel": {
                    "surfaceId": "weather-surface",
                    "path": "/currentTemp",
                    "value": 68
                }
            }
            """.trimIndent()
        )

        delay(500.milliseconds)
        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "updateDataModel": {
                    "surfaceId": "weather-surface",
                    "path": "/currentWeatherCondition",
                    "value": "cloudy"
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
                    "path": "/lastUpdated",
                    "value": "${currentTime.hour % 12}:${currentTime.minute.toString().padStart(2, '0')}"
                }
            }
            """.trimIndent()
        )

    }

}
