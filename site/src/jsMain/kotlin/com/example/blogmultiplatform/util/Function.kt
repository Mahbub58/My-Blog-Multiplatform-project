package com.example.blogmultiplatform.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.data.checkUserId
import com.example.blogmultiplatform.models.EditorControl
import com.example.blogmultiplatform.models.EditorControlStyle
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.util.Constants.COLLAPSED_PANNEL_HEIGHT
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image

import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date


@Composable
fun isUserLoggedIn(content : @Composable () -> Unit) {

    val context = rememberPageContext()
    val remembered = remember { localStorage["remember"].toBoolean() }
    val userId = remember { localStorage["userId"] }
    var userIdExists by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit){
        userIdExists = if(!userId.isNullOrEmpty()) checkUserId(_id = userId) else false
        if(!remembered || !userIdExists){
            context.router.navigateTo(Screen.AdminLogin.route)
        }

    }

    if(remembered && userIdExists){
        content()
    }else{
        println("Loading...")
    }

}


fun logout(){
    localStorage["remember"] = "false"
    localStorage["userId"] = ""
    localStorage["username"] = ""

}


@Composable
fun CollapsedSidePanel(onMenuClick: () -> Unit){

    Row (
        modifier = Modifier
            .fillMaxSize()
            .height(COLLAPSED_PANNEL_HEIGHT.px)
            .padding(leftRight =24.px )
            .backgroundColor(Theme.Secondary.rgb),
        verticalAlignment = Alignment.CenterVertically
    ){
        FaBars(
            modifier = Modifier
                .margin(right = 24.px)
                .color(Colors.White)
                .cursor(Cursor.Pointer)
                .onClick { onMenuClick() },
            size = IconSize.XL
        )

       Image(
           modifier = Modifier
               .width(80.px),
             src = Res.Image.logo,
              description =   "Logo Image"
        )
    }

}


fun Modifier.noBorder():Modifier{
    return this
        .border(
            width = 0.px,
            style = LineStyle.None,
            color = Colors.Transparent
        )
        .outline(
            width = 0.px,
            style = LineStyle.None,
            color = Colors.Transparent
        )
}

fun getEditor() = document.getElementById(Id.editor) as HTMLTextAreaElement

fun getSelectedIntRange(): IntRange? {
    val editor= getEditor()
    val start = editor.selectionStart
    val end = editor.selectionEnd
    return if(start!=null && end != null){
        IntRange(start,(end-1))
    }else null
}

fun getSelectedText():String?{

    val rang= getSelectedIntRange()
    return if(rang!=null){
       getEditor().value.substring(rang)
    }else null
}

fun applyStyle(editorControlStyle: EditorControlStyle){
    val selectedText= getSelectedText()
    val selectedIntRang= getSelectedIntRange()
    if(selectedIntRang!=null && selectedText!=null){
        getEditor().value = getEditor().value.replaceRange(
             range = selectedIntRang,
            replacement = editorControlStyle.style!!
        )
        document.getElementById( Id.editorPreview)?.innerHTML = getEditor().value
    }
}

fun applyEditorControlStyle(
    onImageClick: () -> Unit,
    editorControl: EditorControl,
    onLinkAdded : () -> Unit){
    when(editorControl){
        EditorControl.Bold ->{
            applyStyle(
                EditorControlStyle.Bold(
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Italic ->{ applyStyle(
            EditorControlStyle.Italic (
                selectedText = getSelectedText()
            )
        )}
        EditorControl.Title ->{applyStyle(
            EditorControlStyle.Title (
                selectedText = getSelectedText()
            )
        )}
        EditorControl.Subtitle ->{applyStyle(
            EditorControlStyle.Subtitle (
                selectedText = getSelectedText()
            )
        )}
        EditorControl.Quote ->{
            applyStyle(
                EditorControlStyle.Quote (
                    selectedText = getSelectedText()
                )
            )
        }
        EditorControl.Code  ->{
            applyStyle(
            EditorControlStyle.Code (
                selectedText = getSelectedText()
              )
            )
        }
        EditorControl.Link ->{onLinkAdded()}
        EditorControl.Image ->{
            onImageClick()
        }
    }
}

fun Long.parseDateToString()=Date(this).toLocaleDateString()

fun pressSwitchText(post: List<String>) : String {
    return if(post.size == 1) "1 Posts Selected" else "${post.size} Posts Selected"
}

fun validateEmail(email:String):Boolean{
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return email.matches(Regex(emailRegex))
}

