package com.example.blogmultiplatform.pages.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.LoadingIndicator
import com.example.blogmultiplatform.components.overFlowSidePanel
import com.example.blogmultiplatform.data.fetchLatestPost
import com.example.blogmultiplatform.data.searchPostByCategory
import com.example.blogmultiplatform.data.searchPostByTitle
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Constant
import com.example.blogmultiplatform.models.Constant.CATEGORY_PARAM
import com.example.blogmultiplatform.models.Constant.POST_PER_PAGE
import com.example.blogmultiplatform.models.Constant.QUERY_PERMS
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.sections.FooterSection
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.sections.PostSection
import com.example.blogmultiplatform.util.Constants
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.Res
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement


@Page(routeOverride = "query")
@Composable
fun SearchPage(){
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()
    var apiResponse by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    var searchPost = remember { mutableStateListOf<PostWithoutDetails>() }
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    var postSkip by remember { mutableStateOf(0) }
    var showMorePost by remember { mutableStateOf(false) }


    val hasQueryParam = remember(key1 = context.route){
        context.route.params.containsKey(QUERY_PERMS)
    }

    val hasCategoryParam = remember(key1 = context.route){
        context.route.params.containsKey(CATEGORY_PARAM)
    }
    val value = remember(key1 = context.route){
        if(hasCategoryParam){
            context.route.params.getValue(CATEGORY_PARAM)
        }else if(hasQueryParam){
            context.route.params.getValue(QUERY_PERMS)
        }
        else{
            ""
        }
    }

    LaunchedEffect(key1 = context.route){
        (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value =""
        postSkip=0
        showMorePost=false
        if(hasCategoryParam){
            searchPostByCategory(
                category = kotlin.runCatching { Category.valueOf(value) }.getOrElse { Category.Programing },
                skip = postSkip ,
                onSuccess = {
                    apiResponse = it
                    if(it is ApiListResponse.Success){
                        searchPost.clear()
                        searchPost.addAll(it.data)
                        postSkip += POST_PER_PAGE
                        if(it.data.size >= POST_PER_PAGE){
                            showMorePost=true
                        }
                    }
                },
                onError = {}
            )
        }else if(hasQueryParam){
            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = value.replace("%20"," ")
            searchPostByTitle(
                query = value,
                skip = postSkip ,
                onSuccess = {
                    apiResponse = it
                    if(it is ApiListResponse.Success){
                        searchPost.clear()
                        searchPost.addAll(it.data)
                        postSkip += POST_PER_PAGE
                        if(it.data.size >= POST_PER_PAGE){
                            showMorePost=true
                        }
                    }
                },
                onError = {}
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overFlowMenuOpened) {
            overFlowSidePanel(
                onMenuClose = {
                    overFlowMenuOpened = false
                },
                content = {
                    CategoryNavigationItems(
                        vertical = true,
                        selectedCategory = if (hasCategoryParam) kotlin.runCatching {
                            Category.valueOf(
                                value
                            )
                        }
                            .getOrElse { Category.Programing } else null,
                    )
                }
            )
        }

        HeaderSection(
            breakpoint = breakpoint,
            logo = Res.Image.logo,
            selectedCategory = if (hasCategoryParam) kotlin.runCatching { Category.valueOf(value) }
                .getOrElse { Category.Programing } else null,
            onMenuOpen = { overFlowMenuOpened = true }

        )

        if (apiResponse is ApiListResponse.Success) {

            if (hasCategoryParam) {
                SpanText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .margin(top = 50.px)
                        .textAlign(TextAlign.Center)
                        .fontFamily(FONT_FAMILY)
                        .fontStyle(FontStyle.Revert)
                        .fontSize(36.px),
                    text = if (value.isEmpty()) Category.Programing.name else value
                )
            }


            PostSection(
                breakpoint = breakpoint,
                post = searchPost,
                showMoreVisibility = showMorePost,
                shoMore = {
                    scope.launch {
                        if (hasCategoryParam) {
                            searchPostByCategory(
                                category = kotlin.runCatching { Category.valueOf(value) }
                                    .getOrElse { Category.Programing },
                                skip = postSkip,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success)
                                        if (response.data.isNotEmpty()) {
                                            if (response.data.size < POST_PER_PAGE) {
                                                showMorePost = false
                                            }
                                            searchPost.addAll(response.data)
                                            postSkip += POST_PER_PAGE
                                        } else {
                                            showMorePost = false
                                        }

                                },
                                onError = {}
                            )
                        } else if (hasQueryParam) {
                            searchPostByTitle(
                                query = value,
                                skip = postSkip,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success)
                                        if (response.data.isNotEmpty()) {
                                            if (response.data.size < POST_PER_PAGE) {
                                                showMorePost = false
                                            }
                                            searchPost.addAll(response.data)
                                            postSkip += POST_PER_PAGE
                                        } else {
                                            showMorePost = false
                                        }

                                },
                                onError = {}
                            )
                        }
                    }
                },
                onClick = {}
            )
        }else{
            LoadingIndicator()
        }
        FooterSection()

    }

}