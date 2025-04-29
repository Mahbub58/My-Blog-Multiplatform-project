package com.example.blogmultiplatform.database

import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Constant.MAIN_POST_LIMIT
import com.example.blogmultiplatform.models.Constant.POST_PER_PAGE
import com.example.blogmultiplatform.models.Constant.SPONSORED_POST_LIMIT
import com.example.blogmultiplatform.models.Newsletter
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.User

import com.example.blogmultiplatform.util.Constant
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.varabyte.kobweb.api.data.Data
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates


@InitApi
fun initDB_Implementation(context:InitApiContext){

    System.setProperty(
        "org.litote.mongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )
    context.data.add(DB_Implementation(context))

}


class DB_Implementation(val context: InitApiContext):DB {


    private val client= MongoClient.create(
//    "mongodb+srv://Mahbub58:p12345678@cluster0.efzbz3o.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
        //   "mongodb+srv://mahbub:p12345678@cluster0.efzbz3o.mongodb.net/"
        "mongodb+srv://mahbub:p12345678@cluster0.efzbz3o.mongodb.net/${Constant.DATABASE_NAME}?retryWrites=true&w=majority"

    )
//    private val client= MongoClient.create( System.getenv("MONGODB_URI"))

    private val database = client.getDatabase(Constant.DATABASE_NAME)
    private val userCollection = database.getCollection<User>(  Constant.ADMIN_TABLE)

    private val postCollectionWithoutDetails = database.getCollection<PostWithoutDetails>(  Constant.POST_TABLE)
    private val subscriberCollection = database.getCollection<Newsletter>(  Constant.SUBSCRIBER)



    private val postCollection = database.getCollection<Post>(Constant.POST_TABLE )
    override suspend fun readSelectedPost(id: String): Post {
        return postCollection
            .find(
                Filters.and(
                    Filters.eq(Post::_id.name, id  )
                )
            ).toList().first()

    }

    override suspend fun searchPostByTitle(query: String, skip: Int): List<PostWithoutDetails> {
//        val regexQuery = query.toRegex(RegexOption.IGNORE_CASE)
//        return postCollectionWithoutDetails
//            .withDocumentClass(PostWithoutDetails::class.java)
//            .find(PostWithoutDetails::title regex regexQuery)
//            .sort(compareByDescending(PostWithoutDetails::date))
//            .skip(skip)
//            .limit(POST_PER_PAGE)
//            .toList()
        val regexQuery = Regex(query, RegexOption.IGNORE_CASE)
        return postCollectionWithoutDetails
            .find(Filters.regex(PostWithoutDetails::title.name, regexQuery.pattern))
            .sort(Sorts.descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POST_PER_PAGE)
            .toList()

    }

    override suspend fun searchByCategory(category: Category, skip: Int): List<PostWithoutDetails> {

        return postCollectionWithoutDetails
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(Filters.and(
                Filters.eq(PostWithoutDetails::category.name, category)))
            .sort(Sorts.descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POST_PER_PAGE)
            .toList()
    }

    override suspend fun deleteSelectedPost(ids: List<String>):Boolean {
        return postCollection
            .deleteMany(Filters.`in`("_id", ids))
            .wasAcknowledged()

    }

    override suspend fun readMainPost(): List<PostWithoutDetails> {

        return postCollectionWithoutDetails
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.and(
                    Filters.eq(PostWithoutDetails::main.name, true  )
                )
            )
            .sort(Sorts.descending(PostWithoutDetails::date.name))
            .limit(MAIN_POST_LIMIT)
            .toList()
    }

    override suspend fun readLatestPost(skip: Int): List<PostWithoutDetails> {
        return postCollectionWithoutDetails
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.and(
                    Filters.eq(PostWithoutDetails::main.name, false),
                    Filters.eq(PostWithoutDetails::popular.name, false),
                    Filters.eq(PostWithoutDetails::sponsored.name, false)
                )
            )
            .sort(Sorts.descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POST_PER_PAGE)
            .toList()
    }

    override suspend fun readSponsoredPost(): List<PostWithoutDetails> {
        return postCollectionWithoutDetails
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.and(
                    Filters.eq(PostWithoutDetails::sponsored.name, true  )
                )
            )
            .sort(Sorts.descending(PostWithoutDetails::date.name))
            .limit(SPONSORED_POST_LIMIT)
            .toList()
    }

    override suspend fun readPopularPost(skip: Int): List<PostWithoutDetails> {
        return postCollectionWithoutDetails
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.and(
                    Filters.eq(PostWithoutDetails::popular.name, true),
                )
            )
            .sort(Sorts.descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POST_PER_PAGE)
            .toList()
    }


    override suspend fun getAllPost(): List<PostWithoutDetails> {
        return postCollectionWithoutDetails.find().toList()
    }


    override suspend fun addPost(post: Post): Boolean {
       return postCollection.insertOne(post).wasAcknowledged()
    }

    override suspend fun updatePost(post: Post): Boolean {
        return postCollection.updateOne(
            Filters.eq(Post:: _id.name, post._id),
            Updates.combine(
                Updates.set(Post::title.name, post.title),
                Updates.set(Post::subtitle.name, post.subtitle),
                Updates.set(Post::category.name, post.category),
                Updates.set(Post::thumbnail.name, post.thumbnail),
                Updates.set(Post::content.name, post.content),
                Updates.set(Post::main.name, post.main),
                Updates.set(Post::popular.name, post.popular),
                Updates.set(Post::sponsored.name, post.sponsored)
            )
        ).wasAcknowledged()
    }


    override suspend fun readMyPost(skip: Int, author: String): List<PostWithoutDetails> {

        return postCollectionWithoutDetails
            //.withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.and(
                    Filters.eq(PostWithoutDetails::author.name, author  )
                )
            )
            .sort(Sorts.descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POST_PER_PAGE)
            .toList()

    }


    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection
                .find(
                    Filters.and(
                        Filters.eq(User::username.name, user.username),
                        Filters.eq(User::password.name, user.password)
                    )
                ).firstOrNull()
//                .limit(1) // Limit to one result
//                .first() // Fetch the first result if available
        } catch (e: NoSuchElementException) {
            null // Return null if no matching user is found
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun checkUserId(_id : String):Boolean{

        return try {
          //  val documentCount = userCollection.countDocuments(User:: _id eq _id)
            val documentCount = userCollection.countDocuments(eq("_id", _id))
            documentCount>0
        }catch (e:Exception){
            context.logger.error(e.message.toString())
            false
        }

    }

    override suspend fun subscribe(newsletter: Newsletter): String {
       val result= subscriberCollection
           .find(
               Filters.and(
                   Filters.eq(Newsletter::email.name, newsletter.email)
               )
           ).toList()

        return if(result.isNotEmpty()){
            "You'r alredy subscribed"
        }else{
            val newEmail = subscriberCollection
                .insertOne(newsletter)
                .wasAcknowledged()
            if(newEmail) "Successfully subscribe"
            else "Some thing wants wrong"
        }
    }

}


