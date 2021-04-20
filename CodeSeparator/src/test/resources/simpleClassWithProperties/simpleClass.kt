class simpleClass {
    val a: Int?
    val aa: Int = 13
    lateinit var late: Set<Int>
    private var privateVar = "Hello"
    internal val internalVal = 130L

    val getter: Int
        get() = 5

    var getter2: Int
        private set
        get() = 5

    val getter3: Int
        get() {
            return 5
        }

    var setter: Int
        set(value) {
            field = value + 3
        }

    var delegated: Int by lazy { println("lazy"); 0 }
    private set
}