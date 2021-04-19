import java.io.File

class SourceSets(val pathToSrc: String) {
    lateinit var commonMainKotlin: Kotlin
    lateinit var jvmMainKotlin: Kotlin

    fun create() {
        commonMain {
            commonMainKotlin = kotlin()
            resources()
        }

        commonTest {
            kotlin()
            resources()
        }
        jvmMain {
            jvmMainKotlin = kotlin()
            resources()
        }
        jvmTest {
            kotlin()
            resources()
        }
    }



    private fun commonMain(init: SourceSet.() -> Unit) : SourceSet {
        val sourceSet = SourceSet("commonMain")
        sourceSet.init()
        return sourceSet
    }

    private fun commonTest(init: SourceSet.() -> Unit) : SourceSet {
        val sourceSet = SourceSet("commonTest")
        sourceSet.init()
        return sourceSet
    }


    private fun jvmMain(init: SourceSet.() -> Unit) : SourceSet {
        val sourceSet = SourceSet("jvmMain")
        sourceSet.init()
        return sourceSet
    }

    private fun jvmTest(init: SourceSet.() -> Unit) : SourceSet {
        val sourceSet = SourceSet("jvmTest")
        sourceSet.init()
        return sourceSet
    }


    open class Directory(val pathToParent: String, val name: String) {
        val path: String

        init {
            val dir = File(pathToParent, name)
            dir.mkdir()
            path = dir.absolutePath
        }

        fun createFile(filename: String, fileContent: String) {
            File(path, filename).writeText(fileContent)
        }
    }

    inner class SourceSet(name: String): Directory(pathToSrc, name) {

        fun kotlin(init: (Kotlin.() -> Unit)? = null): Kotlin {
            val kotlin = Kotlin(path)
            init?.let { kotlin.init() }
            return kotlin
        }

        fun resources(init: (Resources.() -> Unit)? = null): Resources {
            val resources = Resources(path)
            init?.let { resources.init() }
            return resources
        }

    }

    inner class Kotlin(pathToModule: String) : Directory(pathToModule, "kotlin")

    inner class Resources(pathToModule: String) : Directory(pathToModule, "resources")
}

