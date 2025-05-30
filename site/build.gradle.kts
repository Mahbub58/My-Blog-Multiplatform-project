import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import kotlinx.html.script

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kotlinx.serialization)
    // alias(libs.plugins.kobwebx.markdown)


}

group = "com.example.blogmultiplatform"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")

            head.add{
                script {
                    src= "/highlight.min.js"
                }
                link {
                    rel ="stylesheet"
                    href="/github-dark.css"
                }

                script {
                    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js"
                }
                link {
                    rel ="stylesheet"
                    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/css/bootstrap.min.css"
                }
            }
        }

        export {
            enableTraces()
        }
    }
}

kotlin {
    configAsKobwebApplication("blogmultiplatform", includeServer = true)

    sourceSets {
        commonMain.dependencies {
          // Add shared dependencies between JS and JVM here
            implementation(libs.compose.runtime)
            implementation(libs.kotlinx.serialization.json)

        }
        jsMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)

            implementation(libs.silk.icons.fa)
          //   implementation(libs.kobwebx.markdown)
         //   implementation(project(":worker"))
        }
        jvmMain.dependencies {
            compileOnly(libs.kobweb.api) // Provided by Kobweb backend at runtime
            implementation(libs.mongodb.kotlin.driver)
//            implementation(libs.mongodb.kotlin.driver)
//            implementation(libs.kmongo.database)

        }
    }
}
