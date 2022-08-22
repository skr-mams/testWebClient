plugins {
    java
    application
    id("io.vertx.vertx-plugin") version "1.3.0"
}

group = "mx.sekura"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    //Vertx core
    implementation("io.vertx:vertx-core:4.2.5")
    //Vertx web
    implementation("io.vertx:vertx-web:4.2.5")
    //Vertx config
    implementation("io.vertx:vertx-config:4.2.5")
    //Vertx webClient
    implementation("io.vertx:vertx-web-client:4.2.5")
    // Vertx web validation
    implementation("io.vertx:vertx-web-validation:4.0.3")
    //Vertx jwt
    implementation("io.vertx:vertx-auth-jwt:4.2.5")
    //JSON
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.2")
    implementation("com.google.code.gson:gson:2.8.9")
    //Json Schema vert.x
    implementation("io.vertx:vertx-json-schema:4.2.5")
    // Open Api
    implementation("io.vertx:vertx-web-openapi:4.2.5")
    // Utilería
    implementation("org.mod4j.org.apache.commons:lang:2.1.0")
    //implementation("dom4j:dom4j:20040902.021138")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.2")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.2")
    // Librería models
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
vertx {
    mainVerticle = "services.Verticle"
}