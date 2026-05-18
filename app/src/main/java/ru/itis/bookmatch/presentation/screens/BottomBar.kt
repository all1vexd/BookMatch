package ru.itis.bookmatch.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomBar(
    selected: Screen,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {

    val screens = listOf(Screen.Discover, Screen.Saved, Screen.Library, Screen.Profile)

    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(0.3f))
        )

        Spacer(modifier = Modifier.height(12.dp))

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
            tonalElevation = 0.dp,
            modifier = modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {

            screens.forEach { screen ->
                NavigationBarItem(
                    selected = selected == screen,
                    onClick = {
                        onTabSelected(screen)
                    },
                    icon = {
                        Icon(
                            imageVector = screen.icon ?: Icons.Default.QuestionMark,
                            contentDescription = screen.title,
                            modifier = Modifier.height(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = screen.title.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}