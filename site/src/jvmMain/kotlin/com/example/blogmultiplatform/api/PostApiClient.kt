package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.database.DB_Implementation
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.json.Json
import org.bson.codecs.ObjectIdGenerator


@Api(routeOverride =  "addpost")
suspend fun addPost(context: ApiContext) {

    try {
        val post =context.req.body?.decodeToString()?.let { Json.decodeFromString<Post>(it) }
        val newPost = post?.copy(_id = ObjectIdGenerator().generate().toString())
        context.res.setBodyText(
            newPost.let {
                it?.let { it1 -> context.data.getValue<DB_Implementation>().addPost(it1).toString() }
            }?:false.toString()
        )
    }catch(e:Exception){
        context.res.setBodyText(Json.encodeToString(e.message))
    }
}

@Api(routeOverride = "updatepost")
suspend fun updatePost(context: ApiContext){
    try {
        val updatedPost = context.req.body?.decodeToString()?.let { Json.decodeFromString<Post>(it) }
        context.res.setBodyText(
            updatedPost?.let{
                context.data.getValue<DB_Implementation>().updatePost(it)
            }.toString()
        )
    }catch (e:Exception){
        context.res.setBodyText(Json.encodeToString(e.message))
    }
}

@Api(routeOverride = "readmainpost")
suspend fun readMainPost(context: ApiContext){
    try {
        val mainPost= context.data.getValue<DB_Implementation>().readMainPost()
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Success(data = mainPost)))
    }catch (e:Exception){
        context.res.setBodyText(ApiListResponse.Error(message = e.message.toString()).toString())
    }
}

@Api(routeOverride = "readlaatestpost")
suspend fun readLatestPost(context: ApiContext){
    try {
        val skip =context.req.params["skip"]?.toInt()?:0
        val latestPost= context.data.getValue<DB_Implementation>().readLatestPost(skip)
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Success(data = latestPost)))
    }catch (e:Exception){
        context.res.setBodyText(ApiListResponse.Error(message = e.message.toString()).toString())
    }
}
@Api(routeOverride = "readsponsoredpost")
suspend fun readSponsoredPost(context: ApiContext){
    try {
        val sponsoredPost= context.data.getValue<DB_Implementation>().readSponsoredPost()
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Success(data = sponsoredPost)))
    }catch (e:Exception){
        context.res.setBodyText(ApiListResponse.Error(message = e.message.toString()).toString())
    }
}

@Api(routeOverride = "readpopularpost")
suspend fun readPopularPost(context: ApiContext){
    try {
        val skip =context.req.params["skip"]?.toInt()?:0
        val latestPost= context.data.getValue<DB_Implementation>().readPopularPost(skip)
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Success(data = latestPost)))
    }catch (e:Exception){
        context.res.setBodyText(ApiListResponse.Error(message = e.message.toString()).toString())
    }
}



@Api(routeOverride = "readmypost")
suspend fun readMyPost(context: ApiContext){

    try {
        val skip=context.req.params["skip"]?.toInt()?:0
        val author= context.req.params["author"]?: ""
        val myPost = context.data.getValue<DB_Implementation>().readMyPost(skip=skip,author=author)
        context.res.setBodyText(
            Json.encodeToString(ApiListResponse.Success(data = myPost))
        )
        println("ApiClient:"+skip+" "+author)
    }catch (e:Exception){
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Error(message = e.message.toString())))
    }


    //other get all post by removing sort key
//    try {
//        val mypost = context.data.getValue<DB_Implementation>().getAllPost()
//        context.res.setBodyText(Json.encodeToString(ApiListResponse.Success (data = mypost)))
//
//    }catch (e:Exception){
//        context.res.setBodyText(Json.encodeToString(e))
//    }
}


@Api(routeOverride = "getallsubscriber")
suspend fun getAllSubscriber(context: ApiContext){
    try {

        val myUsers = context.data.getValue<DB_Implementation>().getAllPost()
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Success (data = myUsers)))

    }catch (e:Exception){
        context.res.setBodyText(Json.encodeToString(e))
    }
}

@Api(routeOverride = "deleteselectedpost")
suspend fun deleteSelectedPost(context: ApiContext){

    try {
val request = context.req.body?.decodeToString()?.let { Json.decodeFromString<List<String>>(it) }
        context.res.setBodyText(request?.let { context.data.getValue<DB_Implementation>().deleteSelectedPost(ids = it).toString()
        }?: "false")
    }catch (e:Exception){
        context.res.setBodyText(Json.encodeToString(e.message))
    }

}

@Api(routeOverride = "searchpost")
suspend fun searchPostByTitle(context: ApiContext){

    try{
        val query = context.req.params["query"]?:""
        val skip = context.req.params["skip"]?.toInt()?:0
        val request = context.data.getValue<DB_Implementation>().searchPostByTitle(
            query = query,
            skip = skip
        )
        context.res.setBodyText(Json.encodeToString(
            ApiListResponse.Success(data = request)
        ))
    }catch (e:Exception){
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Error(e.message.toString())))
    }


}

@Api(routeOverride = "searchpostbycategory")
suspend fun searchPostByCategory(context: ApiContext){

    try{
        val category = Category.valueOf(context.req.params["category"]?:Category.Programing.name)
        val skip = context.req.params["skip"]?.toInt()?:0
        val request = context.data.getValue<DB_Implementation>().searchByCategory(
            category = category,
            skip = skip
        )
        context.res.setBodyText(Json.encodeToString(
            ApiListResponse.Success(data = request)
        ))
    }catch (e:Exception){
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Error(e.message.toString())))
    }


}

@Api(routeOverride = "readselectedpost")
suspend fun readSelectedPost(context: ApiContext){

    val postId=context.req.params["postId"]
    if(!postId.isNullOrEmpty()){
        try{
            val selectedPost = context.data.getValue<DB_Implementation>().readSelectedPost(id= postId)
            context.res.setBodyText(
                Json.encodeToString(
                    ApiResponse.Success(data = selectedPost )
                )
            )
        }catch (e:Exception){
            context.res.setBodyText(
                Json.encodeToString(ApiResponse.Error(message = e.message.toString()))
            )
        }
    }else{
        context.res.setBodyText(
            Json.encodeToString(ApiResponse.Error(message ="Selected Post dose not exist"))
        )
    }

}