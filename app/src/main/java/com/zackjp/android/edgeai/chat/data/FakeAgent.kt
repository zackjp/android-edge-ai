package com.zackjp.android.edgeai.chat.data

import com.zackjp.android.edgeai.chat.domain.ChatAgent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.time.Duration.Companion.milliseconds

class FakeAgent : ChatAgent {

    private val _agentMessages = MutableSharedFlow<String>()

    override fun response(): Flow<String> = _agentMessages.asSharedFlow()

    /**
     * Simulates a real-time stream of A2UI JSON operations.
     */
    override suspend fun sendMessage(userPrompt: String) {
        if ("test" != userPrompt) return

        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "createSurface": {
                    "surfaceId": "surface1",
                    "catalogId": "https://a2ui.org/specification/v0_9_1/catalogs/basic/catalog.json"
                }
            }
        """.trimIndent())

        delay(1000.milliseconds)
        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "updateComponents": {
                    "surfaceId": "surface1",
                    "components": [
                        {
                            "id": "root",
                            "component": "Text",
                            "text": {
                                "path": "/status/message"
                            }
                        }
                    ]
                }
            }
        """.trimIndent())

        delay(1500.milliseconds)
        _agentMessages.emit(
            """
            {
                "version": "v0.9.1",
                "updateDataModel": {
                    "surfaceId": "surface1",
                    "path": "/status/message",
                    "value": "Doing some fake background work..."
                }
            }
            """.trimIndent()
        )

        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "createSurface": {
                    "surfaceId": "surface2",
                    "catalogId": "https://a2ui.org/specification/v0_9_1/catalogs/basic/catalog.json"
                }
            }
        """.trimIndent())

        delay(1000.milliseconds)
        _agentMessages.emit("""
            {
                "version": "v0.9.1",
                "updateComponents": {
                    "surfaceId": "surface2",
                    "components": [
                        {
                            "id": "root",
                            "component": "Text",
                            "text": {
                                "path": "/status/message"
                            }
                        }
                    ]
                }
            }
        """.trimIndent())

        delay(1500.milliseconds)
        _agentMessages.emit(
            """
            {
                "version": "v0.9.1",
                "updateDataModel": {
                    "surfaceId": "surface2",
                    "path": "/status/message",
                    "value": "Surface2 Message Complete."
                }
            }
            """.trimIndent()
        )

        delay(1500.milliseconds)
        _agentMessages.emit(
            """
            {
                "version": "v0.9.1",
                "updateDataModel": {
                    "surfaceId": "surface1",
                    "path": "/status/message",
                    "value": "Job's done."
                }
            }
            """.trimIndent()
        )
    }

}
