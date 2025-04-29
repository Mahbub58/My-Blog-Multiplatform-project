package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Res
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.data.checkUserExistence
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.models.UserWithoutPassword
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.util.Id
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.cursor
import com.varabyte.kobweb.compose.css.margin
import com.varabyte.kobweb.compose.css.outline
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.width
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.set


//name declaration
//@Page("login user")
@Page
@Composable
fun LoginScreen(){

    var errorText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
        ){
        Column(
            modifier = Modifier.padding(leftRight = 50.px, top = 80.px, bottom = 24.px)
                .backgroundColor(Theme.LightGray.rgb),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                modifier = Modifier.margin(bottom=50.px).width(100.px),
                src = Res.Image.logo,
                description = "Logo Image",
            )


            StyledInputUser()
            StyledInputPassword()


//            Input(
//                type = InputType.Text,
//                attrs = Modifier
//                    .margin(bottom = 12.px)
//                    .width(350.px)
//                    .height(54.px)
//                    .fontFamily(FONT_FAMILY)
//                    .padding (leftRight = 20.px)
//                    .backgroundColor(Color.white)
//                    .border(
//                        width = 0.2.px,
//                        style = LineStyle.None,
//                        color = Theme.Primary.rgb
//                    )
//                    .outline(
//                        width = 0.5.px,
//                        style = LineStyle.None,
//                        color = Colors.Gray
//                    )
//
//                    .toAttrs{
//                        attr("placeholder","Username")
//                    }
//
//            )



//            Input(
//                type = InputType.Password,
//                attrs = Modifier
//                    .margin(bottom = 12.px)
//                    .width(350.px)
//                    .height(54.px)
//                    .fontFamily(FONT_FAMILY)
//                    .padding (leftRight = 20.px)
//                    .backgroundColor(Color.white)
//                    .border(
//                        width = 0.px,
//                        style = LineStyle.None,
//                        color = Theme.Primary.rgb
//                    )
//                    .outline(
//                        width = 0.5.px,
//                        style = LineStyle.None,
//                        color = Theme.Primary.rgb
//                    )
//                    .toAttrs{
//                        attr("placeholder","Password")
//                    }
//
//            )
//            Button(
//                attrs = Modifier
//                    .width(350.px)
//                    .height(54.px)
//                    .padding (leftRight = 20.px)
//                    .backgroundColor(Theme.Primary.rgb)
//                    .borderRadius(r = 4.px)
//                    .fontFamily(FONT_FAMILY)
//                    .fontWeight(FontWeight.Medium)
//                    .fontSize(16.px)
//                    .color(Color.white)
//                    .border(
//                        width = 0.px,
//                        style = LineStyle.None,
//                        color = Colors.Transparent
//                    )
//                    .outline(
//                        width = 0.5.px,
//                        style = LineStyle.None,
//                        color = Colors.Transparent
//                    )
//                    .toAttrs()
//            ) {
//                SpanText(text = "Sign in")
//            }

           errorText= StyledButton()

            SpanText(
                modifier = Modifier
                    .width(350.px)
                    .margin (top = 4.px, bottom = 4.px)
                    .color(Color.red)
                    .textAlign(TextAlign.Center),
                text = errorText
            )



        }
    }


}

@Composable
fun StyledButton():String {
    var scope = rememberCoroutineScope()
    var isHovered by remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var context = rememberPageContext()

    Button(
        attrs = {
            style {
                width(350.px)
                height(54.px)

//                padding( )
                backgroundColor(
                    when {
                        isClicked -> Colors.Red
                        isHovered -> Colors.Black
                        else -> Theme.Primary.rgb
                    }
                )
                cursor(Cursor.Pointer)
                borderRadius(4.px)
                fontFamily(FONT_FAMILY)
                fontWeight("Medium")
                fontSize(16.px)
                color(Color.white)
                border(0.px, LineStyle.None, Colors.Transparent)
                outline(0.5.px, LineStyle.None, Colors.Transparent)

            }
            onMouseOver { isHovered = true }
            onMouseOut { isHovered = false }
            onClick {

                scope.launch {
                    isClicked = true
                    delay(600)
                    isClicked = false
                }

                scope.launch {
                    val  username = (document.getElementById(Id.usernameInput) as HTMLInputElement).value
                    val  password  = (document.getElementById(Id.passwordInput) as HTMLInputElement).value
                    if(username.isNotEmpty() && password.isNotEmpty()){
                        var user = checkUserExistence(
                            user=User(
                                username=username,
                                password = password
                            )

                        )
                        if(user !=null){
                            rememberLoggedIn(true,user)
                            context.router.navigateTo(Screen.AdminHome.route)
                        }else{
                            text= "The user dose not exist"
                            delay(3000)
                            text = " "
                        }

                    }else{
                        text= "input fields are not empty"
                        delay(3000)
                        text = " "
                    }

                }



            }
        }
    ) {
        SpanText(text = "Sign in")
    }
   return text

}

@Composable
fun StyledInputUser() {
    var isFocused by remember { mutableStateOf(false) }
    Input(
        type = InputType.Text,
        attrs = {
            attr("id", Id.usernameInput)
            style {
                property("margin", "12px")
                width(350.px)
                height(54.px)
                fontFamily(FONT_FAMILY)
                padding( 20.px)
                backgroundColor(Color.white)
                property("transition", "border 300ms ease-in-out")
                border(
                    width = if (isFocused) 2.px else 0.2.px,
                    style = if (isFocused) LineStyle.Solid else LineStyle.None,
                    color = Theme.Primary.rgb
                )
                outline(
                    width = if (isFocused) 0.5.px else 0.5.px,
                    style = if (isFocused) LineStyle.None else LineStyle.None,
                    color = Colors.Gray
                )
            }
            onFocusIn { isFocused = true }
            onFocusOut { isFocused = false }
            attr("placeholder", "User")
        }
    )
}

@Composable
fun StyledInputPassword() {
    var isFocused by remember { mutableStateOf(false) }
    Input(
        type = InputType.Password,
        attrs = {
            attr("id", Id.passwordInput)
            style {
                property("margin", "12px")
                width(350.px)
                height(54.px)
                fontFamily(FONT_FAMILY)
                padding( 20.px)
                backgroundColor(Color.white)
                property("transition", "border 300ms ease-in-out")
                border(
                    width = if (isFocused) 2.px else 0.2.px,
                    style = if (isFocused) LineStyle.Solid else LineStyle.None,
                    color = Theme.Primary.rgb
                )
                outline(
                    width = if (isFocused) 0.5.px else 0.5.px,
                    style = if (isFocused) LineStyle.None else LineStyle.None,
                    color = Colors.Gray
                )
            }
            onFocusIn { isFocused = true }
            onFocusOut { isFocused = false }
            attr("placeholder", "Password")
        }
    )
}



private fun rememberLoggedIn(
    remember:Boolean,
    user: UserWithoutPassword?=null
){
    localStorage["remember"]=remember.toString()
    if(user != null){
        localStorage["userId"]=user._id
        localStorage["username"]=user.username
    }
}