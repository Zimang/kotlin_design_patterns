
pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
            name = "Aliyun-Google"
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/central")
            name = "Aliyun-center"
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
            name = "Aliyun-gradle-plugin"
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/grails-core")
            name = "Aliyun-grails-core"
        }
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
            name = "Aliyun-Google"
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/central")
            name = "Aliyun-center"
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
            name = "Aliyun-gradle-plugin"
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/grails-core")
            name = "Aliyun-grails-core"
        }
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "kotlin_design_patterns"