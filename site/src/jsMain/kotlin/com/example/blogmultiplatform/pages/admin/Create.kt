package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.AdminPanelLayout
import com.example.blogmultiplatform.components.LinkPopup
import com.example.blogmultiplatform.components.MessagePopup
import com.example.blogmultiplatform.data.addPost
import com.example.blogmultiplatform.data.fetchSelectedPost
import com.example.blogmultiplatform.data.updatePost
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Constant.POST_ID_PARAM
import com.example.blogmultiplatform.models.EditorControl
import com.example.blogmultiplatform.models.EditorControlStyle
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.styles.EditorKeyStyle
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY

import com.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.applyEditorControlStyle
import com.example.blogmultiplatform.util.applyStyle
import com.example.blogmultiplatform.util.getSelectedText
import com.example.blogmultiplatform.util.isUserLoggedIn
import com.example.blogmultiplatform.util.noBorder
import com.varabyte.kobweb.browser.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext

import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.Text
import kotlin.js.Date


data class CreatePageUiEvent(
    var _id:String="",
    var title:String ="",
    var subtitle:String ="",
    var thumbnail:String ="",
    var content:String ="",
    var category: Category = Category.Programing,
    var popular:Boolean=false,
    var main:Boolean=false,
    var sponsored:Boolean=false,
    var thumbnailInputDisabled: Boolean=false,
    var editorVisibility: Boolean = true,
    var messagePopup:Boolean = false,
    var linkAddPopup:Boolean = false,
    var imagePopup: Boolean = false,
    var buttonText:String = "Create"
    ){
    fun reset() = this.copy(
        _id = "",
        title = "",
        subtitle = "",
        thumbnail = "",
        content = "",
        category = Category.Programing,
        popular = false,
        main = false,
        sponsored = false,
        thumbnailInputDisabled = false,
        editorVisibility = true,
        messagePopup = false,
        linkAddPopup = false,
        imagePopup = false,
        buttonText = "Create"
    )
}

@Page
@Composable
fun CreatePage() {

    isUserLoggedIn {
        CreateScreen()
    }

}


