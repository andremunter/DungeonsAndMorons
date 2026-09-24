plugins {
    application
}

dependencies {
    implementation(project(":core"))
}

application {
    mainClass.set("se.dungeonsandmorons.Main")
}

// Enables interactive terminal/scanner input when running via './gradlew :cli:run'
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}