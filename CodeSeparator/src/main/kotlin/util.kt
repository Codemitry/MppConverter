import com.intellij.psi.PsiElement
import com.spbpu.mppconverter.kootstrap.PSICreator
import org.jetbrains.kotlin.lexer.KtTokens.*
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.addRemoveModifier.addModifier
import org.jetbrains.kotlin.psi.psiUtil.isPropertyParameter
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
        override fun visitClass(klass: KtClass) {
            super.visitClass(klass)
            if (!klass.isTopLevel())
                return

            getExpectClass(klass)

            println("visit class: ${klass.name}")

            println("expect: ${getExpectClass(klass).text}\n")
        }

    })
}

val incompatibleWithExpectFunModifiers = listOf(
    PRIVATE_KEYWORD, LATEINIT_KEYWORD
)

val incompatibleWithActualFunModifiers = listOf(
    PRIVATE_KEYWORD, LATEINIT_KEYWORD
)

fun KtModifierListOwner.deleteModifiersIncompatibleWithExpect() {
    for (modifier in incompatibleWithExpectFunModifiers) {
        this.removeModifier(modifier)
    }
}

fun KtClassBody.addInside(element: PsiElement) {
    addAfter(element, lBrace)
    addAfter(KtPsiFactory(element).createNewLine(), lBrace)
}

fun createKtParameterFromProperty(paramProperty: KtParameter): KtParameter {
    val factory = KtPsiFactory(paramProperty)

    val pattern = buildString {
        append("a: Type")

        if (paramProperty.hasDefaultValue())
            append(" = value")
    }
    val ktParameter = factory.createParameter(pattern)

    ktParameter.setName(paramProperty.nameAsSafeName.identifier)
    paramProperty.typeReference?.let { typeRef -> ktParameter.setTypeReference(typeRef) }

    paramProperty.defaultValue?.let { defValue -> ktParameter.defaultValue?.replace(defValue) }

    return ktParameter
}

fun createKtPropertyWithoutInitializer(oldParamProperty: KtParameter): KtProperty {
    val factory = KtPsiFactory(oldParamProperty)
    val ktProperty = factory.createProperty("val a: Type")

    ktProperty.setName(oldParamProperty.nameAsSafeName.identifier)
    oldParamProperty.typeReference?.let { typeRef -> ktProperty.setTypeReference(typeRef) }
    oldParamProperty.valOrVarKeyword?.let { varOrVal -> ktProperty.valOrVarKeyword.replace(varOrVal) }

    return ktProperty
}


fun KtClass.replaceConstructorPropertiesWithParameters() {
    primaryConstructorParameters.forEach { param ->
        param.replace(createKtParameterFromProperty(param))
    }
}


fun KtClass.copyConstructorPropertiesToBody() {
    primaryConstructorParameters.forEach { param ->

        if (param.isPropertyParameter()) {
            getOrCreateBody().apply {
                addInside(createKtPropertyWithoutInitializer(param))
            }
        }
    }
}


fun getExpectFun(function: KtNamedFunction): PsiElement {
    val copy = function.copy() as KtNamedFunction

    copy.deleteModifiersIncompatibleWithExpect()

    copy.addModifier(EXPECT_KEYWORD)

    copy.accept(KtRealizationEraserVisitorVoid())

    return copy
}

fun getActualFun(function: KtNamedFunction): PsiElement {
    val copy = function.copy() as KtNamedFunction

    copy.deleteModifiersIncompatibleWithExpect()

    copy.addModifier(ACTUAL_KEYWORD)

    return copy
}


fun getExpectClass(klass: KtClass): PsiElement {
    val copy = klass.copy() as KtClass

    copy.deleteModifiersIncompatibleWithExpect()

    addModifier(copy, EXPECT_KEYWORD)

    copy.accept(KtRealizationEraserVisitorVoid())

    return copy
}

