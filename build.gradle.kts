plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.modrinth.minotaur") version "2.8.4"
    id("io.github.CDAGaming.cursegradle") version "1.6.1"
}

group = "i7meupdatemod"
version = project.properties["version"].toString() + if ("false" == System.getenv("IS_SNAPSHOT")) "" else "-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.shadowJar {
    manifest {
        attributes(
            "TweakClass" to "i7meupdatemod.launchwrapper.LaunchWrapperTweaker",
            "TweakOrder" to 33,
            "Automatic-Module-Name" to "i7meupdatemod",
        )
    }
    minimize()
    archiveBaseName.set("I7MEUpdateMod")
    relocate("com.google.archivepatcher", "include.com.google.archivepatcher")
    dependencies {
        include(dependency("net.runelite.archive-patcher:archive-patcher-applier:.*"))
    }
    exclude("LICENSE")
}

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net/")
    maven("https://maven.fabricmc.net/")
    maven("https://files.minecraftforge.net/maven")
    maven("https://repo.runelite.net/")
}

configurations.configureEach {
    isTransitive = false
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    implementation("net.runelite.archive-patcher:archive-patcher-applier:1.2")
    compileOnly("org.jetbrains:annotations:24.0.1")

    implementation("net.fabricmc:fabric-loader:0.14.22")
    implementation("cpw.mods:modlauncher:8.1.3")
    implementation("net.minecraft:launchwrapper:1.12")

    implementation("commons-io:commons-io:2.14.0")
    implementation("org.ow2.asm:asm:9.6")
    implementation("com.google.code.gson:gson:2.10.1")

}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    filesMatching("**") {
        expand(
            "version" to project.version,
        )
    }
}