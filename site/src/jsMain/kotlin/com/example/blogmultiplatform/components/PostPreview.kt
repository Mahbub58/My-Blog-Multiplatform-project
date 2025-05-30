package com.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.styles.PostPreviewStyle
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.parseDateToString
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextOverflow
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
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput

@Composable
fun PostPreview(
    modifier: Modifier=Modifier,
    post:PostWithoutDetails,
    selectableMode:Boolean=false,
    darkTheme:Boolean = false,
    vertical : Boolean =true,
    titleMaxLength: Int = 2,
    titleColor: CSSColorValue = Colors.Black,
    thumbnailHeight : CSSSizeValue<CSSUnit.px> = 320.px,
    onSelect: (String) -> Unit = {},
    onDeselect: (String) -> Unit = {},
    onClick : (String) -> Unit ={}
) {
    val context = rememberPageContext()
    var checked by remember(selectableMode){ mutableStateOf(false) }

    if(vertical) {
        Column(
            modifier = PostPreviewStyle.toModifier()
                .then(modifier)
            .fillMaxSize(if(darkTheme) 100.percent
            else if(titleColor == Theme.Sponsored.rgb) 100.percent
            else 95.percent)
            .margin(bottom = 44.px)
            .cursor(Cursor.Pointer)
            .padding(all = if (selectableMode) 10.px else 0.px)
            .border(
                width = if (selectableMode) 4.px else 0.px,
                style = if (selectableMode) LineStyle.Solid else LineStyle.None,
                color = if (checked) Theme.Primary.rgb else Theme.Gray.rgb
            )
            .onClick {
                if (selectableMode) {
                    checked = !checked
                    if (checked) {
                        onSelect(post._id)
                    } else {
                        onDeselect(post._id)
                    }
                } else {
                    onClick(post._id)
                }
            }
            .transition(Transition.all(300.ms))

        ) {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                checked = checked,
                vertical = vertical,
                thumbnailHeight = thumbnailHeight,
                titleMaxLength = titleMaxLength,
                titleColor = titleColor
            )


        }
    }else{
        Row(modifier = PostPreviewStyle.toModifier()
            .then(modifier)
            .cursor(Cursor.Pointer)
            .onClick { onClick(post._id) }
        ){
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                checked = checked,
                vertical = vertical,
                thumbnailHeight = thumbnailHeight,
                titleMaxLength = titleMaxLength,
                titleColor = titleColor

            )
        }
    }
}


@Composable
fun PostContent(
    post: PostWithoutDetails,
    selectableMode:Boolean=false,
    darkTheme:Boolean = false,
    vertical:Boolean,
    titleMaxLength: Int,
    titleColor: CSSColorValue,
    thumbnailHeight : CSSSizeValue<CSSUnit.px>,
    checked : Boolean,

){

    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(thumbnailHeight?: 320.px)
            .margin( bottom = if(darkTheme) 20.px else 16.px)
            .objectFit(objectFit = ObjectFit.Cover),
        src = post.thumbnail,
        description = "post thumbnail image"
    )
   Column(modifier = Modifier.fillMaxWidth()
       .thenIf(
           condition = !vertical,
           other = Modifier.margin(left = 20.px)
       )){
       SpanText(
           modifier = Modifier
               .fontFamily(FONT_FAMILY)
               .fontSize(12.px)
               .color(if(darkTheme) Theme.HalfWhite.rgb else Theme.HalfBlack.rgb),
           text = "${post.date.parseDateToString()}"
       )
       SpanText(
           modifier = Modifier
               .fontFamily(FONT_FAMILY)
               .fontSize(18.px)
               .margin(bottom = 12.px)
               .color(if(darkTheme) Colors.White else titleColor)
               .textOverflow(TextOverflow.Ellipsis)
               .overflow(Overflow.Hidden)
               .styleModifier {
                   property("display","-webkit-box")
                   property("-webkit-line-clamp","$titleMaxLength")
                   property("line-clamp","$titleMaxLength")
                   property("-webkit-box-orient","vertical")
               },
           text = post.title
       )
       SpanText(
           modifier = Modifier
               .fontFamily(FONT_FAMILY)
               .fontSize(14.px)
               .margin(bottom = 10.px)
               .color(if(darkTheme) Colors.White else Colors.Black)
               .textOverflow(TextOverflow.Ellipsis)
               .overflow(Overflow.Hidden)
               .styleModifier {
                   property("display","-webkit-box")
                   property("-webkit-line-clamp","3")
                   property("line-clamp","3")
                   property("-webkit-box-orient","vertical")
               },
           text = post.subtitle
       )


       Row(modifier = Modifier
           .fillMaxWidth(),
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically
       ){
           CategoryChip(
               category = post.category,
               darkTheme=darkTheme
           )

           if(selectableMode){
               CheckboxInput(
                   checked = checked,
                   attrs = Modifier
                       .size(20.px)
                       .color(Theme.Primary.rgb)
                       .cursor(Cursor.Pointer)
                       .backgroundColor(Theme.Primary.rgb)
                       .border(
                           color = Theme.Primary.rgb
                       )
                       .toAttrs()
               )
           }

       }
   }
}

