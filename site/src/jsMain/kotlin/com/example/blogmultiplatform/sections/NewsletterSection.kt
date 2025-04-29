package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.MessagePopup
import com.example.blogmultiplatform.data.subscribeToNewsletter
import com.example.blogmultiplatform.models.Newsletter
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.styles.NewsletterInputStyle
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Constants.PAGE_WIDTH
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.noBorder
import com.example.blogmultiplatform.util.validateEmail
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
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
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement


@Composable
fun NewsletterSection(
    breakpoint: Breakpoint
) {

    val scope = rememberCoroutineScope()
    var responseMessage by remember { mutableStateOf("") }
    var invalidEmailpopup by remember { mutableStateOf(false) }
    var subscribeEmailpopup by remember { mutableStateOf(false) }

    if(invalidEmailpopup){
        MessagePopup(
            message = "Email address is not valid.",
            onDialogDismiss = {
                invalidEmailpopup=false
            }
        )
    }

    if(subscribeEmailpopup){
        MessagePopup(
            message = responseMessage,
            onDialogDismiss = {
                subscribeEmailpopup=false
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(PAGE_WIDTH.px)
            .margin(topBottom = 250.px)
    ){

        NewsletterContent(
            breakpoint=breakpoint,
            onSubscribe = {
                responseMessage=it
                scope.launch {
                    subscribeEmailpopup=true
                    delay(2000)
                    subscribeEmailpopup=false
                }
            },
            onInvalidEmail = {
                scope.launch {
                    invalidEmailpopup=true
                    delay(2000)
                    invalidEmailpopup=false
                }
            }
        )

    }
}




@Composable
fun NewsletterContent(
    breakpoint: Breakpoint,
    onSubscribe: (String) -> Unit,
    onInvalidEmail: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(36.px)
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center),
            text = "Don't miss any New Post."
        )
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(36.px)
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center),
            text = "Sign up to our Newsletter."
        )
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(18.px)
                .fontWeight(FontWeight.Normal)
                .color(Theme.HalfBlack.rgb)
                .margin(top = 6.px)
                .textAlign(TextAlign.Center),
            text = "Keep up with the latest news and blogs."
        )

        if(breakpoint  > Breakpoint.SM){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(top = 40.px),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                SubscribeForm(
                    vertical = false,
                    onSubscribe = onSubscribe,
                    onInvalidEmail = onInvalidEmail
                )
            }
        }else{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(top = 40.px),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){

                SubscribeForm(
                    vertical = true,
                    onSubscribe = onSubscribe,
                    onInvalidEmail = onInvalidEmail
                )
            }
        }
    }
}




@Composable
fun SubscribeForm(
    vertical:Boolean,
    onSubscribe: (String) -> Unit,
    onInvalidEmail: () -> Unit
){
    val scope= rememberCoroutineScope()
    Input(
        type = InputType.Text,
        attrs = NewsletterInputStyle.toModifier()
            .id(Id.emailInput)
            .width(320.px)
            .height(54.px)
            .color(Theme.DarkGray.rgb)
            .backgroundColor(Theme.Gray.rgb)
            .padding(leftRight = 24.px)
            .margin(
                right = if(vertical) 0.px else 20.px,
                bottom = if(vertical) 0.px else 0.px )
            .fontFamily(FONT_FAMILY)
            .fontSize(16.px)
            .borderRadius(100.px)
            .toAttrs{
                attr("placeholder","Your email address")
            }
    )

    Button(
        attrs = Modifier
            .onClick {
                val email = (document.getElementById(Id.emailInput) as HTMLInputElement).value
                if(validateEmail(email=email)){
                  scope.launch {   onSubscribe(
                      subscribeToNewsletter(newsletter = Newsletter(email = email))
                  )
                  }
                }else{
                    onInvalidEmail()
                }
            }
            .height(54.px)
            .borderRadius(100.px)
            .padding(leftRight = 50.px)
            .noBorder()
            .cursor(Cursor.Pointer)
            .backgroundColor(Theme.Primary.rgb)
            .toAttrs()
    ){
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(18.px)
                .fontWeight(FontWeight.Medium)
                .color(Colors.White),
            text = "Subscribe"
        )
    }
}







