import net.minecraftforge.gradle.common.tasks.DownloadMavenArtifact

plugins {
	id("java")
	id("maven-publish")
	id("net.minecraftforge.gradle")
	id("org.parchmentmc.librarian.forgegradle")
}

// gradle.properties
val forgeVersion: String by extra
val minecraftVersion: String by extra
val modGroup: String by extra
val modId: String by extra
val modJavaVersion: String by extra
val parchmentVersionForge: String by extra

val baseArchivesName = "${modId}-${minecraftVersion}-forge-api"
base {
	archivesName.set(baseArchivesName)
}

val dependencyProjects: List<Project> = listOf(
	project(":CommonApi"),
)

dependencyProjects.forEach {
	project.evaluationDependsOn(it.path)
}

sourceSets {
	named("main") {
		resources {
			//The API has no resources
			setSrcDirs(emptyList<String>())
		}
	}
	named("test") {
		resources {
			//The test module has no resources
			setSrcDirs(emptyList<String>())
		}
	}
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(modJavaVersion))
	}
	withSourcesJar()
}

dependencies {
	"minecraft"(
		group = "net.minecraftforge",
		name = "forge",
		version = "${minecraftVersion}-${forgeVersion}"
	)
	dependencyProjects.forEach {
		implementation(it)
	}
}

minecraft {
	mappings("parchment", parchmentVersionForge)

	copyIdeResources.set(true)

	// All minecraft configurations in the multi-project must be identical, including ATs,
	// because of a ForgeGradle bug https://github.com/MinecraftForge/ForgeGradle/issues/844
	accessTransformer(file("../Forge/src/main/resources/META-INF/accesstransformer.cfg"))

	// no runs are configured for API
}

tasks.jar {
	finalizedBy("reobfJar")
}

val sourcesJar = tasks.named<Jar>("sourcesJar")

artifacts {
	archives(tasks.jar.get())
	archives(sourcesJar.get())
}

publishing {
	publications {
		register<MavenPublication>("forgeApi") {
			artifactId = baseArchivesName
			artifact(tasks.jar)
			artifact(sourcesJar)

			val dependencyInfos = dependencyProjects.map {
				mapOf(
					"groupId" to it.group,
					"artifactId" to it.base.archivesName.get(),
					"version" to it.version
				)
			}

			pom.withXml {
				val dependenciesNode = asNode().appendNode("dependencies")
				dependencyInfos.forEach {
					val dependencyNode = dependenciesNode.appendNode("dependency")
					it.forEach { (key, value) ->
						dependencyNode.appendNode(key, value)
					}
				}
			}
		}
	}
	repositories {
		val deployDir = project.findProperty("DEPLOY_DIR")
		if (deployDir != null) {
			maven(deployDir)
		}
	}
}

tasks.withType<DownloadMavenArtifact> {
	notCompatibleWithConfigurationCache("uses Task.project at execution time")
}
