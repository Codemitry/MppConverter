class simpleClass(val a: Int, private var b: Double, c: Long) {
    private constructor(b: Double?, long: Long) : this(0, b ?: 0.0, long) {
        println("hey boy")
    }

    internal constructor() : this(0, 0.0, 0)
}