@Composable
fun CreateScreen(){

    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()

    /**
     * One way to variable or data hendaling
     */
//   var popularSwitch by remember { mutableStateOf(false) }
//    var mainSwitch by remember { mutableStateOf(false) }
//    var sponsoredSwitch by remember { mutableStateOf(false) }
//    var thumbnailInputDisabled by remember { mutableStateOf(false) }
//    var editorVisibility by remember { mutableStateOf(true) }
//    var thumbnail by remember { mutableStateOf("") }
//    var selectedCategory by remember { mutableStateOf(Category.Programing) }

    /**
     *  2nd way to create a separate data class
     */
    var uiEvent by remember { mutableStateOf(CreatePageUiEvent()) }

    val hasPostIdParams = remember(key1 = context.route ) { context.route.params.containsKey(POST_ID_PARAM)  }

    LaunchedEffect(hasPostIdParams){
        if(hasPostIdParams){
            val postId=context.route.params.getValue(POST_ID_PARAM)
            val response= fetchSelectedPost(id = postId)
            if(response is ApiResponse.Success){
                println("DAta: "+response.data)
                (document.getElementById(Id.editor) as HTMLTextAreaElement).value = response.data.content
               uiEvent=uiEvent.copy(
                  _id = response.data._id,
                  title = response.data.title,
                  subtitle = response.data.subtitle,
                  content = response.data.content,
                  category = response.data.category,
                  thumbnail = response.data.thumbnail,
                  main = response.data.main,
                  popular = response.data.popular,
                  sponsored = response.data.sponsored,
                   buttonText = "Update"
              )
            }
        }else{
           uiEvent= uiEvent.reset()
            (document.getElementById(Id.editor) as HTMLTextAreaElement).value = ""
        }
    }

    AdminPanelLayout {
       Box(
           modifier = Modifier
               .padding(left = if(breakpoint> Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px,
                   top = 50.px
               )
               .fillMaxSize(),
           contentAlignment = Alignment.TopCenter
       ){
           Column(
               modifier = Modifier
                   .fillMaxSize()
                   .maxWidth(700.px),
               verticalArrangement = Arrangement.Top,
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
               SimpleGrid(numColumns = numColumns(1,3)){
                   Row(
                       modifier = Modifier
                           .margin(right = if(breakpoint>Breakpoint.SM) 0.px else 24.px,
                               bottom = if(breakpoint<Breakpoint.SM) 12.px else 0.px),
                       verticalAlignment = Alignment.CenterVertically
                   ) {
                       Switch(
                           modifier = Modifier.margin(right = 8.px),
                           checked =  uiEvent.popular,   //popularSwitch,
                           onCheckedChange = {
                               uiEvent = uiEvent.copy(popular = it)
                               //popularSwitch= it
                                             },
                           size = SwitchSize.LG
                       )
                       SpanText(
                           modifier = Modifier
                               .fontSize(14.px)
                               .fontFamily(FONT_FAMILY)
                               .color(Theme.HalfBlack.rgb),
                           text = "Popular"
                       )
                   }

                   Row(
                       modifier = Modifier
                           .margin(right = if(breakpoint<Breakpoint.SM) 0.px else 24.px,
                               bottom = if(breakpoint<Breakpoint.SM) 14.px else 0.px),
                       verticalAlignment = Alignment.CenterVertically
                   ) {
                       Switch(
                           modifier = Modifier.margin(right = 8.px),
                           checked = uiEvent.main, // mainSwitch,
                           onCheckedChange = {
                               uiEvent=uiEvent.copy(main = it)
//                               mainSwitch=it
                                             },
                           size = SwitchSize.LG
                       )
                       SpanText(
                           modifier = Modifier
                               .fontSize(14.px)
                               .fontFamily(FONT_FAMILY)
                               .color(Theme.HalfBlack.rgb),
                           text = "Main"
                       )
                   }

                   Row(
                       modifier = Modifier
                           .margin(right = if(breakpoint<Breakpoint.SM) 0.px else 24.px,
                               bottom = if(breakpoint<Breakpoint.SM) 14.px else 0.px),
                       verticalAlignment = Alignment.CenterVertically
                   ) {
                       Switch(
                           modifier = Modifier.margin(right = 8.px),
                           checked = uiEvent.sponsored, //sponsoredSwitch,
                           onCheckedChange = {
                               uiEvent=uiEvent.copy(sponsored = it)
//                               sponsoredSwitch=it
                                             },
                           size = SwitchSize.LG
                       )
                       SpanText(
                           modifier = Modifier
                               .fontSize(14.px)
                               .fontFamily(FONT_FAMILY)
                               .color(Theme.HalfBlack.rgb),
                           text = "Sponsored"
                       )
                   }

               }

               Input(
                   type = InputType.Text,
                   attrs = Modifier
                       .fillMaxWidth()
                       .id(Id.titleInput)
                       .height(54.px)
                       .margin(topBottom = 12.px)
                       .borderRadius (10.px)
                       .padding (leftRight = 20.px)
                       .backgroundColor(Theme.LightGray.rgb)
                       .border (
                           width=0.px,
                           style = LineStyle.None,
                           color = Colors.Transparent
                       )
                       .outline(
                           width=0.px,
                           style = LineStyle.None,
                           color = Colors.Transparent
                       )
                       .fontFamily(FONT_FAMILY)
                       .fontSize(16.px)
                       .toAttrs{
                           attr("placeholder","Title")
                           attr("value", uiEvent.title)
                       }

               )

               Input(
                   type = InputType.Text,
                   attrs= Modifier
                       .fillMaxWidth()
                       .id(Id.subtitleInput)
                       .height(54.px)
                       .margin(bottom = 12.px)
                       .borderRadius (10.px)
                       .padding (leftRight = 20.px)
                       .backgroundColor(Theme.LightGray.rgb)
                       .border (
                           width=0.px,
                           style = LineStyle.None,
                           color = Colors.Transparent
                       )
                       .outline(
                           width=0.px,
                           style = LineStyle.None,
                           color = Colors.Transparent
                       )
                       .fontFamily(FONT_FAMILY)
                       .fontSize(16.px)
                       .toAttrs{
                           attr("placeholder","Subtitle")
                           attr("value", uiEvent.subtitle)
                       }
               )

               CategoryDropDown(
                   selectCategory = uiEvent.category,
                   onCategorySelect = { uiEvent = uiEvent.copy(category =  it)}
               )

               Row(
                   modifier = Modifier
                       .margin(topBottom = 12.px)
                       .fillMaxWidth(),
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.Start
               ) {
                   Switch(
                       modifier = Modifier.margin(right = 8.px),
                       checked = (uiEvent.thumbnailInputDisabled),
                       onCheckedChange = {uiEvent=uiEvent.copy(thumbnailInputDisabled = it)},
                       size = SwitchSize.MD
                   )
                   SpanText(
                       modifier = Modifier
                           .fontSize(14.px)
                           .fontFamily(FONT_FAMILY)
                           .color(Theme.HalfBlack.rgb),
                       text = "Past an image url instead"
                   )
               }
               ThumbnailUploader(
                   thumbnail = uiEvent.thumbnail,
                   thumbnailInputDisabled=uiEvent.thumbnailInputDisabled,
                   onThumbnailSelect = { filename,file ->
                    (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value =filename
                       uiEvent= uiEvent.copy(thumbnail = file)
                       println("FileName:"+filename+"file path: "+file)

                   }
               )
               EditorControls(
                   breakpoint=breakpoint,
                   editorVisibility= uiEvent.editorVisibility,
                   onEditorVisibilityChanged = {uiEvent = uiEvent.copy(editorVisibility = !(uiEvent.editorVisibility)) },
                   onLinkAdded = {uiEvent=uiEvent.copy(linkAddPopup = true)},
                   onImageClick = {uiEvent=uiEvent.copy(imagePopup  = true)}
                   )
               Editor(editorVisibility = uiEvent.editorVisibility)

              CreateButtor(
                  text =uiEvent.buttonText,
                  onClick = {
                  uiEvent=uiEvent.copy(title = (document.getElementById(Id.titleInput) as HTMLInputElement).value)
                  uiEvent=uiEvent.copy(subtitle = (document.getElementById(Id.subtitleInput) as HTMLInputElement).value)
                 // uiEvent=uiEvent.copy(thumbnail = (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value)
                  uiEvent=uiEvent.copy(content = (document.getElementById(Id.editor) as HTMLTextAreaElement).value)

                  if(uiEvent.thumbnailInputDisabled){
                      uiEvent=uiEvent.copy(thumbnail = (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value)
                  }

                   if( uiEvent.title.isNotEmpty()
                       && uiEvent.subtitle.isNotEmpty()
                       && uiEvent.thumbnail.isNotEmpty()
                       && uiEvent.content.isNotEmpty()
                       ){

                       scope.launch {
                           if(hasPostIdParams){
                               val result = updatePost(
                                   Post(
                                       _id = uiEvent._id,
                                       title = uiEvent.title,
                                       subtitle = uiEvent.subtitle,
                                       thumbnail = uiEvent.thumbnail,
                                       content = uiEvent.content,
                                       category = uiEvent.category,
                                       popular = uiEvent.popular,
                                       main = uiEvent.main,
                                       sponsored = uiEvent.sponsored
                                   )
                               )

                               if(result){
                                   context.router.navigateTo(Screen.AdminSuccess.postUpdate())
                               }
                           }else{
                               val result = addPost(
                                   Post(
                                       author = localStorage.getItem("username").toString(),
                                       date = Date.now().toLong(),
                                       title = uiEvent.title,
                                       subtitle = uiEvent.subtitle,
                                       thumbnail = uiEvent.thumbnail,
                                       content = uiEvent.content,
                                       category = uiEvent.category,
                                       popular = uiEvent.popular,
                                       main = uiEvent.main,
                                       sponsored = uiEvent.sponsored
                                   )
                               )

                               if(result){
                                   context.router.navigateTo(Screen.AdminSuccess.route)
                               }
                           }
                       }



                   }else{
                     scope.launch {
                         uiEvent =uiEvent.copy(messagePopup = true)
                         delay(1000)
                         uiEvent =uiEvent.copy(messagePopup = false )
                     }

                   }

              })




           }

       }

   }
    if(uiEvent.messagePopup){
        MessagePopup(
            message = "Please fill out all field",
            onDialogDismiss = {uiEvent =uiEvent.copy(messagePopup = false)}
        )
    }

    if(uiEvent.linkAddPopup){
        LinkPopup(
            editorControl = EditorControl.Link,
            onDialogDismiss = {uiEvent=uiEvent.copy(linkAddPopup = false)},
            onLinkAdded = {href,title ->
                applyStyle(
                    EditorControlStyle.Link(
                        selectedText = getSelectedText(),
                         href=href,
                        title=title
                    )
                )
            }
        )
    }

    if(uiEvent.imagePopup){
        LinkPopup(
            editorControl = EditorControl.Image,
            onDialogDismiss = {uiEvent=uiEvent.copy(imagePopup = false)},
            onLinkAdded = {imageLink,description ->
                applyStyle(
                    EditorControlStyle.Image(
                        selectedText = getSelectedText(),
                        imageLink =  imageLink,
                        description = description
                    )
                )
            }
        )
    }

}




@Composable
fun CategoryDropDown(
    selectCategory: Category,
    onCategorySelect : (Category ) -> Unit
){

    Box(
        modifier = Modifier
            .classNames("dropdown")
            .margin(topBottom = 14.px)
            .fillMaxWidth()
            .height(54.px)
            .borderRadius(10.px)
            .backgroundColor(Theme.LightGray.rgb)
            .cursor(Cursor.Pointer)
            .attrsModifier {
                attr("data-bs-toggle","dropdown")
            }
    ) {
        Row(
            modifier = Modifier.padding(leftRight = 20.px)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SpanText(
                modifier = Modifier
                    .fontFamily(FONT_FAMILY)
                    .fontSize(16.px)
                    .fillMaxWidth(),
                text = selectCategory.name
            )
            Box(modifier = Modifier.classNames("dropdown-toggle"))
        }

        Ul(
            attrs = Modifier
                .classNames("dropdown-menu")
                .fillMaxWidth()
                .toAttrs()
        ) {
            Category.values().forEach { category ->
                Li {
                    A(
                        attrs = Modifier
                            .classNames("dropdown-item")
                            .color(Colors.Black)
                            .fontFamily(FONT_FAMILY)
                            .fontSize(16.px)
                            .onClick { onCategorySelect(category) }
                            .toAttrs()
                    ){
                        Text(value = category.name)
                    }
                }
            }
        }

    }
}

@Composable
fun ThumbnailUploader(
    thumbnail:String,
    thumbnailInputDisabled: Boolean,
    onThumbnailSelect:(String,String) -> Unit
){

    Row(
        modifier = Modifier
            .fillMaxSize()
            .height(54.px)
            .margin(bottom = 20.px)
    ) {
        Input(
            type = InputType.Text,
            attrs = Modifier
                .id(Id.thumbnailInput)
                .padding (leftRight = 20.px)
                .fillMaxSize()
                .backgroundColor(Theme.LightGray.rgb)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .borderRadius(10.px)
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
                .thenIf(
                    condition = !thumbnailInputDisabled,
                    other = Modifier.disabled()
                )
                .toAttrs {
                    attr("placeholder", "Thumbnail")
                    attr("value", thumbnail)
                }
        )

       Button(attrs = Modifier
           .onClick {
               document.loadDataUrlFromDisk (
                   accept = "image/png, image/jpeg",
                   onLoad = {
                       onThumbnailSelect(filename,it)
                   }
               )
           }
           .fillMaxHeight()
           .padding(14.px)
           .margin(right = 12.px)
           .backgroundColor(if(thumbnailInputDisabled) Theme.Gray.rgb else Theme.Primary.rgb)
           .color(if(thumbnailInputDisabled) Theme.DarkGray.rgb else Theme.White.rgb)
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
           .borderRadius (10.px)
           .fontFamily(FONT_FAMILY)
           .fontSize(14.px)
           .fontWeight(FontWeight.Medium)
           .thenIf(
               condition = thumbnailInputDisabled,
               other = Modifier.disabled()
           )
           .toAttrs(),

       ){
            SpanText(
                "Upload")
        }

    }

}

@Composable
fun EditorControls(
    breakpoint: Breakpoint,
    editorVisibility: Boolean,
    onLinkAdded : () -> Unit,
    onImageClick : () -> Unit,
    onEditorVisibilityChanged : () -> Unit
    ){

Box(modifier = Modifier.fillMaxWidth()) {
    SimpleGrid(numColumns = numColumns(base = 1, sm = 2),
        modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.height(54.px)
            .backgroundColor(Theme.LightGray.rgb)
            .borderRadius(10.px)
        ) {
            EditorControl.values().forEach {
                EditorControlView(control = it, onClick = {
                    applyEditorControlStyle(
                        editorControl = it,
                        onLinkAdded=onLinkAdded,
                        onImageClick = onImageClick
                    )
                })
            }
        }

           Box(contentAlignment = Alignment.CenterEnd) {
               Button(
                   attrs = Modifier
                       .thenIf(
                           condition = breakpoint < Breakpoint.SM,
                           other = Modifier.fillMaxSize()
                       )
                       .margin(topBottom = if(breakpoint<Breakpoint.SM) 10.px else 0.px)
                       .padding(leftRight = 24.px)
                       .borderRadius(10.px)
                       .height(54.px)
                       .backgroundColor(
                           if(editorVisibility) Theme.LightGray.rgb
                           else Theme.Primary.rgb
                       )
                       .color(
                           if(editorVisibility) Theme.DarkGray.rgb
                           else Theme.White.rgb
                       )
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
                       .onClick {
                           onEditorVisibilityChanged()
                           js("hljs.highlightAll()") as Unit
                       }
                       .toAttrs()
               ){
                   SpanText(
                       modifier = Modifier
                           .fontFamily(FONT_FAMILY)
                           .fontSize(14.px)
                           .fontWeight(FontWeight.Medium ),
                       text = "Preview"
                   )
               }

        }
    }
}
}

@Composable
fun EditorControlView(control: EditorControl,onClick: () -> Unit){
    Box(modifier = EditorKeyStyle.toModifier()
        .fillMaxHeight()
        .padding(leftRight = 12.px)
        .borderRadius(10.px)
        .cursor(Cursor.Pointer)
        .onClick { onClick()  },
        contentAlignment = Alignment.Center
    ) {
        Image(
            src= control.icon,
            description="${control.name} Icon"
        )
    }
}


@Composable
fun Editor(editorVisibility: Boolean){
    Box(modifier = Modifier
        .fillMaxWidth()
    ){
        TextArea(
            attrs = Modifier
                .fillMaxWidth()
                .id(Id.editor)
                .height(400.px)
                .resize(Resize.None)
                .maxHeight(400.px)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(10.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
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
                .visibility(
                    if(editorVisibility) Visibility.Visible
                    else Visibility.Hidden
                )
                .onKeyDown {
                    if(it.code == "Enter" && it.shiftKey){
                        applyStyle(
                            editorControlStyle = EditorControlStyle.Break(
                                selectedText = getSelectedText()
                            )
                        )
                    }
                }
                .toAttrs{
                    attr("placeholder","Type here...")
                }
        )

        Div(
            attrs = Modifier
                .id(Id.editorPreview)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(10.px)
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
                .visibility(
                    if(editorVisibility) Visibility.Hidden
                    else Visibility.Visible
                )
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .toAttrs()
        ) {  }
    }
}

@Composable
fun CreateButtor(
    text: String,
    onClick : () ->Unit) {
    Button(attrs = Modifier
        .onClick { onClick() }
        .fillMaxWidth()
        .height(54.px)
        .margin(topBottom = 24.px)
        .borderRadius(10.px)
        .backgroundColor(Theme.Primary.rgb)
        .color(Colors.White)
        .noBorder()
        .fontFamily(FONT_FAMILY)
        .fontSize(16.px)
        .toAttrs()
    ){
        SpanText(text)
    }
}
