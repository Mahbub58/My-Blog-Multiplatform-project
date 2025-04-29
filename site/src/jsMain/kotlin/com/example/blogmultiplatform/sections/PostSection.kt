package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.components.PostView
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.util.Constants.PAGE_WIDTH
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.px


@Composable
fun PostSection(
    title:String?=null,
    breakpoint: Breakpoint,
    post: List<PostWithoutDetails>,
    showMoreVisibility:Boolean,
    shoMore: () -> Unit,
    onClick : (String) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .margin(topBottom = 50.px)
        .maxWidth(PAGE_WIDTH.px),
        contentAlignment = Alignment.Center

    ){
        PostView(
            breakpoint = breakpoint,
            post = post,
            title = title,
            showMoreVisibility = showMoreVisibility,
            showMore = shoMore,
            onClick = onClick

        )
    }
}