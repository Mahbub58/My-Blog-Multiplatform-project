package com.example.blogmultiplatform.models

sealed class EditorControlStyle(val style: String?) {
    data class Bold(val selectedText: String?) : EditorControlStyle(style = "<Strong>$selectedText </strong> ")
    data class Italic(val selectedText: String?) : EditorControlStyle(style = "<em>$selectedText </em> ")
    data class Link(val selectedText: String?, val href:String, val title:String) :
        EditorControlStyle(style = "<a href= \"$href\"title=\"$title\" >$selectedText </a> ")

    data class Title(val selectedText: String?) : EditorControlStyle(style = "<h1><Strong>$selectedText </strong></h1> ")
    data class Subtitle(val selectedText: String?) : EditorControlStyle(style = "<h3>$selectedText </h3>  ")

    data class Quote(val selectedText: String?) :
        EditorControlStyle(style = "<div style=\"background-color:#FAFAFA;padding:12px;border-radius:6px;\"><em>〞$selectedText </em></div> ")

    data class Code(val selectedText: String?) :
        EditorControlStyle(style = "<div style=\"background-color:#0d1117;padding:12px;border-radius:6px;\"><pre><code class=\"language-kotlin\">$selectedText </code></pre></div> ")

    data class Image(val selectedText: String?, val imageLink:String,val description:String) :
        EditorControlStyle(style = "<img src= \"$imageLink\"alt=\"$description\"style=\"ax-width:100%\">$selectedText</img>")

    data class Break(val selectedText: String?) : EditorControlStyle(style = "$selectedText </br> ")
}