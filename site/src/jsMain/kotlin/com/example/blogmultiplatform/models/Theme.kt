package com.example.blogmultiplatform.models

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.css.rgba

enum class Theme(
    val hex:  String,
    val rgb:CSSColorValue
) {

    Primary(
        hex="#00A2FF",
        rgb = rgb(r=0,g=162,b=255)
    ),
    Secondary(
      hex = "#001019",
        rgb = rgb(0,16,25)
    ),
    Tertiary(
        hex = "#001925",
        rgb = rgb(0,25,37)
    ),
    LightGray(
        hex="#FAFAFA",
        rgb = rgb(r=250,g=250,b=250)
    ),
    Gray(
        hex = "#E9E9E9",
        rgb = rgb(233,233,233)
    ),
    DarkGray(
        hex="#646464",
        rgb = rgb(r=100,g=100,b=100)
    ),
    HalfWhite(
        hex="#FFFFFF",
        rgb = rgba(r=255,g=255,b=255, a=0.5)
    )
    ,
    HalfBlack(
        hex="#000000",
        rgb = rgba(r=0,g=0,b=0, a=0.5)
    )
    ,
    White(
        hex="#FFFFFF",
        rgb = rgb(r=255,g=255,b=255)
    ),

    Green(
    hex="#00FF94",
    rgb = rgb(r=0,g=255,b=185)
    )
    ,
    Yellow(
    hex="#FFEC45",
    rgb = rgb(r=255,g=236,b=69)
    )
    ,
    Purple(
    hex="#8B6DFF",
    rgb = rgb(r=139,g=109,b=255)
    ),

    Sponsored(
    hex="#3300FF",
    rgb = rgb(r=51,g=0,b=255)
    )

}