package com.example.newsapp.presentation.news_navigatior

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.R
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.bookmark.BookmarkScreen
import com.example.newsapp.presentation.bookmark.BookmarkViewModel
import com.example.newsapp.presentation.details.DetailsScreen
import com.example.newsapp.presentation.details.DetailsViewModel
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.home.HomeViewModel
import com.example.newsapp.presentation.news_navigatior.components.BottomNavigationItem
import com.example.newsapp.presentation.news_navigatior.components.NewsBottomNavigation
import com.example.newsapp.presentation.nvgraph.Route
import com.example.newsapp.presentation.search.SearchScreen
import com.example.newsapp.presentation.search.SearchViewModel

@Composable
fun NewsNavigator() {
    val bottomNavigationItem = remember {
        listOf(
            BottomNavigationItem(R.drawable.ic_home, "Home"),
            BottomNavigationItem(R.drawable.ic_search, "Search"),
            BottomNavigationItem(R.drawable.ic_bookmark, "Bookmark"),
        )
    }
    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    selectedItem = when(backStackState?.destination?.route) {
        Route.HomeScreen.route -> 0
        Route.SearchScreen.route -> 1
        Route.BookmarkScreen.route -> 2
        else -> 0
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NewsBottomNavigation(items = bottomNavigationItem, selected = selectedItem) {index ->
                when(index) {
                    0 -> navigateToTab(navController, Route.HomeScreen.route)
                    1 -> navigateToTab(navController, Route.SearchScreen.route)
                    2 -> navigateToTab(navController, Route.BookmarkScreen.route)
                }
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ){
            composable(Route.HomeScreen.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                val article = viewModel.news.collectAsLazyPagingItems()
                HomeScreen(articles = article, navigateToSearch = { navigateToTab(navController, Route.HomeScreen.route) }) {article ->
                    navigateToDetail(navController, article)
                }
            }
            composable(Route.SearchScreen.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                val state = viewModel.state.value
                SearchScreen(state = state, event = viewModel::onEvent) {
                    navigateToDetail(navController, it)
                }
            }
            composable(Route.DetailScreen.route) {
                val viewModel: DetailsViewModel = hiltViewModel()
                // TODO: Handle Side Effect
                navController.previousBackStackEntry?.savedStateHandle?.get<Article?>("article")?.let { article ->
                    DetailsScreen(article = article, event = viewModel::onEvent) {
                        navController.navigateUp()
                    }
                }
            }
            composable(Route.BookmarkScreen.route) {
                val viewModel: BookmarkViewModel = hiltViewModel()
                val state = viewModel.state.value
                BookmarkScreen(state = state) {
                    navigateToDetail(navController, it)
                }
            }
        }
    }
}

private fun navigateToTab(
    navController: NavController,
    route: String
) {
    navController.navigate(route){
        navController.graph.startDestinationId.let { homeScreen ->
            popUpTo(homeScreen){
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}

private fun navigateToDetail(navController: NavController, article: Article) {
    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
    navController.navigate(
        route = Route.DetailScreen.route
    )
}