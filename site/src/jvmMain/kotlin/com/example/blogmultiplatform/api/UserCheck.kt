package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.database.DB
import com.example.blogmultiplatform.database.DB_Implementation
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.models.UserWithoutPassword
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bson.codecs.ObjectIdGenerator
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


@Api(routeOverride = "usercheck")
suspend fun userCheck(context: ApiContext) {

    try {
        val userRequest = context.req.body?.decodeToString()?.let {  Json.decodeFromString<User>(it) }
        val user= userRequest?.let { context.data.getValue<DB_Implementation>().checkUserExistence(
            User(username= it.username, password = hashPassword( it.password) )
        ) }

//        val server_user = context.req.body?.decodeToString()?.let { Json.decodeFromString<User>(it) }
//        val newUser = server_user?.copy(_id = ObjectIdGenerator().generate().toString())
//        var user = (newUser?.let { context.data.getValue<DB_Implementation>().checkUserExistence(
//            User(username= it.username, password = hashPassword( it.password) )
//        ) })


        if(user != null){
            context.res.setBodyText(
                Json.encodeToString<UserWithoutPassword>(
                    UserWithoutPassword(
                        _id= user._id, username = user.username
                    )
                )
            )
        }else{
            context.res.setBodyText(Json.encodeToString(Exception("User dose not exist")))
        }
    }catch(e:Exception){
//        context.res.setBodyText(Json.encodeToString(e))
        context.res.setBodyText(Json.encodeToString(Exception(e.message)))
    }

}

@Api(routeOverride = "checkuserid")
suspend fun checkUserId(context: ApiContext){
    try {
        val idRequest= context.req.body?.decodeToString()?.let { Json.decodeFromString<String>(it)  }
        val result = idRequest?.let {
            context.data.getValue<DB_Implementation>().checkUserId(it)
        }
        if(result!=null){
            context.res.setBodyText(Json.encodeToString(result))
        }else{
            context.res.setBodyText(Json.encodeToString(false))
        }
    }catch (e:Exception){
        context.res.setBodyText(Json.encodeToString(false))
    }
}


private fun hashPassword(password: String):String{
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val hashbyte = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
    val hexString = StringBuffer()

    for (byte in hashbyte){
        hexString.append(String.format("%02x", byte))
    }

    return hexString.toString()
}