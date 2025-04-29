package com.example.blogmultiplatform.database

import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Newsletter
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.User


interface DB {
   suspend fun readSelectedPost(id: String) : Post
   suspend fun searchPostByTitle(query:String,skip: Int):List<PostWithoutDetails>
   suspend fun searchByCategory(category: Category, skip: Int):List<PostWithoutDetails>
   suspend fun deleteSelectedPost(ids: List<String>) :Boolean
   suspend fun readMainPost(): List<PostWithoutDetails>
   suspend fun readLatestPost(skip: Int): List<PostWithoutDetails>
   suspend fun readSponsoredPost(): List<PostWithoutDetails>
   suspend fun readPopularPost(skip: Int): List<PostWithoutDetails>
   suspend fun getAllPost(): List<PostWithoutDetails>
   suspend fun addPost(post: Post):Boolean
   suspend fun updatePost(post: Post):Boolean
   suspend fun readMyPost(skip:Int, author:String):List<PostWithoutDetails>
   suspend fun checkUserExistence(user:User): User?
   suspend fun checkUserId(_id: String): Boolean
   suspend fun subscribe(newsletter: Newsletter):String

}