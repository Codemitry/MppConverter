actual fun simpleFunction(a: Int? = null, b: (str: String) -> Unit, c: String) {
    b(c)
    println(c)
    b.invoke(c)
}