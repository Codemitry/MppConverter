class simpleClass {
    class Nested {
        fun hello() {

        }

        protected fun prot(a: Int): Int = 135
    }

    data class Data(val a: Int, var b: Long) {
        constructor(a: Double) : this(a.toInt(), a.toLong()) {
            println("Secondary constructor in nested data class")
        }
    }
}