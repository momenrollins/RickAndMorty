package com.momen.rickandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.momen.rickandmorty.presentation.ui.characterdetail.CharacterDetailScreen
import com.momen.rickandmorty.presentation.ui.characterlist.CharacterListScreen
import kotlinx.serialization.Serializable
import com.momen.rickandmorty.domain.model.Character

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = CharacterList) {
        composable<CharacterList> {
            CharacterListScreen(navController = navController)
        }
        composable<Character> { backStackEntry ->
            val character: Character = backStackEntry.toRoute()
            CharacterDetailScreen(
                navController = navController,
                character = character
            )
        }
    }
}

@Serializable
object CharacterList
