package com.zackjp.android.edgeai.chat.a2ui.components

import androidx.a2ui.compose.runtime.A2uiComponentProperties
import androidx.a2ui.compose.runtime.A2uiComponentScope
import androidx.a2ui.compose.runtime.A2uiProperty
import androidx.a2ui.compose.ui.A2uiComponent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zackjp.android.edgeai.R

private object WeatherCardTokens {
    val MaxWidth = 500.dp
    val LastUpdatedColor = Color(0xff666666)
}

class WeatherComponent : A2uiComponent {

    private companion object {
        private val LocationProp = A2uiProperty.dynamicString(
            key = "location",
            required = true,
        )
        private val CurrentTempProp = A2uiProperty.dynamicNumber(
            key = "currentTemp",
            required = true,
        )
        private val LastUpdatedProp = A2uiProperty.dynamicString(
            key = "lastUpdated",
        )
        private val WeatherConditionProp = A2uiProperty.dynamicString(
            key = "weatherCondition",
        )
    }

    override val name = "WeatherCard"
    override val description = "Displays the weather."
    override val properties = listOf(
        LocationProp,
        CurrentTempProp,
        LastUpdatedProp,
        WeatherConditionProp,
    )

    @Composable
    override fun A2uiComponentScope.isReady(properties: A2uiComponentProperties): Boolean {
        return properties.bind(LocationProp) != null
    }

    @Composable
    override fun A2uiComponentScope.Content(
        properties: A2uiComponentProperties,
        modifier: Modifier
    ) {
        val locationText = properties.bind(LocationProp)
        val currentTemp = properties.bind(CurrentTempProp)?.toInt()
        val lastUpdated = properties.bind(LastUpdatedProp)
        val weatherCondition = properties.bind(WeatherConditionProp)

        if (locationText != null) {
            WeatherCard(
                modifier = Modifier
                    .widthIn(max = WeatherCardTokens.MaxWidth)
                    .fillMaxWidth(),
                locationText = locationText,
                currentTemp = currentTemp,
                lastUpdated = lastUpdated,
                weatherCondition = when (weatherCondition) {
                    "sunny" -> WeatherCondition.Sunny
                    "cloudy" -> WeatherCondition.Cloudy
                    "rainy" -> WeatherCondition.Rainy
                    else -> null
                },
            )
        }
    }
}

@Composable
private fun WeatherCard(
    modifier: Modifier = Modifier,
    locationText: String?,
    currentTemp: Int?,
    lastUpdated: String?,
    weatherCondition: WeatherCondition?,
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            if (locationText != null) {
                WeatherHeader(
                    modifier = Modifier.fillMaxWidth(),
                    locationText = locationText,
                    lastUpdated = lastUpdated,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TodaysConditions(
                modifier = Modifier.fillMaxWidth(),
                currentTemp = currentTemp,
                weatherCondition = weatherCondition
            )
        }
    }
}

@Composable
private fun WeatherHeader(
    modifier: Modifier = Modifier,
    locationText: String,
    lastUpdated: String?
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight(700),
            ),
            text = locationText,
        )

        if (lastUpdated != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                color = WeatherCardTokens.LastUpdatedColor,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp,
                ),
                text = "Updated $lastUpdated",
            )
        }
    }
}

@Composable
private fun TodaysConditions(
    modifier: Modifier = Modifier,
    currentTemp: Int?,
    weatherCondition: WeatherCondition?
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (currentTemp != null) {
            TodayConditionsTextSection(
                modifier = Modifier.weight(1f),
                currentTemp = currentTemp,
                weatherCondition = weatherCondition,
            )
        }

        if (weatherCondition != null) {
            WeatherConditionIcon(
                modifier = Modifier.size(80.dp),
                weatherCondition = weatherCondition,
            )
        }
    }
}

@Composable
private fun TodayConditionsTextSection(
    modifier: Modifier = Modifier,
    currentTemp: Int,
    weatherCondition: WeatherCondition?
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 72.sp,
                fontWeight = FontWeight(700),
            ),
            text = "$currentTemp°",
        )

        if (weatherCondition != null) {
            Spacer(modifier = Modifier.height(4.dp))

            val weatherConditionText = when (weatherCondition) {
                WeatherCondition.Cloudy -> stringResource(R.string.weather_condition_partly_cloudy)
                WeatherCondition.Sunny -> stringResource(R.string.weather_condition_sunny)
                WeatherCondition.Rainy -> stringResource(R.string.weather_condition_rainy)
            }

            Text(
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                ),
                text = weatherConditionText,
            )
        }
    }
}

@Composable
private fun WeatherConditionIcon(
    modifier: Modifier = Modifier,
    weatherCondition: WeatherCondition,
) {
    val iconId = when(weatherCondition) {
        WeatherCondition.Cloudy -> R.drawable.lineicons_cloudy_sun
        WeatherCondition.Sunny -> R.drawable.lineicons_sun
        WeatherCondition.Rainy -> R.drawable.lineicons_rain
    }

    Icon(
        modifier = modifier,
        contentDescription = null,
        painter = painterResource(iconId),
        tint = MaterialTheme.colorScheme.primary,
    )
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun WeatherCardPreview() {
    WeatherCard(
        modifier = Modifier
            .widthIn(max = WeatherCardTokens.MaxWidth)
            .heightIn(max = 500.dp)
            .fillMaxWidth(),
        locationText = "New York, NY",
        currentTemp = 68,
        lastUpdated = "11:42",
        weatherCondition = WeatherCondition.Cloudy,
    )
}

private enum class WeatherCondition {
    Cloudy,
    Sunny,
    Rainy,
}
