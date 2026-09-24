plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    java
}

springBoot {
    mainClass.set("se.dungeonsandmorons.web.DungeonsAndMoronsApplication")
}

dependencies {
    implementation(project(":core"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    runtimeOnly("org.postgresql:postgresql")
}