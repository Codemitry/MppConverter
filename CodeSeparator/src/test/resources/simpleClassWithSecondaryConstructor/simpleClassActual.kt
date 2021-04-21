actual class simpleClass actual constructor(actual val a: Int, actual var b: Double, c: Long) {
    private actual constructor(b: Double?, long: Long) : this(0, b ?: 0.0, long) {
        println("hey boy")
    }
}