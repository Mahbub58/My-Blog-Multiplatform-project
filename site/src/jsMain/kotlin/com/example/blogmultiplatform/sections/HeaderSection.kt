package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.SearchBar
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.util.Constants.HEADER_HEIGHT
import com.example.blogmultiplatform.util.Constants.PAGE_WIDTH
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.Res

import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer

import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import kotlinx.browser.document
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement


@Composable
fun HeaderSection(breakpoint: Breakpoint,
                  logo: String = Res.Image.logoHome,
                  selectedCategory:Category?=null,
                 onMenuOpen : () -> Unit ){
    Box(modifier = Modifier
        .fillMaxWidth()
        .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb)
            .maxWidth(PAGE_WIDTH.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Header(breakpoint=breakpoint,
                onMenuOpen=onMenuOpen,
                logo = logo,
                selectedCategory = selectedCategory
            )
        }
    }
}




@Composable
fun Header(breakpoint: Breakpoint,
           logo: String,
           selectedCategory:Category?,
           onMenuOpen: () -> Unit){
    var fullSearchBaraOpened by remember { mutableStateOf(false) }
    val context = rememberPageContext()

    Row(modifier = Modifier
        .fillMaxWidth(if(breakpoint > Breakpoint.MD) 80.percent else 90.percent)
        .height(HEADER_HEIGHT.px),
        verticalAlignment = Alignment.CenterVertically
    ){
        if(breakpoint <= Breakpoint.MD){
            if(fullSearchBaraOpened) {
                FaXmark(
                    modifier = Modifier
                        .margin(right = 24.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick { fullSearchBaraOpened = false },
                    size = IconSize.XL
                )
            }
            if(!fullSearchBaraOpened) {
                FaBars(
                    modifier = Modifier
                        .margin(right = 24.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick { onMenuOpen() },
                    size = IconSize.XL
                )
            }
        }
        if(!fullSearchBaraOpened) {
            Image(
                modifier = Modifier
                    .margin(right = 50.px)
                    .width(if (breakpoint >= Breakpoint.SM) 100.px else 70.px)
                    .cursor(Cursor.Pointer)
                    .onClick {context.router.navigateTo(Screen.HomePage.route) },
                src = logo,
                description = "Logo Home"
            )
        }
        if(breakpoint >= Breakpoint.LG){
            CategoryNavigationItems(selectedCategory = selectedCategory)
        }
        Spacer()
        SearchBar(
            breakpoint = breakpoint,
            fullwidth = fullSearchBaraOpened,
            darkTheme = true,
            onEnterClick = {
                val query = (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value
                context.router.navigateTo(Screen.SearchPage.searchByTitle(
                    query = query
            ))
                           },
            searchIconClick = {fullSearchBaraOpened=it}

        )
    }
}




