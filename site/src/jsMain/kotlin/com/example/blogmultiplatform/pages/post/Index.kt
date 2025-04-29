package com.example.blogmultiplatform.pages.post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.ErrorView
import com.example.blogmultiplatform.components.LoadingIndicator
import com.example.blogmultiplatform.components.overFlowSidePanel
import com.example.blogmultiplatform.data.fetchSelectedPost
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Constant.CATEGORY_PARAM
import com.example.blogmultiplatform.models.Constant.POST_ID_PARAM
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.sections.FooterSection
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.util.Constants
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.Res
import com.example.blogmultiplatform.util.parseDateToString
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Page(routeOverride = "post")
@Composable
fun PostPage(){

    val scope = rememberCoroutineScope()
    val context= rememberPageContext()
    val breakpoint = rememberBreakpoint()
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    var apiResponse by remember { mutableStateOf<ApiResponse>(ApiResponse.Idle) }
    val hasCategoryParam = remember(key1 = context.route){
        context.route.params.containsKey(POST_ID_PARAM)
    }

    LaunchedEffect(key1 = context.route){
        if(hasCategoryParam){
            val postId = context.route.params.getValue(POST_ID_PARAM)
            apiResponse = fetchSelectedPost(id= postId)
        }

    }

    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (overFlowMenuOpened) {
            overFlowSidePanel(onMenuClose = {
                overFlowMenuOpened = false
            },
                content = {
                    CategoryNavigationItems(true)
                }
            )
        }
        HeaderSection(
            breakpoint =breakpoint ,
            logo = Res.Image.logo,
            onMenuOpen = {overFlowMenuOpened=true},

        )

        when (apiResponse){
            is ApiResponse.Success ->{
                PostContent(post = (apiResponse as ApiResponse.Success).data)
                scope.launch {
                    delay(50)
                    try{
                        js("hljs.highlightAll()" ) as Unit
                    }catch (e:Exception){

                    }
                }
            }

            is ApiResponse.Idle ->{
                LoadingIndicator()
            }

            is ApiResponse.Error ->{
                ErrorView(message= (apiResponse as ApiResponse.Error).message)
            }
        }

    }

}



@Composable
fun PostContent(post: Post){
    LaunchedEffect(post){
        (document.getElementById(Id.postContent) as HTMLDivElement).innerHTML=post.content
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 50.px, bottom = 200.px)
            .maxWidth(800.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px),
            text = post.date.parseDateToString()
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 20.px)
                .padding(leftRight = 24.px)
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(36.px)
                .color(Colors.Black)
                .textOverflow(TextOverflow.Ellipsis)
                .styleModifier {
                    property("display","-webkit-box")
                    property("-webkit-line-clamp","2")
                    property("line-clamp","2")
                    property("-webkit-box-orient","vertical")
                },
            text = post.title
        )

        Image(
            modifier = Modifier
                .margin(bottom = 40.px)
                .fillMaxWidth()
                .height(600.px),
            src = post.thumbnail
        )

        Div(
            attrs = Modifier
                .id(Id.postContent)
                .fontFamily(FONT_FAMILY)
                .fillMaxWidth()
                .toAttrs()
        )
        FooterSection()
    }
}