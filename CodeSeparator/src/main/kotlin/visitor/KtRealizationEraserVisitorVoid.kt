package visitor

import copyConstructorPropertiesToBody
import deleteDelegationAndBody
import deleteModifiersIncompatibleWithExpect
import org.jetbrains.kotlin.lexer.KtTokens.DATA_KEYWORD
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import replaceConstructorPropertiesWithParameters


lateinit var context: BindingContext

class KtRealizationEraserVisitorVoid : KtTreeVisitorVoid() {
    override fun visitProperty(property: KtProperty) {
        println("VISIT property")

        super.visitProperty(property)

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
        println("VISIT function")
        super.visitNamedFunction(function)

       // what is type of fun?
//        if (function.typeReference == null) {
//            function.add(KtPsiFactory(function).createComment("// : Type  - TODO: indicate your type explicitly"))
//        }

        function.deleteModifiersIncompatibleWithExpect()
        function.bodyExpression?.delete()
        function.equalsToken?.delete()
    }

    override fun visitClass(klass: KtClass) {
        println("VISIT class")
        super.visitClass(klass)

        if (klass.isData()) klass.removeModifier(DATA_KEYWORD)
        klass.deleteModifiersIncompatibleWithExpect()

        klass.copyConstructorPropertiesToBody()
        klass.replaceConstructorPropertiesWithParameters()
        klass.secondaryConstructors.forEach {
            it.deleteDelegationAndBody()
        }
    }
}