package com.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MovableContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.util.Constants.ADMIN_DASHBOARD_WIDTH
import com.example.blogmultiplatform.util.Constants.COLLAPSED_PANNEL_HEIGHT
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.px


@Composable
fun AdminPanelLayout(content: @Composable () -> Unit) {
    var  overFlowMenuOpened by remember { mutableStateOf(false) }
    val breakpoint = rememberBreakpoint()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .maxWidth(ADMIN_DASHBOARD_WIDTH.px)

        ) {
            SidePanel(onMenuClick = {
                overFlowMenuOpened = true
            })
            if (overFlowMenuOpened) {
                overFlowSidePanel(onMenuClose = {
                    overFlowMenuOpened = false
                },
                    content = {
                        NavigationItemList()
                    }
                    )
            }

            content()
        }
    }
}