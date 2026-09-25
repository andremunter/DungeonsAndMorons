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
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
    runtimeOnly("org.postgresql:postgresql")
}