plugins {
    `kotlin-dsl`
}

group = "br.com.lira.rickandmorty.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "rickandmorty.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidHilt") {
            id = "rickandmorty.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRetrofit") {
            id = "rickandmorty.android.retrofit"
            implementationClass = "AndroidRetrofitConventionPlugin"
        }
    }
}