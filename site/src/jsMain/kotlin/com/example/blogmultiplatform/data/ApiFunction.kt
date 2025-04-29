package com.example.blogmultiplatform.data

import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Constant.POST_ID_PARAM
import com.example.blogmultiplatform.models.Newsletter
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.RandomJoke
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.models.UserWithoutPassword
import com.example.blogmultiplatform.util.Constants.HUMOR_API_URL
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.browser.http.http
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date


suspend fun checkUserExistence(user: User):UserWithoutPassword? {

    return try {

        val result = window.api.tryPost(
            apiPath = "usercheck",
            body = Json.encodeToString(user).encodeToByteArray()
        )
//        Json.decodeFromString<UserWithoutPassword>(result.toString())
        result?.decodeToString()?.let { Json.decodeFromString<UserWithoutPassword>(it) }

    }catch (e:Exception){
        println(e.message)
        null
    }

}

suspend fun checkUserId(_id: String):Boolean{
    return try{
        val result = window.api.tryPost(
            apiPath = "checkuserid",
            body = Json.encodeToString(_id).encodeToByteArray()
        )
//        Json.decodeFromString<UserWithoutPassword>(result.toString())
        result?.decodeToString()?.let { Json.decodeFromString<Boolean>(it) }?: false
    }catch (e:Exception){
        println(e.message.toString())
        false
    }
}



suspend fun fetchRandomJoke(onComplete : (RandomJoke) -> Unit){
    val date = localStorage["date"]
    if(date != null){
        val difference= (Date.now() - date.toDouble())
        val dayHasPassed= difference >= 86400000
        if(dayHasPassed){
                try {
                    val result = window.http.get(HUMOR_API_URL).decodeToString()
                    onComplete(Json.decodeFromString(result))
                    localStorage["date"]= Date.now().toString()
                    localStorage["joke"]=result
                }catch (e:Exception){
                    onComplete(RandomJoke(id= -1, joke=e.message.toString()))
                    //randomJoke= RandomJoke(id= -1, joke=e.message.toString())
                    println(e.message)
                }
        }else{
            try {
                localStorage["joke"]?.let { Json.decodeFromString<RandomJoke>(it) }
                    ?.let { onComplete(it) }
              //  randomJoke= localStorage["joke"]?.let { Json.decodeFromString<RandomJoke>(it) }
            }catch (e:Exception){
                onComplete(RandomJoke(id= -1, joke=e.message.toString()))
                println(e.message)
            }

        }
    }else{

            try {
                val result = window.http.get(HUMOR_API_URL).decodeToString()
                onComplete(Json.decodeFromString(result))
                localStorage["date"]= Date.now().toString()
                localStorage["joke"]=result
            }catch (e:Exception){
                onComplete(RandomJoke(id= -1, joke=e.message.toString()))
                println(e.message)
            }

    }
}


suspend fun addPost(post: Post):Boolean{
    return try {
         window.api.tryPost(
             apiPath = "addpost",
             body = Json.encodeToString(post).encodeToByteArray()
         )?.decodeToString().toBoolean()
    }catch (e:Exception){
        println(e.message.toString())
        false
    }
}

