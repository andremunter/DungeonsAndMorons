dependencies {
    // Required since DatabaseConnection and PostgresCharacterRepository currently reside here
    implementation("org.postgresql:postgresql:42.7.12")
    // Source: https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok:1.18.48")
}