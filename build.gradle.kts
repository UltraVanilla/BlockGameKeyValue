plugins {
    `java-library`
    `maven-publish`
    id("com.diffplug.spotless") version "7.0.3"
}

group = "josie.blockgamekeyvalue"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jspecify:jspecify:1.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.12.1")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

spotless {
    isEnforceCheck = false
    encoding("UTF-8")
    format("misc") {
        target(".gitignore", "README.md")
        leadingTabsToSpaces()
        endWithNewline()
        trimTrailingWhitespace()
    }
    java {
        target("**/*.java")
        leadingTabsToSpaces()
        endWithNewline()
        trimTrailingWhitespace()
        removeUnusedImports()
        palantirJavaFormat()
    }
    kotlinGradle {
        ktfmt().googleStyle()
        leadingTabsToSpaces()
        endWithNewline()
        trimTrailingWhitespace()
    }
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name = "${project.name} ${project.version}"
                description = project.description
                url = "https://github.com/UltraVanilla/${project.name}"

                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                        distribution = "repo"
                    }
                }

                developers {
                    developer {
                        name = "lordpipe"
                        organization = "UltraVanilla"
                        organizationUrl = "https://ultravanilla.world/"
                    }
                    developer {
                        name = "JosieToolkit Contributors"
                    }
                }

                scm {
                    url = "https://github.com/UltraVanilla/${project.name}"
                    connection = "scm:https://UltraVanilla@github.com/UltraVanilla/${project.name}.git"
                    developerConnection = "scm:git://github.com/UltraVanilla/${project.name}.git"
                }

                issueManagement {
                    system = "GitHub"
                    url = "https://github.com/UltraVanilla/${project.name}/issues"
                }
            }
        }
    }
    repositories {
        maven {
            name = "ultravanillaReleases"
            url = uri("https://maven.ultravanilla.world/releases")
            credentials {
                username = System.getenv("MAVEN_NAME")
                password = System.getenv("MAVEN_SECRET")
            }
        }
    }
}
