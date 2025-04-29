package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.util.Constants
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px

@Composable
fun FooterSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb)
            .padding(topBottom = 50.px),
        contentAlignment = Alignment.Center
    ){
        Row(){
            SpanText(
                modifier = Modifier
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(18.px)
                    .color(Colors.White)
                    .fontWeight(FontWeight.Medium),
                text = "Copyright © 2023 • "
            )

            SpanText(
                modifier = Modifier
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(14.px)
                    .color(Theme.Primary.rgb)
                    .fontWeight(FontWeight.Medium),
                text = "Mahbub Alam"
            )
        }

    }
}