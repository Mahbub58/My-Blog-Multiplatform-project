package com.example.blogmultiplatform.models

import com.example.blogmultiplatform.theme.Theme
import kotlinx.serialization.Serializable

@Serializable
enum class Category(val color:String) {
    Technology(color= Theme.Green.hex),
    Programing(color=Theme.Yellow.hex),
    Design(color=Theme.Purple.hex)
}