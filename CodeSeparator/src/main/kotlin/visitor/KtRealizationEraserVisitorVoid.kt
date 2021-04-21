package visitor

import copyConstructorPropertiesToBody
import deleteDelegationAndBody
import deleteModifiersIncompatibleWithExpect
import org.jetbrains.kotlin.lexer.KtTokens.DATA_KEYWORD
import org.jetbrains.kotlin.lexer.KtTokens.EXPECT_KEYWORD
import org.jetbrains.kotlin.psi.*
import replaceConstructorPropertiesWithParameters


class KtRealizationEraserVisitorVoid : KtTreeVisitorVoid() {
    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)

        if (property.isTopLevel) {
            property.addModifier(EXPECT_KEYWORD)
        }

        // mocks for types with initializer
        if (property.hasInitializer()) {
            property.add(KtPsiFactory(property).createComment("// : Type  - TODO: indicate your type explicitly"))
        }
        property.deleteModifiersIncompatibleWithExpect()
        property.delegate?.delete()
        property.initializer?.delete()
        property.equalsToken?.delete()
        property.setter?.delete()
        property.getter?.delete()
    }
    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (function.isTopLevel) {
            function.addModifier(EXPECT_KEYWORD)
        }

       // what is type of fun?
//        if (function.typeReference == null) {
//            function.add(KtPsiFactory(function).createComment("// : Type  - TODO: indicate your type explicitly"))
//        }

        function.deleteModifiersIncompatibleWithExpect()
        function.bodyExpression?.delete()
        function.equalsToken?.delete()
    }

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)

        if (klass.isTopLevel()) {
            klass.addModifier(EXPECT_KEYWORD)
        }

        if (klass.isData()) klass.removeModifier(DATA_KEYWORD)
        klass.deleteModifiersIncompatibleWithExpect()

        klass.copyConstructorPropertiesToBody()
        klass.replaceConstructorPropertiesWithParameters()
        klass.secondaryConstructors.forEach {
            it.deleteDelegationAndBody()
        }
    }
}