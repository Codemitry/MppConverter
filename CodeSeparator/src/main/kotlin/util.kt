import com.intellij.psi.PsiElement
import com.spbpu.mppconverter.kootstrap.PSICreator
import org.jetbrains.kotlin.lexer.KtTokens.*
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.isPropertyParameter
import visitor.KtActualMakerVisitorVoid
import visitor.KtRealizationEraserVisitorVoid

fun main() {
    convert("W:\\Kotlin\\Projects\\MppConverter\\CodeSeparator\\src\\test\\resources\\simpleClassWithSecondaryConstructor\\simpleClass.kt")
}


fun convert(path: String) {
    val creator = PSICreator()
    val ktFile = creator.getPSIForFile(path)

    println("===== jvm =====")
    println(ktFile.text)

    println("\n===== expect =====")
    println(ktFile.getExpect().text)
    println("\n===== actual =====")
    println(ktFile.getActual().text)

}

fun KtFile.getExpect(): KtFile {
    val copy = copy() as KtFile
    copy.accept(KtRealizationEraserVisitorVoid())
    return copy
}

fun KtFile.getActual(): KtFile {
    val copy = copy() as KtFile
    copy.accept(KtActualMakerVisitorVoid())
    return copy
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

fun KtModifierListOwner.deleteModifiersIncompatibleWithActual() {
    for (modifier in incompatibleWithActualFunModifiers) {
        this.removeModifier(modifier)
    }
}

fun KtClassBody.addInside(element: PsiElement) {
    addAfter(element, lBrace)
    addAfter(KtPsiFactory(element).createNewLine(), lBrace)
}

fun KtSecondaryConstructor.deleteDelegationAndBody() {
    this.getDelegationCallOrNull()?.delete()
    this.bodyBlockExpression?.delete()
    this.colon?.delete()
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

fun KtParameter.removeInitializer() {
    defaultValue?.delete()
    equalsToken?.delete()
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


fun KtNamedFunction.getExpect(): PsiElement {
    val copy = copy() as KtNamedFunction
    copy.addModifier(EXPECT_KEYWORD)
    copy.deleteModifiersIncompatibleWithExpect()
    copy.accept(KtRealizationEraserVisitorVoid())

    return copy
}

fun KtNamedFunction.getActual(): PsiElement {
    val copy = copy() as KtNamedFunction
    copy.addModifier(ACTUAL_KEYWORD)
    copy.deleteModifiersIncompatibleWithExpect()
    return copy
}


fun KtClass.getExpect(): PsiElement {
    val copy = copy() as KtClass
    copy.accept(KtRealizationEraserVisitorVoid())
    return copy
}

fun KtClass.getActual(): PsiElement {
    val copy = copy() as KtClass
    copy.accept(KtActualMakerVisitorVoid())
    return copy
}