suspend fun updatePost(post: Post):Boolean{
    return try {
        window.api.tryPost(
            apiPath = "updatepost",
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()
    }catch (e:Exception){
        println(e.message)
        false
    }
}


suspend fun subscribeToNewsletter(newsletter: Newsletter):String{
    return window.api.tryPost(
            apiPath = "subscribe",
            body = Json.encodeToString(newsletter).encodeToByteArray()
        )?.decodeToString().toString()

}

suspend fun fetchMyPost(
    skip:Int,
    onSuccess: (ApiListResponse)-> Unit,
    onError: (Exception) -> Unit
){
    try {
        val result= window.api.tryGet(
            apiPath = "readmypost?skip=$skip&author=${localStorage["username"]}"
        )?.decodeToString()
        onSuccess(Json.decodeFromString(result.toString()))


    }catch (e:Exception){
        onError(e)
    }

    //other get all post by removing short key
//        try {
//            val result = window.api.tryGet(
//                apiPath = "readmypost"
//            )?.decodeToString()
//            if (result != null) {
//                onSuccess(Json.decodeFromString(result))
//            } else {
//                onError(Exception("Something went wrong"))
//            }
//
//        } catch (e: Exception) {
//            println(e)
//            onError(e)
//        }

}


suspend fun getAllSubscribers(
    onSuccess: (ApiListResponse) -> Unit, onError: (Exception) -> Unit){

    try {
        val result = window.api.tryGet(
            apiPath = "getallsubscriber"
        )?.decodeToString()
        if(result != null){
            onSuccess (Json.decodeFromString(result))
        }else{
            onError(Exception("Something went wrong"))
        }

    }catch (e: Exception){
        println(e)
        onError(e)
    }
}

suspend fun fetchMainPost(
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
){
    try {
        val result = window.api.tryGet(
            apiPath = "readmainpost"
        )?.decodeToString()
        if(result != null)
        onSuccess(Json.decodeFromString(result))
        else   onError(Exception("Something went wrong"))
    }catch (e:Exception){
        println("Main List error: "+e)
        onError(e)
    }
}

suspend fun fetchLatestPost(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
){
    try {
        val result = window.api.tryGet(
            apiPath = "readlaatestpost?skip=$skip"
        )?.decodeToString()
        if(result != null)
            onSuccess(Json.decodeFromString(result))
        else   onError(Exception("Something went wrong"))
    }catch (e:Exception){
        println("Latest List error: "+e)
        onError(e)
    }
}

suspend fun fetchSponsoredPost(
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
){
    try {
        val result = window.api.tryGet(
            apiPath = "readsponsoredpost"
        )?.decodeToString()
        if(result != null)
            onSuccess(Json.decodeFromString(result))
        else   onError(Exception("Something went wrong"))
    }catch (e:Exception){
        println("Sponsored List error: "+e)
        onError(e)
    }
}

suspend fun fetchPopularPost(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
){
    try {
        val result = window.api.tryGet(
            apiPath = "readpopularpost?skip=$skip"
        )?.decodeToString()
        if(result != null)
            onSuccess(Json.decodeFromString(result))
        else   onError(Exception("Something went wrong"))
    }catch (e:Exception){
        println("Popular List error: "+e)
        onError(e)
    }
}

suspend fun deleteSelectedPost(ids: List<String>): Boolean{
   return try {
        val result = window.api.tryPost(
            apiPath =  "deleteselectedpost",
            body = Json.encodeToString(ids).encodeToByteArray()
        )?.decodeToString()
        return result.toBoolean()
    }catch (e:Exception){
        println(e.message)
        false
    }

}

suspend fun searchPostByTitle(
    query: String,
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
){
    try{
        val result = window.api.tryGet(
            apiPath = "searchpost?query=$query&skip=$skip"
        )?.decodeToString()
        onSuccess(Json.decodeFromString(result.toString()))
    }catch (e:Exception){
        println(e.message)
    }
}

suspend fun searchPostByCategory(
    category: Category,
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
){
    try{
        val result = window.api.tryGet(
            apiPath = "searchpostbycategory?category=${category.name}&skip=$skip"
        )?.decodeToString()
        onSuccess(Json.decodeFromString(result.toString()))
    }catch (e:Exception){
        println(e.message)
    }
}


suspend fun fetchSelectedPost(id:String): ApiResponse{
   return try{
        val result= window.api.tryGet(
            apiPath = "readselectedpost?${POST_ID_PARAM}=$id"
        )?.decodeToString()
        return if(result !=null){
             Json.decodeFromString<ApiResponse>(result)
        }else{
             ApiResponse.Error(message = "Result is null")
        }
    }catch (e:Exception){
        return ApiResponse.Error(message = e.message.toString())
    }
}