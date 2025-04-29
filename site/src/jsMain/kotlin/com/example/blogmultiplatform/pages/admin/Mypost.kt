package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.components.AdminPanelLayout
import com.example.blogmultiplatform.components.PostView
import com.example.blogmultiplatform.components.SearchBar
import com.example.blogmultiplatform.data.deleteSelectedPost
import com.example.blogmultiplatform.data.fetchMyPost
import com.example.blogmultiplatform.data.searchPostByTitle
import com.example.blogmultiplatform.models.Constant.POST_PER_PAGE
import com.example.blogmultiplatform.models.Constant.QUERY_PERMS
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY

import com.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.isUserLoggedIn
import com.example.blogmultiplatform.util.noBorder
import com.example.blogmultiplatform.util.pressSwitchText
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext

import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.w3c.dom.HTMLInputElement

@Page
@Composable
fun MyPostPage() {

    isUserLoggedIn {
        MyPostScreen()
    }

}


@Composable
fun MyPostScreen(){
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()
    var seclectableMode by remember { mutableStateOf(false) }
    var postToSkip by remember { mutableStateOf(0) }
    var shoMoreVisibility by remember { mutableStateOf(false) }
    var switchText by remember { mutableStateOf("Select") }
    var myPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var selectedPost = remember { mutableStateListOf<String>() }
    val hasParams = remember(key1 = context.route ) { context.route.params.containsKey(QUERY_PERMS)  }
    var query = remember(key1 = context.route) {
        try{
            context.route.params.getValue(QUERY_PERMS)
        }catch (e:Exception){
            ""
        }
    }

   // var myPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Loading) }


    LaunchedEffect(context.route) {
        postToSkip=0
        if (hasParams) {
            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value=query.replace("%20"," ")
            searchPostByTitle(
                query = query,
                skip = postToSkip,
                onSuccess = {
                    if (it is ApiListResponse.Success) {
                        myPosts.clear()
                        myPosts.addAll(it.data)
                        postToSkip += POST_PER_PAGE
                        shoMoreVisibility = it.data.size >= POST_PER_PAGE
                    }
                },
                onError = {
                    println(it)
                }

            )
        } else {
            fetchMyPost(
                skip = postToSkip,
                onSuccess = {
                    if (it is ApiListResponse.Success) {
                        myPosts.clear()
                        myPosts.addAll(it.data)
                        postToSkip += POST_PER_PAGE
                        shoMoreVisibility = it.data.size >= POST_PER_PAGE
                    }
                },
                onError = {
                    println("onError " + it)
                }
            )
        }
    }



   AdminPanelLayout {
       Column(
           modifier = Modifier
               .fillMaxSize()
               .margin(topBottom = 50.px)
               .padding(left = if(breakpoint> Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px ),
           verticalArrangement = Arrangement.Top,
           horizontalAlignment = Alignment.CenterHorizontally
       ){
           Box(modifier = Modifier
               .margin(bottom = 24.px)
               .fillMaxWidth(
                   if(breakpoint < Breakpoint.MD) 40.percent else 60.percent
               ),
               contentAlignment = Alignment.Center
           ) {
               SearchBar (
                   breakpoint,
                   modifier = Modifier
                       .visibility(if(seclectableMode) Visibility.Hidden else Visibility.Visible)
                       .transition(Transition.all(300.ms)),
                   onEnterClick = {
                   val query = (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value
                   if(query.isNotEmpty()){
                       context.router.navigateTo(Screen.AdminMyPost.searchByTitle(query= query))
                   }else{
                       context.router.navigateTo(Screen.AdminMyPost.route)
                   }
               },
                   searchIconClick = {}
                   )
           }

           Row(
               modifier = Modifier
                   .fillMaxWidth(if(breakpoint > Breakpoint.MD) 80.percent else 90.percent)
                   .margin(bottom = 24.px),
               verticalAlignment = Alignment.Bottom,
               horizontalArrangement = Arrangement.SpaceBetween

           ){
               Row(verticalAlignment = Alignment.CenterVertically){
                   Switch(
                       modifier = Modifier
                           .margin(right = 8.px),
                       size = SwitchSize.LG,
                       checked = seclectableMode,
                       onCheckedChange = {
                           seclectableMode=it
                           if(!seclectableMode){
                               switchText="Select"
                               selectedPost.clear()
                           }else{
                               switchText=" 0 Post Selected"
                           }
                       }
                   )
                   SpanText(modifier = Modifier
                       .color(if(seclectableMode) Colors.Black else Theme.HalfBlack.rgb),
                       text = switchText
                       )
               }

               Button(attrs = Modifier
                   .height(54.px)
                   .margin(right = 15.px)
                   .padding(leftRight = 24.px)
                   .color(Colors.White)
                   .noBorder()
                   .backgroundColor(Colors.Red)
                   .borderRadius(10.px)
                   .fontFamily(FONT_FAMILY)
                   .fontSize(14.px)
                   .visibility(if(selectedPost.toList().size>0) Visibility.Visible else Visibility.Hidden )
                   .onClick {
                       scope.launch {
                           val result = deleteSelectedPost(ids = selectedPost)
                           if(result){
                               seclectableMode=false
                               switchText="Select"
                               postToSkip -=selectedPost.size
                               selectedPost.forEach { deletedPostId ->
                                   myPosts.removeAll {
                                       it._id == deletedPostId
                                   }

                               }
                               selectedPost.clear()
                           }
                       }

                   }
                   .toAttrs ()
               ){
                   SpanText("Delete")
               }

           }
           PostView(post=myPosts, breakpoint = breakpoint,
               selectableMode = seclectableMode ,
               onSelect = {
                   selectedPost.add(it)
                   switchText = pressSwitchText(selectedPost.toList())
               },
               onDeselect = {
                   selectedPost.remove(it)
                   switchText = pressSwitchText(selectedPost.toList())
               },
               showMoreVisibility = shoMoreVisibility,
               showMore = {
                   scope.launch {
                       if(hasParams){
                           searchPostByTitle(
                               query = query,
                               skip = postToSkip,
                               onSuccess = {
                                   if(it is ApiListResponse.Success){
                                       if(it.data.isNotEmpty()){
                                           myPosts.addAll(it.data)
                                           postToSkip+=POST_PER_PAGE
                                           if(it.data.size < POST_PER_PAGE) shoMoreVisibility=false
                                       }else{
                                           shoMoreVisibility = false
                                       }
                                   }
                               },
                               onError = {
                                   println("onError "+it)
                               }
                           )
                       }else{
                           fetchMyPost(
                               skip = postToSkip,
                               onSuccess = {
                                   if(it is ApiListResponse.Success){
                                       if(it.data.isNotEmpty()){
                                           myPosts.addAll(it.data)
                                           postToSkip+=POST_PER_PAGE
                                           if(it.data.size < POST_PER_PAGE) shoMoreVisibility=false
                                       }else{
                                           shoMoreVisibility = false
                                       }
                                   }
                               },
                               onError = {
                                   println("onError "+it)
                               }
                           )
                       }

                   }
               },
               onClick = {
                   context.router.navigateTo(Screen.AdminCreate.passPostId(id =it))
               }
           )
       }
   }
}