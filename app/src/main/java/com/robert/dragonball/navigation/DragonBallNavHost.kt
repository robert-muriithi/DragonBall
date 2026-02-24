package com.robert.dragonball.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.robert.character_details.CharacterDetailsScreen
import com.robert.character_details.CharacterDetailsViewModel
import com.robert.characters.CharactersScreen
import com.robert.characters.CharactersViewModel
import com.robert.navigation.Destination

@Composable
fun DragonBallNavHost(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Destination.CharacterList,
            modifier = modifier
        ) {
            composable<Destination.CharacterList> {
                val viewModel = hiltViewModel<CharactersViewModel>()

                CharactersScreen(
                    viewModel = viewModel,
                    animatedVisibilityScope = this@composable,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle,
                    onCharacterClick = { characterId ->
                        navController.navigate(Destination.CharacterDetails(characterId))
                    }
                )
            }

            composable<Destination.CharacterDetails> { backStackEntry ->
                val args = backStackEntry.toRoute<Destination.CharacterDetails>()
                val viewModel : CharacterDetailsViewModel = hiltViewModel(
                    creationCallback = { factory : CharacterDetailsViewModel.Factory ->
                        factory.create(characterId = args.characterId)
                    }
                )

                CharacterDetailsScreen(
                    viewModel = viewModel,
                    animatedVisibilityScope = this@composable,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

