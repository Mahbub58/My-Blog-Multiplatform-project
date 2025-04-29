package com.example.blogmultiplatform.pages

import androidx.compose.runtime.*
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.NavigationItemList
import com.example.blogmultiplatform.components.overFlowSidePanel
import com.example.blogmultiplatform.data.fetchLatestPost
import com.example.blogmultiplatform.data.fetchMainPost
import com.example.blogmultiplatform.data.fetchPopularPost
import com.example.blogmultiplatform.data.fetchSponsoredPost
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.Constant.POST_PER_PAGE
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.sections.FooterSection
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.sections.MainSection
import com.example.blogmultiplatform.sections.NewsletterSection
import com.example.blogmultiplatform.sections.PostSection
import com.example.blogmultiplatform.sections.SponsoredPostSection
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.dom.Text
import com.varabyte.kobweb.worker.rememberWorker
import kotlinx.coroutines.launch


@Page
@Composable
fun HomePage() {

    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    var mainPost by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    var latestPost = remember { mutableStateListOf<PostWithoutDetails>() }
    var popularPost = remember { mutableStateListOf<PostWithoutDetails>() }
    var sponsoredPost = remember { mutableStateListOf<PostWithoutDetails>() }
    var latestPostToSkip by remember { mutableStateOf(0) }
    var showMoreLatest by remember { mutableStateOf(false) }
    var popularPostToSkip by remember { mutableStateOf(0) }
    var showMorePopular by remember { mutableStateOf(false) }



    LaunchedEffect(Unit){
        fetchMainPost(
            onSuccess = {
                mainPost  = it
                //println(mainPost)
            },
            onError = {}
        )
        fetchLatestPost(
            skip = latestPostToSkip,
            onSuccess = {
                if(it is ApiListResponse.Success){
                    latestPost.addAll(it.data)
                    latestPostToSkip += POST_PER_PAGE
                    if(it.data.size >= POST_PER_PAGE){
                        showMoreLatest=true
                    }
                }
                println(it)
            },
            onError = {

            }
        )

        fetchSponsoredPost(
            onSuccess = {
                if(it is ApiListResponse.Success){
                    sponsoredPost.addAll(it.data)
                }
            },
            onError = {

            }
        )
        fetchPopularPost(
            skip = popularPostToSkip,
            onSuccess = {
                if(it is ApiListResponse.Success){
                    popularPost.addAll(it.data)
                    popularPostToSkip += POST_PER_PAGE
                    if(it.data.size >= POST_PER_PAGE){
                        showMorePopular=true
                    }
                }
                println(it)
            },
            onError = {

            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpen = {overFlowMenuOpened = true}
        )

        if (overFlowMenuOpened) {
            overFlowSidePanel(onMenuClose = {
                overFlowMenuOpened = false
            },
                content = {
                    CategoryNavigationItems(true)
                }
            )
        }

        MainSection(breakpoint = breakpoint, post = mainPost,
            onClick = {context.router.navigateTo(Screen.PostPage.getPost(id = it))})

        PostSection(
            breakpoint = breakpoint,
            post = latestPost,
            title = "Latest Post",
            showMoreVisibility =showMoreLatest ,
            shoMore = {
                scope.launch {
                    fetchLatestPost(
                        skip = latestPostToSkip,
                        onSuccess = { response ->
                            if(response is ApiListResponse.Success)
                            if(response.data.isNotEmpty()){
                                if (response.data.size < POST_PER_PAGE){
                                    showMoreLatest = false
                                }
                                latestPost.addAll(response.data)
                                latestPostToSkip += POST_PER_PAGE
                            }else{
                                showMoreLatest= false
                            }

                        },
                        onError = {}
                    )
                }

            },
            onClick = {
                context.router.navigateTo(Screen.PostPage.getPost(id = it))
            }
        )

        SponsoredPostSection(
            breakpoint = breakpoint,
            post = sponsoredPost,
            onClick = {
                context.router.navigateTo(Screen.PostPage.getPost(id = it))
            }
        )

        PostSection(
            breakpoint = breakpoint,
            post = popularPost,
            title = "Popular Post",
            showMoreVisibility =showMorePopular ,
            shoMore = {
                scope.launch {
                    fetchPopularPost(
                        skip = popularPostToSkip,
                        onSuccess = { response ->
                            if(response is ApiListResponse.Success)
                                if(response.data.isNotEmpty()){
                                    if (response.data.size < POST_PER_PAGE){
                                        showMorePopular = false
                                    }
                                    popularPost.addAll(response.data)
                                    popularPostToSkip += POST_PER_PAGE
                                }else{
                                    showMorePopular= false
                                }

                        },
                        onError = {}
                    )
                }

            },
            onClick = {
                context.router.navigateTo(Screen.PostPage.getPost(id = it))
            }
        )

        NewsletterSection(breakpoint=breakpoint)

        FooterSection()
    }
}
