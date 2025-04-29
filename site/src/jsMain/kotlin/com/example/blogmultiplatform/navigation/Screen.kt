package com.example.blogmultiplatform.navigation

import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Constant.CATEGORY_PARAM
import com.example.blogmultiplatform.models.Constant.POST_ID_PARAM
import com.example.blogmultiplatform.models.Constant.QUERY_PERMS


sealed class Screen(val route:String) {

    object AdminHome : Screen(route = "/admin/")
    object AdminLogin : Screen(route = "/admin/login")
    object AdminCreate : Screen(route = "/admin/create"){
        fun passPostId(id:String) = "/admin/create?${POST_ID_PARAM}=$id"
    }
    object AdminMyPost : Screen(route = "/admin/mypost"){
        fun searchByTitle(query: String) = "/admin/mypost?${QUERY_PERMS}=$query"
    }
    object AdminSuccess : Screen(route = "/admin/success"){
        fun postUpdate() = "/admin/success?updated=true"
    }

    object HomePage: Screen(route = "/")
    object SearchPage:Screen(route = "/search/query"){
        fun searchByCategory(category: Category)= "/search/query?${CATEGORY_PARAM}=${category.name}"
        fun searchByTitle(query: String)= "/search/query?${QUERY_PERMS}=$query"
    }

    object PostPage : Screen(route = "post/post"){
        fun getPost(id: String) = "post/post?${POST_ID_PARAM}=$id"
    }


}