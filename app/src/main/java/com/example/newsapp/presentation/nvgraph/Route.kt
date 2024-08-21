package com.example.newsapp.presentation.nvgraph

sealed class Route(
    val route: String
) {
    object OnBoardingScreen: Route("onBoardingScreen")
    object HomeScreen: Route("homeScreen")
    object SearchScreen: Route("searchScreen")
    object BookmarkScreen: Route("bookmarkScreen")
    object DetailScreen: Route("detailScreen")

    // Sub Navigation
    object AppStartNavigation: Route("AppStartNavigation")
    object NewsNavigation: Route("newsNavigation")
    object NewsNavigatorScreen: Route("newsNavigatorScreen")
}