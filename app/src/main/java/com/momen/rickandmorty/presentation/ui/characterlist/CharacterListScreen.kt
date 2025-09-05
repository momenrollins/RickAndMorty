package com.momen.rickandmorty.presentation.ui.characterlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.momen.rickandmorty.presentation.ui.characterlist.components.CharacterListItem
import com.momen.rickandmorty.presentation.ui.characterlist.components.LoadingItem
import com.momen.rickandmorty.presentation.ui.characterlist.components.SearchBar
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            Pair(totalItemsCount, lastVisibleItemIndex)
        }
            .distinctUntilChanged()
            .collect { (totalItemsCount, lastVisibleItemIndex) ->
                if (lastVisibleItemIndex >= totalItemsCount - 3 && // within last 3
                    !state.isLoadingMore &&
                    state.searchQuery.isBlank()
                ) {
                    viewModel.onEvent(CharacterListEvent.LoadMore)
                }
            }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { viewModel.onEvent(CharacterListEvent.Search(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.characters) { character ->
                CharacterListItem(
                    character = character,
                    onItemClick = {
                        navController.navigate(character)
                    }
                )
            }
            if (state.isLoadingMore) {
                item {
                    LoadingItem()
                }
            }
            if (state.error.isNotBlank()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.onEvent(CharacterListEvent.Retry) }) {
                            Text("Retry")
                        }
                    }
                }
            }

        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}