import com.intellij.psi.PsiElement
import com.spbpu.mppconverter.kootstrap.PSICreator
import org.jetbrains.kotlin.lexer.KtTokens.*
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import visitor.KtRealizationEraserVisitorVoid

fun main() {
    convert("W:\\Kotlin\\Projects\\MPP\\src\\commonMain\\kotlin\\main.kt")
//    convert("W:\\Kotlin\\Projects\\MppConverter\\CodeSeparator\\src\\main\\kotlin\\SourceSets.kt")
}


fun convert(path: String) {
    val creator = PSICreator()

    val ktFile = creator.getPSIForFile(path)
    PSICreator.analyze(ktFile)

    ktFile.accept(object : KtTreeVisitorVoid() {
        override fun visitNamedFunction(function: KtNamedFunction) {
            super.visitNamedFunction(function)
            if (!function.isTopLevel)
                return

//            getExpectFun(function)
//            getActualFun(function)
        }
    })
}

val incompatibleWithExpectFunModifiers = listOf(
    PRIVATE_KEYWORD, LATEINIT_KEYWORD
)

val incompatibleWithActualFunModifiers = listOf(
    PRIVATE_KEYWORD, LATEINIT_KEYWORD
)

fun getExpectFun(function: KtNamedFunction): PsiElement {
    val copy = function.copy() as KtNamedFunction

    for (modifier in incompatibleWithExpectFunModifiers) {
        copy.removeModifier(modifier)
    }

    copy.addModifier(EXPECT_KEYWORD)

    copy.accept(KtRealizationEraserVisitorVoid())

    return copy
}

fun getActualFun(function: KtNamedFunction): PsiElement {
    val copy = function.copy() as KtNamedFunction

    for (modifier in incompatibleWithActualFunModifiers) {
        copy.removeModifier(modifier)
    }

    copy.addModifier(ACTUAL_KEYWORD)

    return copy
}


