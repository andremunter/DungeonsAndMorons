plugins {
    java
    id("org.springframework.boot") version "3.4.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

subprojects {
    repositories {
        mavenCentral()
    }

    plugins.withId("java") {
        configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(25))
            }
        }

        tasks.withType<JavaCompile> {
            options.release.set(21)
        }

        dependencies {
            testImplementation(platform("org.junit:junit-bom:5.11.0"))
            testImplementation("org.junit.jupiter:junit-jupiter")
            testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
}