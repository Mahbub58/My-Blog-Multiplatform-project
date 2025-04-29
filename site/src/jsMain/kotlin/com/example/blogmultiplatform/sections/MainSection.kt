package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.components.PostPreview
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.util.Constants.PAGE_WIDTH
import com.varabyte.kobweb.compose.css.margin
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px


@Composable
fun MainSection(
    post:ApiListResponse,
    breakpoint: Breakpoint,
    onClick: (String) -> Unit
) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .maxWidth(PAGE_WIDTH.px)
            .backgroundColor(Theme.Secondary.rgb),
            contentAlignment = Alignment.Center
        ){
            when(post){
                is ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> {
                    MainPost(
                        post.data, breakpoint = breakpoint,
                        onClick = onClick
                    )
                }
                is ApiListResponse.Error ->{}
            }
        }
    }

}


@Composable
fun MainPost(
    post: List<PostWithoutDetails>,
    breakpoint: Breakpoint,
    onClick: (String) -> Unit
    ){

    Row(
        modifier = Modifier
            .fillMaxWidth(
                if(breakpoint > Breakpoint.MD) 80.percent
                else 90.percent
            )
            .margin(topBottom = 50.px)
    ) {
        if (breakpoint == Breakpoint.XL) {
            PostPreview(
                post = post.first(),
                darkTheme = true,
                thumbnailHeight = 640.px,
                onClick = {onClick(post.first()._id)}
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(80.percent)
                    .margin(left = 20.px)
            ) {
                post.drop(1).forEach { PostWithoutDetails ->
                    PostPreview(
                        post = PostWithoutDetails,
                        darkTheme = true,
                        vertical = false,
                        thumbnailHeight = 200.px,
                        titleMaxLength = 1,
                        onClick = {onClick(PostWithoutDetails._id)}
                    )
                }
            }
        } else if(breakpoint >= Breakpoint.LG){
            Box( modifier = Modifier.margin(right = 10.px)) {
                PostPreview(
                    post = post.first(),
                    darkTheme = true,
                    onClick = {onClick(post.first()._id)}

                )
            }
            Box( modifier = Modifier.margin(left = 10.px)) {
                PostPreview(
                    post = post[1],
                    darkTheme = true,
                    onClick = {onClick(post[1]._id)}
                )
            }


        }else{
            PostPreview(
                post = post.first(),
                darkTheme = true,
                thumbnailHeight = 640.px,
                onClick = {onClick(post.first()._id)}
            )
        }
    }

}