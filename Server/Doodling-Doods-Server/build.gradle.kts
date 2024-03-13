val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val postgres_version: String by project
val h2_version: String by project
plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.8"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"


}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

val sshAntTask = configurations.create("sshAntTask")
repositories {
    mavenCentral()
}

dependencies {

    implementation(group = "org.http4k", name = "http4k-core", version = "4.20.2.0")
    implementation(group = "org.http4k", name = "http4k-server-jetty", version = "4.20.2.0")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version = "1.3.2")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-datetime", version = "0.3.2")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "2.0.0-alpha5")
    runtimeOnly(group = "org.slf4j", name = "slf4j-simple", version = "2.0.0-alpha5")
    implementation(group = "org.postgresql", name = "postgresql", version = "42.+")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
    testImplementation(kotlin("test"))



    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-serialization-gson-jvm")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.ktor:ktor-server-default-headers-jvm")
    implementation("io.ktor:ktor-server-compression-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-double-receive-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //postgres

//    val postgresql_driver_version: String by project
    val exposed_version: String by project
    val hikari_version:String by project



    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")

    implementation ("org.postgresql:postgresql:$postgres_version")
    implementation ("com.zaxxer:HikariCP:$hikari_version")

    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.5.31")

    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    testImplementation ("junit:junit:4.13.2")


    sshAntTask("org.apache.ant:ant-jsch:1.10.12")

}


tasks {
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(sourceSets["test"].output)
        archiveBaseName.set("my-app")
        mergeServiceFiles()
        manifest {
            attributes("Main-Class" to "com.example.doodling-doods-server")
        }
    }
}



//ant.withGroovyBuilder {
//    "taskdef"(
//        "name" to "scp",
//        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.Scp",
//        "classpath" to configurations.get("sshAntTask").asPath
//    )
//    "taskdef"(
//        "name" to "ssh",
//        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.SSHExec",
//        "classpath" to configurations.get("sshAntTask").asPath
//    )
//}
//
//task("deploy") {
//    dependsOn("clean", "shadowJar")
//    ant.withGroovyBuilder {
//        doLast {
//            val knownHosts = File.createTempFile("knownhosts", "txt")
//            val user = "root"
//            val host = "10.51.25.233"
//            val key = file("keys/ttictactoe")
//            val jarFileName = "com.plcoding.tictactoe-all.jar"
//            try {
//                "scp"(
//                    "file" to file("build/libs/$jarFileName"),
//                    "todir" to "$user@$host:/root/tictactoe",
//                    "keyfile" to key,
//                    "trust" to true,
//                    "knownhosts" to knownHosts
//                )
//                "ssh"(
//                    "host" to host,
//                    "username" to user,
//                    "keyfile" to key,
//                    "trust" to true,
//                    "knownhosts" to knownHosts,
//                    "command" to "mv /root/tictactoe/$jarFileName /root/tictactoe/tictactoe.jar"
//                )
//                "ssh"(
//                    "host" to host,
//                    "username" to user,
//                    "keyfile" to key,
//                    "trust" to true,
//                    "knownhosts" to knownHosts,
//                    "command" to "systemctl stop tictactoe"
//                )
//                "ssh"(
//                    "host" to host,
//                    "username" to user,
//                    "keyfile" to key,
//                    "trust" to true,
//                    "knownhosts" to knownHosts,
//                    "command" to "systemctl start tictactoe"
//                )
//            } finally {
//                knownHosts.delete()
//            }
//        }
//    }
//}