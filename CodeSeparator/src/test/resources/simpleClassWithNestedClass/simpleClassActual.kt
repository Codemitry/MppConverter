actual class simpleClass {
    actual class Nested {
        actual fun hello() {

        }

        protected actual fun prot(a: Int): Int = 135
    }

    actual data class Data actual constructor(actual val a: Int, actual var b: Long) {
        actual constructor(a: Double) : this(a.toInt(), a.toLong()) {
            println("Secondary constructor in nested data class")
        }
    }
}