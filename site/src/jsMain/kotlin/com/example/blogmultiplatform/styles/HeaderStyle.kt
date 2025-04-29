package com.example.blogmultiplatform.styles

import com.example.blogmultiplatform.models.Theme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.anyLink
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.ms


val CategoryIemStyle = CssStyle {

    anyLink{
        Modifier.color(Theme.White.rgb)
    }

    base{
        Modifier
            .color(Theme.White.rgb)
            .transition(transition = Transition.of(property = "color", duration = 300.ms))
    }
    hover{
        Modifier.color(Theme.Primary.rgb)
    }
}