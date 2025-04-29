package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.database.DB_Implementation
import com.example.blogmultiplatform.models.Newsletter
import com.example.blogmultiplatform.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.json.Json


@Api(routeOverride = "subscribe")
suspend fun subscribeNewsletter(context: ApiContext){
    try{
        val newsletter =context.req.body?.decodeToString()?.let { Json.decodeFromString<Newsletter>(it) }
        context.res.setBodyText(newsletter?.let {
            context.data.getValue<DB_Implementation>().subscribe(newsletter=it)
        }.toString())
    }catch (e:Exception){
        context.res.setBodyText(Json.encodeToString(e.message))
    }
}