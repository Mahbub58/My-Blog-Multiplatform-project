package com.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.util.CollapsedSidePanel

import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.util.Res
import com.example.blogmultiplatform.util.logout
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.dom.svg.Path
import com.varabyte.kobweb.compose.dom.svg.Svg
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.*
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import  com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SidePanel(onMenuClick : () -> Unit){

    val breakpoint = rememberBreakpoint()
    if(breakpoint > Breakpoint.MD){
        SidePanelInternal()
    }else{
        CollapsedSidePanel(onMenuClick= onMenuClick)

    }


}



@Composable
private fun SidePanelInternal() {
    Column(
        modifier = Modifier
            .padding(leftRight = 40.px, topBottom = 50.px)
            .backgroundColor(Theme.Secondary.rgb)
            .width(SIDE_PANEL_WIDTH.px)
            .height(100.vh)
            .position(Position.Fixed)
            .zIndex(9)
    ) {
        Image(
            modifier = Modifier.margin(bottom = 60.px),
            src = Res.Image.logo,
            description = "Logo Image"
        )

        NavigationItemList()


    }
}

@Composable
fun NavigationItemList(){

    val context = rememberPageContext()

    SpanText(
        modifier = Modifier.fontFamily(FONT_FAMILY)
            .fontSize(14.px)
            .margin(bottom = 30.px)
            .color(Theme.HalfWhite.rgb),
        text = "Dashboard"
    )
    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        title = "Home",
        selected = context.route.path==(Screen.AdminHome.route),
        icon = Res.PathIcon.home,
        onClick = {
            context.router.navigateTo(Screen.AdminHome.route)
        }
    )
    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        title = "Create Post",
        selected = context.route.path== Screen.AdminCreate.route,
        icon = Res.PathIcon.create,
        onClick = {
            context.router.navigateTo(Screen.AdminCreate.route)
        }
    )
    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        title = "My Post",
        selected = context.route.path==(Screen.AdminMyPost.route),
        icon = Res.PathIcon.post,
        onClick = {
            context.router.navigateTo(Screen.AdminMyPost.route)
        }
    )
    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        title = "Logout",
        icon = Res.PathIcon.logout ,
        onClick = {
            logout()
            context.router.navigateTo(Screen.AdminLogin.route)
        }
    )
}

@Composable
private fun NavigationItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    title: String,
    icon: String,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .cursor(Cursor.Pointer)
            .onClick { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        VectorIcon(
            modifier=Modifier.margin(right = 10.px),
            pathData = icon,
            color = if(selected) Theme.Primary.hex else Theme.HalfWhite.hex
        )
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .color( color = if(selected) Theme.Primary.rgb else Theme.White.rgb),
            text = title

        )
    }

}


@Composable
private fun VectorIcon(
    modifier: Modifier,
    pathData:String,
    color:String
){
    Svg(
        attrs = modifier
            .id(value = "svgParent")
            .width(24.px)
            .height(24.px)
            .toAttrs{
                attr("viewBox", "0 0 24 24")
                attr("fill", "none")

            }
    ) {
        Path (

            attrs = Modifier
                .id(value = "vectorIcon")
                .toAttrs {
                    attr("d",pathData)
                    attr("stroke",color)
                    attr("stroke-width","2")
                    attr("stroke-linecap","round")
                    attr("stroke-linejoin","round")

                }
        )

    }
}


@Composable
 fun overFlowSidePanel(
    onMenuClose: () -> Unit,
    content: @Composable () -> Unit
 ){

     val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    var scope = rememberCoroutineScope()
    var translateX by remember { mutableStateOf((-100).percent) }
    var opacity by remember { mutableStateOf(0.percent) }



    LaunchedEffect(key1 = breakpoint){
        translateX=0.percent
        opacity=100.percent
        if( breakpoint> Breakpoint.MD){
          scope.launch {
              translateX=(-100).percent
              opacity=0.percent
              delay(600)
              onMenuClose()
          }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.vh)
            .position(Position.Fixed)
            .zIndex(9)
            .opacity(opacity)

            .transition(
              Transition.all(400.ms)
            )
            .backgroundColor(Theme.HalfBlack.rgb)
    )
    {
        Column(
            modifier = Modifier
                .padding(all = 24.px)
                .fillMaxHeight()
                .translateX(translateX)
                .transition(Transition.all(400.ms))
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .width(if(breakpoint<Breakpoint.MD) 50.percent else 25.percent)
                .backgroundColor(Theme.Secondary.rgb)
        ) {
            Row(
                modifier = Modifier
                    .margin(bottom = 60.px, top = 24.px),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FaXmark(
                    modifier = Modifier
                        .margin(right = 20.px)
                        .color(Color.white)
                        .cursor(Cursor.Pointer)
                        .onClick {
                           scope.launch {
                               translateX=(-100).percent
                               opacity=0.percent
                               delay(600)
                               onMenuClose()
                           }
                        },
                    size = IconSize.LG
                )

                Image(
                    modifier = Modifier
                        .margin(bottom = 0.px)
                        .cursor(Cursor.Pointer)
                        .onClick { context.router.navigateTo(Screen.HomePage.route)  },
                    src = Res.Image.logo,
                    description = "Logo Image"
                )
            }
            content()

        }

    }

}