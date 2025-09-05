package com.momen.rickandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.momen.rickandmorty.presentation.ui.characterlist.CharacterListScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "character_list") {
        composable("character_list") {
            CharacterListScreen(navController = navController)
        }

    }
}